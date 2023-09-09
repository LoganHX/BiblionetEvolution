package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Commento;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor(force = true)
public class CommentoDTO {
    private Long id;
    private String emailUtente;
    private Long postId;
    private LocalDateTime date;
    private String content;

    public CommentoDTO(Commento commento) {
        this.id = commento.getId();
        this.emailUtente = commento.getUser().getEmail();
        this.postId = commento.getPost().getId();
        this.date = commento.getDate();
        this.content = commento.getContent();
    }
}
