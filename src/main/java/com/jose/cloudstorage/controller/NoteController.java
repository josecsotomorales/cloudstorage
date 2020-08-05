package com.jose.cloudstorage.controller;

import com.jose.cloudstorage.model.Note;
import com.jose.cloudstorage.model.User;
import com.jose.cloudstorage.service.NoteService;
import com.jose.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {
    private final UserService userService;
    private final NoteService noteService;

    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping(value = "/add-note")
    public String addNote(Authentication authentication, Note note) {
        User user = userService.getUser(authentication.getPrincipal().toString());
        boolean result;

        if (note.getNoteId() == 0) {
            result = noteService.create(note, user.getUserId());
        } else {
            result = noteService.update(note, user.getUserId());
        }

        if (!result) {
            return "redirect:/result?error";
        }

        return "redirect:/result?success";
    }

    @GetMapping(value = "/delete-note/{noteId}")
    public String deleteNote(Authentication authentication, @PathVariable("noteId") Integer noteId) {
        User user = userService.getUser(authentication.getPrincipal().toString());
        boolean result = noteService.delete(noteId, user.getUserId());

        if (!result) {
            return "redirect:/result?error";
        }

        return "redirect:/result?success";
    }
}
