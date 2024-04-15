package perriAlessandro.DeviceManagementJwtAuthentication.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import perriAlessandro.DeviceManagementJwtAuthentication.entities.Employee;
import perriAlessandro.DeviceManagementJwtAuthentication.exceptions.BadRequestException;
import perriAlessandro.DeviceManagementJwtAuthentication.exceptions.NotFoundException;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.NewEmployeeDTO;
import perriAlessandro.DeviceManagementJwtAuthentication.repositories.DevicesDAO;
import perriAlessandro.DeviceManagementJwtAuthentication.repositories.EmployeesDAO;

import java.io.IOException;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    private EmployeesDAO employeesDAO;

    @Autowired
    private DevicesDAO devicesDAO;

    @Autowired
    private Cloudinary cloudinaryUploader;

    public Page<Employee> getEmployeesList(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.employeesDAO.findAll(pageable);
    }

    public Employee saveEmployee(NewEmployeeDTO body) {
        if (employeesDAO.findByEmail(body.email()).isPresent()) {
            throw new BadRequestException("L'email " + body.email() + " è già in uso!");
        }
        Employee newUser = new Employee(body.username(), body.name(), body.surname(), body.email(),
                "https://ui-avatars.com/api/?name=" + body.name() + "+" + body.surname());

        // 4. Salvo lo user
        return employeesDAO.save(newUser);
    }

    public Employee findById(UUID id) {
        return this.employeesDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Employee findByIdAndUpdate(UUID id, Employee updateEmployee) {
        Employee found = this.findById(id);
        found.setUsername(updateEmployee.getUsername());
        found.setName(updateEmployee.getName());
        found.setSurname(updateEmployee.getSurname());
        found.setEmail(updateEmployee.getEmail());
        found.setImageUrl(updateEmployee.getImageUrl());
        found.setDeviceList(updateEmployee.getDeviceList());
        return employeesDAO.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        Employee found = this.findById(id);
        employeesDAO.delete(found);
    }

    public String updateImage(UUID employeeId, MultipartFile image) throws IOException {
        Employee employee = employeesDAO.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
        String newImageUrl = (String) cloudinaryUploader.uploader().upload(image.getBytes(), ObjectUtils.emptyMap()).get("url");
        employee.setImageUrl(newImageUrl);
        employeesDAO.save(employee);
        return newImageUrl;
    }

}
