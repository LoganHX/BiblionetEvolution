package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Implementa il testing di unità per la classe
 * ClubDelLibroServiceImpl.
 * @author Viviana Pentangelo, Gianmario Voria
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ClubDelLibroServiceImplTest {

    /**
     * Inject del service per simulare
     * le operazioni del service.
     */
    @InjectMocks
    private ClubDelLibroServiceImpl clubService;

    /**
     * Mocking del dao per simulare le
     * CRUD su clubDelLibro.
     */
    @MockBean
    private ClubDelLibroDAO clubDAO;

    /**
     * Mocking del dao per simulare le
     * CRUD su genere.
     */
    @Mock
    private GenereDAO genereDAO;

    /**
     * Mocking del dao per simulare le
     * CRUD su lettore.
     */
    @Mock
    private LettoreDAO lettoreDAO;

    /**
     * Implementa il test della funzionalità di
     * creazione  di un club del libro in service.
     */
    @Test
    public void creaClubDelLibro() throws IOException {
        ClubDelLibro club = new ClubDelLibro();
        when(clubDAO.save(Mockito.any())).thenReturn(club);
        assertEquals(club, clubService.creaClubDelLibro(new ClubDTO(), new Esperto()));
    }

    /**
     * Implementa il test della funzionalità
     * di recupero di
     * tutti i club del libro in service.
     */
    @Test
    public void visualizzaClubsDelLibro() {
        ClubDelLibro club = new ClubDelLibro();
        List<ClubDelLibro> list = new ArrayList<>();
        list.add(club);
        when(clubDAO.findAll()).thenReturn(list);
        assertEquals(list, clubService.visualizzaClubsDelLibro());
    }



    /**
     * Implementa il test della funzionalità di
     * modifica dei dati
     * di un club del libro in service.
     */
    @Test
    public void modificaDatiClub() {
        ClubDelLibro club = new ClubDelLibro();
        when(clubDAO.save(club)).thenReturn(club);
        assertEquals(club, clubService.salvaClub(club));
    }

    /**
     * Implementa il test della funzionalità di select di un
     * club dato il suo ID in service.
     */
    @Test
    public void getClubByID() {
        ClubDelLibro club = new ClubDelLibro();
        when(clubDAO.findById(1)).thenReturn(java.util.Optional.of(club));
        assertEquals(club, clubService.getClubByID(1));
    }



}
