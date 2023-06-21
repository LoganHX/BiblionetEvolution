package it.unisa.c07.biblionet.events;

import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import lombok.Getter;

@Getter
public class MiddleEsperto {
    EspertoDTO espertoDTO;
    String emailBiblioteca;

    public MiddleEsperto(EspertoDTO espertoDTO, String emailBiblioteca){
        this.espertoDTO = espertoDTO;
        this.emailBiblioteca = emailBiblioteca;
    }
}
