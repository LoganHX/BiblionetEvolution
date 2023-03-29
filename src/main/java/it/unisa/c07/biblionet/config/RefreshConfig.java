package it.unisa.c07.biblionet.config;

import it.unisa.c07.biblionet.model.dao.SessionUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class RefreshConfig {

    @Autowired
    SessionUserDao sessionUserDao;


    @Scheduled(fixedDelay = 15*1000)
    public void every15Minutes(){
        System.out.println("REFRESH");
    }
}
