package com.myblog.article.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<ArticleTag> articleTags = new ArrayList<>();

    public Tag(String name) {
        this.name = name;
    }

    public static Tag createTag(String name) {
        return new Tag(name);
    }
}
