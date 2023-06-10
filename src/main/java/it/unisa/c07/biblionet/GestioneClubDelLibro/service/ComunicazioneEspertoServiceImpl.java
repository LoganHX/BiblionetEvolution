package it.unisa.c07.biblionet.GestioneClubDelLibro.service;


import it.unisa.c07.biblionet.GestioneClubDelLibro.ComunicazioneEspertoService;
import it.unisa.c07.biblionet.GestioneGenere.repository.GenereDAO;
import it.unisa.c07.biblionet.GestioneUtenti.repository.EspertoDAO;
import it.unisa.c07.biblionet.GestioneGenere.repository.Genere;
import it.unisa.c07.biblionet.entity.Esperto;
import it.unisa.c07.biblionet.entity.UtenteRegistrato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Service
@RequiredArgsConstructor
public class ComunicazioneEspertoServiceImpl
                                    implements ComunicazioneEspertoService {
    /**
     * Si occupa delle funzioni CRUD per l'esperto.
     */
    private final EspertoDAO espertoDAO;

    /**
     * Si occupa delle funzioni CRUD per il genere.
     */
    private final GenereDAO genereDAO;


    @Override
    public final List<Esperto> getEspertiByGeneri(final Set<String> generi) {

        List<Esperto> allEsperti = new ArrayList<>();
        List<Esperto> toReturn = new ArrayList<>();

        for (UtenteRegistrato utente : espertoDAO.findAll()) { //todo sono prese in carico dal token direttamente

            if (utente.getTipo().equals("Esperto")) {
                allEsperti.add((Esperto) utente);
            }
        }
        for (Esperto esperto: allEsperti) {
            for (String genere : esperto.getGeneri()) {
                if (generi.contains(genere) && !toReturn.contains(esperto)) {
                    toReturn.add(esperto);
                }
            }
        }
        return toReturn;
    }

    /**
     * Implementa la funzionalità che restituisce la lista
     * di tutti gli Esperti del DB.
     * @return la lista di esperti
     */
    @Override
    public List<Esperto> getAllEsperti() {
        return espertoDAO.findAllEsperti();
    }

    /**
     * Implementa la funzionalità che restituisce la lista
     * di tutti gli Esperti del DB filtrati per nome.
     * @name il nome con cui filtrare
     * * @return la lista di esperti
     */
    @Override
    public List<Esperto> getEsperiByName(final String name) {
        return espertoDAO.findByNomeLike(name);
    }

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili di un dato genere.
     * @param gen Il nome del genere
     * @return La lista di libri
     */
    @Override
    public List<Esperto> visualizzaEspertiPerGenere(
            final String gen) {
        String genere = gen.substring(0, 1).toUpperCase() + gen.substring(1);
        List<Esperto> list = espertoDAO.findAllEsperti();
        List<Esperto> list2 = new ArrayList<>();
        Genere g = genereDAO.findByName(genere);
        for (Esperto e : list) {
            if (e.getGeneri().contains(g)) {
                list2.add(e);
            }
        }
        return list2;
    }
}
