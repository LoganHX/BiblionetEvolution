package it.unisa.c07.biblionet.GestioneGenere.service;

import it.unisa.c07.biblionet.GestioneGenere.GenereDTO;
import it.unisa.c07.biblionet.GestioneGenere.GenereService;
import it.unisa.c07.biblionet.GestioneGenere.repository.GenereDAO;
import it.unisa.c07.biblionet.GestioneGenere.repository.Genere;
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
    public Set<GenereDTO> getGeneriByName(final String[] generi) {
        Set<GenereDTO> toReturn = new HashSet<>();

        for (String g: generi) {
            Genere gen = genereDAO.findByName(g);
            GenereDTO dto = new GenereDTO();
            dto.setDescrizione(gen.getDescrizione());
            dto.setNome(gen.getNome());
            toReturn.add(dto);

        }

        return toReturn;
    }



    @Override
    public Set<GenereDTO> getAllGeneri() {
        //todo si può fare di meglio
        List<Genere> list = genereDAO.findAll();
        Set<GenereDTO> setGeneri =new HashSet<>();
        for(Genere genere: list){
            GenereDTO genereDTO = new GenereDTO();
            genereDTO.setNome(genere.getNome());
            genereDTO.setDescrizione(genere.getDescrizione());
        }
        return setGeneri;
    }
}
