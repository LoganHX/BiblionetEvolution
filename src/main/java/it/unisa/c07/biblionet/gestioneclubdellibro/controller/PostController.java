package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final EspertoService espertoService;
    private final LettoreService lettoreService;
    private final PostService postService;
    private final ClubDelLibroService clubDelLibroService;
    private final Utils utils;

    @PostMapping(value = "/crea")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse creaPost(final @Valid @ModelAttribute PostDTO postDTO,
                                                    BindingResult bindingResult,
                                                    @RequestHeader(name = "Authorization") final String token,
                                                    final @RequestParam("idClub") int idClub) {


        postDTO.setDate(LocalDateTime.now());
        ClubDelLibro clubDelLibro = clubDelLibroService.getClubByID(idClub);
        if(clubDelLibro == null)
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);

        BiblionetResponse biblionetResponse = checkAutorizzazioneEsperto(token, clubDelLibro, bindingResult);

        if(null != biblionetResponse) return biblionetResponse;

        Esperto esperto = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));
        if(esperto == null)
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);

        postDTO.setEspertoMail(esperto.getEmail());
        postDTO.setUsername(esperto.getUsername());

        Post p = postService.creaPostDaModel(postDTO, clubDelLibro, esperto);
        if(p == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    @PostMapping(value = "/aggiungi-commento")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse aggiungiCommento(final @Valid @ModelAttribute CommentoDTO commentoDTO,
                                      @RequestHeader(name = "Authorization") final String token,
                                      BindingResult bindingResult,
                                              final @RequestParam("idPost") int idPost) {

        Post post = postService.getPostByID(idPost);
        commentoDTO.setDate(LocalDateTime.now());
        if(post == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        ClubDelLibro clubDelLibro = post.getClubDelLibro();

        //todo funzioni separate per esperto e lettore?

        UtenteRegistrato utenteRegistrato = null;
        BiblionetResponse biblionetResponse = new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(utils.isUtenteEsperto(token)) {
            utenteRegistrato = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));
            if(utenteRegistrato == null)
                return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
            biblionetResponse = checkAutorizzazioneEsperto(token, post.getClubDelLibro(), bindingResult);

            Esperto e = (Esperto) utenteRegistrato;
            commentoDTO.setUsername(e.getUsername());
            commentoDTO.setBoolEsperto(true);

        }
        else if (utils.isUtenteLettore(token)) {
            utenteRegistrato = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));
            if(utenteRegistrato == null)
                return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
            biblionetResponse = checkAutorizzazioneLettore(token, post.getClubDelLibro(), bindingResult);
            Lettore l = (Lettore) utenteRegistrato;
            if(!lettoreService.partecipaClub(clubDelLibro, l))
                return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
            commentoDTO.setUsername(l.getUsername());
            commentoDTO.setBoolEsperto(false);

        }
        if(null != biblionetResponse) return biblionetResponse;

        commentoDTO.setEmailUtente(utenteRegistrato.getEmail());

        post = postService.aggiungiCommento(idPost, commentoDTO, utenteRegistrato);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);


    }

    private BiblionetResponse checkAutorizzazioneEsperto(String token, ClubDelLibro clubDelLibro, BindingResult bindingResult){

        if(!utils.isUtenteEsperto(token)) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }
        if(!utils.match(utils.getSubjectFromToken(token),clubDelLibro.getEsperto().getEmail())) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }

        if(bindingResult.hasErrors()){
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }

        return null;
    }
    private BiblionetResponse checkAutorizzazioneLettore(String token, ClubDelLibro clubDelLibro, BindingResult bindingResult){


        if(bindingResult.hasErrors()){
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }

        return null;
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
    public List<PostDTO> visualizzaPost(final @RequestParam("idClub") int idClub) {

      return postService.getInformazioniPost(postService.visualizzaListaPostByClubId(idClub));

    }




}
