package ro.atm.dmc.objectselector;


import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestServerTask extends AsyncTask<String, Void, String> {
    static private String ServerStateUp = "ServerUP";

    @Override
    protected String doInBackground(String... strings) {

        String serverState = null;
        URL url;

        try {
            url = new URL(strings[0]);

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            InputStream inputStream = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject object = new JSONObject(builder.toString());
            serverState = object.getString("ServerState");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if(serverState.equals(ServerStateUp))
            return "true";
        else
            return "false";
    }
}
