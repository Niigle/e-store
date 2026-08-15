package rs.ac.ni.pmf.rwa.estore.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @ManyToMany(mappedBy = "permissions")
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();
}