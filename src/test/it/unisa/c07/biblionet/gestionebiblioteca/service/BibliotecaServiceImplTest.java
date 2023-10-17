package it.unisa.c07.biblionet.gestionebiblioteca.service;


import it.unisa.c07.biblionet.common.LibroDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.*;
import org.junit.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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


    @MockBean
    private BibliotecaDAO bibliotecaDAO;

    @MockBean
    private BibliotecaServiceImpl bibliotecaService;

    /**
     * Implementa il test della funzionalità che permette di
     * recuperare una biblioteca dato il suo ID.
     */
    @Test
    public void findBibliotecaByEmail_BibliotecaInesistente() {
        when(bibliotecaDAO.findByID(Mockito.anyString())).thenReturn(null);
        assertNull(bibliotecaService.findBibliotecaByEmail(Mockito.anyString()));
    }

    @Test
    public void findBibliotecaByEmail_Ok() {
        when(bibliotecaDAO.findByID(Mockito.anyString())).thenReturn(new Biblioteca());
        assertNull(bibliotecaService.findBibliotecaByEmail(Mockito.anyString()));
    }

    /**
     * Implementa il test della funzionalità che permette di
     * recuperare la lista di tutte le biblioteche del DB.
     */
    @Test
    public void findAllBiblioteche() {
        List<Biblioteca> list = new ArrayList<>();
        list.add(new Biblioteca());
        when(bibliotecaDAO.findAllBiblioteche()).thenReturn(list);
        assertEquals(bibliotecaService.findBibliotecaByNome(Mockito.any()), list);
    }


    /**
     * Implementa il test della funzionalità che permette di
     * recuperare una lista delle biblioteche che contengono
     * la stringa passata nella città.
     */
    @Test
    public void findBibliotecaByCitta_NonPresente() {
        List<Biblioteca> list = new ArrayList<>();
        when(bibliotecaDAO.findByCitta("a")).thenReturn(null);
        assertEquals(bibliotecaService.findBibliotecaByCitta(Mockito.anyString()).isEmpty(), (true));
    }

    @Test
    public void findBibliotecaByCitta_Ok() {
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
                        new LibroDTO(
                                1,
                                "Fru",
                                "9597845613497",
                                "1234567891234",
                                "2010",
                                "Mondadori",
                                "Casa",
                                "@@@@@",
                                null
                        )
                )
        );
    }


}
