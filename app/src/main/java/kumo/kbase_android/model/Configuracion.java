package kumo.kbase_android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by dev_2 on 03/11/2015.
 */

@DatabaseTable(tableName = "Configuracion")
public class Configuracion implements Serializable{

    @DatabaseField(id = true)
    public Integer id;

    @DatabaseField
    public String Id_Aplicacion;

    @DatabaseField
    public String Aplicacion;

    @DatabaseField
    public String Id_Usuario;

    @DatabaseField
    public String Id_Usuario_Clase;

    @DatabaseField
    public String Ruta_Imagen_Aplicacion;

    @DatabaseField
    public String Ruta_Imagen_Usuario;

    public Configuracion(){

    }
}
