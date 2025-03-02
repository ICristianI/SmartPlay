package com.tfg.SmartPlay.service;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

@Service
public class ImagenService {

    public Blob saveImage(MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            return BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize());
        }
        return null;
    }

    public Blob getDefaultProfileImage() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/static/images/userImages/Default.png");
        if (inputStream == null) {
            throw new Exception("No se pudo encontrar la imagen por defecto.");
        }
        byte[] defaultImage = inputStream.readAllBytes();
        return new SerialBlob(defaultImage);
    }

    public ResponseEntity<Object> getImageResponse(Blob image) throws SQLException {
        if (image != null) {
            Resource file = new InputStreamResource(image.getBinaryStream());
            String mimeType;
            try {
                mimeType = URLConnection.guessContentTypeFromStream(image.getBinaryStream());
            } catch (IOException e) {
                mimeType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mimeType)
                    .contentLength(image.length())
                    .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
