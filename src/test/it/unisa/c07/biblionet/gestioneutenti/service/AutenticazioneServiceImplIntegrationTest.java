package it.unisa.c07.biblionet.gestioneutenti.service;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.LettoreDAO;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Implementa l'integration testing del service per il sottosistema
 * Autenticazione.
 * @author Antonio Della Porta
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AutenticazioneServiceImplIntegrationTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private AutenticazioneService autenticazioneService;

    @Autowired
    private LettoreDAO lettoreDAO;

    @Before
    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    /**
     * Implementa il test della
     * funzionalità di login di un lettore
     * nel service.
     * @throws NoSuchAlgorithmException L'eccezione che può essere lanciata
     * dal metodo getInstance().
     */
    @Test
    public void loginLettore() throws NoSuchAlgorithmException {

        String email = "antoniorenatomontefusco@gmail.com";
        String password = "LettorePassword";


        assertEquals(new Lettore(
                "antoniorenatomontefusco@gmail.com",
                "LettorePassword",
                "Napoli",
                "Somma Vesuviana",
                "Via Vesuvio 33",
                "3456789012",
                "antoniomontefusco",
                "Antonio",
                "Montefusco"
        ), autenticazioneService.login(email,
                password));
    }

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideAutenticazione() {
        return Stream.of(
                Arguments.of(
                        new Lettore(
                                "antoniorenatomontefusco@gmail.com",
                                "LettorePassword",
                                "Napoli",
                                "Somma Vesuviana",
                                "Via Vesuvio 33",
                                "3456789012",
                                "antoniomontefusco",
                                "Antonio",
                                "Montefusco"
                        )
                )
        );
    }


}
