package kumo.kbase_android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by dev_2 on 03/11/2015.
 */

@DatabaseTable(tableName = "Configuracion")
public class Configuracion {

    @DatabaseField(id = true)
    Integer id;

    @DatabaseField
    String Id_Aplicacion;

    @DatabaseField
    String Aplicacion;

    @DatabaseField
    String Id_Usuario;

    @DatabaseField
    String Id_Usuario_Clase;

    @DatabaseField
    String Ruta_Imagen_Aplicacion;

    @DatabaseField
    String Ruta_Imagen_Usuario;

    public Configuracion(){

    }
}
