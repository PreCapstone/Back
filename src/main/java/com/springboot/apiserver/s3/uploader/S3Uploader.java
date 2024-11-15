package com.springboot.apiserver.s3.uploader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // S3Client를 생성자 주입으로 받습니다.
    public S3Uploader(S3Client s3Client) {
        this.s3Client = s3Client;
        System.out.println(s3Client.toString());
    }

    public String upload(String filePath) throws RuntimeException {
        System.out.println("FilePath : "+filePath);
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        String uploadImageUrl = putS3(path, fileName);
        System.out.println("FN : "+fileName);
        System.out.println(uploadImageUrl);
//        removeOriginalFile(path);
        return uploadImageUrl;
    }

    private String putS3(Path filePath, String fileName) throws RuntimeException {
        try {
            System.out.println(filePath.toString());
            System.out.println(fileName);
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(fileName)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build(),
                    filePath);
            System.out.println("pubObject Clear");
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(fileName)).toString();
        } catch (S3Exception e) {
            System.out.println("S3Exception: " + e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

//    private void removeOriginalFile(Path path) {
//        try {
//            if (path.toFile().delete()) {
//                System.out.println("File delete success");
//            } else {
//                System.out.println("Fail to remove");
//            }
//        } catch (Exception e) {
//            System.out.println("Exception while deleting file: " + e.getMessage());
//        }
//    }

//    public void removeS3File(String fileName) {
//        try {
//            s3Client.deleteObject(DeleteObjectRequest.builder()
//                    .bucket(bucket)
//                    .key(fileName)
//                    .build());
//            System.out.println("File deleted from S3: " + fileName);
//        } catch (S3Exception e) {
//            System.out.println("Failed to delete file from S3: " + e.awsErrorDetails().errorMessage());
//        }
//    }
}
