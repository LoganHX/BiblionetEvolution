package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.EspertoDAO;
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
    @MockBean
    private EspertoService espertoService; //todo ho fatto una modifica

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
    @DisplayName("Non entra al secondo for")
    public void findEspertiByGeneri1(){

        ArrayList<Esperto> lista = new ArrayList<>();

        when(espertoDAO.findAllEsperti()).thenReturn(lista);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(esperto.getGeneri()).thenReturn(new HashSet<>());
        assertEquals(lista,
                espertoService.findEspertiByGeneri(Mockito.any()));

    }

    @Test
    @DisplayName("Non entra al primo if")
    public void findEspertiByGeneri2(){

        when(espertoDAO.findAll()).thenReturn(new ArrayList<>());
        assertEquals(new ArrayList<>(),
                espertoService.findEspertiByGeneri(new HashSet<>()));

    }
    /*
    @ParameterizedTest
    @MethodSource("provideEsperto") //todo a che serve?
    @DisplayName("Entra al primo for")
    public void findEspertiByGeneri(Esperto esperto, Set<String> generi){
        esperto.setNomeGeneri();
        List<UtenteRegistrato> esperti = Arrays.asList(esperto);
        when(espertoDAO.findAll()).thenReturn(esperti);
        assertEquals(new ArrayList<>(),
                espertoService.findEspertiByGeneri(new ArrayList<>()));

    }
    */

    @ParameterizedTest
    @MethodSource("provideEsperto")
    @DisplayName("Entra al primo for")
    public void findEspertiByGeneri(Esperto esperto, Set<String> generi){

        esperto.setNomeGeneri(generi);
        List<Esperto> esperti = List.of(esperto);
        when(espertoDAO.findAllEsperti()).thenReturn(esperti);
        assertEquals(new ArrayList<>(),
                espertoService.findEspertiByGeneri(new HashSet<>()));
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

    /*

    @ParameterizedTest
    @MethodSource("provideEsperto")
    public void findEspertiByGenereNameForTrue(Esperto esperto) {
        List<Esperto> list = new ArrayList<>();
        Genere genere = new Genere();
        genere.setNome("Test");
        esperto.setNomeGeneri(new HashSet<>(Collections.singleton(genere.getNome())));
        list.add(esperto);

        when(genereDAO.findByName("Test")).thenReturn(genere);
        when(espertoDAO.findAllEsperti()).thenReturn(list);

        assertEquals(new ArrayList<>(), espertoService
                .visualizzaEspertiPerGenere("a"));
    }
todo dovrebbero essere superati


    @Test
    public void getEspertiByGenereNameForFalse() {
        List<Esperto> list = new ArrayList<>();
        Genere genere = new Genere();

        when(genereDAO.findByName("Test")).thenReturn(genere);
        when(espertoDAO.findAllEsperti()).thenReturn(list);

        assertEquals(new ArrayList<>(), espertoService
                .visualizzaEspertiPerGenere("a"));
    }
*/

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideEsperto() {

        Set<String> generi = new HashSet<>();
        generi.add("Fiction");
        return Stream.of(
                Arguments.of(
                        new Esperto(
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
                        ),
                        generi

                )
        );

    }

}
