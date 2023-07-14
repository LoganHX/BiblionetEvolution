package it.unisa.c07.biblionet;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.GenereDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.security.NoSuchAlgorithmException;

/**
 * Questa è la main class del progetto, che fa partire l'applicazione e popola
 * il database.
 */
@SpringBootApplication
public class BiblionetApplication {

    public static void init(ConfigurableApplicationContext configurableApplicationContext) {
        GenereDAO genereDAO = configurableApplicationContext.getBean(GenereDAO.class);
        Genere fantasy = new Genere(
                "Fantasy",
                "Genere fantastico"
        );

        Genere azione = new Genere(
                "Azione",
                "Genere molto movimentato"
        );

        Genere space = new Genere(
                "Space",
                "Genere spaziale"
        );

        Genere biografico = new Genere(
                "Biografico",
                "Genere introspettivo"
        );

        Genere politico = new Genere(
                "Politico",
                "Genere ingannevole"
        );

        Genere narrativa = new Genere(
                "Narrativa",
                "Genere narrativo"
        );

        Genere romanzo = new Genere(
                "Romanzo",
                "Genere che si descrive da solo"
        );

        Genere storico = new Genere(
                "Storico",
                "Genere sulla storia"
        );

        Genere fantascienza = new Genere(
                "Fantascienza",
                "Genere genere fantastico ma scientifico"
        );

        Genere tecnologia = new Genere(
                "Tecnologia",
                "Genere moderno"
        );

        Genere noir = new Genere(
                "Noir",
                "Genere scuro"
        );

        Genere distopia = new Genere(
                "Distopia",
                "Genere ingannevole"
        );

        Genere romantico = new Genere(
                "Romantico",
                "Genere sdolcinato"
        );

        Genere avventura = new Genere(
                "D'Avventura",
                "Genere intraprendente"
        );

        Genere formazione = new Genere(
                "Di formazione",
                "Genere formativo"
        );

        Genere ragazzi = new Genere(
                "Per ragazzi",
                "Genere giovanile"
        );

        Genere horror = new Genere(
                "Horror",
                "Genere spaventosissimo"
        );

        Genere thriller = new Genere(
                "Thriller",
                "Genere ansioso"
        );

        Genere gotico = new Genere(
                "Gotico",
                "Genere dal gusto gotico"
        );

        Genere giallo = new Genere(
                "Giallo",
                "Genere investigativo"
        );

        Genere scientifico = new Genere(
                "Scientifico",
                "Genere scientifico"
        );

        Genere psicologico = new Genere(
                "Psicologico",
                "Genere psicologico"
        );

        Genere saggio = new Genere(
                "Saggio",
                "Genere sapiente"
        );

        Genere comico = new Genere(
                "Comico",
                "Genere divertente"
        );

        Genere fiabefavole = new Genere(
                "Fiabe e favole",
                "Genere fiabesco"
        );

        genereDAO.save(avventura);
        genereDAO.save(azione);
        genereDAO.save(giallo);
        genereDAO.save(gotico);
        genereDAO.save(biografico);
        genereDAO.save(comico);
        genereDAO.save(distopia);
        genereDAO.save(fantasy);
        genereDAO.save(fantascienza);
        genereDAO.save(fiabefavole);
        genereDAO.save(formazione);
        genereDAO.save(horror);
        genereDAO.save(narrativa);
        genereDAO.save(noir);
        genereDAO.save(politico);
        genereDAO.save(psicologico);
        genereDAO.save(ragazzi);
        genereDAO.save(romantico);
        genereDAO.save(romanzo);
        genereDAO.save(saggio);
        genereDAO.save(scientifico);
        genereDAO.save(space);
        genereDAO.save(storico);
        genereDAO.save(tecnologia);
        genereDAO.save(thriller);





    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(BiblionetApplication.class, args);

        init(configurableApplicationContext);
    }

}
