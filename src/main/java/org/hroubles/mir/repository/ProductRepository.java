package org.hroubles.mir.repository;

import org.hroubles.mir.domain.Product;
import org.hroubles.mir.domain.User;
import org.hroubles.mir.domain.enums.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    @Query("from Product p where :tag member of p.tags")
    Page<Product> findByTag(@Param("tag") Tag tag, Pageable pageable);

    @Query("from Product p where :tags member of p.tags")
    Page<Product> findByTags(@Param("tags") Set<Tag> tags, Pageable pageable);

    @Query("from Product p where lower(p.description) like lower(:str) or lower(p.name) like lower(:str) or lower(p.seller.username) like lower(:str)")
    Page<Product> findByDescriptionOrNameOrSellerUsernameOrTagsContains(String str, Pageable pageable);

    Page<Product> findBySeller(User seller, Pageable pageable);
}

