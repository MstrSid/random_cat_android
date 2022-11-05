package by.kos.randomcat;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.disposables.DisposableContainer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class MainViewModel extends AndroidViewModel {

  private static final String BASE_URL = "https://cataas.com/cat?";
  private static final String URL_OPTION_JSON = "json=true";
  private static final String KEY_GET_URL = "url";
  private static final String TAG = "mainActivity";
  private MutableLiveData<CatImage> catImage = new MutableLiveData<>();
  private CompositeDisposable disposableContainer = new CompositeDisposable();

  public LiveData<CatImage> getCatImage() {
    return catImage;
  }

  public MainViewModel(@NonNull Application application) {
    super(application);
  }

  public void loadCatImage() {
    Disposable disposable = loadCatImageRx()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(image -> {
          catImage.setValue(image);
        }, throwable -> {
          Log.d(TAG, throwable.getMessage());
        });
    disposableContainer.add(disposable);
  }

  private Single<CatImage> loadCatImageRx() {
    return Single.fromCallable(() -> {
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

      return new CatImage(jsonObject.get(KEY_GET_URL).toString());
    });
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    disposableContainer.dispose();
  }
}
