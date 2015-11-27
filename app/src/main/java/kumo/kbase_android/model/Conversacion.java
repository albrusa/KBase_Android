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
    public String Nombre;

    @DatabaseField
    public String Mensaje;

    @DatabaseField
    public String Imagen;

    @DatabaseField
    public String Dia;

    @DatabaseField
    public String Hora;

    @DatabaseField
    public boolean si_Leido;

    @DatabaseField
    public Date Actualizado;


    public Conversacion(){

    }
}
