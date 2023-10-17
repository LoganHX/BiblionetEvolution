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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;


/**
 * Implementa l'integration testing del controller per il sottosistema
 * ClubDelLibro.
 * 
 * @author Luca Topo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ClubDelLibroControllerIntegrationTest {
    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private ClubDelLibroDAO clubDAO;

    @Autowired
    private LettoreDAO lettoreDAO;

    @Autowired
    private BibliotecaDAO bibliotecaDAO;

    @Autowired
    private GenereDAO genereDAO;

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

    /*TEST PRECEDENTEMENTE IMPLEMENTATI
    @Test
    public void visualizzaListaEventiClubInesistenteTest() throws Exception {
        var club = clubDAO.getOne(-1);
        var lettore = (Lettore) this.lettoreDAO.findAll()
                                               .stream()
                                               .filter(
                                                 x -> x.getTipo() == "Lettore"
                                               ).findFirst().get();
        var id = club.getIdClub();

        this.mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/club-del-libro/" + id + "/eventi"
            ).sessionAttr("loggedUser", lettore)
        ).andExpect(
            status().isNotFound()
        );
    }

    @Test
    public void visualizzaListaEventiComeBibliotecaTest() throws Exception {
        var club = clubDAO.findAll().get(0);
        var biblioteca = (Biblioteca) this.bibliotecaDAO.findAll()
                                               .stream()
                                               .filter(
                                                 x -> x.getTipo() == "Biblioteca"
                                               ).findFirst().get();
        var id = club.getIdClub();

        this.mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/club-del-libro/" + id + "/eventi"
            ).sessionAttr("loggedUser", biblioteca)
        ).andExpect(
            status().isUnauthorized()
        );
    }


    @Test
    public void visualizzaListaEventiClubTest() throws Exception {
        var club = clubDAO.findAll().get(0);
        var lettore = (Lettore) this.lettoreDAO.findAll()
                                               .stream()
                                               .filter(
                                                 x -> x.getTipo() == "Lettore"
                                               ).findFirst().get();
        var id = club.getIdClub();

        this.mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/club-del-libro/" + id + "/eventi"
            ).sessionAttr("loggedUser", lettore)
        ).andExpect(
            model().attribute(
                "loggedUser",
                lettore
            )
        ).andExpect(
            model().attribute(
                "club",
                Matchers.hasProperty(
                    "idClub",
                    Matchers.equalTo(club.getIdClub())
                )
            )
        ).andExpect(
            model().attributeExists(
                "eventi",
                "mieiEventi"
            )
        );
    }*/

    @Test
    public void creaClubDelLibro_TokenNonDellaGiustaTipologia() throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));

        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};

        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome", "il mio club")
                        .param("descrizione", "descrizione")
                        .param("generi",generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }


    @Test
    public void creaClubDelLibroOK() throws Exception {
        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));

        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome", "il mio club")
                        .param("descrizione", "descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test(expected=javax.servlet.ServletException.class)
    public void creaClubDelLibro_TokenNonFornito() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};

        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome", "il mio club")
                        .param("descrizione", "descrizione")
                        .param("generi", generi));
    }


    @Test
    public void creaClubDelLibro_CopertinaNonFornita() throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";


        String [] generi={"Giallo","Fantasy"};
        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .param("nome", "il mio club")
                        .param("descrizione", "descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void creaClubDelLibro_NomeNonFornito() throws Exception {
        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};
        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("descrizione", "descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaClubDelLibro_DescrizioneNonFornita() throws Exception {
        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};
        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome", "club dei chiacchieroni")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaClubDelLibro_NomeTroppoLungo() throws Exception {
        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};

        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("descrizione", "descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaClubDelLibro_NomeTroppoCorto() throws Exception {
        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};
        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome", "aa")
                        .param("descrizione", "descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaClubDelLibro_CopertinaTroppoPesante() throws Exception {
        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaNotOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};
        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome","nome")
                        .param("descrizione", "descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }

    @Test
    public void creaClubDelLibro_FormatoCopertinaNonValido() throws Exception {
        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaNotOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};

        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome", "nome")
                        .param("descrizione", "descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }

    @Test
    public void creaClubDelLibro_DescrizioneTroppoLunga() throws Exception {
        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};
        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome", "nome")
                        .param("descrizione", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaClubDelLibro_DescrizioneTroppoCorta() throws Exception {
        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        String [] generi={"Giallo","Fantasy"};
        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        .file(copertina)
                        .param("nome", "nome")
                        .param("descrizione", "aa")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    /********************************** Tests fot visualizzaModificaDatiClub **********************************/



    /******************************************* Tests for partecipaClub ***************************/

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per il reinderizzamento alla modifica
     * dei dati di un club del libro
     * simulando la richiesta http.
     *
     *
     * @throws Exception Eccezione per MovkMvc
     */
    @Test
    public void modificaDatiClub_TokenNonAssociatoAEspertoClub()
            throws Exception {

        String tokenEsperto2="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaXJvbWFpb3Jpbm9AZ21haWwuY29tIiwicm9sZSI6IkVzcGVydG8iLCJpYXQiOjE2OTY4NDgxNTF9.0V9p6wa5XhjUtkH_zs3HoFWNFDmTK1B3UsXYofLcsU8";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test
    public void modificaDatiClubOK()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }


    @Test(expected=io.jsonwebtoken.MalformedJwtException.class)
    public void modificaDatiClub_TokenNonValido()
            throws Exception {

        String tokenEsperto="RHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto));
    }

    @Test(expected=javax.servlet.ServletException.class)
    public void modificaDatiClub_TokenNonFornito()
            throws Exception {

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione","descrizione")
                        .param("generi", generi));
    }

    @Test
    public void modificaDatiClub_TokenNonDellaGiustaTipologia()
            throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4OTEwMDQwNn0.b2JpCR1as-1iHUb-6DJNl6WOLLs-mccwbjPNrbMvToA";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }



    @Test
    public void modificaDatiClub_CopertinaTroppoPesante()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaNotOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }

    @Test
    public void modificaDatiClub_CopertinaNonFornita()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }


    @Test
    public void modificaDatiClub_FormatoCopertinaNonValido()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaNotOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }

    @Test
    public void modificaDatiClub_DescrizioneClubNonFornita()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaDatiClub_NomeClubNonFornito()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaDatiClub_DescrizioneTroppoLunga()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @Test
    public void modificaDatiClub_DescrizioneTroppoCorta()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome","nome")
                        .param("descrizione", "aa")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }





    @Test
    public void modificaDatiClub_NomeTroppoLungo()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};

        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome", "Il conta caratteri è Il conta i")
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaDatiClub_NomeTroppoCorto()
            throws Exception {

        String tokenEsperto="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/image/copertine/copertinaOk.png"));
        MockMultipartFile copertina =
                new MockMultipartFile("immagineCopertina",
                        "filename.png",
                        "image/png",
                        fileContent);

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        String [] generi={"Giallo","Fantasy"};
        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .file(copertina)
                        .param("id", id)
                        .param("nome", "Il")
                        .param("descrizione","descrizione")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }


    /************************************** Tests for visualizzaCreaEvento ****************************/

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'iscrizione di un lettore ad un club
     * simulando la richiesta http.
     *
     *
     * @throws Exception Eccezione per MovkMvc
     */
    @Test
    public void partecipaClub() throws Exception {

        String tokenLettore="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnaXVzZXBwZWNvbnRhbGRpQGdtYWlsLmNvbSIsInJvbGUiOiJMZXR0b3JlIiwiaWF0IjoxNjk2ODY5ODc4fQ.Lu4rGHEpUGw4WwQCDZNF6Bcbi4gZg4itTwn0dfTHUos";

        var clubs=clubDAO.findAll();
        var club =clubs.get(0);
        String id = String.valueOf(club.getIdClub());

        this.mockMvc
                .perform(post("/club-del-libro/iscrizione")
                        .param("id", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore)

                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ISCRIZIONE_OK));
    }


}
