package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Commento;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CommentoDTO {
    private Long id;
    private String emailUtente;
    private String username;
    private LocalDateTime date;
    private boolean boolEsperto;

    @NotNull
    private int idPost;

    @NonNull
    @NotNull
    @Size(min= BiblionetConstraints.LUNGHEZZA_MINIMA_DESCRIZIONE, max = BiblionetConstraints.LUNGHEZZA_MASSIMA_DESCRIZIONE)
    private String content;


    public CommentoDTO(Commento commento) {
        this.id = commento.getId();
        this.emailUtente = commento.getUser().getEmail();
        this.idPost = commento.getPost().getId();
        this.date = commento.getDate();
        this.content = commento.getContent();
        this.username = commento.getUsername();
        this.boolEsperto = commento.isBoolEsperto();
    }
}
