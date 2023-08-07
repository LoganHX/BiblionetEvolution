package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
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
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
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
     * @param espertoDTO          il DTO dell'esperto da registrare
     * @param confermaPassword la password da confermare
     * @param emailBiblioteca  la mail della biblioteca
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @DisplayName("Registrazione Esperto che va a buon fine")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEspertoOk(
            final EspertoDTO espertoDTO, final String confermaPassword,
            final String emailBiblioteca) throws Exception {

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
        when(espertoService.creaEspertoDaModel(espertoDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(emailBiblioteca)).
                thenReturn(biblioteca);


        this.mockMvc.perform(post("/registrazione/esperto")
                .param("email", espertoDTO.getEmail())
                .param("nome", espertoDTO.getNome())
                .param("cognome", espertoDTO.getCognome())
                .param("username", espertoDTO.getUsername())
                .param("password", "EspertoPassword")
                .param("conferma_password", confermaPassword)
                .param("provincia", espertoDTO.getProvincia())
                .param("citta", espertoDTO.getCitta())
                .param("via", espertoDTO.getVia())
                .param("recapito_telefonico", espertoDTO.getRecapitoTelefonico())
                .param("email_biblioteca", emailBiblioteca))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));

    }

    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la registrazione di un esperto
     * avvenuta in modo errato poiché la conferma della
     * password è sbagliata simulando la richiesta http.
     *
     * @param espertoDTO L'esperto da registrare
     * @param confermaPassword la password da confermare
     * @param emailBiblioteca  la mail della biblioteca
     * @throws Exception Eccezione per MockMvc
