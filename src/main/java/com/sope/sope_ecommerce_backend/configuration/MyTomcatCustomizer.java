package com.sope.sope_ecommerce_backend.configuration;

import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Integer.MAX_VALUE)
public class MyTomcatCustomizer implements WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> {
      @Override
      public void customize(ConfigurableTomcatWebServerFactory factory) {
            factory.addConnectorCustomizers(connector -> {
                  connector.setMaxParameterCount(1000); // maxParameterCount
                  connector.setMaxPartCount(100); // maxPartCount
                  connector.setMaxPartHeaderSize(1024); // maxPartHeaderSize
            });
      }
}