package com.rookies.ecommerce.service.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service interface for handling file upload-related operations.
 */
public interface UploadService {

    /**
     * Uploads a file to a specified folder with a given file name.
     *
     * @param file the file to be uploaded
     * @param folderName the name of the folder where the file will be stored
     * @param fileName the name to assign to the uploaded file
     * @return the URL or identifier of the uploaded file
     * @throws IOException if an error occurs during file upload
     */
    String uploadFile(MultipartFile file, String folderName, String fileName) throws IOException;

    /**
     * Deletes a file by its public identifier.
     *
     * @param publicId the public identifier of the file to be deleted
     */
    void deleteFile(String publicId);

    /**
     * Renames a file from one name to another.
     *
     * @param from the current name of the file
     * @param to the new name to assign to the file
     * @return the updated name or identifier of the file
     * @throws IOException if an error occurs during file renaming
     */
    String renameFile(String from, String to) throws IOException;

}