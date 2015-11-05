package kumo.kbase_android.httpRequest;


import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import kumo.kbase_android.model.ret_Api;

/**
 * Volley GET request which parses JSON server response into Java object.
 */
public class GsonRequest<T> extends JsonRequest<T> {

    /** JSON parsing engine */
    protected final Gson gson;
    /** class of type of response */
    protected final Class<T> clazz;
    /** result listener */
    private final Response.Listener<T> mlistener;
    private Map<String, String> mHeaders;

    private JsonObject parameters = null;
    private View mView;


    public GsonRequest(int method,View _view, String url, Class<T> classType, JsonObject jsonRequest,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method,_view, url, classType, null, jsonRequest, listener, errorListener);
    }

    public GsonRequest(int method, View _view,  String url, Class<T> classType, Map<String, String> headers,
                       JsonObject jsonRequest, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);

        setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mView = _view;
        gson = new Gson();
        clazz = classType;
        mHeaders = headers;
        mlistener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }


    @Override
    protected void deliverResponse(T response) {
        mlistener.onResponse(response);
    }

    protected VolleyError parseNetworkError(VolleyError volleyError){
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }

        Log.d("error", volleyError.getMessage());

        /*Snackbar.make(mView, "Esto es una prueba", Snackbar.LENGTH_LONG)
                .show();*/

        return volleyError;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {

            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));

            ret_Api valor = gson.fromJson(json, ret_Api.class);

            if(valor.Id_Estado == 0) {
                return Response.success(
                        gson.fromJson(valor.vX, clazz), HttpHeaderParser.parseCacheHeaders(response));
            }else{
                throw new Exception(valor.Mensaje);
            }


            /*return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));*/

           /* String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));*/

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }catch (Exception e){
            return Response.error(new ParseError(e));
        }
    }
}