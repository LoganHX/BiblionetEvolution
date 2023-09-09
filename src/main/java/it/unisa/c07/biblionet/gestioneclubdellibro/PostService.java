package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Commento;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Post;

import java.util.List;

public interface PostService {
    List<PostDTO> visualizzaListaPostByClubId(long id);

    Post getPostByID(long id);

    Post creaPostDaModel(PostDTO postDTO, ClubDelLibro clubDelLibro, Esperto esperto);

    Post aggiungiCommento(long id, CommentoDTO commentoDTO, UtenteRegistrato utente);

    List<PostDTO> getInformazioniPost(List<Post> posts);
}
