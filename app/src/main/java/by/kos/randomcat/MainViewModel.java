package by.kos.randomcat;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.material.snackbar.Snackbar;
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

  private static final String TAG = "mainActivity";
  public final String BASE_URL = "https://cataas.com/";
  private MutableLiveData<CatImage> catImage = new MutableLiveData<>();
  private MutableLiveData<Boolean> isLoad = new MutableLiveData();
  private MutableLiveData<Boolean> isError = new MutableLiveData();
  private CompositeDisposable disposableContainer = new CompositeDisposable();

  public LiveData<Boolean> getIsError() {
    return isError;
  }

  public LiveData<CatImage> getCatImage() {
    return catImage;
  }

  public MainViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<Boolean> getIsLoad() {
    return isLoad;
  }

  public void loadCatImage() {
    Disposable disposable = loadCatImageRx()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable1 -> {
          isLoad.setValue(true);
        })
        .doAfterTerminate(() -> {
          isLoad.setValue(false);
        })
        .doOnError(throwable -> {
          isError.setValue(true);
        })
        .subscribe(image -> {
          catImage.setValue(image);
        }, throwable -> {
          Log.d(TAG, throwable.getMessage());
        });
    disposableContainer.add(disposable);
  }

  private Single<CatImage> loadCatImageRx() {
    return ApiFactory.getApiService().loadCatImage();
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    disposableContainer.dispose();
  }
}
