package it.unisa.c07.biblionet.gestionebiblioteca.service;

import it.unisa.c07.biblionet.common.*;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.TicketPrestitoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.bookapiadapter.BookApiAdapter;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.GenereService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia service per il
 * sottosustema PrenotazioneLibri.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@Service
@RequiredArgsConstructor
public class PrenotazioneLibriServiceImpl implements PrenotazioneLibriService {
    /**
     * Si occupa delle operazioni CRUD per libro.
     */
    private final LibroDAO libroDAO;
    private final BookApiAdapter bookApiAdapter;
    private final PossessoDAO possessoDAO;
    private final TicketPrestitoDAO ticketPrestitoDAO;
    private final BibliotecaService bibliotecaService;
    private final LettoreService lettoreService;
    private final GenereService genereService;

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili sulla piattaforma.
     *
     * @return La lista di libri
     */
    @Override
    public List<Libro> visualizzaListaLibriCompleta() {
        return libroDAO.findAll(Sort.by("titolo"));
    }

    /**
     * Implementa la funzionalità che permette
     * di visualizzare una lista di libri prenotabili
     * filtrata per titolo.
     *
     * @param titolo Stringa che deve essere contenuta
     *               nel titolo
     * @return La lista di libri
     */
    @Override
    public List<Libro> visualizzaListaLibriPerTitolo(final String titolo) {
        return libroDAO.findByTitoloLike(titolo);
    }


    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili da una determinata biblioteca.
     *
     * @param email la mail della biblioteca
     * @return La lista di libri
     * <p>
     * todo completamente rifatta
     */
    @Override
    public List<Libro> visualizzaListaLibriPerBiblioteca(
            final String email) {
        List<Libro> libri = new ArrayList<>();

        Biblioteca biblioteca = bibliotecaService.findBibliotecaByEmail(email);
        List<Possesso> possessi = biblioteca.getPossessi();
        for (Possesso p : possessi) {
            Optional<Libro> l =
                    libroDAO.findById(p.getPossessoID().getLibroID());
            if (!libri.contains(l.orElse(null))) {
                libri.add(l.orElse(null));
            }
        }
        return libri;
    }

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili di un dato genere.
     *
     * @param genere Il nome del genere
     * @return La lista di libri
     */
    @Override
    public List<Libro> visualizzaListaLibriPerGenere(
            final String genere) {
/*
        List<LibroBiblioteca> libri = LibroBibliotecaDAO.findAll();
        List<LibroBiblioteca> list = new ArrayList<>();
        Genere g = genereDAO.findByName(genere);
        for (LibroBiblioteca l : libri) {
            if (l.getGeneri().contains(g)) {
                list.add(l);
            }
        }
        return list;
    */
        return new ArrayList<>();
    }


    /**
     * Implementa la funzionalità che permette
     * di richiedere un prestito per un libro
     * da una biblioteca.
     *
     * @param lettoreMail      l'id del lettore che lo richiede
     * @param idBiblioteca id della biblioteca
     * @param idLibro      id del libro
     * @return Il ticket aperto in attesa di approvazione
     */
    @Override
    public TicketPrestito richiediPrestito(final String lettoreMail,
                                           final String idBiblioteca,
                                           final int idLibro) {

        //todo violazione architettura

        UtenteRegistrato lettore = lettoreService.findLettoreByEmail(lettoreMail);
        TicketPrestito ticket = new TicketPrestito();
        if(!lettore.getTipo().equals("Lettore")) return null;
        ticket.setLettore(lettore);
        ticket.setDataRichiesta(LocalDateTime.now());
        ticket.setStato(TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA);

        Biblioteca biblioteca =
                bibliotecaService.findBibliotecaByEmail(idBiblioteca);
        Libro libro = libroDAO.getOne(idLibro);

        ticket.setBiblioteca(biblioteca);
        ticket.setLibro(libro);

        ticketPrestitoDAO.save(ticket);
        return ticket;
    }

