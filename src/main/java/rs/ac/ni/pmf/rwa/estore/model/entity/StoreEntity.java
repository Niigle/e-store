package rs.ac.ni.pmf.rwa.estore.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 55, nullable = false)
    private String name;

    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "phone", length = 100, nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager", nullable = false)
    private UserEntity manager;

    @Column(name = "is_active")
    private Integer isActive = 1;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_on", insertable = false, updatable = false)
    private LocalDateTime modifiedOn;

}