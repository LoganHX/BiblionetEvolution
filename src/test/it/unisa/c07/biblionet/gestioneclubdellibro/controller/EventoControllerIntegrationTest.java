package unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.LibroDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import it.unisa.c07.biblionet.BiblionetApplication;

import lombok.Getter;
import lombok.Setter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class EventoControllerIntegrationTest {
    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private EventoDAO eventoDAO;

    @Autowired
    private ClubDelLibroDAO clubDAO;

    @Autowired
    private LibroDAO libroDAO;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    /*************************************+ Tests for eliminaEvento *********************************/
    @Test
    public void eliminaEvento() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String id=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());

        this.mockMvc
                .perform(post("/gestione-eventi/elimina-evento")
                        .param("idEvento", id)
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Evento eliminato"));
    }

    @Test
    public void modificaEventoClubOk() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test(expected=java.lang.IllegalArgumentException.class)
    public void modificaEventoClub_TokenNonValido() throws Exception {

        String token = "";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
    }

    @Test
    public void modificaEventoClub_TokenNonAssociatoAEspertoClub() throws Exception {

        String tokenEsperto2="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaXJvbWFpb3Jpbm9AZ21haWwuY29tIiwicm9sZSI6IkVzcGVydG8iLCJpYXQiOjE2OTY4NDgxNTF9.0V9p6wa5XhjUtkH_zs3HoFWNFDmTK1B3UsXYofLcsU8";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test
    public void modificaEventoClub_TokenNonDellaGiustaTipologia() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro",idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test(expected=javax.servlet.ServletException.class)
    public void modificaEventoClub_TokenNonFornito() throws Exception {

        String token = "";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento));
    }

    @Test
    public void modificaEventoClub_IDClubNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());;
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(status().is(400));
    }

    @Test
    public void modificaEventoClub_IDClubNonPresenteNelDB() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }

    @Test
    public void modificaEventoClub_IDClubNonAssociatoAEvento() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        var evento2=eventi.get(2);
        String idEvento=String.valueOf(evento2.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }

    @Test
    public void modificaEventoClub_IDLibroNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void modificaEventoClub_IDLibroNonPresenteNelDB() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", "-1")
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Libro inserito non valido"));
    }

    @Test
    public void modificaEventoClub_DescrizioneTroppoLunga() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "It’s also not even remotely new. Although Gurnee and Tegmark seem impressed with their results (“we atempt to extract an actual map of the world!”) the fact at geography can be weakly but imperfectly inferred from language corpora— is actually already well")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_DescrizioneTroppoCorta() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "aa")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_DescrizioneNonFornita() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_NomeTroppoLungo() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_NomeTroppoCorto() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "aa")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_NomeNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_DataNonValida() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2020-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Data non valida"));
    }

    @Test
    public void modificaEventoClub_DataNonFornita() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        var idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(status().is(400));
    }


    @Test
    public void modificaEventoClub_OraNonFornita() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var eventi=eventoDAO.findAll();
        var evento= eventi.get(0);
        String idEvento=String.valueOf(evento.getIdEvento());
        String idClub=String.valueOf(evento.getClub().getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2020-12-12")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .param("idEvento", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(status().is(400));
    }

    @Test
    public void creaEventoClubOk() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void creaEventoClub_IDClubNonPresenteNelDB() throws Exception {

         String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

         var libri=libroDAO.findAll();
         var libro=libri.get(0);
         String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "-1")
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }


    @Test
    public void creaEventoClub_IDClubNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(status().is(400));
    }

    @Test(expected=javax.servlet.ServletException.class)
    public void creaEventoClub_TokenNonFornito() throws Exception {

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro));
    }

    @Test
    public void creaEventoClub_TokenNonAssociatoAEspertoClub() throws Exception {

        String tokenEsperto2="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaXJvbWFpb3Jpbm9AZ21haWwuY29tIiwicm9sZSI6IkVzcGVydG8iLCJpYXQiOjE2OTY4NDgxNTF9.0V9p6wa5XhjUtkH_zs3HoFWNFDmTK1B3UsXYofLcsU8";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test(expected=io.jsonwebtoken.MalformedJwtException.class)
    public void creaEventoClub_TokenNonValido() throws Exception {

        String tokenEsperto="UBnbWFpb";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto));
    }

    @Test
    public void creaEventoClub_TokenNonDellaGiustaTipologia() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }



    @Test
    public void creaEventoClub_NomeNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }


    @Test
    public void creaEventoClub_DataNonValida() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2021-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Data non valida"));
    }


    @Test
    public void creaEventoClub_DataNonFornita() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(status().is(400));
    }



    @Test
    public void creaEventoClub_OraNonFornita() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("idClub", idClub)
                        .param("dateString", "2024-12-12")

                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(status().is(400));
    }

    @Test
    public void creaEventoClub_DescrizioneTroppoCorta() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "a")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_DescrizioneTroppoLunga() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "It’s also not even remotely new. Although Gurnee and Tegmark seem impressed with their results (“we atempt to extract an actual map of the world!”) the fact at geography can be weakly but imperfectly inferred from language corpora— is actually already well")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_DescrizioneNonFornita() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_IDLibroNonPresenteNelDB() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());


        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", "-1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Libro inserito non valido"));
    }


    @Test
    public void creaEventoClub_NomeTroppoLungo() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-aaa")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_NomeTroppoCorto() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        String idLibro=String.valueOf(libro.getIdLibro());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "a")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .param("idLibro", idLibro)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_IDLibroNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club=clubs.get(0);
        String idClub=String.valueOf(club.getIdClub());


        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", idClub)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }


}