    /**
     * Implementa la funzionalità che permette
     * di ottenere la lista delle biblioteche
     * che posseggono un dato libro.
     *
     * @param libro Il libro di cui estrarre le biblioteche
     * @return La lista delle biblioteche che possiedono il libro
     */
    @Override
    public List<Biblioteca> getBibliotecheLibro(final Libro libro) {
        List<Biblioteca> lista = new ArrayList<>();
        //todo da testare
        for (Possesso p : possessoDAO.findByLibroID(libro.getIdLibro())) {
            lista.add(bibliotecaService.findBibliotecaByEmail(p.getPossessoID().getBibliotecaID()));
        }
        return lista;
    }


    /**
     * Implementa la funzionalità che permette
     * di ottenere un libro dato il suo ID.
     *
     * @param id L'ID del libro da ottenere
     * @return Il libro da ottenere
     */
    @Override
    public Libro getLibroByID(final int id) {
        return libroDAO.getOne(id);
    }

    /**
     * Implementa la funzionalità che permette
     * di ottenere una lista di richieste per una biblioteca.
     *
     * @param biblioteca la biblioteca di cui vedere le richieste
     * @return La lista di richieste
     */
    @Override
    public List<TicketPrestito> getTicketsByBiblioteca(
            final Biblioteca biblioteca) {
        return ticketPrestitoDAO.
                findAllByBibliotecaEmail(biblioteca.getEmail());
    }

    @Override
    public List<TicketPrestito> getTicketsByBibliotecaAndStato(
            final Biblioteca biblioteca, final TicketPrestito.Stati stato) {
        return ticketPrestitoDAO.
                findAllByBibliotecaEmailAndStato(biblioteca.getEmail(), stato);
    }

    /**
     * Implementa la funzionalità che permette
     * di ottenere un ticket dato il suo ID.
     *
     * @param id L'ID del ticket da recuperare
     * @return Il ticket ottenuto
     */
    @Override
    public TicketPrestito getTicketByID(final int id) {
        return ticketPrestitoDAO.getOne(id);
    }

    /**
     * Implementa la funzionalità che permette
     * di accettare la richiesta di prestito di un libro.
     *
     * @param ticket il ticket che rappresenta la richiesta
     * @param giorni il tempo di concessione del libro
     * @return Il ticket aggiornato
     */
    @Override
    public TicketPrestito accettaRichiesta(final TicketPrestito ticket,
                                           final int giorni) {
        ticket.setDataRestituzione(LocalDateTime.now().plusDays(giorni));
        ticket.setStato(TicketPrestito.Stati.IN_ATTESA_DI_RESTITUZIONE);
        Libro l = ticket.getLibro();
        Biblioteca b = ticket.getBiblioteca();
        Possesso pos = possessoDAO.
                getOne(new PossessoId(b.getEmail(), l.getIdLibro()));
        if (pos != null) {
            pos.setNumeroCopie(pos.getNumeroCopie() - 1);
            possessoDAO.save(pos);
        }
        ticketPrestitoDAO.save(ticket);
        return ticket;
    }

    /**
     * Implementa la funzionalità che permette
     * di rifiutare la richiesta di prestito di un libro.
     *
     * @param ticket il ticket che rappresenta la richiesta
     * @return Il ticket aggiornato
     */
    @Override
    public TicketPrestito rifiutaRichiesta(final TicketPrestito ticket) {
        ticket.setStato(TicketPrestito.Stati.RIFIUTATO);
        ticketPrestitoDAO.save(ticket);
        return ticket;
    }

    /**
     * Implementa la funzionalità che permette
     * di chiudere un ticket di prenotazione di un libro
     * quando questo viene riconsegnato.
     *
     * @param ticket il ticket che rappresenta la richiesta da chiudere
     * @return Il ticket aggiornato è stato chiuso
     */
    @Override
    public TicketPrestito chiudiTicket(final TicketPrestito ticket) {
        ticket.setStato(TicketPrestito.Stati.CHIUSO);
        Libro l = ticket.getLibro();
        Biblioteca b = ticket.getBiblioteca();
        Possesso pos = possessoDAO.
                getOne(new PossessoId(b.getEmail(), l.getIdLibro()));
        if (pos != null) {
            pos.setNumeroCopie(pos.getNumeroCopie() + 1);
            possessoDAO.save(pos);
        }
        return ticketPrestitoDAO.save(ticket);
    }

