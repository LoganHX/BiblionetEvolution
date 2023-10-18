package it.unisa.c07.biblionet.gestionebiblioteca.service;

import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.BibliotecaDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BibliotecaServiceImpl implements BibliotecaService {

    private final BibliotecaDAO bibliotecaDAO;

    @Override
    public Biblioteca creaBibliotecaDaModel(BibliotecaDTO form) {
        Biblioteca biblioteca = new Biblioteca(form);
        biblioteca.setTipo("Biblioteca");
        return salvaBiblioteca(biblioteca);

    }

    @Override
    public Biblioteca aggiornaBibliotecaDaModel(BibliotecaDTO form) {
       return creaBibliotecaDaModel(form);
    }

    @Override
    public Biblioteca findBibliotecaByEmailAndPassword(String email, byte[] password) {
        return bibliotecaDAO.findByEmailAndPassword(email, password);
    }

    /**
     * Implementa la funzionalità di trovare una biblioteca.
     *
     * @param email La mail della biblioteca
     * @return La biblioteca se c'è, altrimenti null
     */
    @Override
    public final Biblioteca findBibliotecaByEmail(final String email) {
        return bibliotecaDAO.findByID(email);
    }

    @Override
    public List<Biblioteca> findBibliotecaByNome(String nomeBiblioteca) {
        return bibliotecaDAO.findByNome(nomeBiblioteca);
    }

    @Override
    public List<Biblioteca> findBibliotecaByCitta(String citta) {
        return bibliotecaDAO.findByCitta(citta);
    }

    @Override
    public List<Biblioteca> findAllBiblioteche() {
        return bibliotecaDAO.findAllBiblioteche();
    }

    @Override
    public List<BibliotecaDTO> getInformazioniBiblioteche(List<Biblioteca> biblioteche) {

        List<BibliotecaDTO> bibliotecheDTO = new ArrayList<>(biblioteche.size());

        return biblioteche.stream()
                .filter(Objects::nonNull)
                .map(BibliotecaDTO::new)
                .collect(Collectors.toCollection(() -> bibliotecheDTO));
    }


    /**
     * Implementa la funzionalità di salvataggio delle modifiche
     * all'account biblioteca.
     *
     * @param utente La biblioteca da aggiornare
     * @return la biblioteca aggiornata
     */
    @Override
    public Biblioteca salvaBiblioteca(final Biblioteca utente) {
        return bibliotecaDAO.save(utente);
    }

    @Override
    public BibliotecaDTO getInformazioniBiblioteca(Biblioteca biblioteca){
        return new BibliotecaDTO(biblioteca);
    }

}
