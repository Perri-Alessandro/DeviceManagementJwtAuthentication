package perriAlessandro.DeviceManagementJwtAuthentication.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"password", "role", "authorities", "accountNonExpired", "credentialsNonExpired", "accountNonLocked", "enabled"})
public class Employee implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String imageUrl;
    private String password;
    @OneToMany(mappedBy = "employee")
    @JsonManagedReference
    private List<Device> deviceList;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Employee(String username, String name, String surname, String email, String password, String imageUrl) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.role = Role.USER;
    }

    public void setDefaultImageUrl(String imageUrl) {
        if (name != null && surname != null) {
            this.imageUrl = String.format("https://ui-avatars.com/api/?name=%s+%s", name, surname);
        } else {
            this.imageUrl = "https://as1.ftcdn.net/v2/jpg/03/46/83/96/1000_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg";
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Questo metodo deve ritornare la lista dei ruoli (SimpleGrantedAuthority) dell'utente
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
