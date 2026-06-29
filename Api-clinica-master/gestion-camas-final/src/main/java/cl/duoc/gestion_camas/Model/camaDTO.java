package cl.duoc.gestion_camas.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class camaDTO {
    private String  id;
    private int  prioridad;
    private String  pabellon;
    private Boolean disponible;
}
