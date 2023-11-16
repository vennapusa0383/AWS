package com.alt;

import java.io.File;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
 
public class UploadFileToS3 {
    public static void main(String[] args) {
        String bucketName = "codejava-bucket";
        String folderName = "photos";
         
        String fileName = "Java Logo.png";
        String filePath = "D:/Images/" + fileName;
        String key = folderName + "/" + fileName;
         
        S3Client client = S3Client.builder().build();
         
        PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .acl("public-read")
                        .build();
         
        client.putObject(request, RequestBody.fromFile(new File(filePath)));
         
        S3Waiter waiter = client.waiter();
        HeadObjectRequest requestWait = HeadObjectRequest.builder().bucket(bucketName).key(key).build();
         
        WaiterResponse<HeadObjectResponse> waiterResponse = waiter.waitUntilObjectExists(requestWait);
         
        waiterResponse.matched().response().ifPresent(System.out::println);
         
        System.out.println("File " + fileName + " was uploaded.");     
    }
}
