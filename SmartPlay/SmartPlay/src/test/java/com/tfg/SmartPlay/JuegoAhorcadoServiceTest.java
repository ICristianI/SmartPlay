package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.JuegoAhorcadoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.ImagenService;
import com.tfg.SmartPlay.service.JuegoAhorcadoService;
import com.tfg.SmartPlay.service.JuegoService;

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
public class JuegoAhorcadoServiceTest {

    @InjectMocks
    private JuegoAhorcadoService juegoAhorcadoService;

    @Mock
    private JuegoService juegoService;

    @Mock
    private JuegoAhorcadoRepository juegoAhorcadoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CuadernoRepository cuadernoRepository;

    @Mock
    private ImagenService imagenService;

    private User usuario;
    private JuegoAhorcado juego;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setEmail("test@correo.com");

        juego = new JuegoAhorcado();
        juego.setId(10L);
        juego.setNombre("Juego de prueba");
        juego.setUsuario(usuario);
    }

    @Test
    void testGuardarJuegoConImagen() throws Exception {
        MockMultipartFile imagen = new MockMultipartFile("file", "ahorcado.jpg", "image/jpeg", "datos".getBytes());
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(imagenService.saveImage(imagen)).thenReturn(mock(Blob.class));

        juegoAhorcadoService.guardarJuego(juego, usuario.getEmail(), imagen);

        verify(juegoAhorcadoRepository).save(juego);
        assertEquals(usuario, juego.getUsuario());
        assertNotNull(juego.getImagen());
    }

    @Test
    void testGuardarJuegoSinImagenUsaPorDefecto() throws Exception {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        // No se proporciona imagen
        juegoAhorcadoService.guardarJuego(juego, usuario.getEmail(), null);

        verify(juegoAhorcadoRepository).save(juego);
        assertEquals(usuario, juego.getUsuario());
        assertNotNull(juego.getImagen());
    }

    @Test
    void testEditarJuego() {
        JuegoAhorcado juegoEditado = new JuegoAhorcado();
        juegoEditado.setNombre("Modificado");
        juegoEditado.setIdioma("Español");
        juegoEditado.setAsignatura("Lengua");
        juegoEditado.setContenido("Contenido");
        juegoEditado.setDescripcion("Desc");
        juegoEditado.setPalabra("AHORCADO");
        juegoEditado.setPrivada(true);
        juegoEditado.setMaxIntentos(6);

        when(juegoService.obtenerJuego(juego.getId(), usuario.getEmail())).thenReturn(Optional.of(juego));

        juegoAhorcadoService.editarJuego(juego.getId(), juegoEditado, usuario.getEmail());

        assertEquals("Modificado", juego.getNombre());
        assertEquals("AHORCADO", juego.getPalabra());
        verify(juegoAhorcadoRepository).save(juego);
    }

    @Test
    void testObtenerJuego_ExisteYEsDelUsuario() {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoAhorcadoRepository.findById(juego.getId())).thenReturn(Optional.of(juego));

        Optional<JuegoAhorcado> resultado = juegoAhorcadoService.obtenerJuego(juego.getId(), usuario.getEmail());

        assertTrue(resultado.isPresent());
        assertEquals(juego, resultado.get());
    }

    @Test
    void testObtenerJuego_NoEsDelUsuario() {
        User otro = new User();
        otro.setId(99L);
        juego.setUsuario(otro);

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoAhorcadoRepository.findById(juego.getId())).thenReturn(Optional.of(juego));

        Optional<JuegoAhorcado> resultado = juegoAhorcadoService.obtenerJuego(juego.getId(), usuario.getEmail());

        assertTrue(resultado.isEmpty());
    }
}
