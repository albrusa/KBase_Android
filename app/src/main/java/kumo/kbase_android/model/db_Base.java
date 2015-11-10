package kumo.kbase_android.model;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class db_Base {

    @DatabaseField
    public String Id;

    @DatabaseField
    public String Id_Clase;

    @DatabaseField
    public String Id_Propietario;

    @DatabaseField
    public String Id_Propietario_Clase;

    @DatabaseField
    public String Id_Aplicacion;

    @DatabaseField
    public Date Creado;

    @DatabaseField
    public Date Modificado;

}
