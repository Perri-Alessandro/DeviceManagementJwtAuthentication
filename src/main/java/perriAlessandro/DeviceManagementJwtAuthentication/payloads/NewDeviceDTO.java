package perriAlessandro.DeviceManagementJwtAuthentication.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewDeviceDTO(@NotEmpty(message = "Il nome del Device è obblogatorio")
                           @Size(min = 2, max = 30, message = "Il nome proprio deve essere compreso tra i 2 e i 30 caratteri")
                           String name,
                           @NotEmpty(message = "Lo status è obbligatorio")
                           @Size(min = 3, max = 30, message = "Lo Status deve essere compreso tra i 3 e i 30 caratteri")
                           String status) {

    public String getName() {
        return name;
    }

    // Getter per 'status'
    public String getStatus() {
        return status;
    }

}
