package com.leesh.quiz;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class JasyptRunner {

    @Test
    public void test() {
        System.out.println("Hello World!");

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(4);
        encryptor.setPassword("ThisIsJasyptPassword");
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");

        String content = "leesh";
        String encrypted = encryptor.encrypt(content);
        String decrypted = encryptor.decrypt(encrypted);
        System.out.println("encrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);

    }

}
