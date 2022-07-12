package com.myblog.commen.basetest;

import com.myblog.article.repository.ArticleRepository;
import com.myblog.article.repository.ArticleTagRepository;
import com.myblog.article.repository.TagRepository;
import com.myblog.category.resposiotry.CategoryRepository;
import com.myblog.comment.repository.CommentRepository;
import com.myblog.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@SpringBootTest
public class ServiceBaseTest {
    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    protected ArticleTagRepository articleTagRepository;

    @Autowired
    protected CommentRepository commentRepository;


}