    /**
     * Implementa la funzionalità che permette
     * di ottenere la lista di ticket aperti da un Lettore.
     *
     * @param emailLettore l'id del lettore di cui recuperare i ticket
     * @return la lista dei ticket
     */
    @Override
    public List<TicketPrestito> getTicketsByEmailLettore(String emailLettore) {
        return ticketPrestitoDAO.findAllByLettoreEmail(emailLettore);
    }

    @Override
    public List<TicketPrestitoDTO> getInformazioniTickets(List<TicketPrestito> tickets) {
        List<TicketPrestitoDTO> ticketsDTO = new ArrayList<>();

        for(TicketPrestito t: tickets){
            ticketsDTO.add(new TicketPrestitoDTO(t));
        }
        return ticketsDTO;
    }


    /**
     * Implementa la funzionalità che permette di
     * ottenere una lista di id e titoli di libri
     * sulla base di un titolo dato
     * ! Controllare prima di consegnare
     *
     * @param titolo il titolo che deve matchcare
     * @return la lista di informazioni
     */
    public List<ILibroIdAndName> findByTitoloContains(final String titolo) {
        List<ILibroIdAndName> infoLibroList =
                libroDAO.findByTitoloContains(titolo);

        if (infoLibroList == null) {
            infoLibroList = new ArrayList<>();
        } else if (infoLibroList.size() > 10) {
            infoLibroList = infoLibroList.subList(0, 9);
        }
        return infoLibroList;
    }

    /**
     * Implementa la funzionalità che permette
     * di creare un nuovo libro e inserirlo nella lista
     * a partire da un isbn usando una API di google.
     *
     * @param isbn         il Lettore di cui recuperare i ticket
     * @param idBiblioteca l'id della biblioteca che lo possiede
     * @param numCopie     il numero di copie possedute
     * @param generi       la lista dei generi
     * @return il libro creato
     */
    public Libro inserimentoPerIsbn(final String isbn,
                                              final String idBiblioteca,
                                              final int numCopie,
                                              final Set<String> generi) {

        if(!genereService.doGeneriExist(generi)) return null;

        //Recupero l'oggetto Libro da Api per isbn
        Libro libro = bookApiAdapter.getLibroDaBookApi(isbn, new Libro());
        if (libro == null) {
            return null;
        }
        libro.setGeneri(new HashSet<>(generi));

        Libro libroEsistente = libroDAO.findByIsbn(libro.getIsbn());

        if(libroEsistente == null){
            libroEsistente = libroDAO.save(libro);
        }

        Biblioteca b = bibliotecaService.findBibliotecaByEmail(idBiblioteca);

//        //Se per errore avesse inserito un libro che possiede già,
//        //aggiorno semplicemente il numero di copie che ha. todo ok ma andrebbe richiamata un'apposita funzione non ricopiare il tutto qui
//        for (Possesso p : b.getPossessi()) {
//            if (p.getPossessoID().getLibroID() == libro.getIdLibro()) {
//                p.setNumeroCopie(p.getNumeroCopie() + numCopie);
//                possessoDAO.save(p);
//                bibliotecaService.aggiornaBiblioteca(b);
//                return libro;
//            }
//        }
//
//        //Creo il possesso relativo al libro e alla biblioteca
//        //che lo inserisce e lo memorizzo
//        PossessoId pid = new PossessoId(idBiblioteca, libro.getIdLibro());
//        Possesso possesso = new Possesso(pid, numCopie);
//        possessoDAO.save(possesso);
//        List<Possesso> plist = b.getPossessi();
//        plist.add(possesso);
//        b.setPossessi(plist);

        //Update della biblioteca con il nuovo possesso
       // bibliotecaService.aggiornaBiblioteca(b);

        return gestisciPossessi(libroEsistente, b, numCopie);
    }

