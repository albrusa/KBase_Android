package kumo.kbase_android.fragments.usuariosList;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kumo.kbase_android.R;


public class PinFragment extends Fragment {

    private static final String PIN = "pin";

    private String mPin;
    private int mPint_n = 1;



    public static PinFragment newInstance(String _pin) {
        PinFragment fragment = new PinFragment();
        Bundle args = new Bundle();
        args.putSerializable(PIN,_pin);
        fragment.setArguments(args);
        return fragment;
    }

    public PinFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPin = getArguments().getString(PIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view =  inflater.inflate(R.layout.pin_fragment, container, false);

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        Keyboard mKeyboard= new Keyboard(this.getContext(),R.layout.pin_keyboard);

        // Lookup the KeyboardView
        KeyboardView mKeyboardView= (KeyboardView)getView().findViewById(R.id.pin_keyboard);
        // Attach the keyboard to the view
        mKeyboardView.setKeyboard(mKeyboard);
        // Do not show the preview balloons
        mKeyboardView.setPreviewEnabled(false);

        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);

    }

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override public void onKey(int primaryCode, int[] keyCodes) {
            Log.d("Key:", Integer.toString(primaryCode));
            int id = getContext().getResources().getIdentifier("pin_text_"+mPint_n, "id", getContext().getPackageName());

            ((TextView)getView().findViewById(id)).setText("*");

            mPint_n++;
        }

        @Override public void onPress(int arg0) {

        }

        @Override public void onRelease(int primaryCode) {
        }

        @Override public void onText(CharSequence text) {
        }

        @Override public void swipeDown() {
        }

        @Override public void swipeLeft() {
        }

        @Override public void swipeRight() {
        }

        @Override public void swipeUp() {
        }
    };



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
