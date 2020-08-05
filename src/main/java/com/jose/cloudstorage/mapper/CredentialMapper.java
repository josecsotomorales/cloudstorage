package com.jose.cloudstorage.mapper;

import com.jose.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS")
    List<Credential> findAll();

    @Select("SELECT * FROM credentials WHERE userid = #{userId}")
    List<Credential> findByUserId(Integer userId);

    @Select("SELECT * FROM credentials WHERE credentialid = #{credentialId}")
    Credential findById(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) " +
            "VALUES (#{credential.url}, #{credential.username}, #{credential.key}, #{credential.password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credential.credentialId", keyColumn = "credentialid")
    Integer insert(@Param("credential") Credential credential, Integer userId);

    @Update("UPDATE CREDENTIALS SET url = #{credential.url}, username = #{credential.username}, " +
            "key = #{credential.key}, password = #{credential.password} " +
            "WHERE credentialid = #{credential.credentialId} AND userid = #{userId}")
    Integer update(@Param("credential") Credential credential, Integer userId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
    Integer delete(Integer credentialId, Integer userId);
}
