package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.CommentoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor(force = true)
public class Commento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UtenteRegistrato user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime date;
    private String content;
    private String username;
    private boolean boolEsperto;

    public Commento(CommentoDTO dto, UtenteRegistrato utenteRegistrato, Post post){
        this.user = utenteRegistrato;
        this.username = dto.getUsername();
        this.date = dto.getDate();
        this.content = dto.getContent();
        this.post = post;
        this.boolEsperto = dto.isBoolEsperto();

    }
}