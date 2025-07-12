package com.tfg.SmartPlay;


import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.JuegoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.CuadernoService;
import com.tfg.SmartPlay.service.FichaService;
import com.tfg.SmartPlay.service.ImagenService;
import com.tfg.SmartPlay.service.JuegoService;
import com.tfg.SmartPlay.service.UserComponent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import javax.sql.rowset.serial.SerialBlob;

import java.sql.Blob;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CuadernoServiceTest {

    @InjectMocks
    private CuadernoService cuadernoService;

    @Mock
    private CuadernoRepository cuadernoRepository;
    @Mock
    private FichaRepository fichaRepository;
    @Mock
    private JuegoRepository juegoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserComponent userComponent;
    @Mock
    private FichaService fichaService;
    @Mock
    private JuegoService juegoService;
    @Mock
    private ImagenService imagenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarCuadernoConImagen() throws Exception {
        User user = new User();
        user.setId(1L);
        Cuaderno cuaderno = new Cuaderno();
        List<Ficha> fichas = List.of(new Ficha());
        List<Juego> juegos = List.of(mock(Juego.class));

        MockMultipartFile imagen = new MockMultipartFile("imagen", "test.jpg", "image/jpeg", "data".getBytes());
        Blob blob = new SerialBlob("data".getBytes());

        when(userComponent.getUser()).thenReturn(Optional.of(user));
        when(fichaRepository.findAllById(anyList())).thenReturn(fichas);
        when(juegoRepository.findAllById(anyList())).thenReturn(juegos);
        when(imagenService.saveImage(imagen)).thenReturn(blob);
        when(cuadernoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Cuaderno guardado = cuadernoService.guardarCuaderno(cuaderno, List.of(1L), List.of(1L), imagen);

        assertNotNull(guardado.getImagen());
        assertEquals(user, guardado.getUsuario());
        assertEquals(1, guardado.getNumeroFichas());
        assertEquals(1, guardado.getNumeroJuegos());
    }

    @Test
    void testEditarCuaderno_AgregaFichasYJuegos() {
        Long cuadernoId = 1L;
        String email = "test@example.com";
        User user = new User();
        user.setId(1L);
        Cuaderno cuaderno = new Cuaderno();
        cuaderno.setUsuario(user);
        cuaderno.setFichas(new ArrayList<>());
        cuaderno.setJuegos(new ArrayList<>());
        cuaderno.setNumeroFichas(0);
        cuaderno.setNumeroJuegos(0);

        Ficha ficha = new Ficha();
        Juego juego = mock(Juego.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cuadernoRepository.findById(cuadernoId)).thenReturn(Optional.of(cuaderno));
        when(fichaRepository.findAllById(anyList())).thenReturn(List.of(ficha));
        when(juegoRepository.findAllById(anyList())).thenReturn(List.of(juego));

        boolean resultado = cuadernoService.editarCuaderno(cuadernoId, "Nuevo nombre", List.of(1L), List.of(1L), email);

        assertTrue(resultado);
        assertEquals("Nuevo nombre", cuaderno.getNombre());
        assertEquals(1, cuaderno.getFichas().size());
        assertEquals(1, cuaderno.getJuegos().size());
    }

    @Test
    void testEliminarCuadernoCorrectamente() {
        Long cuadernoId = 1L;
        String email = "user@example.com";

        User user = new User();
        user.setId(1L);

        Cuaderno cuaderno = new Cuaderno();
        cuaderno.setUsuario(user);
        cuaderno.setGrupos(new ArrayList<>());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cuadernoRepository.findById(cuadernoId)).thenReturn(Optional.of(cuaderno));

        boolean eliminado = cuadernoService.eliminarCuaderno(cuadernoId, email);

        assertTrue(eliminado);
        verify(cuadernoRepository).deleteById(cuadernoId);
    }
}
