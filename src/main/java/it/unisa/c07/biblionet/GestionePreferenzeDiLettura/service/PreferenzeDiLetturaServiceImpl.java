package it.unisa.c07.biblionet.GestionePreferenzeDiLettura.service;

import it.unisa.c07.biblionet.GestioneGenere.GenereDTO;
import it.unisa.c07.biblionet.GestionePreferenzeDiLettura.PreferenzeDiLetturaService;
import it.unisa.c07.biblionet.GestioneUtenti.repository.EspertoDAO;
import it.unisa.c07.biblionet.GestioneUtenti.repository.LettoreDAO;
import it.unisa.c07.biblionet.entity.Esperto;
import it.unisa.c07.biblionet.entity.Lettore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Service
@RequiredArgsConstructor
public class PreferenzeDiLetturaServiceImpl implements
        PreferenzeDiLetturaService {



    /**
     * Si occupa delle funzioni CRUD per l'esperto.
     */
    private final EspertoDAO espertoDAO;

    /**
     * Si occupa delle funzioni CRUD per l'utente.
     */
    private final LettoreDAO lettoreDAO;

    /**
     * Implementa la funzionalità di restituire tutti i generi
     * presenti nel database.
     * @return la lista di tutti i generi presenti nel database
     */



    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * ad un esperto.
     * @param generi i generi da inserire
     * @param esperto l'esperto a cui inserirli
     */
    @Override
    public void addGeneriEsperto(final Set<GenereDTO> generi,
                                 final Esperto esperto) {
        Set<String> setGeneri = new HashSet<>();
        for(GenereDTO genere: generi){
            setGeneri.add(genere.getNome());
        }
        esperto.setNomeGeneri(setGeneri);
        espertoDAO.save(esperto);
    }

    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * a un lettore.
     * @param generi i generi da inserire
     * @param lettore il lettore a cui inserirli
     */
    @Override
    public void addGeneriLettore(final Set<GenereDTO> generi,
                                 final Lettore lettore) {
        Set<String> setGeneri = new HashSet<>();
        for(GenereDTO genere: generi){
            setGeneri.add(genere.getNome());
        }
        lettore.setNomeGeneri(setGeneri);
        lettoreDAO.save(lettore);
    }


}
