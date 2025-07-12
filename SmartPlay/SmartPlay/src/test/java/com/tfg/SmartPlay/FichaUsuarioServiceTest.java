package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.FichaUsuario;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.FichaUsuarioRepository;
import com.tfg.SmartPlay.service.FichaUsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class FichaUsuarioServiceTest {

    @InjectMocks
    private FichaUsuarioService fichaUsuarioService;

    @Mock
    private FichaUsuarioRepository fichaUsuarioRepository;

    @Mock
    private FichaRepository fichaRepository;

    private Ficha ficha;
    private User usuario;

    @BeforeEach
    void setUp() {
        ficha = new Ficha();
        ficha.setId(10L);

        usuario = new User();
        usuario.setId(1L);
    }

    @Test
    void testGuardarNota_NuevaRelacion() {
        Double nota = 8.5;

        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));
        when(fichaUsuarioRepository.findByFichaAndUsuario(ficha, Optional.of(usuario)))
            .thenReturn(Optional.empty());

        fichaUsuarioService.guardarNota(ficha.getId(), Optional.of(usuario), nota);

        ArgumentCaptor<FichaUsuario> captor = ArgumentCaptor.forClass(FichaUsuario.class);
        verify(fichaUsuarioRepository).save(captor.capture());

        FichaUsuario guardado = captor.getValue();
        assertEquals(ficha, guardado.getFicha());
        assertEquals(usuario, guardado.getUsuario());
        assertEquals(nota, guardado.getNota());
    }

    @Test
    void testGuardarNota_ActualizarNotaExistente() {
        Double nota = 9.0;
        FichaUsuario existente = new FichaUsuario();
        existente.setFicha(ficha);
        existente.setUsuario(usuario);

        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));
        when(fichaUsuarioRepository.findByFichaAndUsuario(ficha, Optional.of(usuario)))
            .thenReturn(Optional.of(existente));

        fichaUsuarioService.guardarNota(ficha.getId(), Optional.of(usuario), nota);

        verify(fichaUsuarioRepository).save(existente);
        assertEquals(nota, existente.getNota());
    }

    @Test
    void testObtenerNota() {
        FichaUsuario fichaUsuario = new FichaUsuario();
        fichaUsuario.setNota(7.5);

        when(fichaUsuarioRepository.findByFichaIdAndUsuarioId(ficha.getId(), usuario.getId()))
            .thenReturn(Optional.of(fichaUsuario));

        Optional<FichaUsuario> resultado = fichaUsuarioService.obtenerNota(ficha.getId(), usuario.getId());

        assertTrue(resultado.isPresent());
        assertEquals(7.5, resultado.get().getNota());
    }
}
