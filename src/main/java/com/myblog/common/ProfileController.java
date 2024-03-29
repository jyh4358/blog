package com.myblog.common;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse("NotFound");
    }

    @GetMapping("/health")
    public ResponseEntity<Void> health() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
