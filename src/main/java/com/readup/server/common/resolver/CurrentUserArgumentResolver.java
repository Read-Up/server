package com.readup.server.common.resolver;

import com.readup.server.auth.dto.CustomOAuth2User;
import com.readup.server.common.annotaion.CurrentUser;
import com.readup.server.common.exception.ControllerException;
import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.user.domain.User;
import com.readup.server.user.domain.repository.UserRepository;
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
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(User.class);
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

        try {
            Long socialAccountId = Long.parseLong(user.getName());
            return userRepository.findBySocialAccountId(socialAccountId)
                    .orElseThrow(() -> new ControllerException(ErrorCode.USER_NOT_FOUND));
        } catch (NumberFormatException ex) {
            throw new ControllerException(ErrorCode.INVALID_PARAMETER, "잘못된 사용자 식별자입니다.");
        }
    }
}
