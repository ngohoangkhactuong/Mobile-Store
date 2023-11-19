package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.path}")
    private String UPLOAD_DIR;

    /**
     * Method upload file image
     *
     * @param file
     * @return fileName of image
     */
    @Override
    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }

        // Generate a unique file name to avoid overwriting existing files
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String fileName = file.getOriginalFilename();

        // Create the directory for storing uploaded files if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Copy the uploaded file to the upload directory
        Path targetPath = Paths.get(UPLOAD_DIR, fileName);
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }


    /**
     * Method download file image
     *
     * @param fileName
     * @return resource
     */
    @Override
    public Resource downloadFile(String fileName) {

        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Resource resource = null;

        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                new ResourceNotFoundException(Collections.singletonMap("fileName", fileName));
            }
        } catch (MalformedURLException e) {
            new ResourceNotFoundException(Collections.singletonMap("fileName", fileName));
        }

        return resource;
    }

    /**
     * Method delete file image
     *
     * @param fileName
     */
    @Override
    public void deleteFile(String fileName) {
        try {
            if (StringUtils.isNotBlank(fileName)) {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR, fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method generate copy
     *
     * @param fileName
     * @return copyFileName of image
     */
    @Override
    public String generateCopy(String fileName) {
        Path sourceFilePath = Paths.get(UPLOAD_DIR, fileName);

        try {
            // Create the directory for storing copied files if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFilename = fileName.split("_", 2)[1];
            String copyFileName = System.currentTimeMillis() + "_" + originalFilename;

            // Copy the file with the new name
            Path targetFilePath = Paths.get(UPLOAD_DIR, copyFileName);
            Files.copy(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);

            return copyFileName;
        } catch (IOException e) {
            throw new ResourceNotFoundException(Collections.singletonMap("fileName", fileName));
        }
    }

}
