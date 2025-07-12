package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoLike;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.JuegoLikeRepository;
import com.tfg.SmartPlay.repository.JuegoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.JuegoLikeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class JuegoLikeServiceTest {

    @InjectMocks
    private JuegoLikeService juegoLikeService;

    @Mock
    private JuegoLikeRepository juegoLikeRepository;

    @Mock
    private JuegoRepository juegoRepository;

    @Mock
    private UserRepository userRepository;

    private User usuario;
    private Juego juego;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setEmail("test@correo.com");

        juego = mock(Juego.class);
        when(juego.getId()).thenReturn(100L);
        when(juego.getMeGusta()).thenReturn(5);
    }

    @Test
    void testHaDadoLike_CuandoExisteLike() {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoRepository.findById(juego.getId())).thenReturn(Optional.of(juego));
        when(juegoLikeRepository.existsByJuegoAndUsuario(juego, usuario)).thenReturn(true);

        boolean resultado = juegoLikeService.haDadoLike(usuario.getEmail(), juego.getId());

        assertTrue(resultado);
        verify(juegoLikeRepository).existsByJuegoAndUsuario(juego, usuario);
    }

    @Test
    void testHaDadoLike_CuandoNoExisteLike() {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoRepository.findById(juego.getId())).thenReturn(Optional.of(juego));
        when(juegoLikeRepository.existsByJuegoAndUsuario(juego, usuario)).thenReturn(false);

        boolean resultado = juegoLikeService.haDadoLike(usuario.getEmail(), juego.getId());

        assertFalse(resultado);
    }

    @Test
    void testAlternarLike_AgregaLike() {
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoRepository.findById(juego.getId())).thenReturn(Optional.of(juego));
        when(juegoLikeRepository.findByJuegoAndUsuario(juego, usuario)).thenReturn(Optional.empty());

        boolean resultado = juegoLikeService.alternarLike(usuario.getEmail(), juego.getId());

        assertTrue(resultado);
        assertEquals(5, juego.getMeGusta());
        verify(juegoLikeRepository).save(any(JuegoLike.class));
        verify(juegoRepository).save(juego);
    }

    @Test
    void testAlternarLike_EliminaLike() {
        JuegoLike like = new JuegoLike();
        like.setJuego(juego);
        like.setUsuario(usuario);

        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(juegoRepository.findById(juego.getId())).thenReturn(Optional.of(juego));
        when(juegoLikeRepository.findByJuegoAndUsuario(juego, usuario)).thenReturn(Optional.of(like));

        boolean resultado = juegoLikeService.alternarLike(usuario.getEmail(), juego.getId());

        assertFalse(resultado);
        assertEquals(5, juego.getMeGusta());
        verify(juegoLikeRepository).delete(like);
        verify(juegoRepository).save(juego);
    }

    @Test
    void testAlternarLike_ErrorSiNoExisteUsuario() {
        when(userRepository.findByEmail("invalido@correo.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                juegoLikeService.alternarLike("invalido@correo.com", 123L)
        );

        assertEquals("Usuario o juego no encontrado.", exception.getMessage());
    }
}
