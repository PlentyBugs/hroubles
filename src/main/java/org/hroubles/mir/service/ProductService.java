package org.hroubles.mir.service;

import org.hroubles.mir.domain.Product;
import org.hroubles.mir.domain.enums.Tag;
import org.hroubles.mir.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;

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

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public void save(Product product) {
        productRepository.save(product);
    }
}
