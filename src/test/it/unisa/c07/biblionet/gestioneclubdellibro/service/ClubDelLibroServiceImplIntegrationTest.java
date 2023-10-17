package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Implementa l'integration testing del service per il sottosistema
 * ClubDelLibro.
 * @author Luca Topo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ClubDelLibroServiceImplIntegrationTest implements 
                                                    ApplicationContextAware {
    
    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private ClubDelLibroDAO clubDAO;

    @Autowired
    private EspertoDAO espertoDAO;

    @Autowired
    private ClubDelLibroService clubService;

    @Before
    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    /**
     * Verifica il corretto funzionamento della funzionalità di creazione
     * di un Club del Libro.**/

    @Test
    public void creaClubDelLibroValido() throws IOException {
        Esperto esperto = espertoDAO.findEspertoByEmail("eliaviviani@gmail.com","Esperto");

        Set<String> generi = new HashSet<>();
        generi.add("Saggistica");
        var c= new ClubDTO("Club1",
                        "descrizione1",
                        generi);



        var club = this.clubService.creaClubDelLibro(c,esperto);

        assertNotEquals(club, null);
        assertEquals(club.getNome(), c.getNome());
        assertEquals(club.getDescrizione(), c.getDescrizione());


        var clubInDB = this.clubDAO.findById(club.getIdClub());

        assertFalse(clubInDB.isEmpty());
        assertEquals(c.getNome(), clubInDB.get().getNome());
        assertEquals(c.getDescrizione(), clubInDB.get().getDescrizione());


    }


}
