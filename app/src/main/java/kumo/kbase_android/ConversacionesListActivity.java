package kumo.kbase_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kumo.kbase_android.model.Configuracion;

public class ConversacionesListActivity extends AppCompatActivity{

    public Configuracion mConfiguracion;
    public String mCodigo_Acceso;
    public String mTelefono;
    public String mPrefijo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversaciones_list_activity);
    }


        @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
