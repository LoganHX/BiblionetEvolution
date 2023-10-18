package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.CommentoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {
    private PostServiceImpl postService;
    @Mock
    private PostDAO postDAO;
    @Mock
    private CommentoDAO commentoDAO;

    @Before
    public void setUp() {
        postService = new PostServiceImpl(postDAO, commentoDAO);
    }

    @Test
    public void getPostByID_Ok(){
        when(postDAO.findById(Mockito.anyInt())).thenReturn(Optional.of(new Post()));
        assertNotNull(postService.getPostByID(Mockito.anyInt()));
    }

    @Test
    public void getPostByID_Inesistente(){
        when(postDAO.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertNull(postService.getPostByID(Mockito.anyInt()));
    }

    @Test
    public void aggiungiCommento_Ok(){
        when(postDAO.findById(Mockito.anyInt())).thenReturn(Optional.of(new Post()));
        assertNotNull(postService.aggiungiCommento(1, new CommentoDTO(), new Lettore()));

    }


    @Test
    public void aggiungiCommento_PostInesistente(){
        when(postDAO.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertNull(postService.aggiungiCommento(1, new CommentoDTO(), new Esperto()));
    }

    @Test
    public void getCommentiByPostId_ok(){
        List<Commento> commenti = new ArrayList<>();
        commenti.add(new Commento());

        when(commentoDAO.findAllCommentiByPostId(Mockito.anyInt())).thenReturn(commenti);
        assertEquals(postService.getCommentiByPostId(1), commenti);

    }

    @Test
    public void getCommentiByPostId_null(){
        List<Commento> commenti = new ArrayList<>();
        commenti.add(new Commento());

        when(commentoDAO.findAllCommentiByPostId(Mockito.anyInt())).thenReturn(null);
        assertNull(postService.getCommentiByPostId(1));

    }

    @Test
    public void getCommentiByPostId_empty(){
        List<Commento> commenti = new ArrayList<>();

        when(commentoDAO.findAllCommentiByPostId(Mockito.anyInt())).thenReturn(commenti);
        assertEquals(postService.getCommentiByPostId(1), commenti);

    }
}
