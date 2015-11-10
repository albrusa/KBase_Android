package kumo.kbase_android.httpRequest;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dev_2 on 09/11/2015.
 */
public class NetDateTimeAdapter extends TypeAdapter<Date> {

    @Override
    public Date read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        Date result = null;
        String str = reader.nextString();
        str = str.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)","$1$2");
        if (!TextUtils.isEmpty(str)) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                result = format.parse(str);
            }
            catch (NumberFormatException e){}
            catch (ParseException e){}
        }
        return result;
    }
    @Override
    public void write(JsonWriter writer, Date value) throws IOException {

    }
}