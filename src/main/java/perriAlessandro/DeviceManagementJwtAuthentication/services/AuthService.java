package perriAlessandro.DeviceManagementJwtAuthentication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import perriAlessandro.DeviceManagementJwtAuthentication.entities.Employee;
import perriAlessandro.DeviceManagementJwtAuthentication.exceptions.UnauthorizedException;
import perriAlessandro.DeviceManagementJwtAuthentication.payloads.EmployeeLoginDTO;
import perriAlessandro.DeviceManagementJwtAuthentication.security.JWTTools;

@Service
public class AuthService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUserAndGenerateToken(EmployeeLoginDTO payload) {
        //Controllo le credenziali
        Employee user = this.employeeService.findByEmail(payload.email());
        // Verifico se la password combacia con quella ricevuta nel payload
        if (bcrypt.matches(payload.password(), user.getPassword())) {
            // Se è tutto OK, genero un token e lo torno
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali non valide! Effettua di nuovo il login!");
        }


    }
}
