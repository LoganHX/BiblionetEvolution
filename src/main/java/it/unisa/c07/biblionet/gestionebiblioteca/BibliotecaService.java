package it.unisa.c07.biblionet.gestionebiblioteca;

import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;

import java.util.List;

public interface BibliotecaService {
    Biblioteca creaBibliotecaDaModel(BibliotecaDTO form);

    Biblioteca findBibliotecaByEmailAndPassword(String email, byte[] password);

    Biblioteca findBibliotecaByEmail(String email);

    List<Biblioteca> findBibliotecaByNome(String nomeBiblioteca);

    List<Biblioteca> findBibliotecaByCitta(String citta);

    List<Biblioteca> findAllBiblioteche();

    Biblioteca aggiornaBiblioteca(Biblioteca utente);
}
