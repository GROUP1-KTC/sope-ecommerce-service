package com.sope.sope_ecommerce_backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class MultipartJsonConfig implements WebMvcConfigurer {

      private final ObjectMapper objectMapper;

      public MultipartJsonConfig(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
      }

      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new HandlerMethodArgumentResolver() {
                  @Override
                  public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.hasParameterAnnotation(RequestPart.class)
                                    && !MultipartFile.class.isAssignableFrom(parameter.getParameterType());
                  }

                  @Override
                  public Object resolveArgument(MethodParameter parameter,
                              ModelAndViewContainer mavContainer,
                              NativeWebRequest webRequest,
                              WebDataBinderFactory binderFactory) throws Exception {
                        MultipartHttpServletRequest multipartRequest = webRequest
                                    .getNativeRequest(MultipartHttpServletRequest.class);

                        String partName = parameter.getParameterAnnotation(RequestPart.class).value();
                        var part = multipartRequest.getPart(partName);

                        if (part != null && part.getSize() > 0) {
                              String json = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                              return objectMapper.readValue(json, parameter.getParameterType());
                        }
                        return null;
                  }
            });
      }
}
