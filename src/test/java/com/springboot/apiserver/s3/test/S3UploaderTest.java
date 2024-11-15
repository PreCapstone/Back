package com.springboot.apiserver.s3.test;

import com.springboot.apiserver.s3.uploader.S3Uploader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
public class S3UploaderTest {

    @Autowired
    private S3Uploader s3Uploader;

    @Test
    public void testUploadAndDeleteFile() throws IOException {
        // 1. 로컬 파일 생성
        String testFilePath = "./test.PNG";
        Path filePath = Paths.get(testFilePath);

        // S3로 파일 업로드
        String fileUrl = s3Uploader.upload(testFilePath);
        System.out.println("File uploaded to S3: " + fileUrl);

        }
}

