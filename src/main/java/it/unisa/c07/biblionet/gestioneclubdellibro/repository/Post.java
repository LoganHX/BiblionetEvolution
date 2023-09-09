package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.gestioneclubdellibro.PostDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(force = true)
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Esperto esperto;

    @ManyToOne
    private ClubDelLibro clubDelLibro;

    private LocalDateTime date;
    @NonNull
    private String titolo;
    @NonNull
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commento> commenti = new ArrayList<>();

    // Costruttori, getter e setter, metodi di utilità, ecc.

    public void addCommento(Commento commento) {
        commenti.add(commento);
    }

    public void removeCommento(Commento commento) {
        commenti.remove(commento);
    }

    public Post(PostDTO dto, ClubDelLibro clubDelLibro, Esperto esperto){
        this.titolo= dto.getTitolo();
        this.setEsperto(esperto);
        this.setClubDelLibro(clubDelLibro);
        this.setCommenti(new ArrayList<>());
        this.setDate(dto.getDate());
        this.setContent(dto.getContent());
    }

    public Post(PostDTO dto, ClubDelLibro clubDelLibro, Esperto esperto, List<Commento> commenti){
        this.titolo = dto.getTitolo();
        this.setEsperto(esperto);
        this.setClubDelLibro(clubDelLibro);
        this.setCommenti(commenti);
        this.setDate(dto.getDate());
        this.setContent(dto.getContent());
    }

}
