package kumo.kbase_android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by dev_2 on 03/11/2015.
 */

@DatabaseTable(tableName = "Archivo")
public class Archivo extends db_Base implements Serializable{

    @DatabaseField(id = true, generatedId = true)
    public Integer id_db;

    @DatabaseField
    public String Nombre;

    @DatabaseField
    public long Longitud;

    @DatabaseField
    public ByteBuffer Archivo;

    public Archivo(){

    }
}
