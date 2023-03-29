package it.unisa.c07.biblionet.dto;

import it.unisa.c07.biblionet.model.form.ClubForm;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class VisualizzaModificaDatiClub {

    ClubForm club;
    Integer id;
    Set<String> generi;


}
