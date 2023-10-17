package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.EspertoDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EspertoServiceImplTest {

    @MockBean
    private EspertoDAO espertoDAO;

    /**
     * Inject del service per simulare le operazioni.
     */

    private EspertoServiceImpl espertoService;

    @Before
    public void setUp() {
        espertoService = new EspertoServiceImpl(espertoDAO);
    }

    /**
     * Metodo che si occupa di testare
     * la funzione di ricerca di un Esperto
     * nel service.
     */
    @Test
    public void findEspertoByEmail() {
        Esperto dummy = new Esperto();
        String email = "";
        when(espertoDAO.findEspertoByEmail(email, "Esperto")).thenReturn(dummy);
        //System.out.println(espertoDAO.findEspertoByEmail(email, "Esperto"));
        //System.out.println(dummy);
        //System.out.println(espertoService.findEspertoByEmail(email));
        assertEquals(dummy, espertoService.findEspertoByEmail(email));
    }

    @Test
    @DisplayName("Non entra al primo for")
    public void findEspertiByGeneri0(){

        ArrayList<Esperto> lista = new ArrayList<>();
        when(espertoDAO.findAllEsperti()).thenReturn(lista);
        assertEquals(new ArrayList<>(),
                espertoService.findEspertiByGeneri(null));

    }

    @Test
    public void findEspertiByGeneri1(){
        Esperto esperto = Mockito.mock(Esperto.class);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo");
        ArrayList<Esperto> lista = new ArrayList<>();
        lista.add(esperto);

        when(espertoDAO.findAllEsperti()).thenReturn(lista);

        when(esperto.getGeneri()).thenReturn(generi);
        assertEquals(lista,
                espertoService.findEspertiByGeneri(generi));

    }

    @Test
    public void findEspertiByGeneri2(){

        when(espertoDAO.findAll()).thenReturn(new ArrayList<>());
        assertEquals(new ArrayList<>(),
                espertoService.findEspertiByGeneri(new HashSet<>()));

    }


    @Test
    @MethodSource("provideEsperto")
    @DisplayName("Entra al primo for")
    public void findEspertiByGeneri(){

        Set<String> generi = new HashSet<>();
        generi.add("Fiction");
        Esperto esperto = new Esperto(
                "eliaviviani@gmail.com",
                "EspertoPassword",
                "Napoli",
                "Torre del Greco",
                "Via Roma 2",
                "2345678901",
                "Espertissimo",
                "Elia",
                "Viviani",
                null
        );

        esperto.setNomeGeneri(generi);


        ArrayList<Esperto> lista = new ArrayList<>();
        lista.add(esperto);

        when(espertoDAO.findAllEsperti()).thenReturn(lista);

        assertEquals(lista,
                espertoService.findEspertiByGeneri(generi));
    }

    @Test
    public void findAllEsperti() {
        List<Esperto> list = new ArrayList<>();
        when(espertoDAO.findAllEsperti()).thenReturn(list);
        assertEquals(espertoService.findAllEsperti(), list);
    }

    @Test
    public void findEspertiByName() {
        List<Esperto> list = new ArrayList<>();
        when(espertoDAO.findByNomeLike("a")).thenReturn(list);
        assertEquals(espertoService.findEspertiByNome("a"), list);
    }






}
