package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.JuegoSopaLetrasRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.ImagenService;
import com.tfg.SmartPlay.service.JuegoService;
import com.tfg.SmartPlay.service.JuegoSopaLetrasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Blob;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class JuegoSopaLetrasServiceTest {

    @InjectMocks
    private JuegoSopaLetrasService juegoSopaLetrasService;

    @Mock
    private JuegoService juegoService;

    @Mock
    private JuegoSopaLetrasRepository juegoSopaLetrasRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CuadernoRepository cuadernoRepository;

    @Mock
    private ImagenService imagenService;

    private User usuario;
    private JuegoSopaLetras juego;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setEmail("test@correo.com");

        juego = new JuegoSopaLetras();
        juego.setId(5L);
        juego.setUsuario(usuario);
        juego.setNombre("Sopa Test");
    }

    @Test
    void testGuardarJuegoConImagen() throws Exception {
        MockMultipartFile imagen = new MockMultipartFile("file", "sopa.jpg", "image/jpeg", "test data".getBytes());

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(imagenService.saveImage(imagen)).thenReturn(mock(Blob.class));

        juegoSopaLetrasService.guardarJuego(juego, usuario.getEmail(), imagen);

        verify(juegoSopaLetrasRepository).save(juego);
        assertEquals(usuario, juego.getUsuario());
        assertNotNull(juego.getImagen());
    }

    @Test
    void testEditarJuego() {
        JuegoSopaLetras juegoEditado = new JuegoSopaLetras();
        juegoEditado.setNombre("Nuevo");
        juegoEditado.setIdioma("Español");
        juegoEditado.setAsignatura("Lengua");
        juegoEditado.setContenido("Contenido");
        juegoEditado.setDescripcion("Descripción");
        juegoEditado.setPrivada(true);
        juegoEditado.setPalabras("casa,perro");

        when(juegoService.obtenerJuego(juego.getId(), usuario.getEmail())).thenReturn(Optional.of(juego));

        juegoSopaLetrasService.editarJuego(juego.getId(), juegoEditado, usuario.getEmail());

        assertEquals("Nuevo", juego.getNombre());
        assertEquals("casa,perro", juego.getPalabras());
        verify(juegoSopaLetrasRepository).save(juego);
    }

    @Test
    void testObtenerJuego_SiPerteneceAlUsuario() {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoSopaLetrasRepository.findById(juego.getId())).thenReturn(Optional.of(juego));

        Optional<JuegoSopaLetras> resultado = juegoSopaLetrasService.obtenerJuego(juego.getId(), usuario.getEmail());

        assertTrue(resultado.isPresent());
        assertEquals(juego, resultado.get());
    }

    @Test
    void testObtenerJuego_NoPerteneceAlUsuario() {
        User otro = new User();
        otro.setId(99L);
        juego.setUsuario(otro);

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoSopaLetrasRepository.findById(juego.getId())).thenReturn(Optional.of(juego));

        Optional<JuegoSopaLetras> resultado = juegoSopaLetrasService.obtenerJuego(juego.getId(), usuario.getEmail());

        assertTrue(resultado.isEmpty());
    }
}
