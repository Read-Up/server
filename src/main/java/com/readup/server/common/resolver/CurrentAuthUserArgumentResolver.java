package com.readup.server.common.resolver;

import com.readup.server.auth.dto.CustomOAuth2User;
import com.readup.server.common.annotaion.CurrentAuthUser;
import com.readup.server.common.exception.ControllerException;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.user.domain.User;
import com.readup.server.user.domain.repository.UserRepository;
import com.readup.server.user.dto.AuthUser;
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
public class CurrentAuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentAuthUser.class)
                && parameter.getParameterType().equals(AuthUser.class);
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
            throw new ControllerException(ErrorCode.INVALID_PARAMETER, "잘못된 사용자 식별자 형식입니다.");
        }

        User entity = userRepository.findBySocialAccountId(socialAccountId)
                .orElseThrow(() -> new ControllerException(ErrorCode.USER_NOT_FOUND));
        return AuthUser.from(entity);
    }
}
