package by.kos.randomcat;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

  private static final String BASE_URL = "https://cataas.com/cat?";
  private static final String URL_OPTION_JSON = "json=true";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadCatImage();
  }

  private void loadCatImage() {
    new Thread(() -> {
      try {
        URL url = new URL(BASE_URL + URL_OPTION_JSON);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder json = new StringBuilder();
        String res;
        do {
          res = bufferedReader.readLine();
          if (res != null) {
            json.append(res);
          }
        } while (bufferedReader.readLine() != null);

        JSONObject jsonObject = new JSONObject(json.toString());

        CatImage catImage = new CatImage(jsonObject.get("url").toString());
        Log.d("mainActivity", catImage.getUrl());

        Log.d("mainActivity", json.toString());

      } catch (Exception e) {
        Log.d("mainActivity", e.toString());
      }
    }).start();
  }
}