package it.unisa.c07.biblionet.gestionebiblioteca.service;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.BibliotecaDAO;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Implementa l'integration testing del service per il sottosistema
 * Prenotazione Libri.
 * @author Antonio Della Porta
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PrenotazioneLibriServiceIntegrationTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private BibliotecaService bibliotecaService;

    @Autowired
    private BibliotecaDAO bibliotecaDAO;

    @BeforeEach
    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    @Test
    public void getBiblioteca(){
        Biblioteca biblioteca = bibliotecaDAO.findByID("aldomoronocera@gmail.com");
        assertEquals(biblioteca.getNomeBiblioteca(), bibliotecaService.findBibliotecaByEmail("aldomoronocera@gmail.com").getNomeBiblioteca());
    }


}
