package kumo.kbase_android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dev_2 on 03/11/2015.
 */

@DatabaseTable(tableName = "Documento")
public class Documento extends db_Base implements Serializable{

    @DatabaseField(id = true, generatedId = true)
    public Integer id_db;

    @DatabaseField
    public String Nombre;

    @DatabaseField
    public String Descripcion;

    @DatabaseField
    public Date Fecha;

    @DatabaseField
    public boolean si_Publicar;

    @DatabaseField
    public String Id_Archivo;

    @DatabaseField
    public String Imagen;

    @DatabaseField
    public String Propietario;

    @DatabaseField
    public String Url;


    public Documento(){

    }
}