*/
    @ParameterizedTest
    @DisplayName("Registrazione Esperto che non va a buon fine "
               + "perché la conferma password è sbagliata")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEspertoErrorePassword(
            final EspertoDTO espertoDTO, final String confermaPassword,
            final String emailBiblioteca) throws Exception {

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
        when(espertoService.creaEspertoDaModel(espertoDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(emailBiblioteca)).
                thenReturn(biblioteca);

        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", espertoDTO.getEmail())
                        .param("nome", espertoDTO.getNome())
                        .param("cognome", espertoDTO.getCognome())
                        .param("username", espertoDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("conferma_password", "PasswordErrataaaa")
                        .param("provincia", espertoDTO.getProvincia())
                        .param("citta", espertoDTO.getCitta())
                        .param("via", espertoDTO.getVia())
                        .param("recapito_telefonico", espertoDTO.getRecapitoTelefonico())
                        .param("email_biblioteca", emailBiblioteca))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Formato dati non valido"));
    }


    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la registrazione di un esperto
     * avvenuta correttamente
     * simulando la richiesta http.
     *
     * @param espertoDTO          L'esperto da registrare
     * @param confermaPassword la password da confermare
     * @param emailBiblioteca  la mail della biblioteca
     * @throws Exception Eccezione per MockMvc
*/
    @ParameterizedTest
    @DisplayName("Registrazione Esperto che non va a buon fine "
               + "perché la mail della biblioteca non corrisponde a "
               + "nessun utente")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEspertoEmailBibliotecaErrata(
            final EspertoDTO espertoDTO, final String confermaPassword,
            final String emailBiblioteca) throws Exception {

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
        when(espertoService.creaEspertoDaModel(espertoDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(emailBiblioteca)).
                thenReturn(null);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);



        this.mockMvc.perform(post("/registrazione/esperto")
                        .param("email", espertoDTO.getEmail())
                        .param("nome", espertoDTO.getNome())
                        .param("cognome", espertoDTO.getCognome())
                        .param("username", espertoDTO.getUsername())
                        .param("password", "EspertoPassword")
                        .param("conferma_password", confermaPassword)
                        .param("provincia", espertoDTO.getProvincia())
                        .param("citta", espertoDTO.getCitta())
                        .param("via", espertoDTO.getVia())
                        .param("recapito_telefonico", espertoDTO.getRecapitoTelefonico())
                        .param("email_biblioteca", emailBiblioteca))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }


    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la registrazione di un esperto
     * la cui mail è già presente
     * simulando la richiesta http.
     *
     * @param espertoDTO          L'esperto da registrare
     * @param confermaPassword la password da confermare
     * @param emailBiblioteca  la mail della biblioteca
     * @throws Exception Eccezione per MockMvc
*/
    @ParameterizedTest
    @DisplayName("Registrazione Esperto che non va a buon fine" +
                 "perchè la mail inserita è già presente")
    @MethodSource("provideRegistrazioneEsperto")
    public void registrazioneEspertoEmailPresente(
            final EspertoDTO espertoDTO, final String confermaPassword,
            final String emailBiblioteca) throws Exception {

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


        when(espertoService.creaEspertoDaModel(espertoDTO, biblioteca)).
                thenReturn((esperto));
        when(bibliotecaService.findBibliotecaByEmail(emailBiblioteca)).
                thenReturn(null);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(true);

        this.mockMvc.perform(post("/registrazione/esperto")
                .param("email", espertoDTO.getEmail())
                .param("nome", espertoDTO.getNome())
                .param("cognome", espertoDTO.getCognome())
                .param("username", espertoDTO.getUsername())
                .param("password", "EspertoPassword")
                .param("conferma_password", confermaPassword)
                .param("provincia", espertoDTO.getProvincia())
                .param("citta", espertoDTO.getCitta())
                .param("via", espertoDTO.getVia())
                .param("recapito_telefonico", espertoDTO.getRecapitoTelefonico())
                .param("email_biblioteca", emailBiblioteca))
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
                                "Espertissimo"
                        ), "EspertoPassword", "bibliotecacarrisi@gmail.com"
                )
        );

    }

    /**
     * Test che registra correttamente una biblioteca.
     *
     * @param bibliotecaDTO       la biblioteca da registrare
     * @param confermaPassword la password da confermare
     * @throws Exception Eccezione di MockMvc
*/
    @ParameterizedTest
    @DisplayName("Registrazione Biblioteca che va a buon fine")
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBibliotecaOk(final BibliotecaDTO bibliotecaDTO,
                                                final String confermaPassword)
            throws Exception {

        Biblioteca biblioteca = new Biblioteca(bibliotecaDTO);
        when(bibliotecaService.creaBibliotecaDaModel(bibliotecaDTO))
                .thenReturn(biblioteca);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                .param("email", bibliotecaDTO.getEmail())
                .param("nomeBiblioteca", bibliotecaDTO.getNomeBiblioteca())
                .param("password", "BibliotecaPassword")
                .param("conferma_password", confermaPassword)
                .param("provincia", bibliotecaDTO.getProvincia())
                .param("citta", bibliotecaDTO.getCitta())
                .param("via", bibliotecaDTO.getVia())
                .param("recapito_telefonico",
                        bibliotecaDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    /**
     * Test che non registra correttamente una biblioteca.
     * poiché la conferma della password è errata
     *
     * @param bibliotecaDTO       la biblioteca da registrare
     * @param confermaPassword la passoword da confermare
     * @throws Exception Eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Registrazione Biblioteca che non va a buon fine "
           + "perché la conferma password è sbagliata")
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBibliotecaPasswordErrata(
                                                final BibliotecaDTO bibliotecaDTO,
                                                final String confermaPassword)
                                                            throws Exception {

        Biblioteca biblioteca = new Biblioteca(bibliotecaDTO);
        when(bibliotecaService.creaBibliotecaDaModel(bibliotecaDTO))
                .thenReturn(biblioteca);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);



        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", bibliotecaDTO.getEmail())
                        .param("nomeBiblioteca", bibliotecaDTO.getNomeBiblioteca())
                        .param("password", "BibliotecaPassword")
                        .param("conferma_password", "PasswordErrataaa")
                        .param("provincia", bibliotecaDTO.getProvincia())
                        .param("citta", bibliotecaDTO.getCitta())
                        .param("via", bibliotecaDTO.getVia())
                        .param("recapito_telefonico",
                                bibliotecaDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    /**
     * Test che registra correttamente una biblioteca.
     *
     * @param bibliotecaDTO       la biblioteca da registrare
     * @param confermaPassword la password da confermare
     * @throws Exception Eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Registrazione Biblioteca che va a buon fine")
    @MethodSource("provideRegistrazioneBiblioteca")
    public void registrazioneBibliotecaEmailPresente(
                                                final BibliotecaDTO bibliotecaDTO,
                                                final String confermaPassword)
                                                throws Exception {


        Biblioteca biblioteca = new Biblioteca(bibliotecaDTO);
        when(bibliotecaService.creaBibliotecaDaModel(bibliotecaDTO))
                .thenReturn(biblioteca);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(true);


        this.mockMvc.perform(post("/registrazione/biblioteca")
                        .param("email", bibliotecaDTO.getEmail())
                        .param("nomeBiblioteca", bibliotecaDTO.getNomeBiblioteca())
                        .param("password", "BibliotecaPassword")
                        .param("conferma_password", "PasswordErrataaa")
                        .param("provincia", bibliotecaDTO.getProvincia())
                        .param("citta", bibliotecaDTO.getCitta())
                        .param("via", bibliotecaDTO.getVia())
                        .param("recapito_telefonico",
                                bibliotecaDTO.getRecapitoTelefonico()))
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
     * @param lettoreDTO          Il lettore da registrare
     * @param confermaPassword il campo conferma password del
     *                         form per controllare
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @DisplayName("Registrazione Lettore che va a buon fine")
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettoreOK(final LettoreDTO lettoreDTO,
                                             final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(lettoreDTO);
        when(lettoreService.creaLettoreDaModel(lettoreDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                .param("email", "a")
                .param("username", lettoreDTO.getUsername())
                .param("nome", lettoreDTO.getNome())
                .param("cognome", lettoreDTO.getCognome())
                .param("password", "LettorePassword")
                .param("conferma_password", confermaPassword)
                .param("provincia", lettoreDTO.getProvincia())
                .param("citta", lettoreDTO.getCitta())
                .param("via", lettoreDTO.getVia())
                .param("recapito_telefonico", lettoreDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }


    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la registrazione di un lettore
     * avvenuta in modo scorretto
     * simulando la richiesta http.
     *
     * @param lettoreDTO          Il lettore da registrare
     * @param confermaPassword il campo conferma password del
     *                         form per controllare
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @DisplayName("Registrazione Lettore che non va a buon fine, "
               + "password e conferma password sbagliate")
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettoreErrataPassword(final LettoreDTO lettoreDTO,
                                                 final String confermaPassword)
            throws Exception {
        Lettore lettore = new Lettore(lettoreDTO);
        when(lettoreService.creaLettoreDaModel(lettoreDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", "a")
                        .param("username", lettoreDTO.getUsername())
                        .param("nome", lettoreDTO.getNome())
                        .param("cognome", lettoreDTO.getCognome())
                        .param("password", "LettorePassword")
                        .param("conferma_password", "PasswordSbagliataaa")
                        .param("provincia", lettoreDTO.getProvincia())
                        .param("citta", lettoreDTO.getCitta())
                        .param("via", lettoreDTO.getVia())
                        .param("recapito_telefonico", lettoreDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @DisplayName("Registrazione Esperto che non va a buon fine" +
            "perchè la mail inserita è già presente")
    @MethodSource("provideRegistrazioneLettore")
    public void registrazioneLettoreEmailPresente(final LettoreDTO lettoreDTO,
                                       final String confermaPassword)
            throws Exception {

        Lettore lettore = new Lettore(lettoreDTO);
        when(lettoreService.creaLettoreDaModel(lettoreDTO)).thenReturn(lettore);
        when(registrazioneService.isEmailRegistrata(Mockito.anyString())).thenReturn(true);


        this.mockMvc.perform(post("/registrazione/lettore")
                        .param("email", "a")
                        .param("username", lettoreDTO.getUsername())
                        .param("nome", lettoreDTO.getNome())
                        .param("cognome", lettoreDTO.getCognome())
                        .param("password", "LettorePassword")
                        .param("conferma_password", confermaPassword)
                        .param("provincia", lettoreDTO.getProvincia())
                        .param("citta", lettoreDTO.getCitta())
                        .param("via", lettoreDTO.getVia())
                        .param("recapito_telefonico", lettoreDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ISCRIZIONE_FALLITA));
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

}
