package com.nnt.demo.services.impl;

import com.nnt.demo.entities.User;
import com.nnt.demo.exeception.OAuth2AuthenticationProcessingException;
import com.nnt.demo.model.GoogleOAuth2UserInfo;
import com.nnt.demo.model.OAuth2UserInfoFactory;
import com.nnt.demo.model.UserPrincipal;
import com.nnt.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        GoogleOAuth2UserInfo googleOAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(googleOAuth2UserInfo.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(googleOAuth2UserInfo.getEmail());
        User user = new User();
        if(userOptional.isPresent()) {
            user = updateExistingUser(userOptional.get(), googleOAuth2UserInfo);
        }
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User updateExistingUser(User existingUser, GoogleOAuth2UserInfo googleOAuth2UserInfo) {
        existingUser.setProviderId(googleOAuth2UserInfo.getId());
        existingUser.setImageUrl(googleOAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
