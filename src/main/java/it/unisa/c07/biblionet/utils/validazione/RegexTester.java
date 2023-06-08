package it.unisa.c07.biblionet.utils.validazione;

import it.unisa.c07.biblionet.model.entity.utente.Biblioteca;
import it.unisa.c07.biblionet.model.entity.utente.Esperto;
import it.unisa.c07.biblionet.model.entity.utente.Lettore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

/**
 * Implementa la funzionalità di verifica che una stringa
 * rispetti una regex.
 */
public class RegexTester {

    /**
     * Implementa la funzionalità di verifica che una stringa rispetti
     * una regex.
     * @param regexToTest la regex da testare
     * @return true se la rispetta, false altrimenti
     */
    public static boolean toTest(final HashMap<String, String> regexToTest) {

        return regexToTest.entrySet().stream().allMatch(
                entry -> {
                    if (entry.getKey() == null) {
                        return true;
                    }

                    return entry.getKey().matches(entry.getValue());
                }
        );
    }

    private static boolean testPersonaFisica(String nome, String cognome, String recapitoTelefonico, String via){
        HashMap<String, String> tester = new HashMap<>();
        tester.put(nome, "^[A-zÀ-ù ‘-]{2,60}$");
        tester.put(cognome, "^[A-zÀ-ù ‘-]{2,60}$");
        tester.put(recapitoTelefonico, "^\\d{10}$");
        tester.put(via, "^[0-9A-zÀ-ù ‘-]{2,30}$");

        RegexTester regexTester = new RegexTester();

        if (!regexTester.toTest(tester)) {
            return false;
        }
        return true;
    }
    public static boolean testEsperto(Esperto esperto){
        return testPersonaFisica(esperto.getNome(), esperto.getCognome(), esperto.getRecapitoTelefonico(), esperto.getVia());
    }
    public static boolean testLettore(Lettore lettore){
        return testPersonaFisica(lettore.getNome(), lettore.getCognome(), lettore.getRecapitoTelefonico(), lettore.getVia());
    }
    public static boolean testBiblioteca(Biblioteca biblioteca){
        HashMap<String, String> tester = new HashMap<>();
        tester.put(biblioteca.getNomeBiblioteca(), "^[A-zÀ-ù ‘-]{2,60}$");
        tester.put(biblioteca.getRecapitoTelefonico(), "^\\d{10}$");
        tester.put(biblioteca.getVia(), "^[0-9A-zÀ-ù ‘-]{2,30}$");

        RegexTester regexTester = new RegexTester();

        if (!regexTester.toTest(tester)) {
            return false;
        }
        return true;
    }

}
