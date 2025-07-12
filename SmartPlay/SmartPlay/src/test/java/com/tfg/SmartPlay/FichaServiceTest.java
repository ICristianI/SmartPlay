package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.*;
import com.tfg.SmartPlay.service.FichaService;
import com.tfg.SmartPlay.service.ImagenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Blob;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class FichaServiceTest {

    @InjectMocks
    private FichaService fichaService;

    @Mock
    private FichaRepository fichaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImagenService imagenService;

    @Mock
    private CuadernoRepository cuadernoRepository;

    @Mock
    private FichaLikeRepository fichaLikeRepository;

    private User usuario;
    private Ficha ficha;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setEmail("test@correo.com");

        ficha = new Ficha();
        ficha.setId(10L);
        ficha.setUsuario(usuario);
        ficha.setNombre("Ficha de prueba");
    }

    @Test
    void testGuardarFichaConImagen() throws Exception {
        MockMultipartFile imagen = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", "test data".getBytes());

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(imagenService.saveImage(imagen)).thenReturn(mock(Blob.class));

        fichaService.guardarFicha(ficha, imagen, usuario.getEmail());

        verify(fichaRepository).save(ficha);
        assertEquals(usuario, ficha.getUsuario());
        assertNotNull(ficha.getImagen());
    }

    @Test
    void testEditarFicha() {
        Ficha fichaEditada = new Ficha();
        fichaEditada.setNombre("Ficha nueva");
        fichaEditada.setIdioma("Español");
        fichaEditada.setAsignatura("Lengua");
        fichaEditada.setContenido("Contenido nuevo");
        fichaEditada.setDescripcion("Descripción nueva");
        fichaEditada.setPrivada(true);

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));

        fichaService.editarFicha(ficha.getId(), fichaEditada, usuario.getEmail());

        assertEquals("Ficha nueva", ficha.getNombre());
        assertEquals("Español", ficha.getIdioma());
        assertTrue(ficha.isPrivada());
        verify(fichaRepository).save(ficha);
    }

@Test
void testEliminarFicha() {
    // Crear un cuaderno y asociarlo a la ficha
    Cuaderno cuaderno = new Cuaderno();
    cuaderno.setId(99L);
    cuaderno.setNumeroFichas(1);
    cuaderno.setFichas(new ArrayList<>(List.of(ficha)));

    // Asignar una lista mutable de cuadernos a la ficha
    ficha.setCuadernos(new ArrayList<>(List.of(cuaderno)));

    // Mocks necesarios
    when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
    when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));

    // Ejecutar el método a testear
    fichaService.eliminarFicha(ficha.getId(), usuario.getEmail());

    // Verificaciones
    verify(cuadernoRepository).save(cuaderno);
    verify(fichaLikeRepository).deleteByFicha_Id(ficha.getId());
    verify(fichaRepository).save(ficha);
    verify(fichaRepository).delete(ficha);

    // Asserts adicionales para validar el estado
    assertTrue(cuaderno.getFichas().isEmpty(), "Las fichas del cuaderno deberían estar vacías");
    assertTrue(ficha.getCuadernos().isEmpty(), "Los cuadernos de la ficha deberían estar vacíos");
    assertEquals(0, cuaderno.getNumeroFichas(), "El número de fichas debería ser 0");
}

    @Test
    void testGuardarElementosSuperpuestos() {
        ficha.setElementosSuperpuestos(null);
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));

        fichaService.guardarElementosSuperpuestos(ficha.getId(), "{\"elemento\":1}", usuario.getEmail());

        assertEquals("{\"elemento\":1}", ficha.getElementosSuperpuestos());
        verify(fichaRepository).save(ficha);
    }

    @Test
    void testObtenerFicha_SiPerteneceAlUsuario() {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));

        Optional<Ficha> resultado = fichaService.obtenerFicha(ficha.getId(), usuario.getEmail());

        assertTrue(resultado.isPresent());
        assertEquals(ficha, resultado.get());
    }

    @Test
    void testObtenerFicha_NoPerteneceAlUsuario() {
        User otroUsuario = new User();
        otroUsuario.setId(2L);
        ficha.setUsuario(otroUsuario);

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));

        Optional<Ficha> resultado = fichaService.obtenerFicha(ficha.getId(), usuario.getEmail());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testObtenerImagenFicha_NoExisteImagen() {
        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));
        ResponseEntity<Object> response = fichaService.obtenerImagenFicha(ficha.getId());
        assertEquals(404, response.getStatusCodeValue());
    }
}
