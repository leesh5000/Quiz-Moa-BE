package com.leesh.quiz.global.configuration;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.Test;

class JasyptConfigurationTest {

    @Test
    public void jasyptTest() {
        String password = "QP!)2KDQPOQMKWIJHAPLKWPQOKD(@(@";
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(4);
        encryptor.setPassword(password);
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
        String content = "31866c2bb69e13554a3499771db4af1308776334b2eb432dca274143d5dca6863339ce589147df9e7b84355513c260d0fd07396eddbf3424cdf78b57dd610614";    // 암호화 할 내용
        String encryptedContent = encryptor.encrypt(content); // 암호화
        String decryptedContent = encryptor.decrypt(encryptedContent); // 복호화
        System.out.println("Enc : " + encryptedContent + ", Dec: " + decryptedContent);
    }

}