package it.unisa.c07.biblionet.events;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import lombok.Getter;

@Getter
public class ConfermaPrenotazioneEvent {
    UtenteRegistrato utenteRegistrato;
    String emailBiblioteca;
    int idLibro;

    public ConfermaPrenotazioneEvent(UtenteRegistrato utenteRegistrato, String emailBiblioteca, String idLibro){
        this.utenteRegistrato = utenteRegistrato;
        this.emailBiblioteca = emailBiblioteca;
        this.idLibro = Integer.parseInt(idLibro);
    }
}
