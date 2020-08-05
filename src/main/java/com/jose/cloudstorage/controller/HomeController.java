package com.jose.cloudstorage.controller;

import com.jose.cloudstorage.model.User;
import com.jose.cloudstorage.service.CredentialService;
import com.jose.cloudstorage.service.FileService;
import com.jose.cloudstorage.service.NoteService;
import com.jose.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/", "/home"})
public class HomeController {

    private final UserService userService;
    private final NoteService noteService;
    private final FileService fileService;
    private final CredentialService credentialService;

    public HomeController(UserService userService, NoteService noteService, FileService fileService, CredentialService credentialService) {
        this.userService = userService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String homeView(Model model, Authentication authentication) {
        User user = userService.getUser(authentication.getPrincipal().toString());

        model.addAttribute("notes", noteService.getAllByUserId(user.getUserId()));
        model.addAttribute("files", fileService.getAllByUserId(user.getUserId()));
        model.addAttribute("credentials", credentialService.getAllByUserId(user.getUserId()));

        return "home";
    }
}
