package com.jose.cloudstorage.service;

import com.jose.cloudstorage.mapper.FileMapper;
import com.jose.cloudstorage.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getAll() {
        return fileMapper.findAll();
    }

    public List<File> getAllByUserId(Integer userId) {
        return fileMapper.findByUserId(userId);
    }

    public File getById(Integer fileId, Integer userId) {
        return fileMapper.findById(fileId, userId);
    }

    public boolean addFile(MultipartFile file, Integer userId) {
        try {
            File theFile = new File(file.getOriginalFilename(), file.getContentType(), String.valueOf(file.getSize()), file.getBytes());

            fileMapper.insert(theFile, userId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFile(Integer fileId, Integer userId) {
        Integer result = fileMapper.delete(fileId, userId);

        return result > 0;
    }
}
