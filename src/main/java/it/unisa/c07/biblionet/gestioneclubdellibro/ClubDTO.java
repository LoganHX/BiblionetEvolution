package it.unisa.c07.biblionet.gestioneclubdellibro;

import java.util.Set;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * Classe che rappresenta il form per la creazione di un club del libro.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ClubDTO {

    private int id;

    /**
     * Nome del club.
     */
    @NonNull
    @Size(max = 30)
    private String nome;

    /**
     * Descrizione del club.
     */
    @NonNull
    @Size(max = 255)
    private String descrizione;

    /**
     * Lista di generi del club.
     */
    @NonNull
    private Set<String> generi;

    /**
     * Copertina del club.
     */
    private String copertina;

    public ClubDTO(ClubDelLibro clubDelLibro){
        this.id = clubDelLibro.getIdClub();
        this.nome = clubDelLibro.getNome();
        this.descrizione = clubDelLibro.getDescrizione();
        this.generi = clubDelLibro.getGeneri();
        this.copertina = clubDelLibro.getImmagineCopertina();
    }
}