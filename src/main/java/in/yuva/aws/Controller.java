package in.yuva.aws;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequestMapping(Method.API)
public class Controller {

    private final Service service;

    public Controller(Service service){
        this.service = service;
    }

    @GetMapping(Method.UP)
    public String health() {
        return "OK";
    }

    @PostMapping(value = Method.UPLOAD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("Inside upload method {}" , file.getOriginalFilename());
        return service.uploadFile(file.getOriginalFilename(), file);
    }

    @GetMapping(Method.DOWNLOAD)
    public ResponseEntity<Resource> download(@RequestParam("filename") String filename) {
        log.info("Inside download method {}" , filename);
        var S3Object = service.getObject(filename);
        var content = S3Object.getObjectContent();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(content));
    }

    @GetMapping(Method.VIEW)
    public ResponseEntity<InputStreamResource> view(@RequestParam("filename") String filename) {
        log.info("Inside view method {}" , filename);
        var S3Object = service.getObject(filename);
        var content = S3Object.getObjectContent();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(new InputStreamResource(content));
    }

    @DeleteMapping(Method.DELETE)
    public String delete(@RequestParam("filename") String filename) {
        log.info("Inside delete method {}" , filename);
        return service.deleteObject(filename);
    }
}