    /**
     * Implementa la funzionalità che permette
     * d'inserire un libro già memorizzato negli
     * archivi della piattaforma alla lista dei propri
     * libri prenotabili.
     *
     * @param idLibro      il Libro da inserire
     * @param idBiblioteca l'id della biblioteca che lo possiede
     * @param numCopie     il numero di copie possedute
     * @return il libro inserito
     */
    public Libro inserimentoDalDatabase(final int idLibro,
                                                  final String idBiblioteca,
                                                  final int numCopie) {
        Libro l = libroDAO.getOne(idLibro);
        Biblioteca b = bibliotecaService.findBibliotecaByEmail(idBiblioteca);
//        //Se per errore avesse inserito un libro che possiede già,
//        //aggiorno semplicemente il numero di copie che ha.
//        for (Possesso p : b.getPossessi()) {
//            if (p.getPossessoID().getLibroID() == idLibro) {
//                p.setNumeroCopie(p.getNumeroCopie() + numCopie);
//                possessoDAO.save(p);
//                bibliotecaService.aggiornaBiblioteca(b);
//                return l;
//            }
//        }
//
//        //Creo e salvo il nuovo possesso
//        PossessoId pid = new PossessoId(idBiblioteca, idLibro);
//        Possesso p = new Possesso(pid, numCopie);
//        possessoDAO.save(p);
//        List<Possesso> plist = b.getPossessi();
//        plist.add(p);
//        b.setPossessi(plist);
//
//        //Update della biblioteca con il nuovo possesso
//        bibliotecaService.aggiornaBiblioteca(b);

        return gestisciPossessi(l, b, numCopie);
    }

    /**
     * Implementa la funzionalità che permette
     * d'inserire un libro attraverso un form.
     *
     * @param libroDTO        il dto del Libro da memorizzare
     * @param idBiblioteca l'id della biblioteca che lo possiede
     * @param numCopie     il numero di copie possedute
     * @param generi       la lista dei generi del libro
     * @return il libro inserito
     */
    public Libro creaLibroDaModel(final LibroDTO libroDTO,
                                              final String idBiblioteca,
                                              final int numCopie,
                                              final Set<String> generi) {


        Biblioteca b = bibliotecaService.findBibliotecaByEmail(idBiblioteca);
        if(!genereService.doGeneriExist(generi)) return null;

        Libro libro = new Libro(libroDTO);

        Libro libroEsistente = libroDAO.findByIsbn(libroDTO.getIsbn());

        if(libroEsistente == null){
            libroEsistente = libroDAO.save(libro);
        }


        return gestisciPossessi(libroEsistente, b, numCopie);
    }

    @Override
    public List<LibroDTO> getInformazioniLibri(List<Libro> libroList) {
        List<LibroDTO> libroDTOS = new ArrayList<>();
        for(Libro l: libroList){
            libroDTOS.add(new LibroDTO(l));
        }
        return libroDTOS;
    }

    private Libro gestisciPossessi(Libro libroEsistente, Biblioteca b, int numCopie){
        //controllo se la biblioteca possiede già questo libro
        Possesso p = possessoDAO.findByBibliotecaIDAndLibroID(b.getNomeBiblioteca(), libroEsistente.getIdLibro());

        if(p != null){
            p.setNumeroCopie(p.getNumeroCopie() + numCopie);
            possessoDAO.save(p);
            bibliotecaService.aggiornaBiblioteca(b);
            return libroEsistente;
        }


        //Creo e salvo il nuovo possesso, nel caso in cui la biblioteca non possieda già il libro
        PossessoId pid = new PossessoId(b.getEmail(), libroEsistente.getIdLibro());
        p = new Possesso(pid, numCopie);
        possessoDAO.save(p);
        List<Possesso> plist = b.getPossessi();
        plist.add(p);
        b.setPossessi(plist);

        //Update della biblioteca con il nuovo possesso
        bibliotecaService.aggiornaBiblioteca(b);
        return libroEsistente;
    }



}

