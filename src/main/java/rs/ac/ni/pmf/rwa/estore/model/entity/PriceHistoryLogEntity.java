package rs.ac.ni.pmf.rwa.estore.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_history_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceHistoryLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "old_price", nullable = false)
    private Integer oldPrice;

    @Column(name = "new_price", nullable = false)
    private Integer newPrice;

    @Column(name = "modified_on", insertable = false, updatable = false)
    private LocalDateTime modifiedOn;

}