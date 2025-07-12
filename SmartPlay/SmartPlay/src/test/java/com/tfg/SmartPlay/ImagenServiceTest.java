package com.tfg.SmartPlay;

import com.tfg.SmartPlay.service.ImagenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import javax.sql.rowset.serial.SerialBlob;

import java.io.InputStream;
import java.sql.Blob;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

public class ImagenServiceTest {

    private ImagenService imagenService;

    @BeforeEach
    void setUp() {
        imagenService = new ImagenService();
    }

    @Test
    void testSaveImageConArchivoValido() throws Exception {
        byte[] contenido = "imagen de prueba".getBytes();
        MockMultipartFile archivo = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", contenido);

        Blob blob = imagenService.saveImage(archivo);

        assertNotNull(blob);
        assertEquals(contenido.length, blob.length());
    }

    @Test
    void testSaveImageConArchivoVacio() throws Exception {
        MockMultipartFile archivo = new MockMultipartFile("file", "vacio.jpg", "image/jpeg", new byte[0]);

        Blob blob = imagenService.saveImage(archivo);

        assertNull(blob);
    }

    @Test
    void testGetDefaultProfileImage() throws Exception {
        Blob imagen = imagenService.getDefaultProfileImage();

        assertNotNull(imagen);
        assertTrue(imagen.length() > 0);
    }

    @Test
    void testGetImageResponse_ImagenValida() throws Exception {
        Blob blob = new SerialBlob("contenido".getBytes());

        ResponseEntity<Object> response = imagenService.getImageResponse(blob);

        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Resource);
    }

    @Test
    void testGetImageResponse_ImagenNula() throws Exception {
        ResponseEntity<Object> response = imagenService.getImageResponse(null);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }
}
