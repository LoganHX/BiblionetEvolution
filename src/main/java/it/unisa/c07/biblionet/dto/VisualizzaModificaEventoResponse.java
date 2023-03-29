package it.unisa.c07.biblionet.dto;

import it.unisa.c07.biblionet.model.entity.ClubDelLibro;
import it.unisa.c07.biblionet.model.form.EventoForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisualizzaModificaEventoResponse {
    EventoForm evento;
    ClubDelLibro club;
    Integer id;
}
