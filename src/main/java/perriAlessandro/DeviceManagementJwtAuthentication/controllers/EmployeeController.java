package perriAlessandro.DeviceManagementJwtAuthentication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping("/me")
    public Employee getProfile(@AuthenticationPrincipal Employee currentAuthenticatedUser) {
        // @AuthenticationPrincipal mi consente di accedere all'utente attualmente autenticato
        // Questa cosa è resa possibile dal fatto che precedentemente a questo endpoint (ovvero nel JWTFilter)
        // ho estratto l'id dal token e sono andato nel db per cercare l'utente ed "associarlo" a questa richiesta
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public Employee updateProfile(@AuthenticationPrincipal Employee currentAuthenticatedUser, @RequestBody Employee updatedUser) {
        return this.employeeService.findByIdAndUpdate(currentAuthenticatedUser.getId(), updatedUser);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Employee currentAuthenticatedUser) {
        this.employeeService.findByIdAndDelete(currentAuthenticatedUser.getId());
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
    public Employee findBlogById(@PathVariable UUID employID) {
        return employeeService.findById(employID);
    }

    // PUT .../employees/{employID} (+ body)
    @PutMapping("/{employID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee findBlogByIdAndUpdate(@PathVariable UUID employID, @RequestBody Employee body) {
        return employeeService.findByIdAndUpdate(employID, body); // return this. ???
    }

    // DELETE .../employees/{employID}
    @DeleteMapping("/{employID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByDeviceIdAndDelete(@PathVariable UUID employID) {
        employeeService.findByIdAndDelete(employID);
    }

    @PatchMapping("/{employeeId}/update")
    public String updateImage(@PathVariable UUID employeeId, @RequestParam("imageUrl") MultipartFile image) throws IOException {
//        if (image.isEmpty() || !image.getContentType().startsWith("multipart")) {
//            throw new BadRequestException("La richiesta deve essere di tipo multipart e contenere un'immagine.");
//        }
        return this.employeeService.updateImage(employeeId, image);
    }

}
