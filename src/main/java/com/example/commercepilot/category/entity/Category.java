package com.example.commercepilot.category.entity;

import com.example.commercepilot.config.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@SoftDelete(columnName = "is_deleted")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    // 편의 메서드
    public void addChildCategory(Category child) {
        if (!this.children.contains(child)) {
            this.children.add(child);
        }

        if (child.getParent() != this) {
            child.setParent(this);
        }
    }

    public void setParent(Category parent) {
        // 부모 카테고리가 변경되는 상황을 대비
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = parent;

        if (parent != null && !parent.getChildren().contains(this)) {
            parent.getChildren().add(this);
        }
    }

    public void deleteChildCategory() {
        this.isDeleted = true;
        for (Category child : this.children) {
            if (!child.isDeleted()) {
                child.deleteChildCategory();
            }
        }
    }
}
