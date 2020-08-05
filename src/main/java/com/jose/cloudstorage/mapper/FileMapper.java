package com.jose.cloudstorage.mapper;

import com.jose.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES")
    List<File> findAll();

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> findByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId} AND userid = #{userId}")
    File findById(Integer fileId, Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, filedata, userid) " +
            "VALUES (#{file.fileName}, #{file.contentType}, #{file.fileSize}, #{file.fileData}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "file.fileId", keyColumn = "fileid")
    Integer insert(@Param("file") File file, Integer userId);

    @Update("UPDATE FILES SET filename = #{file.fileName}, contenttype = #{file.contentType}, filesize = #{file.fileSize}, " +
            "filedata = #{file.fileData} " +
            "WHERE fileid = #{file.fileId} AND userid = #{userid}")
    Integer update(@Param("file") File file, Long userId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId} AND userid = #{userId}")
    Integer delete(Integer fileId, Integer userId);
}
