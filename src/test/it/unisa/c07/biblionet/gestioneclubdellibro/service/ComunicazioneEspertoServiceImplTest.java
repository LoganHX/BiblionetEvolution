package it.unisa.c07.biblionet.gestioneclubdellibro.service;


import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.EspertoDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.GenereDAO;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ComunicazioneEspertoServiceImplTest {


    /**
     * Inject del service per simulare le operazioni.
     */
    @InjectMocks
    private EspertoService espertoService;

    @Mock
    private EspertoDAO espertoDAO;

    @Mock
    private GenereDAO genereDAO;






}
