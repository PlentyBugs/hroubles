package org.hroubles.mir.service;

import org.hroubles.mir.domain.Product;
import org.hroubles.mir.domain.enums.Tag;
import org.hroubles.mir.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        List<Page<Product>> pages = new ArrayList<>();
        for (Tag tag : tags) pages.add(searchByTag(tag, pageable));
        return merge(pages, pageable);
    }

    public Page<Product> search(String str, Pageable pageable) {
        List<Page<Product>> pages = new ArrayList<>();
        Set<String> allTags = Arrays.stream(Tag.values()).map(Tag::name).collect(Collectors.toSet());
        Set<Tag> tags = new HashSet<>();
        String[] words = str.split("\\s+");
        for (String word : words) {
            String upperWord = word.toUpperCase();
            if (allTags.contains(upperWord)) {
                tags.add(Tag.valueOf(upperWord));
            }
            pages.add(productRepository.findByDescriptionOrNameOrSellerUsernameContains("%" + word + "%", pageable));
        }
        pages.add(searchByTags(tags, pageable));
        return merge(pages, pageable);
    }

    private Page<Product> merge(List<Page<Product>> pages, Pageable pageable) {

        Set<Product> products = new HashSet<>();
        for (Page<Product> page : pages) {
            products.addAll(page.toSet());
        }

        List<Product> resProducts = new ArrayList<>(products);

        return new PageImpl<>(resProducts, pageable, resProducts.size());
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
