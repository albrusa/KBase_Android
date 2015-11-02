package kumo.kbase_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import kumo.kbase_android.model.xPrefijo_Telf;
import kumo.kbase_android.utils.Cultura;

public class RegistroTelefonoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_telefono);

        Bundle extras = getIntent().getExtras();


        AutoCompleteTextView mTelefono = (AutoCompleteTextView) findViewById(R.id.registro_telefono);


        TextView mCodigo_Acceso = (TextView)findViewById(R.id.registro_codigo_acceso);
        mCodigo_Acceso.setText(extras.get("Codigo_Acceso").toString());


        Spinner s = ( Spinner )findViewById( R.id.registro_prefijo );
        List<xPrefijo_Telf> prefijos = Cultura.obt_prefijos();
        s.setAdapter( prefijos );

        TelephonyManager man = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        int index = CountryCodes.getIndex( man.getSimCountryIso() );
        if( index > -1 )
        {
            s.setSelection( index );
        }


        Button mSiguiente = (Button) findViewById(R.id.registro_to_telefono);
        mSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                Bundle b = new Bundle();
                //  b.putString("EXTRA_MESSAGE", editText.getText().toString());

                intent.putExtras(b);
                startActivity(intent);*/

            }
        });

    }


    @Override
    protected void onResume(){
        super.onResume();
    }
}
