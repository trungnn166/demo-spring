package com.nnt.demo.common;

import com.nnt.demo.config.AppProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class MethodUtils {
    @Autowired
    private AppProperties appProperties;

    public String generateUrlResetPassword() {
        return UriComponentsBuilder
                .fromUriString(appProperties.getClient().getUrl() + Constants.URL_PAGE_RESET_PASSWORD)
                .queryParam("token", createToken())
                .build().toUriString();
    }

    public String createToken() {
        String token = RandomStringUtils.randomAlphanumeric(20);
        return token;
    }
}
