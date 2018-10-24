package io.mehow.luckystrike;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import dagger.android.AndroidInjection;
import io.mehow.luckystrike.CreateDeckPresenter.CreateDeckEvent;
import io.mehow.luckystrike.CreateDeckPresenter.CreateDeckResult;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Inject;

public final class CreateDeckActivity extends Activity {
  @Inject CreateDeckPresenter presenter;

  private TextView deckCountLabel;
  private SeekBar deckCountSlider;

  private final CompositeDisposable disposables = new CompositeDisposable();

  @Override protected void onCreate(@Nullable Bundle inState) {
    super.onCreate(inState);
    AndroidInjection.inject(this);
    setContentView(R.layout.create_deck);
    deckCountLabel = findViewById(R.id.deck_count_label);
    deckCountSlider = findViewById(R.id.deck_count_slider);

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
    Observable<CreateDeckEvent> events = RxUtil.viewClicks(createDeckButton)
        .map(click -> new CreateDeckEvent(deckCount(deckCountSlider.getProgress())));

    disposables.add(events.compose(presenter::accept)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::consumeResult, RxUtil::rethrow));
  }

  private void consumeResult(CreateDeckResult result) {
    if (result.deckId != null) {
      // TODO: Navigate to another Activity.
      Toast.makeText(this, result.deckId, Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show();
    }
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
}
