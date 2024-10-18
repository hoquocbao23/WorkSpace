package fpt.swp.WorkSpace.service;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwsS3Service {

     final String bucketname = "swp-workspace-images" ;       // bucket name on S3

    @Autowired
    private AmazonS3 s3Client;


     public String saveImgToS3(MultipartFile file) {
         String s3LocationImg = null;

         try {
             String fileName = file.getOriginalFilename();


             InputStream inputStream = file.getInputStream();
             ObjectMetadata metadata = new ObjectMetadata();
             metadata.setContentType("image/jpeg");

             PutObjectRequest putObjectRequest = new PutObjectRequest(bucketname, fileName, inputStream, metadata);
             s3Client.putObject(putObjectRequest);
             return s3LocationImg = "https://"+bucketname +".s3.amazonaws.com/" + fileName;

         } catch (Exception e) {
             throw new RuntimeException(e.getMessage());
         }
     }

    public String saveMultiImgToS3(MultipartFile[] files) {
        String s3LocationImg = null;
        List<String> listFiles = new ArrayList<>();
        String listUrl;
        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                InputStream inputStream = file.getInputStream();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("image/jpeg");

                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketname, fileName, inputStream, metadata);
                s3Client.putObject(putObjectRequest);
                 s3LocationImg = "https://"+bucketname +".s3.amazonaws.com/" + fileName;
                 listFiles.add(s3LocationImg);
            }
             listUrl = String.join(", ", listFiles);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return listUrl;
    }


}
