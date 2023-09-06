package com.seo.mobilestore.common.util.impl;

import java.util.UUID;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.seo.mobilestore.common.FileUpload;
import com.seo.mobilestore.common.util.UpdateFile;

@Component
public class UpdateFileImpl implements UpdateFile {
    @Autowired
    ServletContext application;

    @Autowired
    Storage storage;
    @Value("${url.firebase.bucket}")
    String bucketName;
    @Value("${url.firebase.folder}")
    String linkFolder;
    public final static Logger LOGGER = LoggerFactory.getLogger("info");
    public final static String FOLDER_NAME = "images/";

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {

            ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setStorageBucket(bucketName)
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }

    @Override
    public void update(FileUpload fileUpload) {
        // try {
        // DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        // fileUpload.setOutput(LocalDateTime.now().format(formatter) +
        // fileUpload.getFile().getOriginalFilename().substring(fileUpload.getFile().getOriginalFilename().lastIndexOf(".")));
        //
        // Credentials credentials = GoogleCredentials.fromStream(new
        // ClassPathResource("keystorage.json").getInputStream());
        // Storage storage =
        // StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        //
        // String folderName = "images/";
        // BlobId blobId = BlobId.of("r2s",folderName + fileUpload.getOutput());
        // BlobInfo blobInfo =
        // BlobInfo.newBuilder(blobId).setContentType(fileUpload.getFile().getContentType()).build();
        // byte[] arr = fileUpload.getFile().getBytes();
        // storage.create(blobInfo, arr);
        //
        // fileUpload.setOutput("https://storage.googleapis.com/" + bucketName + "/" +
        // folderName +fileUpload.getOutput());
        // fileUpload.setFile(null);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        try {

            UUID uuid = UUID.randomUUID();
            fileUpload.setOutput(uuid +
                    fileUpload.getFile().getOriginalFilename()
                            .substring(fileUpload.getFile().getOriginalFilename().lastIndexOf(".")));

            String name = fileUpload.getOutput();

            byte[] arr = fileUpload.getFile().getBytes();
            Bucket bucket = StorageClient.getInstance().bucket();
            bucket.create(FOLDER_NAME + name, arr, fileUpload.getFile().getContentType());

            fileUpload.setOutput(linkFolder + bucketName + "/o/" + "images%2F"
                    + name + "?alt=media");
            fileUpload.setFile(null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void uploadCV(FileUpload fileUpload) {
        // try {
        // DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        // fileUpload.setOutput(LocalDateTime.now().format(formatter) +
        // fileUpload.getFile().getOriginalFilename().substring(fileUpload.getFile().getOriginalFilename().lastIndexOf(".")));
        //
        // Credentials credentials = GoogleCredentials.fromStream(new
        // ClassPathResource("keystorage.json").getInputStream());
        // Storage storage =
        // StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        //
        // String folderName = "CV/";
        // BlobId blobId = BlobId.of("r2s",folderName + fileUpload.getOutput());
        // BlobInfo blobInfo =
        // BlobInfo.newBuilder(blobId).setContentType(fileUpload.getFile().getContentType()).build();
        // byte[] arr = fileUpload.getFile().getBytes();
        // storage.create(blobInfo, arr);
        //
        // fileUpload.setOutput("https://storage.googleapis.com/" + bucketName + "/" +
        // folderName +fileUpload.getOutput());
        // fileUpload.setFile(null);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        try {

            UUID uuid = UUID.randomUUID();
            fileUpload.setOutput(uuid +
                    fileUpload.getFile().getOriginalFilename()
                            .substring(fileUpload.getFile().getOriginalFilename().lastIndexOf(".")));

            String name = fileUpload.getOutput();

            byte[] arr = fileUpload.getFile().getBytes();
            Bucket bucket = StorageClient.getInstance().bucket();
            bucket.create(FOLDER_NAME + name, arr, fileUpload.getFile().getContentType());

            fileUpload.setOutput(linkFolder + bucketName + "/o/" + "images%2F"
                    + name + "?alt=media");
            fileUpload.setFile(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadExcel(FileUpload fileUpload) {
        // try {
        // DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        // fileUpload.setOutput(LocalDateTime.now().format(formatter) +
        // fileUpload.getFile().getOriginalFilename().substring(fileUpload.getFile().getOriginalFilename().lastIndexOf(".")));
        //
        // Credentials credentials = GoogleCredentials.fromStream(new
        // ClassPathResource("keystorage.json").getInputStream());
        // Storage storage =
        // StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        //
        // String folderName = "Students/";
        // BlobId blobId = BlobId.of("r2s",folderName + fileUpload.getOutput());
        // BlobInfo blobInfo =
        // BlobInfo.newBuilder(blobId).setContentType(fileUpload.getFile().getContentType()).build();
        // byte[] arr = fileUpload.getFile().getBytes();
        // storage.create(blobInfo, arr);
        //
        // fileUpload.setOutput("https://storage.googleapis.com/" + bucketName + "/" +
        // folderName +fileUpload.getOutput());
        // fileUpload.setFile(null);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        try {

            UUID uuid = UUID.randomUUID();
            fileUpload.setOutput(uuid +
                    fileUpload.getFile().getOriginalFilename()
                            .substring(fileUpload.getFile().getOriginalFilename().lastIndexOf(".")));

            String name = fileUpload.getOutput();

            byte[] arr = fileUpload.getFile().getBytes();
            Bucket bucket = StorageClient.getInstance().bucket();
            bucket.create(FOLDER_NAME + name, arr, fileUpload.getFile().getContentType());

            fileUpload.setOutput(linkFolder + bucketName + "/o/" + "images%2F"
                    + name + "?alt=media");
            fileUpload.setFile(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String fullPath) {
        // try {
        // String name = fullPath.substring(fullPath.lastIndexOf("/") + 1);
        //
        // Credentials credentials = GoogleCredentials.fromStream(new
        // ClassPathResource("keystorage.json").getInputStream());
        // Storage storage =
        // StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        //
        // storage.deleteById("r2s", name);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        try {
            String name = fullPath.substring(fullPath.lastIndexOf("%") + 3);
            String pathImg = name.substring(0, name.indexOf("?"));

            System.out.println(pathImg + " " + fullPath.lastIndexOf("%"));
            Bucket bucket = StorageClient.getInstance().bucket(bucketName);

            Blob blob = bucket.get(FOLDER_NAME + pathImg);

            blob.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void uploadCVApplication(FileUpload fileUpload) {
        // try {
        // DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        // fileUpload.setOutput(LocalDateTime.now().format(formatter) +
        // fileUpload.getFile().getOriginalFilename().substring(fileUpload.getFile().getOriginalFilename().lastIndexOf(".")));
        //
        // Credentials credentials = GoogleCredentials.fromStream(new
        // ClassPathResource("keystorage.json").getInputStream());
        // Storage storage =
        // StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        //
        // String folderName = "images/";
        // BlobId blobId = BlobId.of("r2s",folderName + fileUpload.getOutput());
        // BlobInfo blobInfo =
        // BlobInfo.newBuilder(blobId).setContentType(fileUpload.getFile().getContentType()).build();
        // byte[] arr = fileUpload.getFile().getBytes();
        // storage.create(blobInfo, arr);
        //
        // fileUpload.setOutput("https://storage.googleapis.com/" + bucketName + "/" +
        // folderName +fileUpload.getOutput());
        // fileUpload.setFile(null);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        try {

            UUID uuid = UUID.randomUUID();
            fileUpload.setOutput(uuid +
                    fileUpload.getFile().getOriginalFilename()
                            .substring(fileUpload.getFile().getOriginalFilename().lastIndexOf(".")));

            String name = fileUpload.getOutput();

            byte[] arr = fileUpload.getFile().getBytes();
            Bucket bucket = StorageClient.getInstance().bucket();
            bucket.create(FOLDER_NAME + name, arr, fileUpload.getFile().getContentType());

            fileUpload.setOutput(linkFolder + bucketName + "/o/" + "images%2F"
                    + name + "?alt=media");
            fileUpload.setFile(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
