package io.mehow.luckystrike.game.play;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import com.jakewharton.rxbinding3.view.RxView;
import dagger.android.AndroidInjection;
import io.mehow.luckystrike.R;
import io.mehow.luckystrike.card.CardSequence;
import io.mehow.luckystrike.game.start.StartGameActivity;
import io.mehow.luckystrike.util.RxUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public final class PlayGameActivity extends Activity {
  private static final int DRAW_COUNT = 5;
  static final String DECK_ID_KEY = "DECK_ID_KEY";

  @Inject Provider<PlayGamePresenter> presenterProvider;
  private PlayGamePresenter presenter;

  private TextView drawAnalysisInfo;
  private PlayGameAdapter adapter;

  private final CompositeDisposable disposables = new CompositeDisposable();

  public static Intent show(Context context, String deckId) {
    Intent show = new Intent(context, PlayGameActivity.class);
    show.putExtra(DECK_ID_KEY, deckId);
    return show;
  }

  @Override protected void onCreate(@Nullable Bundle inState) {
    super.onCreate(inState);
    AndroidInjection.inject(this);
    Object object = getLastNonConfigurationInstance();
    if (object == null) {
      presenter = presenterProvider.get();
    } else {
      presenter = (PlayGamePresenter) object;
    }

    setContentView(R.layout.play_game);
    drawAnalysisInfo = findViewById(R.id.draw_analysis_info);
    adapter = new PlayGameAdapter(getLayoutInflater());
    RecyclerView cardsRecycler = findViewById(R.id.cards_recycler);
    cardsRecycler.setAdapter(adapter);
    cardsRecycler.setLayoutManager(layoutManager());

    Button createDeckButton = findViewById(R.id.draw_cards_button);
    Observable<DrawEvent> events = RxView.clicks(createDeckButton)
        .map(click -> new DrawEvent(DRAW_COUNT));

    disposables.add(presenter.accept(events));
    disposables.add(presenter.uiModels()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::render, RxUtil::rethrow));
  }

  private void render(UiModel uiModel) {
    Set<CardSequence> sequences = uiModel.hand.getSequences();
    if (sequences.isEmpty()) {
      drawAnalysisInfo.setText(null);
    } else {
      drawAnalysisInfo.setText(getString(R.string.draw_analysis, sequences));
    }
    adapter.updateCards(uiModel.hand.getCards());
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

  @Override public void onBackPressed() {
    startActivity(new Intent(this, StartGameActivity.class));
    finish();
  }

  private LayoutManager layoutManager() {
    if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
      return new GridLayoutManager(this, 2, HORIZONTAL, false);
    }
    return new GridLayoutManager(this, 3);
  }

  private void toast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
