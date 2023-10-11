package it.unisa.c07.biblionet.gestioneutenti.service;

import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.common.UtenteRegistrato;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.BibliotecaDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.EspertoDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.LettoreDAO;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Ciro Maiorino , Giulio Triggiani
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AutenticazioneServiceImplTest {

    /**
     * Inject del service per simulare le operazioni.
     */
    @InjectMocks
    private AutenticazioneServiceImpl autenticazioneService;



    /**
     * Mocking del dao per simulare le
     * CRUD.
     */
    @InjectMocks
    private LettoreService lettoreService;
    /**
     * Mocking del dao per simulare le
     * CRUD.
     */
    @InjectMocks
    private EspertoService espertoService;
    /**
     * Mocking del dao per simulare le
     * CRUD.
     */
    @InjectMocks
    private BibliotecaService bibliotecaService;

    /**
     * Implementa il test della
     * funzionalità di login di un lettore
     * nel service.
     * @throws NoSuchAlgorithmException L'eccezione che può essere lanciata
     * dal metodo getInstance().
     */
    @ParameterizedTest
    @MethodSource("provideLettore")
    public void loginLettore(final Lettore lettore) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");

        String email = "lettore@lettore.com";
        String password = "mipiaccionoglialberi";

        byte[] arr = md.digest(password.getBytes());

        when(BiblionetConstraints.trasformaPassword(password)).thenReturn(arr);

        when(lettoreService.findLettoreByEmailAndPassword(email,
                                            arr)).thenReturn(lettore);

        assertEquals(lettore, autenticazioneService.login(email,
                                                        password));
    }
    /**
     * Implementa il test della
     * funzionalità di login di una biblioteca
     * nel service.
     * @throws NoSuchAlgorithmException L'eccezione che può
     * essere lanciata dal metodo getInstance().
     */
    @Test
    public void loginBiblioteca() throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");

        String email = "biblioteca@biblioteca.com";
        String password = "mipiacelacarta";

        byte[] arr = md.digest(password.getBytes());

        when(BiblionetConstraints.trasformaPassword(password)).thenReturn(arr);

        Biblioteca biblioteca = new Biblioteca();

        when(lettoreService.findLettoreByEmailAndPassword(email,
                                                arr)).thenReturn(null);

        when(bibliotecaService.findBibliotecaByEmailAndPassword(email, arr)).thenReturn(biblioteca);

        assertEquals(biblioteca, autenticazioneService.login(email,
                                                            password));
    }
    /**
     * Implementa il test della
     * funzionalità di login di un esperto
     * nel service.
     * @throws NoSuchAlgorithmException L'eccezione che può
     * essere lanciata dal metodo getInstance().
     */
    @Test
    public void loginEsperto() throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");

        String email = "esperto@esperto.com";
        String password = "mipiacelaconsocenza";

        byte[] arr = md.digest(password.getBytes());

        when(BiblionetConstraints.trasformaPassword(password)).thenReturn(arr);


        Esperto esperto = new Esperto();

        when(lettoreService.findLettoreByEmailAndPassword(email,
                arr)).thenReturn(null);

        when(bibliotecaService.findBibliotecaByEmailAndPassword(email, arr)).thenReturn(null);

        when(espertoService.findEspertoByEmailAndPassword(email,
                                            arr)).thenReturn(esperto);

        assertEquals(esperto, autenticazioneService.login(email,
                                                        password));
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
