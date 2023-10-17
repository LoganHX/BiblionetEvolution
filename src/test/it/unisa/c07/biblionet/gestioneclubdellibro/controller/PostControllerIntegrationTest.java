package unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.BibliotecaDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import it.unisa.c07.biblionet.BiblionetApplication;

import lombok.Getter;
import lombok.Setter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PostControllerIntegrationTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private ClubDelLibroDAO clubDAO;

    @Autowired
    private EspertoDAO espertoDAO;

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private MockMvc mockMvc;

    private static Stream<Arguments> provideClubDTO() {
        Set<String> generi = new HashSet<>();
        generi.add("Saggistica");
        return Stream.of(
                Arguments.of(new ClubDTO("Club1",
                        "descrizione1",
                        generi))
        );
    }

    @Before
    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    private static Stream<Arguments> provideClubDelLibro() {
        return Stream.of(
                Arguments.of(new ClubDelLibro("Club1",
                        "descrizione1",
                        new Esperto("eliaviviani@gmail.com",
                                "ALotOfBeerInMyLife",
                                "Salerno",
                                "Salerno",
                                "Via vicino casa di Stefano 2",
                                "3694578963",
                                "mrDuff",
                                "Nicola",
                                "Pagliara",
                                new Biblioteca("gmail@gmail.com",
                                        "Ueuagliobellstuorolog69",
                                        "Napoli",
                                        "Scampia",
                                        "Via Portici 47",
                                        "3341278415",
                                        "Vieni che non ti faccio niente"))))
        );
    }






    /*@Test
    public void creaPostOk() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void creaPost_TitoloNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("content", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaPost_ContenutoNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaPost_IDClubNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(status().is(400));
    }

    @Test
    public void creaPost_IDClubNonMatchaConClubEsperto() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var esperto=espertoDAO.findEspertoByEmail("ciromaiorino@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test
    public void creaPost_IDClubInesistente() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", "-1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }


    @Test
    public void creaPost_TitoloTroppoLungo() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("content", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " +tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @Test
    public void creaPost_ContenutoTroppoLungo() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("content", "Il conta caratteri è semplice da usare, scrivi nella casella di input oppure copia e incolla un testo e sulla destra appariranno automaticamente tutte le statistiche. Il calcolo è dinamico, dall'istante in cui inizi a scrivere, oppure dopo aver incollato a")
                        .param("titolo", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @Test
    public void creaPost_ContenutoTroppoCorto() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("content", "aa")
                        .param("titolo", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @Test
    public void creaPost_TitoloTroppoCorto() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("content", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test(expected=org.springframework.web.util.NestedServletException.class)
    public void creaPost_TokenNonValido() throws Exception {

        String tokenEsperto="AKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto));
    }
    @Test
    public void creaPost_TokenNonFornito() throws Exception {

        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", idClub))
                .andExpect(status().is(400));
    }

    @Test
    public void creaPost_TokenNonDellaGiustaTipologia() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";
        var esperto=espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");
        var clubs=clubDAO.findAllByEsperto(esperto);
        var idClub=String.valueOf(clubs.get(0).getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }*/

    @Test
    public void aggiungiCommento_ByEspertoOk() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var posts=postDAO.findAll();
        var idPost=String.valueOf(posts.get(0).getId());


        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idPost", idPost)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void aggiungiCommento_ByLettoreOk() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";
        var posts=postDAO.findAll();
        var idPost=String.valueOf(posts.get(0).getId());


        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idPost", idPost)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void aggiungiCommento_IDPostNonFornito() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }

    @Test
    public void aggiungiCommento_ContenutoNonFornito() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";
        var posts=postDAO.findAll();
        var idPost=String.valueOf(posts.get(0).getId());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("idPost", idPost)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }


    @Test
    public void aggiungiCommento_ContenutoTroppoCorto() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";
        var posts=postDAO.findAll();
        var idPost=String.valueOf(posts.get(0).getId());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "aa")
                        .param("idPost", idPost)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void aggiungiCommento_ContenutoTroppoLungo() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";
        var posts=postDAO.findAll();
        var idPost=String.valueOf(posts.get(0).getId());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "Il conta caratteri è semplice da usare, scrivi nella casella di input oppure copia e incolla un testo e sulla destra appariranno automaticamente tutte le statistiche. Il calcolo è dinamico, dall'istante in cui inizi a scrivere, oppure dopo aver incollato a")
                        .param("idPost", idPost)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void aggiungiCommento_PostInesistente() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idPost", "1000100")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }


    @Test(expected=org.springframework.web.util.NestedServletException.class)
    public void aggiungiCommento_TokenNonValido() throws Exception {

        String tokenLettore = "eyJhbGcas-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";
        var posts=postDAO.findAll();
        var idPost=String.valueOf(posts.get(0).getId());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idPost", idPost)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore));
    }


    @Test
    public void aggiungiCommento_TokenBiblioteca() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        var posts=postDAO.findAll();
        var idPost=String.valueOf(posts.get(0).getId());



        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idPost", idPost)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

}
