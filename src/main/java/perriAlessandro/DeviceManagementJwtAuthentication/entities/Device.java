package perriAlessandro.DeviceManagementJwtAuthentication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Device {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String status;
    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonBackReference
    ///Per avere la lista di Device in ogni dipendente, nel json di ritorno,
    // ho duvuto far così, togliendo dal json che torna tutti i device i reciproci dipendenti.
    private Employee employee;

    public void availableDevice(String status) {
        this.status = "Available";
    }

    public void assignedDevice(String status, Employee employee) {
        this.status = "Assigned";
        setEmployee(employee);
    }

    public void underMaintenanceDevice(String status) {
        this.status = "Under Maintenance";
    }

    public void decommissionedDevice(String status) {
        this.status = "Decommissioned";
    }

}
