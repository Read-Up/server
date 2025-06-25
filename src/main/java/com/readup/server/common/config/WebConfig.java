package com.readup.server.common.config;

import com.readup.server.common.resolver.CurrentAuthUserArgumentResolver;
import com.readup.server.common.resolver.CurrentSocialAccountArgumentResolver;
import com.readup.server.common.resolver.CurrentUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final CurrentSocialAccountArgumentResolver currentSocialAccountArgumentResolver;
    private final CurrentAuthUserArgumentResolver currentAuthUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
        resolvers.add(currentSocialAccountArgumentResolver);
        resolvers.add(currentAuthUserArgumentResolver);
    }
}