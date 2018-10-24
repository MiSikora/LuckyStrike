package io.mehow.luckystrike.game.start;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.jakewharton.rxbinding3.view.RxView;
import dagger.android.AndroidInjection;
import io.mehow.luckystrike.R;
import io.mehow.luckystrike.game.play.PlayGameActivity;
import io.mehow.luckystrike.util.RxUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class StartGameActivity extends Activity {
  @Inject StartGamePresenter presenter;

  private TextView deckCountLabel;
  private SeekBar deckCountSlider;
  private ProgressBar progressBar;

  private final CompositeDisposable disposables = new CompositeDisposable();

  @Override protected void onCreate(@Nullable Bundle inState) {
    super.onCreate(inState);
    AndroidInjection.inject(this);
    setContentView(R.layout.create_deck);
    deckCountLabel = findViewById(R.id.deck_count_label);
    deckCountSlider = findViewById(R.id.deck_count_slider);
    progressBar = findViewById(R.id.progress_bar);

    updateDeckCountProgress(deckCountSlider.getProgress());
    deckCountSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateDeckCountProgress(progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    Button createDeckButton = findViewById(R.id.create_deck_button);
    Observable<CreateDeckEvent> events = RxView.clicks(createDeckButton)
        .map(click -> new CreateDeckEvent(deckCount(deckCountSlider.getProgress())));

    disposables.add(events.compose(presenter::accept)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::render, RxUtil::rethrow));
  }

  private void render(UiModel uiModel) {
    progressBar.setVisibility(uiModel.pending ? VISIBLE : GONE);
    uiModel.deckId.ifPresent(deckId -> startActivity(PlayGameActivity.show(this, deckId)));
    uiModel.error.ifPresent(this::toast);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    disposables.clear();
  }

  private int deckCount(int sliderProgress) {
    int adjustment = deckCountSlider.getMax() / 4;
    return (adjustment + sliderProgress) / adjustment;
  }

  private void updateDeckCountProgress(int progress) {
    deckCountLabel.setText(getString(R.string.deck_count, deckCount(progress)));
  }

  private void toast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
