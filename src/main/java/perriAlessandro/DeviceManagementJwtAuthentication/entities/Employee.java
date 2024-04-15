package perriAlessandro.DeviceManagementJwtAuthentication.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Employee {

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

    public Employee(String username, String name, String surname, String email, String password, String imageUrl) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public void setDefaultImageUrl(String imageUrl) {
        if (name != null && surname != null) {
            this.imageUrl = String.format("https://ui-avatars.com/api/?name=%s+%s", name, surname);
        } else {
            this.imageUrl = "https://as1.ftcdn.net/v2/jpg/03/46/83/96/1000_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg";
        }
    }
}
