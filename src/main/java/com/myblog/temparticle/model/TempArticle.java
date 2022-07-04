package com.myblog.temparticle.model;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class TempArticle {

    @Id
    private Long id;
    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false, length = 10000)
    private String content;
    private String thumbnailUrl;
    private String category;
    private String tags;

    public TempArticle() {
    }

    public TempArticle(String title, String content, String thumbnailUrl, String category, String tags) {
        this.id = 1L;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.tags = tags;
    }

    public static TempArticle createTempArticle(
            String title,
            String content,
            String thumbnailUrl,
            String category,
            String tags
    ) {
        return new TempArticle(
                title,
                content,
                thumbnailUrl,
                category,
                tags
        );
    }
}
