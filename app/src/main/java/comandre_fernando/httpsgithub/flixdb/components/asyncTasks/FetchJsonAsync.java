package comandre_fernando.httpsgithub.flixdb.components.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FetchJsonAsync extends AsyncTask<URL, Void ,String> {
    @Override
    protected String doInBackground(URL... url) {
        HttpURLConnection urlConnection =null;
        String jsonStringResponse=null;
        try {
            urlConnection = (HttpURLConnection) url[0].openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in).useDelimiter("\\A");

            jsonStringResponse = scanner.hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonStringResponse;
    }
}
