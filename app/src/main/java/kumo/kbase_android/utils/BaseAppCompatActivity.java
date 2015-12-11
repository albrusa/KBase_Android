package kumo.kbase_android.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dev_2 on 10/12/2015.
 */
public class BaseAppCompatActivity extends AppCompatActivity{

    private ReceiverManager mReceiverManager;
    protected MemoryBoss mMemoryBoss;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        mMemoryBoss = MemoryBoss.init(getBaseContext());

        registerComponentCallbacks(mMemoryBoss);
        getApplication().registerActivityLifecycleCallbacks(mMemoryBoss);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if(mMemoryBoss != null) {
            unregisterComponentCallbacks(mMemoryBoss);
            getApplication().unregisterActivityLifecycleCallbacks(mMemoryBoss);
        }

    }
    /*private DestruirActivityReceiver destruirActivityReceiver = new DestruirActivityReceiver();

    public class DestruirActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ReceiverManager.DESTRUIR_TODAS_ACTIVITIES)) {
                finish();
            }
        }
    }*/
}
