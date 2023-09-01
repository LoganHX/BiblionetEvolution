package it.unisa.c07.biblionet.gestionebiblioteca.service;


import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Implementa il testing di unità per la classe
 * PrenotazioneLibriServiceImpl.
 *
 * @author Viviana Pentangelo
 * @author Gianmario Voria
 * @author Antonio Della Porta
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BibliotecaServiceImplTest {


    @Mock
    private BibliotecaDAO bibliotecaDAO;

    @InjectMocks
    private BibliotecaServiceImpl bibliotecaService;

    /**
     * Implementa il test della funzionalità che permette di
     * recuperare una biblioteca dato il suo ID.
     */
    @Test
    public void findBibliotecaByEmail() {
        Biblioteca biblioteca =  new Biblioteca();
        when(bibliotecaDAO.findByID("a")).thenReturn(biblioteca);
        assertEquals(bibliotecaService.findBibliotecaByEmail("a"), biblioteca);
    }

    /**
     * Implementa il test della funzionalità che permette di
     * recuperare la lista di tutte le biblioteche del DB.
     */
    @Test
    public void findAllBiblioteche() {
        List<Biblioteca> list = new ArrayList<>();
        when(bibliotecaDAO.findAllBiblioteche()).thenReturn(list);
        assertEquals(bibliotecaService.findBibliotecaByNome("a"), list);
    }


    /**
     * Implementa il test della funzionalità che permette di
     * recuperare una lista delle biblioteche che contengono
     * la stringa passata nella città.
     */
    @Test
    public void findBibliotecaByCitta() {
        List<Biblioteca> list = new ArrayList<>();
        when(bibliotecaDAO.findByCitta("a")).thenReturn(list);
        assertEquals(bibliotecaService.findBibliotecaByCitta("a"), list);
    }

    /**
     * Utilizzato per fornire il libro ai test
     * @return il libro
     */
    private static Stream<Arguments> provideLibro() {
        return Stream.of(
                Arguments.of(
                        new Libro(
                                "Amore Amaro",
                                "Fru",
                                "9597845613497",
                                LocalDateTime.of(LocalDate.of(2010, 10, 15), LocalTime.now()),
                                "Biblioteche 2.0",
                                "Mondadori"
                        )
                )
        );
    }


}
