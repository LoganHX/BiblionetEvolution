package it.unisa.c07.biblionet.gestioneutenti.service;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RegistrazioneServiceImplTest {

    /**
     * Si occupa di gestire le operazioni CRUD dell'Esperto.
     */
    @Mock
    private EspertoService espertoService;

    /**
     * Si occupa di gestire le operazioni CRUD della biblioteca.
     */
    @Mock
    private BibliotecaService bibliotecaService;

    /**
     * Mocking del dao per simulare le
     * CRUD.
     */
    @Mock
    private LettoreService lettoreService;

    private RegistrazioneServiceImpl registrazioneService;
    @Before
    public void setUp() {
        registrazioneService = new RegistrazioneServiceImpl(bibliotecaService, espertoService, lettoreService);
    }

    @Test
    public void isEmailRegistrata_Lettore(){

        Lettore lettore = new Lettore();

        when(lettoreService.findLettoreByEmail(Mockito.any())).thenReturn(lettore);
        when(bibliotecaService.findBibliotecaByEmail(Mockito.any())).thenReturn(null);
        when(espertoService.findEspertoByEmail(Mockito.any())).thenReturn(null);

        assertTrue(registrazioneService.isEmailRegistrata("test@test"));

    }
    /**
     * Metodo che testa la funzione di ricerca
     * di una mail se già esistente con una lista
     * con un solo utente in input.
     * Ritorna true perchè la mail usata esiste nella lista.
     */
    @Test
    public void isEmailRegistrata_Biblioteca(){

        Biblioteca biblioteca = new Biblioteca();

        when(lettoreService.findLettoreByEmail(Mockito.any())).thenReturn(null);
        when(bibliotecaService.findBibliotecaByEmail(Mockito.any())).thenReturn(biblioteca);
        when(espertoService.findEspertoByEmail(Mockito.any())).thenReturn(null);

        assertTrue(registrazioneService.isEmailRegistrata("test@test"));

    }

    /**
     * Metodo che testa la funzione di ricerca
     * di una mail se già esistente con una lista
     * con un solo utente in input.
     * Ritorna true perchè la mail usata esiste nella lista.
     */
    @Test
    public void isEmailRegistrata_Esperto(){

        Esperto esperto = new Esperto();

        when(lettoreService.findLettoreByEmail(Mockito.any())).thenReturn(null);
        when(bibliotecaService.findBibliotecaByEmail(Mockito.any())).thenReturn(null);
        when(espertoService.findEspertoByEmail(Mockito.any())).thenReturn(esperto);

        assertTrue(registrazioneService.isEmailRegistrata("test@test"));

    }

    @Test
    public void isEmailRegistrata_False(){


        when(lettoreService.findLettoreByEmail(Mockito.any())).thenReturn(null);
        when(bibliotecaService.findBibliotecaByEmail(Mockito.any())).thenReturn(null);
        when(espertoService.findEspertoByEmail(Mockito.any())).thenReturn(null);

        assertFalse(registrazioneService.isEmailRegistrata("test@test"));

    }
}
