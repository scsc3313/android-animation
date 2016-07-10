package jejunu.ac.kr.animation.gif;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by HSH on 16. 7. 10..
 */
public class ByteArrayHttpClient {

    private static final String TAG = "ByteArrayHttpClient";
    private static OkHttpClient client = new OkHttpClient();

    public static byte[] get(String gifURL) {
        InputStream inputStream = null;


        try {
            String decodeUrl = URLDecoder.decode(gifURL, "UTF-8");
            URL url = new URL(decodeUrl);
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            return IOUtils.toByteArray(inputStream);

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unsupported encoding " + e);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException " + e);
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e);
        }
        finally {
            if(inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "InputStream Close Exception " + e);
                }
        }

        return null;
    }
}
