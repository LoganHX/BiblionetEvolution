package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.CommentoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.PostDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.PostService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostDAO postDAO;
    private final CommentoDAO commentoDAO;



    @Override
    public List<Post> visualizzaListaPostByClubId(int id){
        return postDAO.findPostByClubDelLibro_IdClub(id);
    }

    @Override
    public Post getPostByID(int id){
        Optional<Post> p = postDAO.findById(id);
        return p.orElse(null);
    }
    @Override
    public Post creaPostDaModel(PostDTO postDTO, ClubDelLibro clubDelLibro, Esperto esperto){
        Post p = new Post(postDTO, clubDelLibro, esperto);
        return postDAO.save(p);
    }

    @Override
    public Post aggiungiCommento(int id, CommentoDTO commentoDTO, UtenteRegistrato utente){
        if(utente == null) return  null;
        Optional<Post> optionalPost = postDAO.findById(id);
        if(optionalPost.isEmpty()) return null;
        Post p = optionalPost.get();

        Commento c = new Commento(commentoDTO, utente, p);

        p.addCommento(c);
        commentoDAO.save(c);
        //p = postDAO.save(p);

        return p;
    }
    @Override
    public List<Commento> getCommentiByPostId(int idPost){
        return commentoDAO.findAllCommentiByPostId(idPost);
    }

    @Override
    public List<PostDTO> getInformazioniPost(List<Post> posts){
        List<PostDTO> dtos = new ArrayList<>();
        for(Post p: posts){
            dtos.add(new PostDTO(p));
        }
        return dtos;
    }
    @Override
    public List<CommentoDTO> getInformazioniCommenti(List<Commento> commenti){
        List<CommentoDTO> dtos = new ArrayList<>();
        for(Commento c: commenti){
            dtos.add(new CommentoDTO(c));
        }
        return dtos;
    }
}
