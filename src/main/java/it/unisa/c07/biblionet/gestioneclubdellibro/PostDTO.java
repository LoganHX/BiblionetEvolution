package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Post;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class PostDTO {

    private int id;
    private String espertoMail;
    private LocalDateTime date;
    private String titolo;
    private String content;
    private String username;
    //private List<CommentoDTO> commenti;


    public PostDTO(Post post) {
        this.titolo = post.getTitolo();
        this.id = post.getId();
        this.espertoMail = post.getEsperto().getEmail();
        this.date = post.getDate();
        this.content = post.getContent();
        this.username = post.getUsername();
        //this.commenti = new ArrayList<>();
    }

  /*  public PostDTO(Post post, List<CommentoDTO> commenti) {
        this.titolo = post.getTitolo();
        this.id = post.getId();
        this.espertoMail = post.getEsperto().getEmail();
        this.date = post.getDate();
        this.content = post.getContent();
        //this.commenti = commenti;
    }*/
}
