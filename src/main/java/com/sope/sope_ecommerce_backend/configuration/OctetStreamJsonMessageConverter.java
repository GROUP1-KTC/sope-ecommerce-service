package com.sope.sope_ecommerce_backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OctetStreamJsonMessageConverter extends MappingJackson2HttpMessageConverter {

      public OctetStreamJsonMessageConverter(ObjectMapper objectMapper) {
            super(objectMapper);
            List<MediaType> mediaTypes = new ArrayList<>(getSupportedMediaTypes());
            mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
            setSupportedMediaTypes(mediaTypes);
      }
}
