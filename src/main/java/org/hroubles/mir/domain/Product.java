package org.hroubles.mir.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hroubles.mir.domain.enums.Tag;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Product name can't be empty")
    @Length(max = 256, message = "Product name is too long")
    private String name;

    @NotBlank(message = "Product description can't be empty")
    @Length(max = 2048, message = "Product description is too long")
    private String description;

    @ElementCollection(targetClass = Tag.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "product_tag", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private Set<Tag> tags = new HashSet<>();

    @Min(value = 0)
    private Long price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User seller;

    private Long count;

    private String filename;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
