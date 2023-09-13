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
                                                  @RequestHeader(name = "Authorization") final String token,
                                                  BindingResult bindingResult,
                                                  final @RequestParam("idClub") int idClub) {

        //postDTO.setDate(LocalDateTime.now());

        ClubDelLibro clubDelLibro = clubDelLibroService.getClubByID(idClub);
        Esperto esperto = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));

        BiblionetResponse biblionetResponse = new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        if(!utils.isUtenteEsperto(token))
            return biblionetResponse;

        biblionetResponse = checkAutorizzazioneEsperto(token, clubDelLibro, esperto, postDTO.getEspertoMail(), bindingResult);
        if(null != biblionetResponse) return biblionetResponse;

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
        if(post == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        //todo funzioni separate per esperto e lettore?

        UtenteRegistrato utenteRegistrato = null;
        BiblionetResponse biblionetResponse = new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(utils.isUtenteEsperto(token)) {
            utenteRegistrato = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));
            biblionetResponse = checkAutorizzazioneEsperto(token, post.getClubDelLibro(), utenteRegistrato, commentoDTO.getEmailUtente(), bindingResult);
            Esperto e = (Esperto) utenteRegistrato;
            commentoDTO.setUsername(e.getUsername());
            commentoDTO.setBoolEsperto(true);

        }
        else if (utils.isUtenteLettore(token)) {
            utenteRegistrato = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));
            biblionetResponse = checkAutorizzazioneLettore(token, post.getClubDelLibro(), utenteRegistrato, commentoDTO.getEmailUtente(), bindingResult);
            Lettore l = (Lettore) utenteRegistrato;
            commentoDTO.setUsername(l.getUsername());
            commentoDTO.setBoolEsperto(false);

        }
        if(null != biblionetResponse) return biblionetResponse;

        post = postService.aggiungiCommento(idPost, commentoDTO, utenteRegistrato);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);


    }

    private BiblionetResponse checkAutorizzazioneEsperto(String token, ClubDelLibro clubDelLibro, UtenteRegistrato esperto, String utenteMail, BindingResult bindingResult){
        if(!utils.getSubjectFromToken(token).equals(clubDelLibro.getEsperto().getEmail()))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(esperto == null)
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);

        if(!utils.isUtenteEsperto(token) || !utils.getSubjectFromToken(token).equals(clubDelLibro.getEsperto().getEmail()))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(!utils.getSubjectFromToken(token).equals(utenteMail)) //todo too much?
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);


        if(bindingResult.hasErrors()){
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }

        return null;
    }
    private BiblionetResponse checkAutorizzazioneLettore(String token, ClubDelLibro clubDelLibro, UtenteRegistrato lettore, String utenteMail, BindingResult bindingResult){

        if(lettore == null)
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);

        if(!lettoreService.partecipaClub(clubDelLibro, (Lettore) lettore))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(!utils.getSubjectFromToken(token).equals(utenteMail)) //todo too much?
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

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
