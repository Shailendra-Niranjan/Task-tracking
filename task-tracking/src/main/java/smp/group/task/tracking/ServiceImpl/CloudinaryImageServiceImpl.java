package smp.group.task.tracking.ServiceImpl;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import smp.group.task.tracking.Service.CloudinaryImageService;

import java.io.IOException;
import java.util.Map;
@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
  @Autowired
  private Cloudinary cloudinary;
    @Override
    public Map upload(MultipartFile file)  {
        try {
         Map data =   this.cloudinary.uploader().upload(file.getBytes() , Map.of());
            System.out.println(data);
            return data;
        }
        catch (IOException e){
            throw  new RuntimeException("Image uploading error !!");
        }

    }



    @Override
    public Map deleteImg(String public_id) throws IOException {

        try {
            Map data =   this.cloudinary.uploader().destroy(public_id ,Map.of());
            System.out.println(data);
            return data;
        }
        catch (IOException e){
            throw  new RuntimeException("Image deleting error !!");
        }
    }


}
