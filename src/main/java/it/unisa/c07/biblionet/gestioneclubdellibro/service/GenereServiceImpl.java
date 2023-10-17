package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.GenereService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.GenereDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenereServiceImpl implements GenereService {
    /**
     * Si occupa delle funzioni CRUD per il genere.
     */
    private final GenereDAO genereDAO;


    /**
     * Implementa la funzionalità di restituire tutti i generi
     * data una lista di nomi di generi.
     * @param generi i generi da trovare
     * @return la lista di generi contenente solamente i generi effettivamente
     * presenti nel database
     */
    @Override
    public Set<Genere> getGeneriByName(final List<String> generi) {
        Set<Genere> toReturn = new HashSet<>();

        for (String g: generi) {
            Genere gen = genereDAO.findByName(g);
            if(gen == null) return null;
            toReturn.add(gen);
        }

        return toReturn;
    }
    @Override
    public boolean doGeneriExist(Set<String> generi){
        if(generi.isEmpty()) return true;
        List<String> lista = new ArrayList<>(generi);
        for(String genere: lista){
            if(genereDAO.findByName(genere) == null) return false;
        }
        return true;
    }

    @Override
    public Genere getGenereByName(String nome) {
       return genereDAO.findByName(nome);
    }


    @Override
    public Set<Genere> getAllGeneri() {
        Set<Genere> genereSet = new HashSet<>(genereDAO.findAll());
        return genereSet;
    }
}
