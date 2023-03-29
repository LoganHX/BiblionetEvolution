package it.unisa.c07.biblionet.dto;

import it.unisa.c07.biblionet.model.entity.ClubDelLibro;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class VisualizzaListClubResponse implements Serializable {

    private List<ClubDelLibro> listaClubs;
    private Set<String> generi;
    private Set<String> citta;
}
