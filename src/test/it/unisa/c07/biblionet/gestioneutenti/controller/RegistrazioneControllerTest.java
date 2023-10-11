package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneutenti.RegistrazioneService;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

/**
 * @author Alessio Casolaro, Antonio Della Porta
 */
@SpringBootTest
@AutoConfigureMockMvc
public final class RegistrazioneControllerTest {

    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private RegistrazioneService registrazioneService;

    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private EspertoService espertoService;
    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private LettoreService lettoreService;
    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private BibliotecaService bibliotecaService;

    /**
     * Inject di MockMvc per simulare
     * le richieste http.
     */
    @Autowired
    private MockMvc mockMvc;


    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la registrazione di un esperto
     * avvenuta correttamente
     * simulando la richiesta http.
     *
     * @param utenteDTO          il DTO dell'esperto da registrare
     * @param confermaPassword la password da confermare
     * @throws Exception Eccezione per MockMvc
     */


    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEspertoOk(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_NomeUtenteNonFornito(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        //.param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_NomeNonFornito(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        //.param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_CognomeNonFornito(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        //.param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_EmailNonFornita(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        //.param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_RecapitoTelefonicoNonFornito(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        //.param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_PasswordNonFornita(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        //.param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_ConfermaPasswordNonFornita(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        //.param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(status().is(400));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_EmailBibliotecaNonFornita(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                        //.param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_UsernameTroppoLungo(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_UsernameTroppoCorto(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", "aa")
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_CittaNonFornita(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        //.param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_ProvinciaNonFornita(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        //.param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_FormatoProvinciaNonValido(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", "$@1€rn0")
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }


    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_FormatoCittaNonValido(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", "$@1€rn0")
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_IndirizzoNonFornito(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        //.param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_FormatoEmailBibliotecaNonValido(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", "email@ao"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));

    }

    @ParameterizedTest
    @DisplayName("Registrazione Esperto che va a buon fine")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_FormatoIndirizzoNonValido(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", "@")
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @DisplayName("Registrazione Esperto che va a buon fine")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_FormatoRecapitoTelefonicoNonValido(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", "-5050505005")
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @DisplayName("Registrazione Esperto che va a buon fine")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_FormatoCognomeNonValido(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", "!")
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }


    @ParameterizedTest
    @DisplayName("Registrazione Esperto che va a buon fine")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_FormatoNomeNonValido(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", "!")
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @DisplayName("Registrazione Esperto che va a buon fine")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_PasswordTroppoCorta(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "1234")
                        .param("confermaPassword", "1234")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));

    }

    @ParameterizedTest
    @DisplayName("Registrazione Esperto che va a buon fine")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_FormatoEmailNonValido(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", "!francia!italia!mazzini!giuseppe")
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }
    @ParameterizedTest
    @DisplayName("Registrazione Esperto che va a buon fine")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_NomeTroppoLungo(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    @ParameterizedTest
    @DisplayName("Registrazione Esperto che va a buon fine")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_CognomeTroppoLungo(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "123456780",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-aa")
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));

    }

    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la registrazione di un esperto
     * avvenuta in modo errato poiché la conferma della
     * password è sbagliata simulando la richiesta http.
     *
     * @param utenteDTO L'esperto da registrare
     * @param confermaPassword la password da confermare
     * @throws Exception Eccezione per MockMvc
*/
    @ParameterizedTest
    @DisplayName("Registrazione Esperto che non va a buon fine "
               + "perché la conferma password è sbagliata")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_PasswordNonCombacia(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "1234567890",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(biblioteca);

        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", "PasswordErrataaaa")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }


    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la registrazione di un esperto
     * avvenuta correttamente
     * simulando la richiesta http.
     *
     * @param utenteDTO          L'esperto da registrare
     * @param confermaPassword la password da confermare
     * @throws Exception Eccezione per MockMvc
*/
    @ParameterizedTest
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_EmailBibliotecaInesistente(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "1234567890",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);
        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(null);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);



        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                        .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }


    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la registrazione di un esperto
     * la cui mail è già presente
     * simulando la richiesta http.
     *
     * @param utenteDTO          L'esperto da registrare
     * @param confermaPassword la password da confermare
     * @throws Exception Eccezione per MockMvc
*/
    @ParameterizedTest
    @DisplayName("Registrazione Esperto che non va a buon fine" +
                 "perchè la mail inserita è già presente")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEsperto_EmailPresente(
            final EspertoDTO utenteDTO, final String confermaPassword) throws Exception {

        Biblioteca biblioteca = new Biblioteca(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "1234567890",
                "Biblioteca Carrisi"
        );

        Esperto esperto = Mockito.mock(Esperto.class);


        when(espertoService.creaEspertoDaModel(utenteDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(utenteDTO.getEmailBiblioteca())).
                thenReturn(null);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(true);

        this.mockMvc.perform(post("/registrazione/esperto")
                .param("email", utenteDTO.getEmail())
                .param("nome", utenteDTO.getNome())
                .param("cognome", utenteDTO.getCognome())
                .param("username", utenteDTO.getUsername())
                .param("password", "EspertoPassword")
                .param("confermaPassword", confermaPassword)
                .param("provincia", utenteDTO.getProvincia())
                .param("citta", utenteDTO.getCitta())
                .param("via", utenteDTO.getVia())
                .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico())
                .param("emailBiblioteca", utenteDTO.getEmailBiblioteca()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ISCRIZIONE_FALLITA));
    }

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
    */
    private static Stream<Arguments> provideRegistrazioneEsperto() {

        return Stream.of(
                Arguments.of(new EspertoDTO(
                                "eliaviviani@gmail.com",
                                "EspertoPassword",
                                "Napoli",
                                "Torre del Greco",
                                "Via Roma 2",
                                "1234567890",
                                "Elia",
                                "Viviani",
                                "Espertissimo",
                                "bibliotecacarrisi@gmail.com"
                        ), "EspertoPassword"
                )
        );

    }

    /**
     * Test che registra correttamente una biblioteca.
     *
     * @param utenteDTO       la biblioteca da registrare
     * @param confermaPassword la password da confermare
     * @throws Exception Eccezione di MockMvc
*/

    @ParameterizedTest
    @DisplayName("Registrazione Biblioteca che va a buon fine")
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBibliotecaOk(final BibliotecaDTO utenteDTO,
                                          final String confermaPassword)
            throws Exception {

        Biblioteca biblioteca = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO))
                .thenReturn(biblioteca);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", "BibliotecaPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico",
                                utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }


    @ParameterizedTest
    @DisplayName("Registrazione Biblioteca che va a buon fine")
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBibliotecaFormatoRecapitoTelefonicoNonValido(final BibliotecaDTO utenteDTO,
                                          final String confermaPassword)
            throws Exception {

        Biblioteca biblioteca = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO))
                .thenReturn(biblioteca);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", "BibliotecaPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico",
                                "tretrequat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }


    @ParameterizedTest
    @DisplayName("Registrazione Biblioteca che va a buon fine")
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_FormatoNomeBibliotecaNonValido(final BibliotecaDTO utenteDTO,
                                          final String confermaPassword)
            throws Exception {

        Biblioteca biblioteca = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO))
                .thenReturn(biblioteca);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", "Biblioteca del Dipartimento di Teologia di Copenhaghen Biblioteca del Dipartimento di Teologia di Copenhaghen")
                        .param("password", "BibliotecaPassword")
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico",
                                utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    /**
     * Test che non registra correttamente una biblioteca.
     * poiché la conferma della password è errata
     *
     * @param utenteDTO       la biblioteca da registrare
     * @param confermaPassword la passoword da confermare
     * @throws Exception Eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Registrazione Biblioteca che non va a buon fine "
           + "perché la conferma password è sbagliata")
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_PasswordErrata(
                                                final BibliotecaDTO utenteDTO,
                                                final String confermaPassword)
                                                            throws Exception {

        Biblioteca biblioteca = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO))
                .thenReturn(biblioteca);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);



        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", "BibliotecaPassword")
                        .param("confermaPassword", "PasswordErrataaa")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico",
                                utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }

    /**
     * Test che registra correttamente una biblioteca.
     *
     * @param utenteDTO       la biblioteca da registrare
     * @param confermaPassword la password da confermare
     * @throws Exception Eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Registrazione Biblioteca che va a buon fine")
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBibliotecaEmailPresente(
                                                final BibliotecaDTO utenteDTO,
                                                final String confermaPassword)
                                                throws Exception {


        Biblioteca biblioteca = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO))
                .thenReturn(biblioteca);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(true);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", "BibliotecaPassword")
                        .param("confermaPassword", "PasswordErrataaa")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico",
                                utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ISCRIZIONE_FALLITA));
    }


    /**
     * Restituisce i dati per la registrazione della biblioteca.
     *
     * @return i dati per il testing
     */
    private static Stream<Arguments> provideRegistrazioneBiblioteca() {

        return Stream.of(Arguments.of(
                new BibliotecaDTO(
                        "bibliotecacarrisi@gmail.com",
                        "BibliotecaPassword",
                        "Napoli",
                        "Torre del Greco",
                        "Via Carrisi 47",
                        "1234567890",
                        "Biblioteca Carrisi"
                ),
                "BibliotecaPassword"//Password Conferma
        ));
    }


    /**
     * Test per la registrazione di un lettore
     * avvenuta correttamente
     * simulando la richiesta http.
     *
     * @param utenteDTO          Il lettore da registrare
     * @param confermaPassword il campo conferma password del
     *                         form per controllare
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettoreOK(final LettoreDTO utenteDTO,
                                             final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                .param("email", utenteDTO.getEmail())
                .param("username", utenteDTO.getUsername())
                .param("nome", utenteDTO.getNome())
                .param("cognome", utenteDTO.getCognome())
                .param("password", confermaPassword)
                .param("confermaPassword", confermaPassword)
                .param("provincia", utenteDTO.getProvincia())
                .param("citta", utenteDTO.getCitta())
                .param("via", utenteDTO.getVia())
                .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_FormatoProvinciaNonValido(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", "$@1€rn0")
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_FormatoCittaNonValido(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", "$@1€rn0")
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_RecapitoNonFornito(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia()))
                        //.param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_FormatoEmailNonValido(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", "abaco")
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_EmailNonFornita(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        //.param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_EmailRegistrataPrecedentemente(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(true);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ISCRIZIONE_FALLITA));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_PasswordNonFornita(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        //.param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_ConfermaPasswordNonFornita(final LettoreDTO utenteDTO,
                                                        final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        //.param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(status().is(400));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_PasswordNonCombaciano(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", "EspertoPassword")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_CognomeTroppoLungo(final LettoreDTO utenteDTO,
                                                     final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("cognome", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("nome", utenteDTO.getNome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_CognomeTroppoCorto(final LettoreDTO utenteDTO,
                                                     final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("cognome", "aa")
                        .param("nome", utenteDTO.getNome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_NomeTroppoLungo(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_NomeTroppoCorto(final LettoreDTO utenteDTO,
                                                     final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", "aa")
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_NomeUtenteTroppoLungo(final LettoreDTO utenteDTO,
                                                     final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("username", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_NomeUtenteTroppoCorto(final LettoreDTO utenteDTO,
                                                     final String confermaPassword)
            throws Exception {


        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", "aa")
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_IndirizzoNonFornito(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        //.param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_CittaNonFornita(final LettoreDTO utenteDTO,
                                                         final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        //.param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_CognomeNonFornito(final LettoreDTO utenteDTO,
                                                     final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        //.param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_NomeNonFornito(final LettoreDTO utenteDTO,
                                                     final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        //.param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_ProvinciaNonFornita(final LettoreDTO utenteDTO,
                                                     final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", utenteDTO.getEmail())
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        //.param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }


    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_FormatoCognomeNonValido(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", "a")
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", "de Berger4c")
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_RecapitoTelefonicoNonValido(final LettoreDTO utenteDTO,
                                                            final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", "a")
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", "de Berger4c")
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", "Roosevekt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_FormatoIndirizzoNonValido(final LettoreDTO utenteDTO,
                                                                final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", "a")
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", "de Berger4c")
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", "Via Casilina Vecchia $")
                        .param("recapitoTelefonico", "Roosevekt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_FormatoNomeNonValido(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", "a")
                        .param("username", utenteDTO.getUsername())
                        .param("nome", "ישׁמעאל")
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_PasswordTroppoCorta(final LettoreDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", "a")
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", "1234")
                        .param("confermaPassword", "1234")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettore_FormatoRecapitoNonValido(final LettoreDTO utenteDTO,
                                                  final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(utenteDTO);
        when(lettoreService.creaLettoreDaModel(utenteDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", "a")
                        .param("username", utenteDTO.getUsername())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("password", "1234")
                        .param("confermaPassword", "1234")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", "recapitononvalido"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideRegistrazioneLettore() {

        return Stream.of(
                Arguments.of(
                        new LettoreDTO(
                                "giuliociccione@gmail.com",
                                "LettorePassword",
                                "Salerno",
                                "Baronissi",
                                "Via Barone 11",
                                "3456789012",
                                "SuperLettore",
                                "Giulio",
                                "Ciccione"
                        ), "LettorePassword"//Password Conferma
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneLettoreOK(final BibliotecaDTO utenteDTO,
                                       final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())

                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_FormatoProvinciaNonValido(final BibliotecaDTO utenteDTO,
                                                                  final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", "$@1€rn0")
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_FormatoCittaNonValido(final BibliotecaDTO utenteDTO,
                                                              final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", "$@1€rn0")
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_RecapitoNonFornito(final BibliotecaDTO utenteDTO,
                                                           final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia()))
                //.param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_FormatoEmailNonValido(final BibliotecaDTO utenteDTO,
                                                              final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", "abaco")
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_EmailNonFornita(final BibliotecaDTO utenteDTO,
                                                        final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        //.param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_EmailRegistrataPrecedentemente(final BibliotecaDTO utenteDTO,
                                                           final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(true);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ISCRIZIONE_FALLITA));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_PasswordNonFornita(final BibliotecaDTO utenteDTO,
                                                           final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        //.param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_ConfermaPasswordNonFornita(final BibliotecaDTO utenteDTO,
                                                                   final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        //.param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(status().is(400));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_PasswordNonCombaciano(final BibliotecaDTO utenteDTO,
                                                              final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", "EspertoPassword")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }



    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_NomeTroppoLungo(final BibliotecaDTO utenteDTO,
                                                        final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_NomeTroppoCorto(final BibliotecaDTO utenteDTO,
                                                        final String confermaPassword)
            throws Exception {


        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", "aa")
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }



    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_IndirizzoNonFornito(final BibliotecaDTO utenteDTO,
                                                            final String confermaPassword)
            throws Exception {

        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        //.param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_CittaNonFornita(final BibliotecaDTO utenteDTO,
                                                        final String confermaPassword)
            throws Exception {

        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        //.param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }


    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_NomeNonFornito(final BibliotecaDTO utenteDTO,
                                                       final String confermaPassword)
            throws Exception {

        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        //.param("nome", utenteDTO.getNome())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_ProvinciaNonFornita(final BibliotecaDTO utenteDTO,
                                                            final String confermaPassword)
            throws Exception {

        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", utenteDTO.getEmail())
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        //.param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }



    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_RecapitoTelefonicoNonValido(final BibliotecaDTO utenteDTO,
                                                                    final String confermaPassword)
            throws Exception {

        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", "a")
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", "Roosevekt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_FormatoIndirizzoNonValido(final BibliotecaDTO utenteDTO,
                                                                  final String confermaPassword)
            throws Exception {

        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", "a")
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", "Via Casilina Vecchia $")
                        .param("recapitoTelefonico", "Roosevekt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_FormatoNomeNonValido(final BibliotecaDTO utenteDTO,
                                                             final String confermaPassword)
            throws Exception {

        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", "a")
                        .param("nomeBiblioteca", "ישׁמעאל")
                        .param("password", confermaPassword)
                        .param("confermaPassword", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_PasswordTroppoCorta(final BibliotecaDTO utenteDTO,
                                                            final String confermaPassword)
            throws Exception {

        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", "a")
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", "1234")
                        .param("confermaPassword", "1234")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBiblioteca_FormatoRecapitoNonValido(final BibliotecaDTO utenteDTO,
                                                                 final String confermaPassword)
            throws Exception {

        Biblioteca b = new Biblioteca(utenteDTO);
        when(bibliotecaService.creaBibliotecaDaModel(utenteDTO)).thenReturn(b);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", "a")
                        .param("nomeBiblioteca", utenteDTO.getNomeBiblioteca())
                        .param("password", "1234")
                        .param("confermaPassword", "1234")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapitoTelefonico", "recapitononvalido"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

}
