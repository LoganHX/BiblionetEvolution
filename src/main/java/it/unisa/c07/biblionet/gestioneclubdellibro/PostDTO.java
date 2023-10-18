package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Post;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

public class PostDTO {

    private int id;
    private String espertoMail;
    private LocalDateTime date;

    @NonNull
    @NotNull
    @Size(min= BiblionetConstraints.LUNGHEZZA_MINIMA_DESCRIZIONE, max = BiblionetConstraints.LUNGHEZZA_MASSIMA_NOME)
    private String titolo;
    @NonNull
    @NotNull
    @Size(min= BiblionetConstraints.LUNGHEZZA_MINIMA_DESCRIZIONE, max = BiblionetConstraints.LUNGHEZZA_MASSIMA_DESCRIZIONE)
    private String content;
    private String username;

    public PostDTO(Post post) {
        this.titolo = post.getTitolo();
        this.id = post.getId();
        this.espertoMail = post.getEsperto().getEmail();
        this.date = post.getDate();
        this.content = post.getContent();
        this.username = post.getUsername();
    }

}
