package it.unisa.c07.biblionet.config;

import it.unisa.c07.biblionet.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean jwtFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new JwtFilter());


        filter.addUrlPatterns("/biblioteca/inserimento-isbn");
        filter.addUrlPatterns("/biblioteca/inserimento-archivio");
        filter.addUrlPatterns("/biblioteca/inserimento-manuale");

        filter.addUrlPatterns("/prenotazione-libri/conferma-prenotazione");
        filter.addUrlPatterns("/prenotazione-libri/visualizza-richieste");
        filter.addUrlPatterns("/prenotazione-libri/visualizza-prenotazioni");

        filter.addUrlPatterns("/club-del-libro/crea");
        filter.addUrlPatterns("/club-del-libro/modifica");

        filter.addUrlPatterns("/gestione-eventi/crea");
        filter.addUrlPatterns("/gestione-eventi/modifica");




        return filter;
    }


}