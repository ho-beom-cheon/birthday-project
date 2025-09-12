package com.birthday.chb;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class JasyptEncryptorTest {

    @Test
    void encrypt_password() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        // .env 파일에 정의된 Jasypt 비밀키를 여기에 입력합니다.
        encryptor.setPassword("96s#a^238Bw@x!11zT7y$2pQ");
        encryptor.setAlgorithm("PBEWithMD5AndDES");

        // .env 파일에 정의된 실제 DB 비밀번호를 여기에 입력합니다.
        String plainText = "cJS414GHQJA!";
        String encryptedText = encryptor.encrypt(plainText);

        System.out.println("Encrypted Password: " + encryptedText);
    }
}