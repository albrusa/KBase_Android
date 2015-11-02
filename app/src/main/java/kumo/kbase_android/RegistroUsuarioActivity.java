package kumo.kbase_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class RegistroUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);


        final AutoCompleteTextView mCodigo_Acceso = (AutoCompleteTextView) findViewById(R.id.registro_codigo_acceso);


        Button mSiguiente  = (Button) findViewById(R.id.registro_to_telefono);
        mSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codigo_Acceso = mCodigo_Acceso.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if (!TextUtils.isEmpty(codigo_Acceso)) {
                    Intent intent = new Intent(RegistroUsuarioActivity.this, RegistroTelefonoActivity.class);

                    Bundle b = new Bundle();
                    b.putString("Codigo_Acceso",codigo_Acceso);

                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    mCodigo_Acceso.setError(getString(R.string.campo_obligatorio));
                    focusView = mCodigo_Acceso;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();

                }
            }
        }

        );

    }


        @Override
    protected void onResume(){
        super.onResume();
    }
}
