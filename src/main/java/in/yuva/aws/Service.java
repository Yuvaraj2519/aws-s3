package in.yuva.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@org.springframework.stereotype.Service
public class Service {

    private AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public String uploadFile(String fileName, MultipartFile file) throws IOException {
        var putObjectRequest = s3client.putObject(bucket, fileName, file.getInputStream(), null);
        log.info("{} has been uploaded", putObjectRequest.getMetadata());
        return "File uploaded successfully";
    }

    public S3Object getObject(String key) {
        return s3client.getObject(bucket, key);
    }

    public String deleteObject(String key) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
        s3client.deleteObject(deleteObjectRequest);
        return key+" has been deleted";
    }
}
