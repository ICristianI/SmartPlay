package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.JuegoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.ImagenService;
import com.tfg.SmartPlay.service.JuegoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Blob;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class JuegoServiceTest {

    @InjectMocks
    private JuegoService juegoService;

    @Mock
    private JuegoRepository juegoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImagenService imagenService;

    private User usuario;
    private Juego juego;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new User();
        usuario.setId(1L);
        usuario.setEmail("test@correo.com");

        juego = mock(Juego.class);
        when(juego.getId()).thenReturn(100L);
        when(juego.getNombre()).thenReturn("Juego de prueba");
        when(juego.getUsuario()).thenReturn(usuario);
        when(juego.isPrivada()).thenReturn(false);
    }

    @Test
    void testGuardarJuego() {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        juegoService.guardarJuego(juego, usuario.getEmail());

        assertEquals(usuario, juego.getUsuario());
        verify(juegoRepository).save(juego);
    }

    @Test
    void testObtenerJuego() {
        when(juegoRepository.findByIdAndUsuario_Email(100L, usuario.getEmail()))
                .thenReturn(Optional.of(juego));

        Optional<Juego> resultado = juegoService.obtenerJuego(100L, usuario.getEmail());

        assertTrue(resultado.isPresent());
        assertEquals(juego, resultado.get());
    }

    @Test
    void testEliminarJuego() {
        Cuaderno cuaderno = new Cuaderno();
        cuaderno.setId(1L);
        cuaderno.setNumeroJuegos(1);
        cuaderno.setJuegos(new ArrayList<>(List.of(juego)));

        juego.setCuadernos(new ArrayList<>(List.of(cuaderno)));

        when(juegoRepository.findByIdAndUsuario_Email(juego.getId(), usuario.getEmail()))
                .thenReturn(Optional.of(juego));

        juegoService.eliminarJuego(juego.getId(), usuario.getEmail());

        verify(juegoRepository).save(juego);
        verify(juegoRepository).delete(juego);
        assertTrue(juego.getCuadernos().isEmpty());
        assertEquals(0, cuaderno.getNumeroJuegos() - 1);
    }

    @Test
    void testObtenerJuegosPaginadosPorUsuario() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Juego> pagina = new PageImpl<>(List.of(juego), pageable, 1);

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoRepository.obtenerJuegosPaginados(usuario, pageable)).thenReturn(pagina);

        Page<Juego> resultado = juegoService.obtenerJuegosPaginadosPorUsuario(usuario.getEmail(), 0, 10);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(juego, resultado.getContent().get(0));
    }

    @Test
    void testOrdenarPorFechaConBusqueda() {
        Page<Juego> pagina = new PageImpl<>(List.of(juego));
        when(juegoRepository.buscarPublicosPorNombreFecha(eq("buscar"), any())).thenReturn(pagina);

        Page<Juego> resultado = juegoService.ordenarPorFecha("buscar", 0, 10);

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void testOrdenarPorMeGustaSinBusqueda() {
        Page<Juego> pagina = new PageImpl<>(List.of(juego));
        when(juegoRepository.buscarPublicosPorLikes(any())).thenReturn(pagina);

        Page<Juego> resultado = juegoService.ordenarPorMeGusta(null, 0, 10);

        assertEquals(1, resultado.getTotalElements());
    }
}
