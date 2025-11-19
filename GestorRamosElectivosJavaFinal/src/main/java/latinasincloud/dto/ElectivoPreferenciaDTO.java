package latinasincloud.dto;

public class ElectivoPreferenciaDTO {
    private int electivoId;
    private int prioridad; // 1, 2, o 3

    public ElectivoPreferenciaDTO() {}

    public int getElectivoId() {
        return electivoId;
    }

    public void setElectivoId(int electivoId) {
        this.electivoId = electivoId;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }
}