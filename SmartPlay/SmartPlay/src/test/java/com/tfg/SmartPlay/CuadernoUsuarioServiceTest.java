package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.*;
import com.tfg.SmartPlay.repository.*;
import com.tfg.SmartPlay.service.CuadernoUsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CuadernoUsuarioServiceTest {

    @InjectMocks
    private CuadernoUsuarioService cuadernoUsuarioService;

    @Mock
    private CuadernoRepository cuadernoRepository;

    @Mock
    private CuadernoUsuarioRepository cuadernoUsuarioRepository;

    @Mock
    private FichaUsuarioRepository fichaUsuarioRepository;

    private User user;
    private Cuaderno cuaderno;
    private Ficha ficha1;
    private Ficha ficha2;
    private FichaUsuario fichaUsuario1;
    private FichaUsuario fichaUsuario2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        ficha1 = new Ficha();
        ficha2 = new Ficha();

        cuaderno = new Cuaderno();
        cuaderno.setId(10L);
        cuaderno.setFichas(List.of(ficha1, ficha2));

        fichaUsuario1 = new FichaUsuario();
        fichaUsuario1.setNota(8.0);

        fichaUsuario2 = new FichaUsuario();
        fichaUsuario2.setNota(6.0);
    }

    @Test
    void testActualizarNota() {
        when(cuadernoRepository.findById(10L)).thenReturn(Optional.of(cuaderno));
        when(fichaUsuarioRepository.findByFichaAndUsuario(ficha1, Optional.of(user)))
                .thenReturn(Optional.of(fichaUsuario1));
        when(fichaUsuarioRepository.findByFichaAndUsuario(ficha2, Optional.of(user)))
                .thenReturn(Optional.of(fichaUsuario2));
        when(cuadernoUsuarioRepository.findByCuadernoAndUsuario(cuaderno, Optional.of(user)))
                .thenReturn(Optional.empty());

        ArgumentCaptor<CuadernoUsuario> captor = ArgumentCaptor.forClass(CuadernoUsuario.class);

        cuadernoUsuarioService.actualizarNota(10L, Optional.of(user));

        verify(cuadernoUsuarioRepository).save(captor.capture());

        CuadernoUsuario guardado = captor.getValue();
        assertEquals(6.0, guardado.getNota());
        assertEquals(user, guardado.getUsuario());
        assertEquals(cuaderno, guardado.getCuaderno());
    }

    @Test
    void testObtenerNota() {
        CuadernoUsuario cuadernoUsuario = new CuadernoUsuario();
        cuadernoUsuario.setNota(9.5);

        when(cuadernoUsuarioRepository.findByCuadernoIdAndUsuarioId(10L, 1L))
                .thenReturn(Optional.of(cuadernoUsuario));

        Optional<CuadernoUsuario> resultado = cuadernoUsuarioService.obtenerNota(10L, 1L);

        assertTrue(resultado.isPresent());
        assertEquals(9.5, resultado.get().getNota());
    }
}
