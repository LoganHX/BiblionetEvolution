package it.unisa.c07.biblionet.gestioneutenti.service;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;


import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AutenticazioneServiceImplTest {

    @Mock
    private LettoreService lettoreService;

    @Mock
    private EspertoService espertoService;

    @Mock
    private BibliotecaService bibliotecaService;

    private AutenticazioneServiceImpl autenticazioneService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        autenticazioneService = new AutenticazioneServiceImpl(espertoService, lettoreService, bibliotecaService);

    }

    @Test
    public void loginLettore()  {

        Lettore lettore = new Lettore("giuliociccione@gmail.com",
                "LettorePassword",
                "Salerno",
                "Baronissi",
                "Via Barone 11",
                "3456789012",
                "SuperLettore",
                "Giulio",
                "Ciccione"
        );

        when(lettoreService.findLettoreByEmailAndPassword(Mockito.any(), Mockito.any())).thenReturn(lettore);
        assertEquals(lettore, autenticazioneService.login("a", "a"));
    }

    @Test
    public void loginEsperto() {

        Esperto esperto = new Esperto(
                "eliaviviani@gmail.com",
                "EspertoPassword",
                "Napoli",
                "Torre del Greco",
                "Via Roma 2",
                "2345678901",
                "Espertissimo",
                "Elia",
                "Viviani",
                null
        );

        when(espertoService.findEspertoByEmailAndPassword(Mockito.any(), Mockito.any())).thenReturn(esperto);
        assertEquals(esperto, autenticazioneService.login("a", "a"));
    }

    @Test
    public void loginBiblioteca() {

        BibliotecaDTO b = new BibliotecaDTO(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "1234567890",
                "Biblioteca Carrisi"
        );
        Biblioteca biblioteca = new Biblioteca(b);

        when(espertoService.findEspertoByEmailAndPassword(Mockito.any(), Mockito.any())).thenReturn(biblioteca);
        assertEquals(biblioteca, autenticazioneService.login("a", "a"));
    }
}