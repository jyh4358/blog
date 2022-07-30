package com.myblog.category.model;

import com.myblog.common.model.BasicEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BasicEntity {

    @Column(nullable = false, unique = false, length = 50)
    private String title;

    // tier 필요한가..?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> child = new ArrayList<>();

    public Category(String title) {
        this.title = title;
    }

    public Category(String title, Category parent) {
        this.title = title;
        this.parent = parent;
    }

    public Category(Long id, String title, Category parent) {
        this.id = id;
        this.title = title;
        this.parent = parent;
    }

    public static Category createCategory(Long id, String title, Category parent) {
        return new Category(id, title, parent);
    }

    public void setChild(List<Category> child) {
        this.child = child;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void setParent(Category category) {
        this.parent = category;
    }

    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
