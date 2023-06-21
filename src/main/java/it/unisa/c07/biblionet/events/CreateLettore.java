package it.unisa.c07.biblionet.events;

import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import lombok.Getter;

@Getter
public class CreateLettore {
    LettoreDTO lettoreDTO;

    public CreateLettore(LettoreDTO lettoreDTO){
        this.lettoreDTO = lettoreDTO;
    }
}
