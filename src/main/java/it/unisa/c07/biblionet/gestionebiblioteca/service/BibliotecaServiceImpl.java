package it.unisa.c07.biblionet.gestionebiblioteca.service;

import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.BibliotecaDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BibliotecaServiceImpl implements BibliotecaService {

    private final BibliotecaDAO bibliotecaDAO;

    @Override
    public Biblioteca creaBibliotecaDaModel(BibliotecaDTO form) {
        Biblioteca biblioteca = new Biblioteca(form);
        biblioteca.setTipo("Biblioteca");
        return aggiornaBiblioteca(biblioteca);

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
        return bibliotecaDAO.findBibliotecaByEmail(email, "Biblioteca"); //todo usare una costante
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


    /**
     * Implementa la funzionalità di salvataggio delle modifiche
     * all'account biblioteca.
     *
     * @param utente La biblioteca da aggiornare
     * @return la biblioteca aggiornata
     */
    @Override
    public Biblioteca aggiornaBiblioteca(final Biblioteca utente) {
        return bibliotecaDAO.save(utente);
    }


}
