package com.jose.cloudstorage.controller;

import com.jose.cloudstorage.model.Credential;
import com.jose.cloudstorage.model.User;
import com.jose.cloudstorage.service.CredentialService;
import com.jose.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {

    private final UserService userService;
    private final CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping(value = "/add-credential")
    public String addCredential(Authentication authentication, Credential credential) {
        User user = userService.getUser(authentication.getPrincipal().toString());
        boolean result;

        if (credential.getCredentialId() == 0) {
            result = credentialService.create(credential, user.getUserId());
        } else {
            result = credentialService.update(credential, user.getUserId());
        }

        if (!result) {
            return "redirect:/result?error";
        }

        return "redirect:/result?success";
    }

    @GetMapping(value = "/delete-credential/{id}")
    public String deleteCredential(Authentication authentication, @PathVariable("credentialId") Integer credentialId) {
        User user = userService.getUser(authentication.getPrincipal().toString());
        boolean result = credentialService.delete(credentialId, user.getUserId());

        if (!result) {
            return "redirect:/result?error";
        }

        return "redirect:/result?success";
    }
}
