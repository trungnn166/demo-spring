package com.nnt.demo.common;

import com.nnt.demo.config.AppProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class MethodUtils {
    @Autowired
    private AppProperties appProperties;

    public String generateUrlResetPassword(String token) {
        return UriComponentsBuilder
                .fromUriString(appProperties.getClient().getUrl() + Constants.URL_PAGE_RESET_PASSWORD)
                .queryParam("token", token)
                .build().toUriString();
    }

    public String createToken() {
        return RandomStringUtils.randomAlphanumeric(20);
    }

    public static String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkCurrentPassword(String password, String currentPassword) {
        PasswordEncoder passwordEnocder = new BCryptPasswordEncoder();
        return passwordEnocder.matches(password, currentPassword);

    }

    public static boolean isEmpty(String str) {
        return (str == null || str.isBlank());
    }
}
