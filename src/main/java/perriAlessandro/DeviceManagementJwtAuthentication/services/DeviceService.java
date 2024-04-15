package perriAlessandro.DeviceManagementJwtAuthentication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import perriAlessandro.DeviceManagementJwtAuthentication.entities.Device;
import perriAlessandro.DeviceManagementJwtAuthentication.entities.Employee;
import perriAlessandro.DeviceManagementJwtAuthentication.exceptions.NotFoundException;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.NewDeviceDTO;
import perriAlessandro.DeviceManagementJwtAuthentication.repositories.DevicesDAO;

import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    private DevicesDAO deviceDAO;

    @Autowired
    private EmployeeService employeeService;

    public Page<Device> getDeviceList(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.deviceDAO.findAll(pageable);
    }

    public Device saveDevice(NewDeviceDTO body) {
//        Employee employee = employeeService.findById(body.getEmployeeId());
//        if (employee == null) {
//            throw new NotFoundException(body.getEmployeeId());
//        }
        Device newDevice = new Device();
        newDevice.setName(body.getName());
        newDevice.setStatus(body.getStatus());
//        newDevice.setEmployee(employee);
        return deviceDAO.save(newDevice);
    }

    public Device findById(UUID id) {
        return this.deviceDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Device findByIdAndUpdate(UUID id, NewDeviceDTO updatedDevice) {
        Device found = this.findById(id);
        found.setName(updatedDevice.getName());
        found.setStatus(updatedDevice.getStatus());
//        Employee employee = employeeService.findById(updatedDevice.getEmployeeId());
//        found.setEmployee(employee);
        return deviceDAO.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        Device found = this.findById(id);
        found.setEmployee(null);
        this.deviceDAO.save(found); // Salva le modifiche al post senza l'autore
        this.deviceDAO.delete(found);
    }

    public Device assignDeviceToEmployee(UUID deviceId, UUID employeeId) {
        Device device = findById(deviceId);
        if (device == null) {
            throw new NotFoundException(deviceId);
        }
        Employee employee = employeeService.findById(employeeId);
        if (employee == null) {
            throw new NotFoundException(employeeId);
        }
        device.setEmployee(employee);
        device.setStatus("Assigned");
        return deviceDAO.save(device);
    }

}
