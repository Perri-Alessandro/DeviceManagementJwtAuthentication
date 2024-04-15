package perriAlessandro.DeviceManagementJwtAuthentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import perriAlessandro.DeviceManagementJwtAuthentication.entities.Device;
import perriAlessandro.DeviceManagementJwtAuthentication.entities.Employee;

import java.util.List;
import java.util.UUID;

public interface DevicesDAO extends JpaRepository<Device, UUID> {
    boolean existsByEmployee(Employee employee);

    List<Device> findByEmployee(Employee employee);
}
