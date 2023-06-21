package it.unisa.c07.biblionet.events;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import lombok.Getter;

@Getter
public class CreateEsperto {
    EspertoDTO espertoDTO;
    UtenteRegistrato biblioteca;

    public CreateEsperto(EspertoDTO espertoDTO, UtenteRegistrato biblioteca){
        this.espertoDTO = espertoDTO;
        this.biblioteca = biblioteca;
    }
}
