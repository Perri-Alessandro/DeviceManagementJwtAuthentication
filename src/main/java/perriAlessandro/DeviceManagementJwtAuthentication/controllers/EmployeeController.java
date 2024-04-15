package perriAlessandro.DeviceManagementJwtAuthentication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import perriAlessandro.DeviceManagementJwtAuthentication.entities.Employee;
import perriAlessandro.DeviceManagementJwtAuthentication.exceptions.BadRequestException;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.NewEmployeeDTO;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.NewEmployeeRespDTO;
import perriAlessandro.DeviceManagementJwtAuthentication.services.EmployeeService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    // GET .../employees
    @GetMapping
    public Page<Employee> getAllDevice(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        return this.employeeService.getEmployeesList(page, size, sortBy);
    }

    // POST .../employees (+ body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewEmployeeRespDTO saveEmployee(@RequestBody @Validated NewEmployeeDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return new NewEmployeeRespDTO(this.employeeService.saveEmployee(body).getId());
    }

    // GET .../employees/{employID}
    @GetMapping("/{employID}")
    private Employee findBlogById(@PathVariable UUID employID) {
        return employeeService.findById(employID);
    }

    // PUT .../employees/{employID} (+ body)
    @PutMapping("/{employID}")
    private Employee findBlogByIdAndUpdate(@PathVariable UUID employID, @RequestBody Employee body) {
        return employeeService.findByIdAndUpdate(employID, body);
    }

    // DELETE .../employees/{employID}
    @DeleteMapping("/{employID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void findByDeviceIdAndDelete(@PathVariable UUID employID) {
        employeeService.findByIdAndDelete(employID);
    }

    @PatchMapping("/{employeeId}/update")
    public String updateImage(@PathVariable UUID employeeId, @RequestParam("imageUrl") MultipartFile image) throws IOException {
        return this.employeeService.updateImage(employeeId, image);
    }

}
