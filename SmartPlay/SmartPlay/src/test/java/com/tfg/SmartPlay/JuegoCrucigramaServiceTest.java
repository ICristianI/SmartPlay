package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.JuegoCrucigrama;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.JuegoCrucigramaRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.ImagenService;
import com.tfg.SmartPlay.service.JuegoCrucigramaService;
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
public class JuegoCrucigramaServiceTest {

    @InjectMocks
    private JuegoCrucigramaService juegoCrucigramaService;

    @Mock
    private JuegoService juegoService;

    @Mock
    private JuegoCrucigramaRepository juegoCrucigramaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CuadernoRepository cuadernoRepository;

    @Mock
    private ImagenService imagenService;

    private User usuario;
    private JuegoCrucigrama juego;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setEmail("test@correo.com");

        juego = new JuegoCrucigrama();
        juego.setId(10L);
        juego.setNombre("Crucigrama de prueba");
        juego.setUsuario(usuario);
    }

    @Test
    void testGuardarJuegoConImagen() throws Exception {
        MockMultipartFile imagen = new MockMultipartFile("file", "cruci.jpg", "image/jpeg", "datos".getBytes());

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(imagenService.saveImage(imagen)).thenReturn(mock(Blob.class));

        juegoCrucigramaService.guardarJuego(juego, usuario.getEmail(), imagen);

        verify(juegoCrucigramaRepository).save(juego);
        assertEquals(usuario, juego.getUsuario());
        assertNotNull(juego.getImagen());
    }

    @Test
    void testGuardarJuegoSinImagen_UsaPorDefecto() throws Exception {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        juegoCrucigramaService.guardarJuego(juego, usuario.getEmail(), null);

        verify(juegoCrucigramaRepository).save(juego);
        assertEquals(usuario, juego.getUsuario());
        assertNotNull(juego.getImagen());
    }

    @Test
    void testEditarJuego() {
        JuegoCrucigrama juegoEditado = new JuegoCrucigrama();
        juegoEditado.setNombre("Editado");
        juegoEditado.setIdioma("Español");
        juegoEditado.setAsignatura("Lengua");
        juegoEditado.setContenido("Contenido");
        juegoEditado.setDescripcion("Descripción");
        juegoEditado.setPrivada(true);
        juegoEditado.setPistas("pistas");
        juegoEditado.setRespuestas("respuestas");

        when(juegoService.obtenerJuego(juego.getId(), usuario.getEmail())).thenReturn(Optional.of(juego));

        juegoCrucigramaService.editarJuego(juego.getId(), juegoEditado, usuario.getEmail());

        assertEquals("Editado", juego.getNombre());
        assertEquals("pistas", juego.getPistas());
        verify(juegoCrucigramaRepository).save(juego);
    }

    @Test
    void testObtenerJuego_ExisteYEsDelUsuario() {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoCrucigramaRepository.findById(juego.getId())).thenReturn(Optional.of(juego));

        Optional<JuegoCrucigrama> resultado = juegoCrucigramaService.obtenerJuego(juego.getId(), usuario.getEmail());

        assertTrue(resultado.isPresent());
        assertEquals(juego, resultado.get());
    }

    @Test
    void testObtenerJuego_NoEsDelUsuario() {
        User otro = new User();
        otro.setId(99L);
        juego.setUsuario(otro);

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoCrucigramaRepository.findById(juego.getId())).thenReturn(Optional.of(juego));

        Optional<JuegoCrucigrama> resultado = juegoCrucigramaService.obtenerJuego(juego.getId(), usuario.getEmail());

        assertTrue(resultado.isEmpty());
    }
}
