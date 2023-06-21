package it.unisa.c07.biblionet.events;

import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import lombok.Getter;

@Getter
public class CreateBiblioteca {
    BibliotecaDTO bibliotecaDTO;

    public CreateBiblioteca(BibliotecaDTO bibliotecaDTO){
        this.bibliotecaDTO = bibliotecaDTO;
    }
}
