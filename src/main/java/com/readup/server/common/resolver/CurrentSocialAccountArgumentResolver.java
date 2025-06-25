package com.readup.server.common.resolver;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.auth.dto.CustomOAuth2User;
import com.readup.server.auth.domain.SocialAccountService;
import com.readup.server.common.annotaion.CurrentSocialAccount;
import com.readup.server.common.exception.ControllerException;
import com.readup.server.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CurrentSocialAccountArgumentResolver implements HandlerMethodArgumentResolver {

    private final SocialAccountService socialAccountService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentSocialAccount.class)
                && parameter.getParameterType().equals(SocialAccount.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User user)) {
            throw new ControllerException(ErrorCode.UNAUTHORIZED);
        }

        Long socialAccountId;
        try {
            socialAccountId = Long.parseLong(user.getName());
        } catch (NumberFormatException ex) {
            throw new ControllerException(ErrorCode.INVALID_PARAMETER);
        }
        return socialAccountService.getById(socialAccountId);
    }
}
