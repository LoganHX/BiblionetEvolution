package it.unisa.c07.biblionet.gestioneclubdellibro.service;


import it.unisa.c07.biblionet.gestioneclubdellibro.ComunicazioneEspertoService;
import it.unisa.c07.biblionet.gestionegenere.GenereService;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.entity.Genere;
import it.unisa.c07.biblionet.entity.Esperto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Service
@RequiredArgsConstructor
public class ComunicazioneEspertoServiceImpl
                                    implements ComunicazioneEspertoService {

    private final AutenticazioneService autenticazioneService;

    /**
     * Si occupa delle funzioni CRUD per il genere.
     */
    private final GenereService genereService;




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
        List<Esperto> list = autenticazioneService.findAllEsperti();
        List<Esperto> list2 = new ArrayList<>();
        String g = genereService.getGenereByName(genere).getNome();
        for (Esperto e : list) {
            if (e.getGeneri().contains(g)) {
                list2.add(e);
            }
        }
        return list2;
    }
}
