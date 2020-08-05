package com.jose.cloudstorage.service;

import com.jose.cloudstorage.mapper.NoteMapper;
import com.jose.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAll() {
        return noteMapper.findAll();
    }

    public List<Note> getAllByUserId(Integer userId) {
        return noteMapper.findByUserId(userId);
    }

    public Note getById(Integer userId) {
        return noteMapper.findById(userId);
    }

    public boolean create(Note note, Integer userId) {
        return noteMapper.insert(note, userId) > 0;
    }

    public boolean update(Note note, Integer userId) {
        return noteMapper.update(note, userId) > 0;
    }

    public boolean delete(Integer noteId, Integer userId) {
        return noteMapper.delete(noteId, userId) > 0;
    }
}
