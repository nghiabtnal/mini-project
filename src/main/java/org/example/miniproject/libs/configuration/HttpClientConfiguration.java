package org.example.miniproject.libs.configuration;

import lombok.AllArgsConstructor;
import org.example.miniproject.libs.configuration.entity.RestTemplateHeaderModifierInterceptor;
import org.example.miniproject.libs.configuration.entity.RestemplateErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Configuration
public class HttpClientConfiguration {

//    @Value("${rest_template.client.timeout:600000}")
//    private Integer clientTimeout;
//
//    @Value("${rest_template.server.timeout:600000}")
//    private Integer serverTimeout;
//
//    @Bean
//    public RestTemplate restTemplate()
//    {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(Integer.valueOf(clientTimeout));
//        requestFactory.setReadTimeout(Integer.valueOf(serverTimeout));
//        restTemplate.setRequestFactory(requestFactory);
//        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
//        interceptors.add(new RestTemplateHeaderModifierInterceptor());
//        restTemplate.setInterceptors(interceptors);
//        RestemplateErrorHandler handler = new RestemplateErrorHandler();
//        restTemplate.setErrorHandler(handler);
//        return restTemplate;
//    }


}
