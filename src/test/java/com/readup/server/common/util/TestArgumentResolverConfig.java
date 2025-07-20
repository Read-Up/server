//package com.readup.server.common.util;
//
//import com.readup.server.common.annotaion.CurrentAuthUser;
//import com.readup.server.common.annotaion.CurrentUser;
//import com.readup.server.user.UserTestBuilder;
//import com.readup.server.user.domain.User;
//import com.readup.server.user.dto.AuthUser;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.MethodParameter;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
//@TestConfiguration
//public class TestArgumentResolverConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(currentAuthUserResolver());
//        resolvers.add(currentUserResolver());
//    }
//
//    @Bean
//    public HandlerMethodArgumentResolver currentAuthUserResolver() {
//        return new HandlerMethodArgumentResolver() {
//            @Override
//            public boolean supportsParameter(MethodParameter parameter) {
//                return parameter.hasParameterAnnotation(CurrentAuthUser.class);
//            }
//
//            @Override
//            public Object resolveArgument(MethodParameter parameter,
//                                          ModelAndViewContainer mavContainer,
//                                          NativeWebRequest webRequest,
//                                          WebDataBinderFactory binderFactory) {
//                User user = UserTestBuilder.builder().build();
//                return new AuthUser(user.getId(), user.getNickname());
//            }
//        };
//    }
//
//    @Bean
//    public HandlerMethodArgumentResolver currentUserResolver() {
//        return new HandlerMethodArgumentResolver() {
//            @Override
//            public boolean supportsParameter(MethodParameter parameter) {
//                return parameter.hasParameterAnnotation(CurrentUser.class);
//            }
//
//            @Override
//            public Object resolveArgument(MethodParameter parameter,
//                                          ModelAndViewContainer mavContainer,
//                                          NativeWebRequest webRequest,
//                                          WebDataBinderFactory binderFactory) {
//                return UserTestBuilder.builder().build();
//            }
//        };
//    }
//}