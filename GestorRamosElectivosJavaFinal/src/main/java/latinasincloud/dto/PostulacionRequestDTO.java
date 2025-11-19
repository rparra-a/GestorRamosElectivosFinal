package latinasincloud.dto;

import java.util.List;

// (Representa la solicitud completa del estudiante)
public class PostulacionRequestDTO {
    private int estudianteId;
    private List<ElectivoPreferenciaDTO> preferencias; // Contendrá 3 elementos

    public PostulacionRequestDTO() {}

    public int getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(int estudianteId) {
        this.estudianteId = estudianteId;
    }

    public List<ElectivoPreferenciaDTO> getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(List<ElectivoPreferenciaDTO> preferencias) {
        this.preferencias = preferencias;
    }
}