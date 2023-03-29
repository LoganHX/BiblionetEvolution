package it.unisa.c07.biblionet.dto;

import it.unisa.c07.biblionet.model.form.ClubForm;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class VisualizzaCreaClubLibroResponse implements Serializable {
    private Set<String> generi;
    private ClubForm club;

}
