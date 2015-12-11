package kumo.kbase_android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by dev_2 on 03/11/2015.
 */

@DatabaseTable(tableName = "Mensaje")
public class Mensaje extends db_Base implements Serializable{

    @DatabaseField(id = true, generatedId = true)
    public Integer id_db;

    @DatabaseField
    public String Id_0;

    @DatabaseField
    public String Id_0_Clase;

    @DatabaseField
    public String Mensaje;

    @DatabaseField
    public int Orden;

    @DatabaseField
    public int c_Estado;

    @DatabaseField
    public String Id_Archivo;

    @DatabaseField
    public String Medida_Archivo;

    @DatabaseField
    public String Imagen_Archivo;

    @DatabaseField
    public boolean si_Leido;

    public String archivo_bytes;



    public Mensaje(){

    }
}
