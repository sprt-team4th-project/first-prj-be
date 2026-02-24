package com.example.commercepilot.category.entity;

import com.example.commercepilot.config.BaseEntity;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    public Category(String name, Category parent) {
        this.name = name;
        changeParent(parent);
    }

    public void changeParent(Category parent) {
        if (parent != null && parent.getId().equals(this.id)) {
            throw new CustomException(ErrorCode.SELF_REFERENCE_CATEGORY);
        }
        if (parent != null && isDescendant(parent)) {
            throw new CustomException(ErrorCode.CIRCULAR_CATEGORY_REFERENCE);
        }
        if (this.parent != null) {
            this.parent.children.remove(this);
        }

        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    private boolean isDescendant(Category category) {
        for (Category child : this.children) {
            if (child.getId().equals(category.getId())) {
                return true;
            }
            if (child.isDescendant(category)) {
                return true;
            }
        }
        return false;
    }

    public void rename(String name) {
        if (name == null || name.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        this.name = name;
    }

    public void deleteWithDescendants() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        for (Category child : this.children) {
            if (!child.isDeleted()) {
                child.deleteWithDescendants();
            }
        }
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
    }
}
