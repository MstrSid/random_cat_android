package by.kos.randomcat;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

  private MainViewModel viewModel;
  private static final String TAG = "mainActivity";

  private ImageView ivCat;
  private Button btnNext;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();

    viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    viewModel.loadCatImage();

    viewModel.getIsLoad().observe(this, isLoading -> {
      if (isLoading) {
        progressBar.setVisibility(View.VISIBLE);
      } else {
        progressBar.setVisibility(View.INVISIBLE);
      }
    });

    viewModel.getCatImage().observe(this, catImage -> {
      Glide.with(MainActivity.this)
          .load(catImage.getUrl())
          .into(ivCat);
    });

    viewModel.getIsError().observe(this, isError -> {
      if (isError) {
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.txt_error_loading,
            Snackbar.LENGTH_SHORT).show();
      }
    });

    btnNext.setOnClickListener(view -> {
      viewModel.loadCatImage();
    });
  }

  private void initViews() {
    ivCat = findViewById(R.id.ivCat);
    btnNext = findViewById(R.id.btnNext);
    progressBar = findViewById(R.id.progressBar);
  }

}