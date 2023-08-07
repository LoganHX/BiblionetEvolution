package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneutenti.RegistrazioneService;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/registrazione")
public final class RegistrazioneController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final RegistrazioneService registrazioneService;
    private final LettoreService lettoreService;
    private final EspertoService espertoService;
    private final BibliotecaService bibliotecaService;

    /**
     * Implementa la funzionalità di registrazione di un esperto.*/

     @PostMapping(value = "/esperto")
     @ResponseBody
     @CrossOrigin
     public BiblionetResponse registrazioneEsperto(final @Valid @ModelAttribute EspertoDTO esperto,
     BindingResult bindingResult,
     final @RequestParam("conferma_password") String password,
     final @RequestParam("email_biblioteca") String bibliotecaEmail) {

         BiblionetResponse response = controlliPreliminari(bindingResult, password, (UtenteRegistratoDTO) esperto);
         if(response != null) return response;

         Esperto e = espertoService.creaEspertoDaModel(esperto, bibliotecaService.findBibliotecaByEmail(bibliotecaEmail));
         if(e == null)
             return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);

         return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
     }



    /**
     * Implementa la funzionalità di registrazione di una biblioteca.
     *
     * @param biblioteca la biblioteca da registrare
     * @param password   la password di conferma
     * @return la view di login
     */
    @PostMapping(value = "/biblioteca")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse registrazioneBiblioteca(@Valid @ModelAttribute BibliotecaDTO biblioteca,
                                                     BindingResult bindingResult,
                                                     @RequestParam("conferma_password") String password
    ) {
        BiblionetResponse response = controlliPreliminari(bindingResult, password, (UtenteRegistratoDTO) biblioteca);
        if(response != null) return response;

        Biblioteca b = bibliotecaService.creaBibliotecaDaModel(biblioteca);
        if(b == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }



    /**
     * Implementa la funzionalità di registrazione di
     * un lettore.
     * Gestisce la chiamata POST
     * per creare un nuovo lettore.
     *
     * @param lettore  Il lettore da registrare
     * @param password il campo conferma password del form per controllare
     *                 il corretto inserimento della stessa.
     * @return La view per effettuare il login
     */
    @PostMapping(value = "/lettore")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse registrazioneLettore(@Valid @ModelAttribute LettoreDTO lettore,
                                                  BindingResult bindingResult,
                                                  final @RequestParam("conferma_password")
                                                  String password
    ) {
        BiblionetResponse response = controlliPreliminari(bindingResult, password, (UtenteRegistratoDTO) lettore);
        if(response != null) return response;

        lettoreService.creaLettoreDaModel(lettore);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    private BiblionetResponse controlliPreliminari(BindingResult bindingResult, String password, UtenteRegistratoDTO utenteRegistrato) {
        if(bindingResult.hasErrors()) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }
        if (registrazioneService.isEmailRegistrata(utenteRegistrato.getEmail())) {
            return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_FALLITA, false);
        }

        if(!BiblionetConstraints.passwordRispettaVincoli(utenteRegistrato.getPassword(), password)) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }

        return null;

    }

}
