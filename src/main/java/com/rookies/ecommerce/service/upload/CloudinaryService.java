package com.rookies.ecommerce.service.upload;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService implements UploadService{

    Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName, String fileName) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folderName,
                        "public_id", fileName,
                        "overwrite", true
                ));
        return uploadResult.get("secure_url").toString();
    }

    @Override
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + publicId, e);
        }
    }

    @Override
    public String renameFile(String from, String to) throws IOException {
        Map result = cloudinary.uploader().rename(from, to, ObjectUtils.emptyMap());
//        System.out.println(result);
        return result.get("secure_url").toString();
    }

}
