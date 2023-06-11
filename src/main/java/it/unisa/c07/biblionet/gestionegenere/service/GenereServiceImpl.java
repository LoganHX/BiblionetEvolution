package it.unisa.c07.biblionet.gestionegenere.service;

import it.unisa.c07.biblionet.gestionegenere.GenereService;
import it.unisa.c07.biblionet.gestionegenere.repository.GenereDAO;
import it.unisa.c07.biblionet.entity.Genere;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Set<Genere> getGeneriByName(final String[] generi) {
        Set<Genere> toReturn = new HashSet<>();

        for (String g: generi) {
            Genere gen = genereDAO.findByName(g);
            Genere dto = new Genere();
            dto.setDescrizione(gen.getDescrizione());
            dto.setNome(gen.getNome());
            toReturn.add(dto);

        }

        return toReturn;
    }

    @Override
    public Genere getGenereByName(String nome) {
       return genereDAO.findByName(nome);
    }


    @Override
    public Set<Genere> getAllGeneri() {
        //todo si può fare di meglio
        List<Genere> list = genereDAO.findAll();
        Set<Genere> setGeneri =new HashSet<>();
        for(Genere genere: list){
            Genere genereDTO = new Genere();
            genereDTO.setNome(genere.getNome());
            genereDTO.setDescrizione(genere.getDescrizione());
        }
        return setGeneri;
    }
}
