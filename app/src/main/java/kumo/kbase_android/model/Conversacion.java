package kumo.kbase_android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dev_2 on 03/11/2015.
 */

@DatabaseTable(tableName = "Conversacion")
public class Conversacion extends db_Base implements Serializable{

    @DatabaseField(id = true, generatedId = true)
    public Integer id_db;

    @DatabaseField
    public String Id_Usuario_def;

    @DatabaseField
    public String Id_Usuario_Clase_def;

    @DatabaseField
    public String Tema;

    @DatabaseField
    public String Consulta_De;

    @DatabaseField
    public int c_Importancia;

    @DatabaseField
    public int c_Estado;

    @DatabaseField
    public String Id_Grupo;

    @DatabaseField
    public String Id_Grupo_Clase;

    @DatabaseField
    public String Id_Ultimo_Mensaje;

    @DatabaseField
    public String Mensaje;

    @DatabaseField
    public String Mensaje_Id_Propietario;

    @DatabaseField
    public String Nombre;

    @DatabaseField
    public String Imagen;

    @DatabaseField
    public String Hora;

    @DatabaseField
    public String Dia;

    @DatabaseField
    public boolean si_Leido;

    @DatabaseField
    public Date Actualizado;


    public Conversacion(){

    }
}
