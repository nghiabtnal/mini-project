package org.example.miniproject.libs.configuration.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class RestemplateErrorHandler extends DefaultResponseErrorHandler  {
    public void handleError(ClientHttpResponse _res) throws IOException {
        log.info("Handle error");
    }
}
