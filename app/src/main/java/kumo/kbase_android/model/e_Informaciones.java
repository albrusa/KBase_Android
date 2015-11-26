package kumo.kbase_android.model;

/**
 * Created by dev_2 on 26/11/2015.
 */
public enum e_Informaciones {
    Noticia(0), Visita(1), Anotacion(2);

    private final int value;
    private e_Informaciones(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}