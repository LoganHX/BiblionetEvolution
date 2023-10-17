package it.unisa.c07.biblionet.gestionebiblioteca.service;


import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.LibroDAO;
import it.unisa.c07.biblionet.common.LibroDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.bookapiadapter.BookApiAdapter;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.GenereService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDateTime;
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
public class PrenotazioneLibriServiceImplTest {

    /**
     * Inject del service per simulare
     * le operazioni del service.
     */
    private PrenotazioneLibriServiceImpl prenotazioneService;
    /**
     * Inject del service per simulare
     * le operazioni del service.
     */
    @Mock
    private BibliotecaService bibliotecaService;

    /**
     * Inject del service per simulare
     * le operazioni del service.
     */
    @Mock
    private GenereService genereService;

    @Mock
    private LettoreService lettoreService;

    /**
     * Inject dell'api di google.
     */
    @Mock
    private BookApiAdapter bookApiAdapter;

    /**
     * Mocking del dao per simulare le
     * CRUD su libro.
     */
    @Mock
    private LibroDAO libroDAO;


    /**
     * Mocking del dao per simulare le
     * CRUD su possesso.
     */
    @Mock
    private PossessoDAO possessoDAO;

    /**
     * Mocking del dao per simulare le
     * CRUD su ticket.
     */
    @Mock
    private TicketPrestitoDAO ticketPrestitoDAO;

    /**
     * Implementa il test della funzionalità di
     * selezione di tutti i libri prenotabili.
     */
    @Before
    public void setUp() {
        prenotazioneService = new PrenotazioneLibriServiceImpl(libroDAO, bookApiAdapter, possessoDAO, ticketPrestitoDAO, bibliotecaService, lettoreService, genereService);
    }
    @Test
    public void visualizzaListaLibriCompleta() {
        List<Libro> list = new ArrayList<>();
        list.add(new Libro());
        when(libroDAO.findAll(Sort.by("titolo"))).thenReturn(list);
        assertEquals(list, prenotazioneService.visualizzaListaLibriCompleta());
    }

    /**
     * Implementa il test della funzionalità di
     * selezione di tutti i libri prenotabili che contengono
     * una determinata stringa nel titolo.
     */
    @Test
    public void visualizzaListaLibriPerTitolo() {
        List<Libro> list = new ArrayList<>();
        list.add(new Libro());
        when(libroDAO.findByTitoloLike("a")).thenReturn(list);
        assertEquals(list,
                prenotazioneService.visualizzaListaLibriPerTitolo("a"));
    }



    /**
     * Implementa il test della funzionalità di
     * selezione di tutti i libri prenotabili di
     * una determinata biblioteca.
     */
    @Test
    public void visualizzaListaLibriPerBiblioteca_NessunLibro() {
        Biblioteca b1 = Mockito.mock(Biblioteca.class);
        b1.setEmail("b1");

        Possesso p1 = new Possesso(new PossessoId("a", 1), 1);
        Possesso p2 = new Possesso(new PossessoId("b", 1), 1);
        List<Possesso> possessi = new ArrayList<>();
        possessi.add(p1);
        possessi.add(p2);

        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b1);
        when(b1.getPossessi()).thenReturn(new ArrayList<>());

