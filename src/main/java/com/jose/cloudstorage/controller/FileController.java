package com.jose.cloudstorage.controller;

import com.jose.cloudstorage.model.File;
import com.jose.cloudstorage.model.User;
import com.jose.cloudstorage.service.FileService;
import com.jose.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {
    private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/download-file/{id}")
    public ResponseEntity<Resource> downloadFile(Authentication authentication, @PathVariable("fileId") Integer fileId) {
        User user = userService.getUser(authentication.getPrincipal().toString());
        File file = fileService.getById(fileId, user.getUserId());

        if (file != null) {
            HttpHeaders headers = new HttpHeaders();

            headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(
                    "attachment; filename=", file.getFileName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            ByteArrayResource resource = new ByteArrayResource(file.getFileData());

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .contentLength(Long.parseLong(file.getFileSize()))
                    .body(resource);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add-file")
    public String addFile(Authentication authentication, MultipartFile fileUpload) {
        User user = userService.getUser(authentication.getPrincipal().toString());

        if (!fileUpload.isEmpty()) {
            boolean result = fileService.addFile(fileUpload, user.getUserId());

            if (result) {
                return "redirect:/result?success";
            }
        }

        return "redirect:/result?error";
    }

    @GetMapping(value = "/delete-file/{id}")
    public String deleteFile(Authentication authentication, @PathVariable("fileId") Integer fileId) {
        User user = userService.getUser(authentication.getPrincipal().toString());
        boolean result = fileService.deleteFile(fileId, user.getUserId());

        if (!result) {
            return "redirect:/result?error";
        }

        return "redirect:/result?success";
    }
}
