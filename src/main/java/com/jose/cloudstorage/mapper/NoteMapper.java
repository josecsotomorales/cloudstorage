package com.jose.cloudstorage.mapper;

import com.jose.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES")
    List<Note> findAll();

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> findByUserId(Integer userId);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note findById(Integer noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " +
            "VALUES (#{note.noteTitle}, #{note.noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "note.noteId")
    Integer insert(@Param("note") Note note, Integer userId);

    @Update("UPDATE NOTES SET notetitle = #{note.noteTitle}, notedescription = #{note.noteDescription} " +
            "WHERE noteid = #{note.noteId} AND userid = #{userId}")
    Integer update(@Param("note") Note note, Integer userId);

    @Delete("DELETE FROM NOTES WHERE noteid = #{id} AND userid = #{userId}")
    Integer delete(Integer id, Integer userId);
}
