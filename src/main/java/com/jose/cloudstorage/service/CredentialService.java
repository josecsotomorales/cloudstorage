package com.jose.cloudstorage.service;

import com.jose.cloudstorage.mapper.CredentialMapper;
import com.jose.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getAll() {
        List<Credential> credentials = credentialMapper.findAll();

        if (credentials != null) {
            for (Credential credential : credentials) {
                credential.setDecryptedPassword(decryptPassword(credential.getPassword(),
                        credential.getKey()));
            }

            return credentials;
        } else {
            return new ArrayList<>();
        }

    }

    public List<Credential> getAllByUserId(Integer userId) {
        List<Credential> credentials = credentialMapper.findByUserId(userId);

        if (credentials != null) {
            for (Credential credential : credentials) {
                credential.setDecryptedPassword(decryptPassword(credential.getPassword(),
                        credential.getKey()));
            }

            return credentials;
        } else {
            return new ArrayList<>();
        }

    }

    public Credential getById(Integer id) {
        Credential credential = credentialMapper.findById(id);

        if (credential != null) {
            credential.setDecryptedPassword(decryptPassword(credential.getPassword(), credential.getKey()));
        }

        return credential;
    }

    public boolean create(Credential credential, Integer userId) {
        Integer result = credentialMapper.insert(encryptPassword(credential), userId);

        return result > 0;
    }

    public boolean update(Credential credential, Integer userId) {
        Integer result = credentialMapper.update(encryptPassword(credential), userId);

        return result > 0;
    }

    public boolean delete(Integer credentialId, Integer userId) {
        Integer result = credentialMapper.delete(credentialId, userId);

        return result > 0;
    }

    private Credential encryptPassword(Credential credential) {
        if (credential.getKey() == null) {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            credential.setKey(Base64.getEncoder().encodeToString(key));
        }

        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());

        credential.setPassword(encryptedPassword);

        return credential;
    }

    private String decryptPassword(String password, String key) {
        return encryptionService.decryptValue(password, key);
    }
}
