package com.example.jorge.proyectomovil_if7100;

/**
 * Created by Jorge on 5/20/2015.
 */
public class Citas {

    private int id,idPaciente;
    private String hora;

    public Citas() {
    }

    public Citas(int id, int idPaciente, String hora) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }



}
