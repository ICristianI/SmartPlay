package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.FichaLike;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.FichaLikeRepository;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.FichaLikeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FichaLikeServiceTest {

    @InjectMocks
    private FichaLikeService fichaLikeService;

    @Mock
    private FichaRepository fichaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FichaLikeRepository fichaLikeRepository;

    private User user;
    private Ficha ficha;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@smartplay.com");

        ficha = new Ficha();
        ficha.setId(10L);
        ficha.setMeGusta(0);
    }

    @Test
    void testAlternarLike_AgregarLike() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));
        when(fichaLikeRepository.findByFichaAndUsuario(ficha, user)).thenReturn(Optional.empty());

        fichaLikeService.alternarLike(user.getEmail(), ficha.getId());

        verify(fichaLikeRepository).save(any(FichaLike.class));
        assertEquals(1, ficha.getMeGusta());
        verify(fichaRepository).save(ficha);
    }

    @Test
    void testAlternarLike_EliminarLike() {
        FichaLike like = new FichaLike(1L, user, ficha);
        ficha.setMeGusta(1);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));
        when(fichaLikeRepository.findByFichaAndUsuario(ficha, user)).thenReturn(Optional.of(like));

        fichaLikeService.alternarLike(user.getEmail(), ficha.getId());

        verify(fichaLikeRepository).delete(like);
        assertEquals(0, ficha.getMeGusta());
        verify(fichaRepository).save(ficha);
    }

    @Test
    void testHaDadoLike_True() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));
        when(fichaLikeRepository.existsByFichaAndUsuario(ficha, user)).thenReturn(true);

        boolean resultado = fichaLikeService.haDadoLike(user.getEmail(), ficha.getId());

        assertTrue(resultado);
    }

    @Test
    void testHaDadoLike_False() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(fichaRepository.findById(ficha.getId())).thenReturn(Optional.of(ficha));
        when(fichaLikeRepository.existsByFichaAndUsuario(ficha, user)).thenReturn(false);

        boolean resultado = fichaLikeService.haDadoLike(user.getEmail(), ficha.getId());

        assertFalse(resultado);
    }

    @Test
    void testHaDadoLike_SiNoExisteUsuarioOFicha() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        boolean resultado = fichaLikeService.haDadoLike(user.getEmail(), ficha.getId());

        assertFalse(resultado);
    }
}
