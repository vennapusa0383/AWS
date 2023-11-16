package com.alt;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import java.util.ArrayList;
public class DeleteObjects {
	public static void main(String[] args) {

        final String usage = "\n" +
            "Usage: " +
            "   <bucketName> <objectName>\n\n" +
            "Where:\n" +
            "   bucketName - The Amazon S3 bucket to delete the website configuration from.\n"+
            "   objectName - The object name.\n" ;

        if (args.length != 2) {
            System.out.println(usage);
            System.exit(1);
        }

        String bucketName = args[0];
        String objectName = args[1];
        System.out.println("Deleting "+objectName +" from the Amazon S3 bucket: " + bucketName);
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder()
            .region(region)
            .credentialsProvider(credentialsProvider)
            .build();

        deleteBucketObjects(s3, bucketName, objectName);
        s3.close();
    }

    public static void deleteBucketObjects(S3Client s3, String bucketName, String objectName) {

        ArrayList<ObjectIdentifier> toDelete = new ArrayList<>();
        toDelete.add(ObjectIdentifier.builder()
            .key(objectName)
            .build());

        try {
            DeleteObjectsRequest dor = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder()
                .objects(toDelete).build())
                .build();
            
            s3.deleteObjects(dor);

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        
        System.out.println("Done!");
    }

}
