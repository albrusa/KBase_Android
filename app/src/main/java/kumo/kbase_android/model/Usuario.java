package kumo.kbase_android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by dev_2 on 03/11/2015.
 */

@DatabaseTable(tableName = "Usuario")
public class Usuario implements Serializable{

    @DatabaseField(id = true)
    public Integer id;

    @DatabaseField
    public String Codigo_Acceso;

    @DatabaseField
    public String Clave;

    @DatabaseField
    public String e_Clave;

    @DatabaseField
    public String Nombre;

    @DatabaseField
    public String Apellidos;

    @DatabaseField
    public String Prefijo;

    @DatabaseField
    public String Tlf_Movil;

    @DatabaseField
    public String Correo;

    @DatabaseField
    public String Idioma;

    @DatabaseField
    public String Imagen_Perfil;

    @DatabaseField
    public int c_Estado_Pago;

    public Usuario(){

    }
}
