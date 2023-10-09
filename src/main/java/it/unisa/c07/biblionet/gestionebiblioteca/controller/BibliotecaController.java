package it.unisa.c07.biblionet.gestionebiblioteca.controller;

import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.common.LibroDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import jdk.jfr.Name;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementa il controller per il sottosistema
 * PrenotazioneLibri, in particolare la gestione
 * delle Biblioteche.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/biblioteca")
public class BibliotecaController {

    /**
     * Il service per effettuare le operazioni di
     * persistenza.
     */
    private final BibliotecaService bibliotecaService;
    private final PrenotazioneLibriService prenotazioneService;
    private final Utils utils;



    /**
     * Implementa la funzionalità che permette di
     * visualizzare tutte le biblioteche iscritte.
     * @return La view per visualizzare le biblioteche
     */
    @GetMapping(value = "/visualizza-biblioteche")
    @ResponseBody
    @CrossOrigin
    public List<BibliotecaDTO> visualizzaListaBiblioteche() {
        return bibliotecaService.getInformazioniBiblioteche(bibliotecaService.findAllBiblioteche());
    }

    /**
     * Implementa la funzionalità che permette inserire
     * un libro tramite l'isbn e una Api di Google.
     * @param isbn l'isbn del libro
     * @param generi la lista dei generi del libro
     * @param numCopie il numero di copie possedute
     * @return La view per visualizzare il libro
     */
    @PostMapping(value = "/inserimento-isbn")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse inserisciPerIsbn(@RequestParam final String isbn,
                                              @RequestHeader (name="Authorization") final String token,
                                              @RequestParam (required = false) final String[] generi,
                                              @RequestParam final int numCopie) {

        if(!isbn.matches(BiblionetConstraints.ISBN_REGEX)) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        if (!utils.isUtenteBiblioteca(token)) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }
//        if (isbn == null) {
//            return new BiblionetResponse("L'ISBN inserito non è valido", false);
//        }
        Biblioteca b = bibliotecaService.findBibliotecaByEmail(utils.getSubjectFromToken(token));

        Set<String> gSet = new HashSet<>();
        if(generi != null){
            gSet =  new HashSet<>(Arrays.asList(generi.clone()));
        }

        if(numCopie <= 0) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);

        Libro l = prenotazioneService.inserimentoPerIsbn(
                isbn, b.getEmail(), numCopie, gSet);
        if (l == null) {
            return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        }
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);

    }

    /**
     * Implementa la funzionalità che permette inserire
     * un libro alla lista dei possessi preso
     * dal db.
     *
     * @param idLibro  l'ID del libro
     * @param numCopie il numero di copie possedute
     * @return La view per visualizzare il libro
     */
    @PostMapping(value = "/inserimento-archivio")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse inserisciDaDatabase(@RequestParam final int idLibro,
                                                 @RequestHeader (name="Authorization") final String token,
                                                 @RequestParam final int numCopie) {

        if (!utils.isUtenteBiblioteca(token)) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }

        if(numCopie <= 0) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);

        Biblioteca b =  bibliotecaService.findBibliotecaByEmail(utils.getSubjectFromToken(token));
        Libro l = prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie);
        if(l== null) return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);

    }

    /**
     * Implementa la funzionalità che permette inserire
     * un libro manualmente tramite form.
     *
     * @param libro             Il libro da salvare
     * @param numCopie          il numero di copie possedute
     * @return La view per visualizzare il libro
     */
    @PostMapping(value = "/inserimento-manuale")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse inserisciManualmente(
            @RequestHeader (name="Authorization") final String token,
            @Valid @ModelAttribute final LibroDTO libro,
            BindingResult bindingResult,
            @RequestParam final int numCopie,
            @RequestParam (required = false) MultipartFile copertina) throws IOException {

        if(numCopie <= 0) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);

        if(bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        if (!utils.isUtenteBiblioteca(token)) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }
        Biblioteca b = bibliotecaService.findBibliotecaByEmail(utils.getSubjectFromToken(token));

        if(copertina != null && utils.immagineOk(copertina))
            libro.setImmagineLibro(Utils.getBase64Image(copertina));
        else return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        //System.err.println(libro);

        Libro newLibro = prenotazioneService.creaLibroDaModel(libro, b.getEmail(), numCopie, libro.getGeneri());
        if(newLibro == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);

    }

    /**
     * Implementa la funzionalità che permette di
     * visualizzare le biblioteche filtrate.
     *
     * @param stringa La stringa di ricerca
     * @param filtro  L'informazione su cui filtrare
     * @return La view che visualizza la lista
     */
    @GetMapping(value = "/ricerca")
    @ResponseBody
    @CrossOrigin
    public List<BibliotecaDTO> visualizzaListaFiltrata(
            @RequestParam("stringa") final String stringa,
            @RequestParam("filtro") final String filtro) {

        return switch (filtro) {
            case "nome" -> bibliotecaService.getInformazioniBiblioteche(bibliotecaService.findBibliotecaByNome(stringa));
            case "citta" -> bibliotecaService.getInformazioniBiblioteche(bibliotecaService.findBibliotecaByCitta(stringa));
            default -> bibliotecaService.getInformazioniBiblioteche(bibliotecaService.findAllBiblioteche());
        };
    }

    /**
     * Implementa la funzionalità di visualizzazione
     * del profilo di una singola biblioteca.
     * @param email della biblioteca
     * @return La view di visualizzazione singola biblioteca
     */
    @GetMapping(value = "/{email}")
    @ResponseBody
    @CrossOrigin
    public BibliotecaDTO visualizzaDatiBiblioteca(final @PathVariable String email) {
        return new BibliotecaDTO(bibliotecaService.findBibliotecaByEmail(email));
    }
}
