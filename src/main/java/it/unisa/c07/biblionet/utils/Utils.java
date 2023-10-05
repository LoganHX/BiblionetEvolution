package it.unisa.c07.biblionet.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

@Service
public class Utils {
    private Utils(){}

    public Claims getClaimsFromTokenWithoutKey(String token){
        token = token.substring(7);
        String[] splitToken = token.split("\\.");
        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";

        DefaultJwtParser parser = new DefaultJwtParser();
        Jwt<?, ?> jwt = parser.parse(unsignedToken);
        return (Claims) jwt.getBody();
    }

    public boolean isUtenteBiblioteca(String token){
        if(token == null) return false;
        return "Biblioteca".equalsIgnoreCase((String) getClaimsFromTokenWithoutKey(token).get("role"));
    }
    public boolean isUtenteLettore(String token){
        if(token == null) return false;
        return "Lettore".equalsIgnoreCase((String) getClaimsFromTokenWithoutKey(token).get("role"));
    }
    public boolean isUtenteEsperto(String token){
        if(token == null) return false;
        return "Esperto".equalsIgnoreCase((String) getClaimsFromTokenWithoutKey(token).get("role"));
    }
    public String getSubjectFromToken(String token){
        if(token == null) return null;
        return getClaimsFromTokenWithoutKey(token).getSubject();
    }


    public static String getBase64Image(MultipartFile copertina) {
        if (copertina != null && !copertina.isEmpty()) {
            try {
                return Base64.getEncoder().encodeToString(copertina.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean match(String a, String b){
        return a.equals(b);
    }
    public boolean immagineOk(MultipartFile imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile.getInputStream());

        int larghezzaImmagine = image.getWidth();
        int altezzaImmagine = image.getHeight();

        if (larghezzaImmagine > 1920 || altezzaImmagine > 1080) {
            return false;
        }
        return true;
    }

}
