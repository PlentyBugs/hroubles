package org.hroubles.mir.service;

import org.hroubles.mir.domain.Product;
import org.hroubles.mir.domain.User;
import org.hroubles.mir.domain.enums.Tag;
import org.hroubles.mir.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> searchByTag(Tag tag, Pageable pageable) {
        return productRepository.findByTag(tag, pageable);
    }

    public Page<Product> searchByTags(Set<Tag> tags, Pageable pageable) {
        return productRepository.findByTags(tags, pageable);
    }

    public Page<Product> search(String str, Pageable pageable) {
        return productRepository.findByDescriptionOrNameOrSellerUsernameOrTagsContains("%" + str + "%", pageable);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> getPage(String filter, Pageable pageable) {
        return "".equals(filter) ? productRepository.findAll(pageable): search(filter, pageable);
    }

    public boolean addProduct(Product product, User user, Set<Tag> tags, MultipartFile file) throws IOException {
        product.setSeller(user);
        product.setTags(tags);
        saveFile(product, file);
        productRepository.save(product);

        return true;
    }

    public boolean addProduct(Product product) {
        productRepository.save(product);

        return true;
    }

    private void saveFile(@Valid Product product, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (uploadDir.exists()) uploadDir.mkdir();

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            product.setFilename(resultFilename);
        }
    }
}
