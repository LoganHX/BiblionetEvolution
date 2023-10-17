package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.GenereDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GenereServiceImplTest {

    @Mock
    private GenereDAO genereDAO;

    private GenereServiceImpl genereService;


    @Before
    public void setUp() {
        genereService = new GenereServiceImpl(genereDAO);
    }
    /**
     * Implementa il test della funzionalità di selezione
     * di una lista di generi dato il loro nome in service.
     */

    @Test
    public void getAllGeneri() {
        /*
        Set<Genere> listaGeneri = new ArrayList<>();
        Set<Genere> nomi = new ArrayList<>();
        when(genereDAO.findByName("")).thenReturn(new Genere());
        assertEquals(listaGeneri, genereService.getAllGeneri(nomi));*/
    }
    /**
     * Metodo che si occupa di testare
     * la funzione di ricerca di un
     * genere facendo 0 iterazioni nel service.
     */
    @Test
    public void findGeneriByName0IT() {
        Set<Genere> list = new HashSet<>();
        String[] generi = {""};
        assertEquals(list, genereService.getGeneriByName(Arrays.stream(generi).toList()));
    }

    /**
     * Metodo che si occupa di testare
     * la funzione di ricerca di un
     * genere facendo una iterazione nel service.
     */
    @Test
    public void findGeneriByName1IT() {

        Set<Genere> list = new HashSet<>();
        list.add(new Genere());
        String[] generi = {"test"};
        when(genereDAO.findByName("test")).thenReturn(new Genere());
        assertEquals(list, genereService.getGeneriByName(Arrays.stream(generi).toList()));
    }

    /**
     * Metodo che si occupa di testare
     * la funzione di ricerca di un
     * genere facendo due iterazioni nel service.
     */
    @Test
    public void findGeneriByName2IT() {

        Set<Genere> list = new HashSet<>();
        list.add(new Genere());
        list.add(new Genere());
        String[] generi = {"test", "test2"};
        when(genereDAO.findByName("test")).thenReturn(new Genere());
        when(genereDAO.findByName("test2")).thenReturn(new Genere());
        assertEquals(list, genereService.getGeneriByName(Arrays.stream(generi).toList()));
    }

    /**
     * Metodo che si occupa di testare
     * la funzione di ricerca di un genere
     * facendo fallire l'if nel service.
     */
    @Test
    public void findGeneriByName_GenereInesistente() {

        Set<Genere> list = new HashSet<>();
        String[] generi = {"test"};
        when(genereDAO.findByName("test")).thenReturn(null);
        Assertions.assertNull(genereService.getGeneriByName(Arrays.stream(generi).toList()));
    }

    /**
     * Metodo che si occupa di testare
     * la funzione di ricerca di un genere
     * facendo riuscire l'if nel service.
     */
    @Test
    public void findGeneriByName2() {

        Set<Genere> list = new HashSet<>();
        list.add(new Genere());
        String[] generi = {"test"};
        when(genereDAO.findByName("test")).thenReturn(new Genere());
        assertEquals(list, genereService.getGeneriByName(Arrays.stream(generi).toList()));
    }

}