        List<Libro> libri = new ArrayList<>();
        assertEquals(libri,
                prenotazioneService.visualizzaListaLibriPerBiblioteca("b1"));
    }

    @Test
    public void visualizzaListaLibriPerBiblioteca_UnLibro() {
        Biblioteca b1 = Mockito.mock(Biblioteca.class);
        b1.setEmail("b1");

        Possesso p1 = new Possesso(new PossessoId("a", 1), 1);
        Possesso p2 = new Possesso(new PossessoId("b", 1), 1);
        List<Possesso> possessi = new ArrayList<>();
        possessi.add(p1);
        //possessi.add(p2);

        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b1);
        when(libroDAO.findById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(b1.getPossessi()).thenReturn(possessi);

        List<Libro> libri = new ArrayList<>();
        libri.add(new Libro());
        assertEquals(libri,
                prenotazioneService.visualizzaListaLibriPerBiblioteca("b1"));
    }

    /**
     * Implementa il test della funzionalità di
     * selezione di tutti i libri prenotabili di
     * una determinata biblioteca.
     */
    @Test
    public void visualizzaListaLibriPerBiblioteca_PiùLibri() {
        Biblioteca b1 = Mockito.mock(Biblioteca.class);
        b1.setEmail("b1");

        when(bibliotecaService.findBibliotecaByEmail("b1")).thenReturn(b1);
        List<Possesso> listPos = new ArrayList<>();
        Possesso p1 = new Possesso(
                new PossessoId("b1", 1), 1);
        Possesso p2 = new Possesso(
                new PossessoId("b2", 2), 2);
        listPos.add(p1);
        listPos.add(p2);
        when(b1.getPossessi()).thenReturn(listPos);
        List<Libro> libri = new ArrayList<>();
        Libro libro1 = new Libro();
        libro1.setIdLibro(1);
        libri.add(libro1);
        Optional<Libro> l1 = Optional.of(libro1);
        Optional<Libro> l2 = Optional.of(libro1);
        when(libroDAO.findById(1)).thenReturn(l1);
        when(libroDAO.findById(2)).thenReturn(l2);

        assertEquals(libri,
                prenotazioneService.visualizzaListaLibriPerBiblioteca("b1"));
    }


    @Test
    public void richiediPrestito() {
        Biblioteca b = new Biblioteca();
        Libro l = new Libro();
        TicketPrestito t = new TicketPrestito();

        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(new Lettore());
        when(bibliotecaService.findBibliotecaByEmail("id")).thenReturn(b);
        when(libroDAO.getOne(2)).thenReturn(l);
        when(ticketPrestitoDAO.save(t)).thenReturn(t);
        t.setStato(TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA);
        LocalDateTime ld = LocalDateTime.of(1, 1, 1, 1, 1);
        t.setDataRichiesta(ld);
        TicketPrestito test = prenotazioneService.richiediPrestito(
                "lettore@email.com", "id", 2);
        test.setDataRichiesta(ld);
        assertEquals(t.toString(), test.toString());
    }

    /**
     * Implementa il test della funzionalità che permette
     * di ottenere la lista delle biblioteche
     * che posseggono un dato libro.
     */
    @Test
    public void getBibliotecheLibroFor2() {
        Libro libro = new Libro();
        libro.setIdLibro(1);
        List<Possesso> pl = new ArrayList<>();
        Possesso p1 = new Possesso(new PossessoId("a", 1), 1);
        Possesso p2 = new Possesso(new PossessoId("b", 1), 1);
        pl.add(p1);
        pl.add(p2);
        when(possessoDAO.findByLibroID(1)).thenReturn(pl);
        Biblioteca b1 = new Biblioteca();
        b1.setEmail("a");
        Biblioteca b2 = new Biblioteca();
        b1.setEmail("b");
        List<Biblioteca> bl = new ArrayList<>();
        bl.add(b1);
        bl.add(b2);
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b1);
        when(bibliotecaService.findBibliotecaByEmail("b")).thenReturn(b1);
        assertEquals(bl.get(0).getEmail(), prenotazioneService.getBibliotecheLibro(libro).get(0).getEmail());
    }

    /**
     * Implementa il test della funzionalità che permette
     * di ottenere la lista delle biblioteche
     * che posseggono un dato libro.
     */
    @Test
    public void getBibliotecheLibroFor0() {
        Libro libro = new Libro();
        libro.setIdLibro(1);

        List<Possesso> pl = new ArrayList<>();
        when(possessoDAO.findByLibroID(1)).thenReturn(pl);
        List<Biblioteca> bl = new ArrayList<>();
        assertEquals(bl, prenotazioneService.getBibliotecheLibro(libro));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di ottenere un libro dato il suo ID.
     */
    @Test
    public void getLibroByID() {
        Libro l = new Libro();
        when(libroDAO.getOne(1)).thenReturn(l);
        assertEquals(l, prenotazioneService.getLibroByID(1));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di ottenere una lista di richieste per una biblioteca.
     */

    @Test
    public void getTicketsByBiblioteca() {
        List<TicketPrestito> list = new ArrayList<>();
        when(ticketPrestitoDAO.findAllByBibliotecaEmail("email"))
                .thenReturn(list);
        assertEquals(list, prenotazioneService
                .getTicketsByBiblioteca(new Biblioteca()));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di ottenere un ticket dato il suo ID.
     */
    @Test
    public void getTicketByID() {
        TicketPrestito ticket = new TicketPrestito();
        when(ticketPrestitoDAO.getOne(1)).thenReturn(ticket);
        assertEquals(ticket, prenotazioneService.getTicketByID(1));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di accettare la richiesta di prestito di un libro.
     */
    @Test
    public void accettaRichiesta() {
        TicketPrestito ticket = new TicketPrestito();
        Biblioteca b = new Biblioteca();
        b.setEmail("a");
        Libro l = new Libro();
        l.setIdLibro(1);
        ticket.setBiblioteca(b);
        ticket.setLibro(l);
        Possesso pos = new Possesso(
                new PossessoId(b.getEmail(), l.getIdLibro()), 1);
        when(possessoDAO.getOne(new PossessoId(b.getEmail(), l.getIdLibro())))
                .thenReturn(pos);
        when(ticketPrestitoDAO.save(ticket)).thenReturn(ticket);
        assertEquals(ticket, prenotazioneService.accettaRichiesta(ticket, 1));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di accettare la richiesta di prestito di un libro.
     */
    @Test
    public void accettaRichiestaNull() {
        TicketPrestito ticket = new TicketPrestito();
        ticket.setBiblioteca(new Biblioteca());
        ticket.setLibro(new Libro());
        when(ticketPrestitoDAO.save(ticket)).thenReturn(ticket);
        when(possessoDAO.getOne(new PossessoId("a", 1)))
                .thenReturn(null);
        assertEquals(ticket, prenotazioneService.accettaRichiesta(ticket, 1));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di rifiutare la richiesta di prestito di un libro.
     */
    @Test
    public void rifiutaRichiesta() {
        TicketPrestito ticket = new TicketPrestito();
        when(ticketPrestitoDAO.save(ticket)).thenReturn(ticket);
        assertEquals(ticket, prenotazioneService.rifiutaRichiesta(ticket));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di chiudere un ticket quando il libro viene
     * restituito.
     */
    @Test
    public void chiudiTicket() {
        TicketPrestito ticket = new TicketPrestito();
        Biblioteca b = new Biblioteca();
        b.setEmail("a");
        Libro l = new Libro();
        l.setIdLibro(1);
        ticket.setBiblioteca(b);
        ticket.setLibro(l);
        Possesso pos = new Possesso(
                new PossessoId(b.getEmail(), l.getIdLibro()), 1);
        when(possessoDAO.getOne(new PossessoId(b.getEmail(), l.getIdLibro())))
                .thenReturn(pos);
        when(ticketPrestitoDAO.save(ticket)).thenReturn(ticket);
        assertEquals(ticket, prenotazioneService.chiudiTicket(ticket));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di chiudere un ticket quando il libro viene
     * restituito.
     */
    @Test
    public void chiudiTicketNull() {
        TicketPrestito ticket = new TicketPrestito();
        ticket.setBiblioteca(new Biblioteca());
        ticket.setLibro(new Libro());
        when(ticketPrestitoDAO.save(ticket)).thenReturn(ticket);
        when(possessoDAO.getOne(new PossessoId("a", 1)))
                .thenReturn(null);
        assertEquals(ticket, prenotazioneService.chiudiTicket(ticket));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di ottenere la lista di ticket aperti da un Lettore.
     * todo boh
     */
    @Test
    public void getTicketsLettore() {
        List<TicketPrestito> list = new ArrayList<>();
        when(ticketPrestitoDAO.findAllByLettoreEmail("a")).thenReturn(list);
        assertEquals(list,
                prenotazioneService.getTicketsByEmailLettore("a"));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di creare un nuovo libro e inserirlo nella lista
     * a partire da un isbn usando una API di google
     * quando l'isbn inserito non viene trovato.
     */
    @Test
    public void inserimentoPerIsbnApiNull() {
        when(bookApiAdapter.getLibroDaBookApi("1234", new Libro())).thenReturn(null);
        assertEquals(null,
                prenotazioneService.inserimentoPerIsbn("a", "a", 1, null));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di creare un nuovo libro e inserirlo nella lista
     * a partire da un isbn usando una API di google.
     */
    @Test
    public void inserimentoPerIsbnGeneriVuotoLibroTrovatoPosseduto() {

        LibroDTO libroDTO = (new LibroDTO(
                1,
                "Fru",
                "9597845613497",
                "1234567891234",
                "2010",
                "Mondadori",
                "Casa",
                "@@@@@",
                null
        ));
        Libro libro = new Libro(libroDTO);
        when(bookApiAdapter.getLibroDaBookApi(libro.getIsbn(), new Libro())).thenReturn(libro);
        libro.setGeneri(new HashSet<>());
        when(genereService.doGeneriExist(new HashSet<>())).thenReturn(true);


        List<Libro> libroList = new ArrayList<>();
        libroList.add(libro);
        when(libroDAO.findAll()).thenReturn(libroList);
        when(libroDAO.save(libro)).thenReturn(libro);

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setEmail("test@test.com");

        PossessoId pid = new PossessoId(biblioteca.getEmail(), libro.getIdLibro());
        Possesso possesso = new Possesso(pid, 1);

        List<Possesso> possessoList = new ArrayList<>();
        possessoList.add(possesso);
        biblioteca.setPossessi(possessoList);

        when(bibliotecaService.findBibliotecaByEmail(biblioteca.getEmail())).thenReturn(biblioteca);

        assertEquals(libro,
                prenotazioneService.inserimentoPerIsbn(
                        libro.getIsbn(), biblioteca.getEmail(), 1, new HashSet<>()));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di creare un nuovo libro e inserirlo nella lista
     * a partire da un isbn usando una API di google.
     */
    @Test
    public void inserimentoPerIsbnGeneriInseriti() {
        LibroDTO libroDTO = (new LibroDTO(
                1,
                "Fru",
                "9597845613497",
                "1234567891234",
                "2010",
                "Mondadori",
                "Casa",
                "@@@@@",
                null
        ));
        Libro libro = new Libro(libroDTO);
        String genere = "test";
        when(genereService.doGeneriExist(new HashSet<>(Collections.singleton("test")))).thenReturn(true);

        when(bookApiAdapter.getLibroDaBookApi(libro.getIsbn(), new Libro())).thenReturn(libro);
        when(libroDAO.findAll()).thenReturn(new ArrayList<>());
        when(libroDAO.save(libro)).thenReturn(libro);

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setEmail("test@test.com");
        biblioteca.setPossessi(new ArrayList<>());
        when(bibliotecaService.findBibliotecaByEmail(biblioteca.getEmail())).thenReturn(biblioteca);

        assertEquals(libro,
                prenotazioneService.inserimentoPerIsbn(
                        libro.getIsbn(), biblioteca.getEmail(), 1, new HashSet<>(Arrays.asList("test"))));
    }

    /**
     * Implementa il test della funzionalità che permette di
     * creare un nuovo libro prendendolo dal database
     */
    @Test
    public void inserimentoDatabase(){

        Libro libro = new Libro(new LibroDTO(
                1,
                "Fru",
                "9597845613497",
                "1234567891234",
                "2010",
                "Mondadori",
                "Casa",
                "@@@@@",
                null
        ));
        Biblioteca biblioteca= new Biblioteca();
        biblioteca.setEmail("test");

        PossessoId pid= new PossessoId("test",1);
        Possesso p= new Possesso();
        p.setPossessoID(pid);
        p.setNumeroCopie(1);
        biblioteca.setPossessi(Arrays.asList(p));

        when(libroDAO.getOne(1)).thenReturn(libro);
        when(bibliotecaService.findBibliotecaByEmail("test")).thenReturn(biblioteca);
        assertEquals(libro, prenotazioneService.inserimentoDalDatabase(1,"test",1));

    }

    /**
     * Implementa il test della funzionalità che permette di
     * creare un nuovo libro prendendolo dal database
     */
    @Test
    public void inserimentoDatabaseLibriDiversi(){
        Libro libro = new Libro(new LibroDTO(
                1,
                "Fru",
                "9597845613497",
                "1234567891234",
                "2010",
                "Mondadori",
                "Casa",
                "@@@@@",
                null
        ));

        Biblioteca biblioteca= new Biblioteca();
        biblioteca.setEmail("test");

        PossessoId pid= new PossessoId("test",2);
        Possesso p= new Possesso();
        p.setPossessoID(pid);
        p.setNumeroCopie(1);
        ArrayList<Possesso> possessi= new ArrayList<>();
        possessi.add(p);
        biblioteca.setPossessi(possessi);

        when(libroDAO.getOne(1)).thenReturn(libro);
        when(bibliotecaService.findBibliotecaByEmail("test")).thenReturn(biblioteca);
        assertEquals(libro, prenotazioneService.inserimentoDalDatabase(1,"test",1));

    }

    /**
     * Implementa il test della funzionalità che permette di
     * creare un nuovo libro prendendolo dal database
     */
    @Test
    public void inserimentoDatabaseNoPossessi(){
        Libro libro = new Libro(new LibroDTO(
                1,
                "Fru",
                "9597845613497",
                "1234567891234",
                "2010",
                "Mondadori",
                "Casa",
                "@@@@@",
                null
        ));
        Biblioteca biblioteca= new Biblioteca();
        biblioteca.setEmail("test");
        biblioteca.setPossessi(new ArrayList<>());

        when(libroDAO.getOne(1)).thenReturn(libro);
        when(bibliotecaService.findBibliotecaByEmail("test")).thenReturn(biblioteca);
        assertEquals(libro, prenotazioneService.inserimentoDalDatabase(1,"test",1));

    }


    @Test
    public void findByTitoloContainsNull() {
        when(prenotazioneService.findByTitoloContains("test")).thenReturn(null);
        assertEquals(new ArrayList<>(),prenotazioneService.findByTitoloContains("test"));
    }

    private static Stream<Arguments> provideLibro() {
        return Stream.of(
                Arguments.of(
                        new Libro(new LibroDTO(
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
                ))
        );
    }
}