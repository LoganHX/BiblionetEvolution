package it.unisa.c07.biblionet.utils;

import it.unisa.c07.biblionet.entity.UtenteRegistrato;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Implementa la funzionalità di verifica che una stringa
 * rispetti una regex.
 */
public class RispettoVincoli {
    public static final String NAME_REGEX = "^[A-zÀ-ù ‘-]{2,60}$";
    public static final String PHONE_REGEX = "^\\d{10}$";
    public static final String ADDRESS_REGEX = "^[0-9A-zÀ-ù ‘-]{2,30}$";


    public static boolean passwordRispettaVincoli(UtenteRegistrato utente, String password){
        if(password.length() <= 7) return false;
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-256");
            return Arrays.equals(md.digest(password.getBytes()), utente.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean confrontoPassword(String nuova, String conferma){
        if (!nuova.isEmpty() && !conferma.isEmpty()) {
            if (nuova.length() <= 7)
                return false;
            return nuova.equals(conferma);
        }
        return false;
    }


}
