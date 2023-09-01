package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.GenereDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class GenereServiceImplTest {

    @Autowired
    private GenereDAO genereDAO;
    /**
     * Implementa il test della funzionalità di selezione
     * di una lista di generi dato il loro nome in service.
     */
    @Test
    public void getAllGeneri() {
        /*
        List<Genere> listaGeneri = new ArrayList<>();
        List<Genere> nomi = new ArrayList<>();
        when(genereDAO.findByName("")).thenReturn(new Genere());
        assertEquals(listaGeneri, genereService.getAllGeneri(nomi));*/
    }
}
