package perriAlessandro.DeviceManagementJwtAuthentication.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import perriAlessandro.DeviceManagementJwtAuthentication.exceptions.BadRequestException;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.EmployeeLoginDTO;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.EmployeeLoginResponseDTO;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.NewEmployeeDTO;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.NewEmployeeRespDTO;
import perriAlessandro.DeviceManagementJwtAuthentication.services.AuthService;
import perriAlessandro.DeviceManagementJwtAuthentication.services.EmployeeService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public EmployeeLoginResponseDTO login(@RequestBody EmployeeLoginDTO body) {
        return new EmployeeLoginResponseDTO(this.authService.authenticateUserAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewEmployeeRespDTO saveUser(@RequestBody @Validated NewEmployeeDTO body, BindingResult validation) {
        // @Validated valida il payload in base ai validatori utilizzati nella classe NewUserDTO
        // BindingResult validation ci serve per valutare il risultato di questa validazione
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors()); // Inviamo la lista degli errori all'Error Handler opportuno
        }
        // Altrimenti se non ci sono stati errori posso salvare tranquillamente lo user
        return new NewEmployeeRespDTO(this.employeeService.saveEmployee(body).getId());
    }
}
