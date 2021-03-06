package com.myblog.article.service;

import com.myblog.article.model.Article;
import com.myblog.article.properties.GithubProperties;
import com.myblog.common.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static com.myblog.common.exception.ExceptionMessage.FAIL_BACKUP;

@Service
@RequiredArgsConstructor
public class GitHubBackupService {
    private final GithubProperties githubProperties;
    private GitHub github;
    private GHRepository repository;



    public void backupArticleToGitHub(Article article) {;
        String path = "";
        if (article.getCategory().getParent() != null) {
            path = article.getCategory().getParent().getTitle() + "/" + article.getCategory().getTitle() + "/[" + article.getCreatedDate() + "]" + article.getTitle() + ".md";
                }
        if (article.getCategory().getParent() == null) {
            path = article.getCategory().getTitle() + "/[" + article.getCreatedDate() + "]" + article.getTitle() + ".md";
        }
        try {
            github = new GitHubBuilder().withOAuthToken(githubProperties.getToken()).build();
            repository = github.getRepository(githubProperties.getRepository());
            repository.createContent()
                    .path(path)
                    .content(article.getContent())
                    .message("article backup")
                    .branch("main")
                    .commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
