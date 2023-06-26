package com.seny.book.springboot.web;
//무중단 배포 스크립트, 배포 시 사용할 포트를 판단하는 컨트롤러

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
        
        //실행중인 ActiveProfile을 모두 가져오고 배포에 사용될 profile들(real, real1, real2) 중 하나라도 있으면 반환
        //하나도 없으면 첫번째 반환
        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }

}
