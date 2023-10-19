package unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.common.LibroDAO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.BibliotecaDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.CommentoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.service.PostServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostServiceImplIntegrationTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private LettoreDAO lettoreDAO;

    @Before
    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    @Test
    public void getPostByID_Ok(){
        var id= postDAO.findAll().get(0).getId();

        assertNotNull(postService.getPostByID(id));
    }

    @Test
    public void getPostByID_Inesistente(){

        assertNull(postService.getPostByID(500));
    }


    @Test
    public void aggiungiCommento_UtenteNull(){
        var id= postDAO.findAll().get(0).getId();
        assertNull(postService.aggiungiCommento(id, new CommentoDTO("vuoto"), null));

    }

    @Test
    public void aggiungiCommento_PostInesistente(){
        Lettore lettore=lettoreDAO.findLettoreByEmail("antoniorenatomontefusco@gmail.com","Lettore");
        assertNull(postService.aggiungiCommento(500, new CommentoDTO("prova commento"), lettore));
    }



    @Test
    public void getCommentiByPostId_null(){

        assertEquals(postService.getCommentiByPostId(500),new ArrayList<>());

    }


}
