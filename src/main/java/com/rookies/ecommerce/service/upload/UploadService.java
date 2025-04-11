package com.rookies.ecommerce.service.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    String uploadFile(MultipartFile file, String folderName, String fileName) throws IOException;

    void deleteFile(String publicId);

    String renameFile(String from, String to) throws IOException;

}
