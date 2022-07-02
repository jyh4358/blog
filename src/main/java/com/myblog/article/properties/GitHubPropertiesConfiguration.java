package com.myblog.article.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = GithubProperties.class)
public class GitHubPropertiesConfiguration {
}
