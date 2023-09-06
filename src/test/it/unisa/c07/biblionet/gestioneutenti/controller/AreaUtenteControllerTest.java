package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Stream;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AreaUtenteControllerTest {


    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private AutenticazioneService autenticazioneService;

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
    private EspertoService espertoService;

    @MockBean
    private Utils utils;

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
     * controller per la modifica di un lettore
     * avvenuta in modo corretto.
     *
     * @param utenteDTO Il lettore da modificare
     * @param vecchiaPassword La vecchia password dell'account
     * @param nuovaPassword La nuova password dell'account
     * @param confermaPassword La conferma password
     * @throws Exception eccezione di mockMvc
     */
    @ParameterizedTest
    @DisplayName("Modifica Dati Lettore")
    @MethodSource("provideModificaLettore")
    public void modificaLettoreOk(
            final LettoreDTO utenteDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword) throws Exception {

        //todo dovrei testare controlli preliminari sia qua che in registrazione?
        String token="";
        Lettore lettore = new Lettore();

        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(lettoreService.findLettoreByEmail(utenteDTO.getEmail())).thenReturn(lettore);
        when(lettoreService.aggiornaLettoreDaModel(Mockito.any())).thenReturn(lettore);
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(lettore);

        this.mockMvc.perform(post("/area-utente/modifica-lettore")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                .param("nome", utenteDTO.getNome())
                .param("cognome", utenteDTO.getCognome())
                .param("username", utenteDTO.getUsername())
                .param("vecchia_password", vecchiaPassword)
                .param("nuova_password", nuovaPassword)
                .param("conferma_password", confermaPassword)
                .param("provincia", utenteDTO.getProvincia())
                .param("citta", utenteDTO.getCitta())
                .param("via", utenteDTO.getVia())
                .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }


    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la modifica di un lettore
     * avvenuta in modo scorretto.
     * La vecchiaPassword, nuovaPassword oppure confermaPassword
     * sono vuote.
     *
     * @param utenteDTO Il lettore da modificare
     * @throws Exception eccezione di mockMvc
     */
    @ParameterizedTest
    @DisplayName("Modifica Dati Lettore Errato 1")
    @MethodSource("provideModificaLettore")
    public void modificaLettoreConfermaPasswordDiversa(
            final LettoreDTO utenteDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword) throws Exception {

        //todo dovrei testare controlli preliminari sia qua che in registrazione?
        String token="";
        Lettore lettore = new Lettore();

        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(lettore);
        when(lettoreService.aggiornaLettoreDaModel(Mockito.any())).thenReturn(lettore);
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(lettore);

        this.mockMvc.perform(post("/area-utente/modifica-lettore")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("vecchia_password", vecchiaPassword)
                        .param("nuova_password", nuovaPassword)
                        .param("conferma_password", "PasswordErrataaa")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }

    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la modifica di un lettore
     * avvenuta in modo scorretto.
     * Se la vecchia password inserita é diversa
     * da quella corrente.
     * OPPURE.
     * Se nuovaPassword é diversa da confermaPassword.
     *
     * @param lettoreDTO Il lettore da modificare
     * @param vecchiaPassword La vecchia password dell'account
     * @throws Exception eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Passord Lettore Diverse")
    @MethodSource("provideModificaLettore")
    public void modificaLettorePasswordErrata(
            final LettoreDTO lettoreDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword) throws Exception {

        String token="";

        Lettore lettore = new Lettore();
        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(lettoreDTO.getEmail());
        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(lettore);
        when(lettoreService.aggiornaLettoreDaModel(Mockito.any())).thenReturn(lettore);
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(lettore);

        this.mockMvc.perform(post("/area-utente/modifica-lettore")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", lettoreDTO.getEmail())
                        .param("nome", lettoreDTO.getNome())
                        .param("cognome", lettoreDTO.getCognome())
                        .param("username", lettoreDTO.getUsername())
                        .param("vecchia_password", vecchiaPassword)
                        .param("nuova_password", nuovaPassword)
                        .param("conferma_password", "PasswordDiversaaa")
                        .param("provincia", lettoreDTO.getProvincia())
                        .param("citta", lettoreDTO.getCitta())
                        .param("via", lettoreDTO.getVia())
                        .param("recapito_telefonico", lettoreDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideModificaLettore() {

        return Stream.of(
                Arguments.of(
                        new LettoreDTO(
                                "antoniorenatomontefusco@gmail.com",
                                "LettorePassword",
                                "Salerno",
                                "Baronissi",
                                "Via Barone 11",
                                "3456789012",
                                "SuperLettore",
                                "Giulio",
                                "Ciccione"
                        ),
                        "LettorePassword", // vecchia password
                        "NuovaPassword",   // nuova password
                        "NuovaPassword"    // conferma password
                )
        );
    }


    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la modifica di un esperto
     * avvenuta in modo corretto.
     *
     * @param utenteDTO L'esperto da modificare
     * @param vecchiaPassword La vecchia password dell'account
     * @param nuovaPassword La nuova password dell'account
     * @param confermaPassword La conferma password
     * @param emailBiblioteca la mail della biblioteca dove lavora
     * @throws Exception eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Modifica Dati Esperto Corretto")
    @MethodSource("provideModificaEsperto")
    public void modificaEspertoOk(
            final EspertoDTO utenteDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword,
            final String emailBiblioteca) throws Exception {

        //todo dovrei testare controlli preliminari sia qua che in registrazione?
        String token="";

        Esperto esperto = new Esperto();
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(esperto);
        when(espertoService.aggiornaEspertoDaModel(Mockito.any(), Mockito.any())).thenReturn(esperto);
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(esperto);
        when(bibliotecaService.findBibliotecaByEmail(emailBiblioteca)).thenReturn(new Biblioteca());

        this.mockMvc.perform(post("/area-utente/modifica-esperto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("vecchia_password", vecchiaPassword)
                        .param("nuova_password", nuovaPassword)
                        .param("conferma_password", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("email_biblioteca", emailBiblioteca)
                        .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));

    }

    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la modifica di un esperto
     * avvenuta in modo scorretto.
     * La vecchiaPassword, nuovaPassword oppure confermaPassword
     * sono vuote.
     *
     * @param utenteDTO L'esperto da modificare
     * @param emailBiblioteca L'email della biblioteca in cui lavora.
     * @throws Exception eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Modifica Dati Esperto Password non cambiata")
    @MethodSource("provideModificaEsperto")
    public void modificaEspertoConfermaPasswordErrata(
            final EspertoDTO utenteDTO,
            final String emailBiblioteca ) throws Exception {
//todo dovrei testare controlli preliminari sia qua che in registrazione?
        String token="";
        Esperto esperto = new Esperto();

        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(esperto);
        when(espertoService.aggiornaEspertoDaModel(Mockito.any(), Mockito.any())).thenReturn(esperto);
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(esperto);
        when(bibliotecaService.findBibliotecaByEmail(emailBiblioteca)).thenReturn(new Biblioteca());

        this.mockMvc.perform(post("/area-utente/modifica-esperto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
//                        .param("vecchia_password", vecchiaPassword)
//                        .param("nuova_password", nuovaPassword)
//                        .param("conferma_password", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("email_biblioteca", emailBiblioteca)
                        .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(status().is4xxClientError());

    }

    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la modifica di una biblioteca
     * avvenuta in modo scorretto.
     * Se la vecchia password inserita é diversa
     * da quella corrente.
     *
     * @param utenteDTO L'esperto da modificare
     * @param nuovaPassword La nuova password dell'account
     * @param confermaPassword La conferma password
     * @param emailBiblioteca L'email della biblioteca in cui lavora.
     * @throws Exception eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Modifica Dati Esperto vecchia password errata")
    @MethodSource("provideModificaEsperto")
    public void modificaEspertoPasswordErrata(
            final EspertoDTO utenteDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword,
            final String emailBiblioteca) throws Exception {

        //todo dovrei testare controlli preliminari sia qua che in registrazione?

        String token="";
        Esperto esperto = new Esperto();

        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(esperto);
        when(espertoService.aggiornaEspertoDaModel(Mockito.any(), Mockito.any())).thenReturn(esperto);
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(bibliotecaService.findBibliotecaByEmail(emailBiblioteca)).thenReturn(new Biblioteca());

        this.mockMvc.perform(post("/area-utente/modifica-esperto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("vecchia_password", vecchiaPassword)
                        .param("nuova_password", nuovaPassword)
                        .param("conferma_password", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("email_biblioteca", emailBiblioteca)
                        .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la modifica di una biblioteca
     * avvenuta in modo scorretto.
     * Se nuovaPassword é diversa da confermaPassword.
     *
     * @param utenteDTO L'esperto da modificare
     * @param nuovaPassword La nuova password dell'account
     * @param emailBiblioteca L'email della biblioteca in cui lavora.
     * @throws Exception eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Modifica Dati Esperto conferma password errata")
    @MethodSource("provideModificaEsperto")
    public void modificaEspertoConfermaPasswordErrata(
            final EspertoDTO utenteDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword,
            final String emailBiblioteca) throws Exception {

        //todo dovrei testare controlli preliminari sia qua che in registrazione?

        String token="";
        Esperto esperto = new Esperto();

        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(esperto);
        when(espertoService.aggiornaEspertoDaModel(Mockito.any(), Mockito.any())).thenReturn(esperto);
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(esperto);
        when(bibliotecaService.findBibliotecaByEmail(emailBiblioteca)).thenReturn(new Biblioteca());

        this.mockMvc.perform(post("/area-utente/modifica-esperto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("vecchia_password", vecchiaPassword)
                        .param("nuova_password", nuovaPassword)
                        .param("conferma_password", "PasswordErrataaa")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("email_biblioteca", emailBiblioteca)
                        .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }

    /**
     * Metodo che testa la funzionalità gestita dal
     * controller per la modifica di una biblioteca
     * avvenuta in modo scorretto.
     *
     * @param utenteDTO L'esperto da modificare
     * @param nuovaPassword La nuova password dell'account
     * @param confermaPassword La conferma password
     * @param emailBiblioteca L'email della biblioteca in cui lavora.
     * @throws Exception eccezione di MockMvc
     */
    @ParameterizedTest
    @DisplayName("Modifica Dati Esperto biblioteca null")
    @MethodSource("provideModificaEsperto")
    public void modificaEspertoBibliotecaInesistente(
            final EspertoDTO utenteDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword,
            final String emailBiblioteca) throws Exception {

        //todo dovrei testare controlli preliminari sia qua che in registrazione?
        String token="";

        Esperto esperto = new Esperto();

        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(esperto);
        when(espertoService.aggiornaEspertoDaModel(Mockito.any(), Mockito.any())).thenReturn(esperto);
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(esperto);
        when(bibliotecaService.findBibliotecaByEmail(emailBiblioteca)).thenReturn(null);

        this.mockMvc.perform(post("/area-utente/modifica-esperto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNome())
                        .param("cognome", utenteDTO.getCognome())
                        .param("username", utenteDTO.getUsername())
                        .param("vecchia_password", vecchiaPassword)
                        .param("nuova_password", nuovaPassword)
                        .param("conferma_password", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("email_biblioteca", emailBiblioteca)
                        .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }


    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideModificaEsperto() {

        return Stream.of(
                Arguments.of(
                        new EspertoDTO(
                                "eliaviviani@gmail.com",
                                "EspertoPassword",
                                "Napoli",
                                "Torre del Greco",
                                "Via Roma 2",
                                "1234567890",
                                "Espertissimo",
                                "Elia",
                                "Viviani"





                        ),
                        "EspertoPassword",              // vecchia password
                        "NuovaPassword",                // nuova password
                        "NuovaPassword",                // conferma password
                        "bibliotecacarrisi@gmail.com"   // mail della biblioteca
                )
        );
    }





    @ParameterizedTest
    @DisplayName("Modifica Dati Biblioteca Corretto")
    @MethodSource("provideModificaBiblioteca")
    public void modificaBibliotecaOk(
            final BibliotecaDTO utenteDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword) throws Exception {

        //todo dovrei testare controlli preliminari sia qua che in registrazione?
        String token="";

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(new Biblioteca());
        when(bibliotecaService.aggiornaBibliotecaDaModel(Mockito.any())).thenReturn(new Biblioteca());
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(new Biblioteca());

        this.mockMvc.perform(post("/area-utente/modifica-biblioteca")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNomeBiblioteca())
                        .param("vecchia_password", vecchiaPassword)
                        .param("nuova_password", nuovaPassword)
                        .param("conferma_password", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @ParameterizedTest
    @DisplayName("Modifica Dati Biblioteca Vecchia Password non corretta")
    @MethodSource("provideModificaBiblioteca")
    public void modificaBibliotecaPasswordErrata(
            final BibliotecaDTO utenteDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword) throws Exception {

        //todo dovrei testare controlli preliminari sia qua che in registrazione?
        String token="";

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(new Biblioteca());
        when(bibliotecaService.aggiornaBibliotecaDaModel(Mockito.any())).thenReturn(new Biblioteca());
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        this.mockMvc.perform(post("/area-utente/modifica-biblioteca")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNomeBiblioteca())
                        .param("vecchia_password", vecchiaPassword)
                        .param("nuova_password", nuovaPassword)
                        .param("conferma_password", confermaPassword)
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @ParameterizedTest
    @DisplayName("Modifica Dati Biblioteca Conferma Password non corretta")
    @MethodSource("provideModificaBiblioteca")
    public void modificaBibliotecaConfermaPasswordDiversa(
            final BibliotecaDTO utenteDTO,
            final String vecchiaPassword,
            final String nuovaPassword,
            final String confermaPassword) throws Exception {

        //todo dovrei testare controlli preliminari sia qua che in registrazione?
        String token="";

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utenteDTO.getEmail());
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(new Biblioteca());
        when(bibliotecaService.aggiornaBibliotecaDaModel(Mockito.any())).thenReturn(new Biblioteca());
        when(autenticazioneService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        this.mockMvc.perform(post("/area-utente/modifica-biblioteca")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                        .param("email", utenteDTO.getEmail())
                        .param("nome", utenteDTO.getNomeBiblioteca())
                        .param("vecchia_password", vecchiaPassword)
                        .param("nuova_password", nuovaPassword)
                        .param("conferma_password", "PasswordErrataaa")
                        .param("provincia", utenteDTO.getProvincia())
                        .param("citta", utenteDTO.getCitta())
                        .param("via", utenteDTO.getVia())
                        .param("recapito_telefonico", utenteDTO.getRecapitoTelefonico()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }



    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideModificaBiblioteca() {
        return Stream.of(
          Arguments.of(
                  new BibliotecaDTO(
                          "bibliotecacarrisi@gmail.com",
                          "BibliotecaPassword",
                          "Napoli",
                          "Torre del Greco",
                          "Via Carrisi 47",
                          "1234567890",
                          "Biblioteca Carrisi"
                  ),
                  "BibliotecaPassword",
                  "BibliotecaPassword",
                  "BibliotecaPassword"
          )
        );
    }


    /**
     * Metodo che testa la funzionalità di
     * di visualizzare i clubs dell' utente Lettore.
     * @param utente Il lettore in sessione
     * @throws Exception eccezione di mockMvc
     */
    @ParameterizedTest
    @MethodSource("provideLettore")
    @DisplayName("provideLettore")
    public void visualizzaClubsLettoreOk(final Lettore utente)
            throws Exception {

        String token="";


        ClubDelLibro clubDelLibro = new ClubDelLibro();
        clubDelLibro.setNome("ClubName");
        List<ClubDelLibro> listaClub = new ArrayList<>();
        listaClub.add(clubDelLibro);
        utente.setClubs(listaClub);

        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utente.getEmail());
        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(utente);


        this.mockMvc.perform(post("/area-utente/visualizza-clubs-lettore")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value(clubDelLibro.getNome()));
    }

    /**
     * Metodo che testa la funzionalità di
     * di visualizzare i clubs dell' utente Lettore.
     * @param utente Il lettore in sessione
     * @throws Exception eccezione di mockMvc
     */
    @ParameterizedTest
    @MethodSource("provideLettore")
    @DisplayName("provideLettore")
    public void visualizzaClubsLettoreNonAutorizzato(final Lettore utente)
            throws Exception {

        String token="";
        ClubDelLibro clubDelLibro = new ClubDelLibro();
        clubDelLibro.setNome("ClubName");
        List<ClubDelLibro> listaClub = new ArrayList<>();
        listaClub.add(clubDelLibro);
        utente.setClubs(listaClub);

        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(utente);
        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(false);


        this.mockMvc.perform(post("/area-utente/visualizza-clubs-lettore")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    /**
     * Metodo che testa la funzionalità di
     * di visualizzare i clubs dell' utente Esperto.
     * @param utente L'esperto in sessione
     * @throws Exception eccezione di mockMvc
     */
    @ParameterizedTest
    @MethodSource("provideEsperto")
    @DisplayName("Visualizza Clubs Esperto")
    public void visualizzaClubsEspertoOk(final Esperto utente)
            throws Exception {
        String token="";

        ClubDelLibro clubDelLibro = new ClubDelLibro();
        clubDelLibro.setNome("ClubName");
        List<ClubDelLibro> listaClub = new ArrayList<>();
        listaClub.add(clubDelLibro);
        utente.setClubs(listaClub);

        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(utente);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn(utente.getEmail());

        this.mockMvc.perform(post("/area-utente/visualizza-clubs-esperto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))

                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value(clubDelLibro.getNome()));

    }

    /**
     * Metodo che testa la funzionalità di
     * di visualizzare i clubs dell' utente Esperto.
     * @param utente L'esperto in sessione
     * @throws Exception eccezione di mockMvc
     */
    @ParameterizedTest
    @MethodSource("provideEsperto")
    @DisplayName("Visualizza Clubs Esperto")
    public void visualizzaClubsEspertoNonAutorizzato(final Esperto utente)
            throws Exception {
        String token="";

        ClubDelLibro clubDelLibro = new ClubDelLibro();
        clubDelLibro.setNome("ClubName");
        List<ClubDelLibro> listaClub = new ArrayList<>();
        listaClub.add(clubDelLibro);
        utente.setClubs(listaClub);

        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(utente);
        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(post("/area-utente/visualizza-clubs-esperto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))

                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());

    }

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideEsperto() {

        Set<String> generi = new HashSet<>();
        generi.add("Fiction");
        return Stream.of(
                Arguments.of(
                        new Esperto(
                                "eliaviviani@gmail.com",
                                "EspertoPassword",
                                "Napoli",
                                "Torre del Greco",
                                "Via Roma 2",
                                "2345678901",
                                "Espertissimo",
                                "Elia",
                                "Viviani",
                                null
                        ),
                        generi

                )
        );

    }

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideLettore() {
        return Stream.of(Arguments.of(new Lettore("giuliociccione@gmail.com",
                "LettorePassword",
                "Salerno",
                "Baronissi",
                "Via Barone 11",
                "3456789012",
                "SuperLettore",
                "Giulio",
                "Ciccione"
        )));
    }

}
