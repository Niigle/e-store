package rs.ac.ni.pmf.rwa.estore.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname", length = 55, nullable = false)
    private String firstName;

    @Column(name = "lastname", length = 55, nullable = false)
    private String lastName;

    @Column(name = "username", unique = true, length = 100, nullable = false)
    private String username;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @Column(name = "phone", length = 100, nullable = false)
    private String phone;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "created_on", insertable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "modified_on", insertable = false, updatable = false)
    private LocalDateTime modifiedOn;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();

}
