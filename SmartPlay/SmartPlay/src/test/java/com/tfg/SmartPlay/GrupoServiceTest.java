package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Grupo;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.GrupoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.GrupoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class GrupoServiceTest {

    @InjectMocks
    private GrupoService grupoService;

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CuadernoRepository cuadernoRepository;

    private User creador;
    private Grupo grupo;

    @BeforeEach
    void setUp() {
        creador = new User();
        creador.setId(1L);
        creador.setEmail("profesor@test.com");

        grupo = new Grupo();
        grupo.setId(100L);
        grupo.setNombre("Grupo Test");
        grupo.setCreador(creador);
        grupo.setUsuarios(new ArrayList<>(List.of(creador)));
    }

    @Test
    void testGuardarGrupo() {
        Cuaderno cuaderno = new Cuaderno();
        cuaderno.setId(10L);

        when(userRepository.findByEmail(creador.getEmail())).thenReturn(Optional.of(creador));
        when(cuadernoRepository.findAllById(List.of(10L))).thenReturn(List.of(cuaderno));
        when(grupoRepository.existsByCodigoAcceso(anyString())).thenReturn(false);
        when(grupoRepository.save(any(Grupo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Grupo guardado = grupoService.guardarGrupo(grupo, creador.getEmail(), List.of(10L));

        assertEquals("Grupo Test", guardado.getNombre());
        assertEquals(creador, guardado.getCreador());
        assertTrue(guardado.getCuadernos().contains(cuaderno));
        assertTrue(guardado.getUsuarios().contains(creador));
        assertNotNull(guardado.getCodigoAcceso());
    }

    @Test
    void testUnirseAGrupo() {
        User alumno = new User();
        alumno.setId(2L);
        alumno.setEmail("alumno@test.com");

        when(grupoRepository.findByCodigoAcceso(anyString())).thenReturn(Optional.of(grupo));
        when(userRepository.findByEmail(alumno.getEmail())).thenReturn(Optional.of(alumno));

        boolean resultado = grupoService.unirseAGrupo("CODIGO123", alumno.getEmail());

        assertTrue(resultado);
        verify(grupoRepository).save(grupo);
        assertTrue(grupo.getUsuarios().contains(alumno));
    }

    @Test
    void testEliminarGrupo_SiEsCreador() {
        when(grupoRepository.findById(grupo.getId())).thenReturn(Optional.of(grupo));
        when(userRepository.findByEmail(creador.getEmail())).thenReturn(Optional.of(creador));

        boolean eliminado = grupoService.eliminarGrupo(grupo.getId(), creador.getEmail());

        assertTrue(eliminado);
        verify(grupoRepository).deleteById(grupo.getId());
    }

    @Test
    void testObtenerGrupo_Autorizado() {
        when(userRepository.findByEmail(creador.getEmail())).thenReturn(Optional.of(creador));
        when(grupoRepository.findById(grupo.getId())).thenReturn(Optional.of(grupo));

        Grupo resultado = grupoService.obtenerGrupo(grupo.getId(), creador.getEmail());

        assertNotNull(resultado);
        assertEquals(grupo, resultado);
    }

    @Test
    void testListarGruposPaginados() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Grupo> page = new PageImpl<>(List.of(grupo));

        when(userRepository.findByEmail(creador.getEmail())).thenReturn(Optional.of(creador));
        when(grupoRepository.findByUsuariosContaining(creador, pageable)).thenReturn(page);

        Page<Grupo> resultado = grupoService.listarGruposPaginados(creador.getEmail(), 0, 5);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(grupo, resultado.getContent().get(0));
    }
}
