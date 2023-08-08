package com.example.gymgp2.Contextos;

public class Api {

    private static final String ipaddress = "elkinhn.online";
    public static final String StringHttp = "https://";
    //EndPoint Urls
    private static final String GetAmigosAgregados = "/APIG2/listaamigos.php";
    private static final String GetBuscar = "/APIG2/validarLogin.php";
    private static final String getBuscarCorreo = "/APIG2/listasingleusuario.php";
    private static final String setUpdate = "/APIG2/actualizarusuario.php";
    private static final String CreateUsuario = "/APIG2/crearusuario.php";
    private static final String listaPaises = "/APIG2/listapaises.php";
    private static final String listaUsuarioPaise = "/APIG2/listasingleusuariopais.php";
    private static final String ListaAgregarAmigo = "/APIG2/listaagregaramigos.php";
    private static final String agregarAmigo = "/APIG2/crearamigo.php";
    private static final String guardarActividad = "/APIG2/crearactividad.php";
    private static final String detallesguardarActividad = "/APIG2/creardetalleactividad.php";
    private static final String EliminarAmigos = "/APIG2/eliminaramigo.php";
    private static final String listaActividades = "/APIG2/listaactividades.php";
    private static final String mostrarEstadistica = "/APIG2/estadisticas.php";
    private static final String listatop = "/APIG2/listaranking.php";


    //metodo get
    //public static final String EndPointGetContact = StringHttp + ipaddress + GetEmple;
    public static final String EndPointValidarLogin = StringHttp + ipaddress + GetBuscar;
    public static final String EndPointBuscarCorreo = StringHttp + ipaddress + getBuscarCorreo;

    public static final String EndPointSetUpdateUser= StringHttp + ipaddress + setUpdate;
    public static final String EndPointCreateUsuario = StringHttp + ipaddress + CreateUsuario;
    public static final String EndPointListarPaises = StringHttp + ipaddress + listaPaises;
    public static final String EndPointListarUsuarioPaise = StringHttp + ipaddress + listaUsuarioPaise;
    public static final String EndPointAgregarAmigo = StringHttp + ipaddress + agregarAmigo;
    public static final String EndPointListarAmigo = StringHttp + ipaddress + ListaAgregarAmigo;
    public static final String GuardarActidad = StringHttp + ipaddress + guardarActividad;
    public static final String DetallesGuardarActidad = StringHttp + ipaddress + detallesguardarActividad;
    public static final String EndPointListaAmigosAdd = StringHttp + ipaddress + GetAmigosAgregados;
    public static final String EndPointEliminarAmigosAdd = StringHttp + ipaddress + EliminarAmigos;
    public static final String ListarActividades = StringHttp + ipaddress + listaActividades;
    public static final String Estadistico = StringHttp + ipaddress + mostrarEstadistica;
    public static final String listaClasifiacion = StringHttp + ipaddress + listatop;


    public static String EndPointBuscarUsuario;

    public static  String correo = "";
    public static  String codigo_usuario = "";

}
