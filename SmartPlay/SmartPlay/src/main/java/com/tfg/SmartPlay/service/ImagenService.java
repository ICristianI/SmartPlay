package com.tfg.SmartPlay.service;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;

@Service
public class ImagenService {

    public Blob saveImage(MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            return BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize());
        }
        return null;
    }
}
