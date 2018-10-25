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
import com.jakewharton.rxbinding3.widget.RxSeekBar;
import dagger.android.AndroidInjection;
import io.mehow.luckystrike.R;
import io.mehow.luckystrike.game.play.PlayGameActivity;
import io.mehow.luckystrike.util.RxUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observables.ConnectableObservable;
import javax.inject.Inject;
import javax.inject.Provider;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class StartGameActivity extends Activity {
  @Inject Provider<StartGamePresenter> presenterProvider;
  private StartGamePresenter presenter;

  private TextView deckCountLabel;
  private SeekBar deckCountSlider;
  private ProgressBar progressBar;

  private final CompositeDisposable disposables = new CompositeDisposable();

  @Override protected void onCreate(@Nullable Bundle inState) {
    super.onCreate(inState);
    AndroidInjection.inject(this);
    Object object = getLastNonConfigurationInstance();
    if (object == null) {
      presenter = presenterProvider.get();
    } else {
      presenter = (StartGamePresenter) object;
    }

    setContentView(R.layout.create_deck);
    deckCountLabel = findViewById(R.id.deck_count_label);
    deckCountSlider = findViewById(R.id.deck_count_slider);
    progressBar = findViewById(R.id.progress_bar);
    Button createDeckButton = findViewById(R.id.create_deck_button);

    ConnectableObservable<Integer> deckCounts = RxSeekBar.changes(deckCountSlider)
        .map(this::deckCount)
        .publish();
    disposables.add(deckCounts.subscribe(this::updateDeckCountProgress, RxUtil::rethrow));

    Observable<CreateDeckEvent> events = RxView.clicks(createDeckButton)
        .withLatestFrom(deckCounts, (click, count) -> new CreateDeckEvent(count));
    disposables.add(presenter.accept(events));
    disposables.add(presenter.uiModels()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::render, RxUtil::rethrow));
    deckCounts.connect();
  }

  private void render(UiModel uiModel) {
    progressBar.setVisibility(uiModel.pending ? VISIBLE : GONE);
    uiModel.deckId.ifPresent(deckId -> {
      startActivity(PlayGameActivity.show(this, deckId));
      finish();
    });
    // Toast is good enough for this sample.
    uiModel.error.ifPresent(this::toast);
  }

  @Override public Object onRetainNonConfigurationInstance() {
    return presenter;
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
    deckCountLabel.setText(getString(R.string.deck_count, progress));
  }

  private void toast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
