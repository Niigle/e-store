package rs.ac.ni.pmf.rwa.estore.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_turnover_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyTurnoverLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date", insertable = false, updatable = false)
    private LocalDateTime date;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "total_turnover", nullable = false)
    private Integer totalTurnover;

    @Column(name = "unique_purchase_count", nullable = false)
    private Integer uniquePurchaseCount;

}