package kumo.kbase_android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by dev_2 on 03/11/2015.
 */


@DatabaseTable(tableName = "Informacion")
public class Informacion extends db_Base implements Serializable{

    @DatabaseField(id = true, generatedId = true)
    public Integer id_db;

    @DatabaseField
    public String Titulo;

    @DatabaseField
    public String Descripcion;

    @DatabaseField
    public String Imagen;

    @DatabaseField
    public String Dia;

    @DatabaseField
    public String Hora;

    @DatabaseField
    public int c_Tipo;

    public Informacion(){

    }
}