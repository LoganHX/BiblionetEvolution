package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.LettoreDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@SpringBootTest
@RunWith(SpringRunner.class)
public class LettoreServiceImplTest {

    @Mock
    private LettoreDAO lettoreDAO;

    /**
     * Inject del service per simulare le operazioni.
     */
    private LettoreServiceImpl lettoreService;

    @Before
    public void setUp() {
        lettoreService = new LettoreServiceImpl(lettoreDAO);
    }

    /**
     * Metodo che si occupa di testare
     * la funzione di ricerca di un
     * Lettore nel service.
     */
    @Test
    public void findLettoreByEmail() {
        Lettore dummy = new Lettore("giuliociccione@gmail.com",
                "LettorePassword",
                "Salerno",
                "Baronissi",
                "Via Barone 11",
                "3456789012",
                "SuperLettore",
                "Giulio",
                "Ciccione"
        );
        String email = "";
        when(lettoreDAO.findLettoreByEmail(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(dummy);
        assertEquals(dummy, lettoreService.findLettoreByEmail(email));
    }

    /**
     * Implementa il test della funzionalità di iscrizione
     * di un lettore ad un club del libro in service.
     */
    @Test
    public void partecipaClub() {
        Lettore l = new Lettore("giuliociccione@gmail.com",
                "LettorePassword",
                "Salerno",
                "Baronissi",
                "Via Barone 11",
                "3456789012",
                "SuperLettore",
                "Giulio",
                "Ciccione"
        );
        when(lettoreDAO.save(l)).thenReturn(l);
        assertEquals(l, lettoreService.effettuaIscrizioneClub(new ClubDelLibro(), l));
    }


}
