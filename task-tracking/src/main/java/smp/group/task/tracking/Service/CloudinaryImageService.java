package smp.group.task.tracking.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryImageService {
    public Map upload(MultipartFile file) throws IOException;


    Map deleteImg(String public_id) throws IOException;
}
