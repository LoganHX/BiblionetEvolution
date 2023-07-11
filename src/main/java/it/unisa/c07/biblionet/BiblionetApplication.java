package it.unisa.c07.biblionet;

import it.unisa.c07.biblionet.gestionebiblioteca.bookapiadapter.BookApiAdapter;
import it.unisa.c07.biblionet.gestionebiblioteca.bookapiadapter.GoogleBookApiAdapterImpl;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * Questa è la main class del progetto, che fa partire l'applicazione e popola
 * il database.
 */
@SpringBootApplication
public class BiblionetApplication {


    public static void main(String[] args) {
        SpringApplication.run(BiblionetApplication.class, args);
    }

}
