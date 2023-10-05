package it.unisa.c07.biblionet.gestioneclubdellibro;

import java.util.Set;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Classe che rappresenta il form per la creazione di un club del libro.
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ClubDTO {

    private int id;

    /**
     * Nome del club.
     */
    @NotNull
    @NonNull
    @Size(min= BiblionetConstraints.LUNGHEZZA_MINIMA_NOME, max = BiblionetConstraints.LUNGHEZZA_MASSIMA_NOME)
    private String nome;

    /**
     * Descrizione del club.
     */
    @NotNull
    @NonNull
    @Size(min=BiblionetConstraints.LUNGHEZZA_MINIMA_DESCRIZIONE, max = BiblionetConstraints.LUNGHEZZA_MASSIMA_DESCRIZIONE)
    private String descrizione;

    /**
     * Lista di generi del club.
     */
    @NotNull
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