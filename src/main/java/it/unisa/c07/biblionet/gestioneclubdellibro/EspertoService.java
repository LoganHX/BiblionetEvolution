package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;

import java.util.List;
import java.util.Set;

public interface EspertoService {


    Esperto creaEspertoDaModel(EspertoDTO form, Biblioteca biblioteca);


    Esperto aggiornaEspertoDaModel(EspertoDTO form, Biblioteca biblioteca);

    Esperto aggiornaEsperto(Esperto utente);

    List<Esperto> findEspertiByGeneri(Set<String> generi);


    List<Esperto> getEspertiByBiblioteca(String email);

    List<String> getEspertiEmailByBiblioteca(String email);

    Esperto findEspertoByEmail(String email);

    List<Esperto> findAllEsperti();

    List<Esperto> findEspertiByNome(String nome);

    UtenteRegistrato findEspertoByEmailAndPassword(String email, byte[] password);

    List<EspertoDTO> getInformazioniEsperti(List<Esperto> espertoList);
}
