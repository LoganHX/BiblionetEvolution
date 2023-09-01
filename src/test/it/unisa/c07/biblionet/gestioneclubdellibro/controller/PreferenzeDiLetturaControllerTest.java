package it.unisa.c07.biblionet.gestioneclubdellibro.controller;


        import it.unisa.c07.biblionet.gestioneclubdellibro.*;
        import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
        import it.unisa.c07.biblionet.utils.BiblionetResponse;
        import org.junit.jupiter.api.DisplayName;
        import org.junit.jupiter.api.Test;
        import org.mockito.Mockito;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.boot.test.mock.mockito.MockBean;
        import org.springframework.http.HttpHeaders;
        import org.springframework.test.web.servlet.MockMvc;
        import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

        import java.util.HashSet;
        import java.util.Set;

        import static org.mockito.Mockito.doNothing;
        import static org.mockito.Mockito.when;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Implementa il testing di unità per la classe
 * ClubDelLibroController.
 *
 * @author Viviana Pentangelo
 * @author Gianmario Voria
 * @author Nicola Pagliara
 * @author Luca Topo
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PreferenzeDiLetturaControllerTest {

    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private PreferenzeDiLetturaService preferenzeDiLetturaService;

    @MockBean
    private GenereService genereService;
    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private LettoreService lettoreService;
    @MockBean
    private EspertoService espertoService;

    /**
     * Inject di MockMvc per simulare
     * le richieste http.
     */
    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Modifica di generi di un lettore")
    public void modificaGeneriLettore() throws Exception {
        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4NzI3NDk4OH0.5oCy7B9dBs97F7XGr1uVa-x1ofyjodrrthsd_xEu3_s";
        Lettore lettore=new Lettore();
        String[] gen = {"Fantasy"};

        Genere genere = new Genere("Fantasy", "Descrizione");
        Set<Genere> generi = new HashSet<>();
        generi.add(genere);
        when(genereService.getGeneriByName(gen)).thenReturn(generi);
        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(lettore);
        doNothing().when(preferenzeDiLetturaService).addGeneriEsperto(Mockito.any(),Mockito.any());

        this.mockMvc.perform(post("/preferenze-di-lettura/modifica-generi")
                        .param("genere", gen)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));


    }


    @Test
    @DisplayName("Modifica di generi di un esperto")
    public void modificaGeneriEsperto() throws Exception {
        String tokenEsperto = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        Esperto esperto=new Esperto();
        String[] gen = {"Fantasy"};

        Genere genere = new Genere("Fantasy", "Descrizione");
        Set<Genere> generi = new HashSet<>();
        generi.add(genere);
        when(genereService.getGeneriByName(gen)).thenReturn(generi);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(esperto);
        doNothing().when(preferenzeDiLetturaService).addGeneriEsperto(Mockito.any(),Mockito.any());

        this.mockMvc.perform(post("/preferenze-di-lettura/modifica-generi")
                        .param("genere", gen)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));

    }
    /**
     * Modifica di generi con utente null.
     * @throws Exception eccezione di mockMvc
     */
    @Test
    @DisplayName("Modifica di generi con utente non valido")
    public void modificaGeneriUtenteNonValido() throws Exception {

        String tokenBiblio = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";
        String[] gen = {""};

        when(genereService.getGeneriByName(gen)).thenReturn(new HashSet<>());

        this.mockMvc.perform(post("/preferenze-di-lettura/modifica-generi")
                        .param("genere", gen)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblio))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));

    }


}
