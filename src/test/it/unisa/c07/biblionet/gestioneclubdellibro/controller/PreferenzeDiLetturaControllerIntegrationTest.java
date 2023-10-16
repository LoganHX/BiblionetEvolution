package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;

import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneutenti.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PreferenzeDiLetturaControllerIntegrationTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PreferenzeDiLetturaService preferenzeDiLetturaService;

    @Autowired
    private EspertoService espertoService;

    @Autowired
    private GenereDAO genereDAO;

    @Before
    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    /**
     * Test di Inserimento nuovi generi ad un esperto con esperto
     * e generi non null.
     *
     * @throws Exception eccezione di mockMvc
     */
    @Test
    public void generiLetterari() throws Exception {
        String tokenEsperto = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        String[] generi = {"Fantasy", "Giallo"};


        this.mockMvc.perform(post("/preferenze-di-lettura/modifica-generi")
                        .param("generi", generi)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    /**
     * Test di Inserimento nuovi generi ad un esperto con esperto
     * null e generi non null.
     *
     * @throws Exception eccezione di mockMvc
     */
    @Test
    public void generiLetterariNoUser() throws Exception {

        String[] generi = {"Fantasy", "Giallo"};


        this.mockMvc.perform(post("/preferenze-di-lettura/modifica-generi")
                        .param("generi", generi))
                .andExpect(status().is(400));
    }

}
