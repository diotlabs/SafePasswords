package com.diot.safepasswords; /**
 * Created by Nishant on 09-04-2018.
 */
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Toast;

//import org.apache.http.HttpException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Belal on 8/6/2015.
 */
public class LoginClass {

    public String sendPostRequest(String requestURL, JSONObject jsonObject) {
        URL url;
        StringBuilder response = new StringBuilder();
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            conn.connect();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os));

            Log.e("json",jsonObject.toString());
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            Log.e("response Login Class",""+responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.e("here","HttpOk");
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line =null;
                while((line = br.readLine()) != null)
                {
                    // Append server response in string
                    response.append(line + "\n");
                }
//                response = br.readLine();
                Log.e("hereea",response.toString());
                conn.disconnect();
            }
            else {
                response.append("Error Logging in.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("nishreu","herere"+e);
        }

        return response.toString().trim();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuffer result = new StringBuffer();
        boolean first = true;

        for(Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
            {
                first = false;
                result.append("?");
                result.append(entry.getValue());
                result.append(entry.getValue());
            }else
                result.append("&");
            result.append(entry.getValue());
            result.append("=");
            result.append(entry.getValue());
        }

        Log.e("Len: ",Integer.toString(result.length()));
        return result.toString();
    }
}
