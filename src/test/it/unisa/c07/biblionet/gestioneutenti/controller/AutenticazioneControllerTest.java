package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.config.JwtGeneratorInterface;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import lombok.Getter;
import lombok.Setter;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.mockito.Mockito;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.HashMap;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AutenticazioneControllerTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    /**
     * Inject di MockMvc per simulare
     * le richieste http.
     */
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtGeneratorInterface jwtGenerator;

    @MockBean
    private AutenticazioneService autenticazioneService;

    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    @Test
    public void loginOk() throws Exception {
        // Preparazione dei dati di test
        String email = "example@example.com";
        String password = "password";

        HashMap<String, String> token = new HashMap<>();
        token.put("token", "ValoreToken");

        UtenteRegistrato utenteRegistrato = Mockito.mock(UtenteRegistrato.class);

        when(autenticazioneService.login(email, password)).thenReturn(utenteRegistrato);
        when(jwtGenerator.generateToken(utenteRegistrato)).thenReturn(token);

        this.mockMvc.perform(post("/autenticazione/login")
                        .param("email", email)
                        .param("password", password)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.token").value(token.get("token")));
    }

    @Test
    public void loginFailed() throws Exception {
        // Preparazione dei dati di test
        String email = "example@example.com";
        String password = "password";

        HashMap<String, String> token = new HashMap<>();
        token.put("token", "ValoreToken");

        when(autenticazioneService.login(email, password)).thenReturn(null);

        this.mockMvc.perform(post("/autenticazione/login")
                        .param("email", email)
                        .param("password", password)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.LOGIN_FALLITO));
    }
}