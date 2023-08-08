package com.example.gymgp2.Contextos;

public class Cuentas {

    public int id;
    public String nombres;
    public String apellidos;
    public String nacimiento;
    public String telefono;
    public String correo;
    public String clave;
    public String peso;
    public String foto;
    public int idpais;
    public String pais;


    public Cuentas(int codigo_usuario, String nombres, String apellidos,String foto) {
        this.nombres = nombres;
        this.id = codigo_usuario;
        this.apellidos = apellidos;
        this.foto = foto;

    }

    public Cuentas(int codigo_usuario, String nombres, String apellidos, String nacimiento, String telefono, String correo, String peso, String foto, int idpais, String pais) {
        this.id = codigo_usuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.nacimiento = nacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.peso = peso;
        this.foto = foto;
        this.idpais = idpais;
        this.pais = pais;
    }

    public Cuentas(int codigo_usuario, String nombres, String apellidos, String nacimiento, String telefono, String correo, String clave, String peso, String foto, int idpais, String pais) {
        this.id = codigo_usuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.nacimiento = nacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.clave = clave;
        this.peso = peso;
        this.foto = foto;
        this.idpais = idpais;
        this.pais = pais;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getIdpais() {
        return idpais;
    }

    public void setIdpais(int idpais) {
        this.idpais = idpais;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
