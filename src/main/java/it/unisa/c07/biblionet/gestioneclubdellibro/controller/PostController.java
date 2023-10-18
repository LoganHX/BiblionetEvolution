package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Post;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final EspertoService espertoService;
    private final LettoreService lettoreService;
    private final PostService postService;
    private final ClubDelLibroService clubService;
    private final Utils utils;

    @PostMapping(value = "/crea")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse creaPost(final @Valid @ModelAttribute PostDTO postDTO,
                                      BindingResult bindingResult,
                                      @RequestHeader(name = "Authorization") final String token,
                                      final @RequestParam("idClub") int idClub) {


        postDTO.setDate(LocalDateTime.now());
        ClubDelLibro clubDelLibro = clubService.getClubByID(idClub);
        if (clubDelLibro == null)
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);

        if (bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        if (!utils.isUtenteEsperto(token)) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }
        if (!utils.match(utils.getSubjectFromToken(token), clubDelLibro.getEsperto().getEmail())) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }

        Esperto esperto = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));
        if (esperto == null)
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);

        postDTO.setEspertoMail(esperto.getEmail());
        postDTO.setUsername(esperto.getUsername());

        Post p = postService.creaPostDaModel(postDTO, clubDelLibro, esperto);
        if (p == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    @PostMapping(value = "/aggiungi-commento")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse aggiungiCommento(final @Valid @ModelAttribute CommentoDTO commentoDTO,
                                              BindingResult bindingResult,
                                              @RequestHeader(name = "Authorization") final String token
    ) {

        System.err.println(bindingResult);
        if (bindingResult.hasErrors()) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }

        Post post = postService.getPostByID(commentoDTO.getIdPost());
        if (post == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        commentoDTO.setDate(LocalDateTime.now());

        if (utils.isUtenteEsperto(token)) {
            return aggiungiCommentoByEsperto(commentoDTO, token, post);
        } else if (utils.isUtenteLettore(token)) {
            return aggiungiCommentoByLettore(commentoDTO, token, post);
        } else return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);


    }

    private BiblionetResponse aggiungiCommentoByEsperto(CommentoDTO commentoDTO, String token, Post post) {
        Esperto esperto = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));
        if (esperto == null)
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);

        if (!utils.match(utils.getSubjectFromToken(token), post.getClubDelLibro().getEsperto().getEmail())) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }
        commentoDTO.setUsername(esperto.getUsername());
        commentoDTO.setEmailUtente(esperto.getEmail());
        commentoDTO.setBoolEsperto(true);

        postService.aggiungiCommento(post.getId(), commentoDTO, esperto);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    private BiblionetResponse aggiungiCommentoByLettore(CommentoDTO commentoDTO, String token, Post post) {
        Lettore lettore = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));

        if (lettore == null)
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);

        if (!clubService.isLettoreIscrittoAClub(lettore.getEmail(), post.getClubDelLibro().getIdClub())) {
            System.err.println(post.getClubDelLibro().getIdClub());
            System.err.println(lettore.getEmail());
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }
        commentoDTO.setUsername(lettore.getUsername());
        commentoDTO.setEmailUtente(lettore.getEmail());
        commentoDTO.setBoolEsperto(false);

        postService.aggiungiCommento(post.getId(), commentoDTO, lettore);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);

    }


    @PostMapping(value = "/visualizza-commenti")
    @ResponseBody
    @CrossOrigin
    public List<CommentoDTO> visualizzaCommentiPost(final @RequestParam("idPost") int idPost) {

        return postService.getInformazioniCommenti(postService.getCommentiByPostId(idPost));
    }


    @PostMapping(value = "/visualizza-post")
    @ResponseBody
    @CrossOrigin
    public PostDTO visualizzaPost(final @RequestParam("idClub") int idClub, final @RequestParam("idPost") int idPost) {

        Post post = postService.getPostByID(idPost);
        if(post.getClubDelLibro().getIdClub() != idClub) return null;
        return new PostDTO(post);

    }


}
