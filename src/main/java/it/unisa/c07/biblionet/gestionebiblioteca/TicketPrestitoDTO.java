package it.unisa.c07.biblionet.gestionebiblioteca;

import it.unisa.c07.biblionet.common.LibroDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.TicketPrestito;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TicketPrestitoDTO {

    /**
     * Rappresenta l'ID autogenerato di un ticket.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idTicket;
    /**
     * Rappresenta lo stato del ticket.
     */
    @NonNull
    @Column(nullable = false)
    private it.unisa.c07.biblionet.gestionebiblioteca.repository.TicketPrestito.Stati stato;
    /**
     * Rappresenta la data in cui è stata fatta la richiesta di prestito.
     */
    @NonNull
    @Column(nullable = false)
    private LocalDateTime dataRichiesta;
    /**
     * Rappresenta la data di restituzione del libro.
     */
    private LocalDateTime dataRestituzione;
    /**
     * Rappresenta il libro che si prende il prestito.
     */
    @NonNull
    private LibroDTO libro;
    /**
     * Rappresenta la biblioteca da cui si prende il prestito il libro.
     */
    @NonNull
    private String biblioteca;
    /**
     * Rappresenta il lettore che prende in prestito il libro.
     */
    @NonNull
    private String lettore;

   public TicketPrestitoDTO(TicketPrestito ticketPrestito){
       this.stato = ticketPrestito.getStato();
       this.biblioteca = ticketPrestito.getBiblioteca().getEmail();
       this.lettore = ticketPrestito.getLettore().getEmail();
       this.libro = new LibroDTO(ticketPrestito.getLibro());
       this.dataRestituzione = ticketPrestito.getDataRestituzione();
       this.dataRichiesta = ticketPrestito.getDataRichiesta();
   }
}




