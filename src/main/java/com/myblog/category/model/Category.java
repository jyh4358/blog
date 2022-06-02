package com.myblog.category.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false, length = 20)
    private String title;

    // tier 필요한가..?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parents_id")
    private Category parents;

    @OneToMany(mappedBy = "parents")
    private List<Category> categories = new ArrayList<>();

    @Builder
    public Category(String title, Category parents) {
        this.title = title;
        this.parents = parents;
    }
}
