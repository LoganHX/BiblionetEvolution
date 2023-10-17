package it.unisa.c07.biblionet.gestionebiblioteca.controller;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.LibroDAO;
import it.unisa.c07.biblionet.common.LibroDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.BibliotecaDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.GenereDAO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import lombok.Getter;
import lombok.Setter;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Antonio Della Porta
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BibliotecaControllerTestIntegrationTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LibroDAO libroDAO;


    @Before
    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    /* TEST PRECEDENTEMENTE IMPLEMENTATI
    @Test
    public void inserisciNuovoLibro() throws Exception {

        Biblioteca biblioteca = bibliotecaDAO.findByID("bibliotecacarrisi@gmail.com");

        this.mockMvc.perform(get("/biblioteca/inserisci-nuovo-libro")
                .sessionAttr("loggedUser", biblioteca))
                .andExpect(view().name(
                        "/biblioteca/inserimento-nuovo-libro-prenotabile"));
    }

    @Test
    public void inserimentoDatabase() throws Exception {

        Biblioteca biblioteca = bibliotecaDAO.findByID("bibliotecacarrisi@gmail.com");

        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                .sessionAttr("loggedUser", biblioteca)
                .param("idLibro", "3")
                .param("numCopie", "1"))
                .andExpect(view().name(
                        "redirect:/prenotazione-libri/3/visualizza-libro"));
    }*/

    @Test
    public void inserimentoIsbnOK() throws Exception {


        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"Giallo","Fantasy"};
        String isbn = "9780735611313";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test(expected = io.jsonwebtoken.MalformedJwtException.class)
    public void inserimentoIsbn_TokenNonValido() throws Exception {


        String tokenBiblioteca="eyJhbGc2Mn0.u4Ej7gDIkbEd2H_o";
        String[] generi = {"Giallo","Fantasy"};
        String isbn = "9780735611313";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"));
    }

    @Test
    public void inserimentoIsbn_TokenNonDellaGiustaTipologia() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        String[] generi = {"Giallo","Fantasy"};
        String isbn = "9780735611313";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test(expected=javax.servlet.ServletException.class)
    public void inserimentoIsbn_TokenNonFornito() throws Exception {

        String[] generi = {"Giallo","Fantasy"};
        String isbn = "9780735611313";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"));
    }



    @Test
    public void inserimentoIsbn_NumeroCopieNonValido() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"Giallo","Fantasy"};
        String isbn = "9780735611313";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "-1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }

    @Test
    public void inserimentoIsbn_NumeroCopieNonFornito() throws Exception {


        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"Giallo","Fantasy"};
        String isbn = "9780735611313";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi))
                //.param("numCopie", "1"))
                .andExpect(status().is(400));
    }


    @Test
    public void inserimentoIsbn_ISBNNonFornito() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"Giallo","Fantasy"};

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(status().is(400));
    }

    @Test
    public void inserimentoIsbn_ISBNNonValido() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"Giallo","Fantasy"};

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", "123456789")
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void inserimentoIsbn_ISBNLibroNonPresenteNelCatalogoRemoto() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"Giallo","Fantasy"};
        String isbn = "9786735611413";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }

    @Test
    public void inserimentoIsbn_GenereSingoloEsistente_OK() throws Exception {


        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"Giallo"};
        String isbn = "9788891904454";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void inserimentoIsbn_GeneriMultipliEsistenti_OK() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"Giallo","Fantasy"};
        String isbn = "9788891904454";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void inserimentoIsbn_GenereSingoloNonEsistente() throws Exception {


        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"yellows"};
        String isbn = "9786735611413";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }

    @Test
    public void inserimentoIsbn_GeneriMultipliNonEsistenti() throws Exception {


        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"yellows","marines"};
        String isbn = "9786735611413";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }
    @Test
    public void inserimentoIsbn_GeneriMultipliEsistentiENonEsistenti() throws Exception {


        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] generi = {"yellows","Giallo"};
        String isbn = "9786735611413";

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }




    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da archivio
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoDatabaseOK() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        int idLibro=libro.getIdLibro();
        int numCopie = 5;

        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));

    }

    @Test(expected=javax.servlet.ServletException.class)
    public void inserimentoDatabase_TokenNonFornito() throws Exception {

        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        int idLibro=libro.getIdLibro();
        int numCopie = 5;

        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)));

    }

    @Test
    public void inserimentoDatabase_TokenNonDellaGiustaTipologia() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        int idLibro=libro.getIdLibro();
        int numCopie = 5;

        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto)
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));

    }

    @Test(expected=io.jsonwebtoken.MalformedJwtException.class)
    public void inserimentoDatabase_TokenNonValido() throws Exception {

        String tokenBiblioteca="eypbC5jb20iAswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        int idLibro=libro.getIdLibro();
        int numCopie = 5;

        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));

    }

    @Test
    public void inserimentoDatabase_IDLibroNonFornito() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        int numCopie = 5;

        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(status().is(400));

    }

    @Test
    public void inserimentoDatabase_NumeroCopieNonFornito() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        int idLibro=libro.getIdLibro();

        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("idLibro", String.valueOf(idLibro)))
                .andExpect(status().is(400));

    }

    @Test
    public void inserimentoDatabase_NumeroCopieNonValido() throws Exception {

        String tokenBiblioteca="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        var libri=libroDAO.findAll();
        var libro=libri.get(0);
        int idLibro=libro.getIdLibro();

        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblioteca)
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(-5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));

    }







    private static Stream<Arguments> provideLibroDTO() {
        return Stream.of(
                Arguments.of(
                        new LibroDTO(
                                1,
                                "Fru",
                                "Aldo",
                                "1234567891234",
                                "2010",
                                "Mondadori",
                                "Casa",
                                "@@@@@",
                                null
                        )
                )
        );
    }

}
