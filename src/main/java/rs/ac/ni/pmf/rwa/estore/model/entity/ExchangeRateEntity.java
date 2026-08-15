package rs.ac.ni.pmf.rwa.estore.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "exchange_rate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currency_from", length = 10, nullable = false)
    private String currencyFrom;

    @Column(name = "currency_to", length = 10, nullable = false)
    private String currencyTo;

    @Column(name = "exchange_rate", nullable = false)
    private Float exchangeRate;

    @Column(name = "date_of", nullable = false)
    private LocalDate dateOf;

}