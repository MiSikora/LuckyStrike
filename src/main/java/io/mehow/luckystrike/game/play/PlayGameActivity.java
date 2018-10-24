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
import com.jakewharton.rxbinding3.view.RxView;
import dagger.android.AndroidInjection;
import io.mehow.luckystrike.R;
import io.mehow.luckystrike.card.CardSequence;
import io.mehow.luckystrike.util.RxUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.Set;
import javax.inject.Inject;

public final class PlayGameActivity extends Activity {
  private static final int DRAW_COUNT = 5;
  static final String DECK_ID_KEY = "DECK_ID_KEY";

  @Inject PlayGamePresenter presenter;

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
    setContentView(R.layout.play_game);

    drawAnalysisInfo = findViewById(R.id.draw_analysis_info);
    adapter = new PlayGameAdapter(getLayoutInflater());
    RecyclerView cardsRecycler = findViewById(R.id.cards_recycler);
    cardsRecycler.setAdapter(adapter);
    cardsRecycler.setLayoutManager(new GridLayoutManager(this, 3));

    Button createDeckButton = findViewById(R.id.draw_cards_button);
    Observable<DrawEvent> events = RxView.clicks(createDeckButton)
        .map(click -> new DrawEvent(DRAW_COUNT));

    disposables.add(events.compose(presenter::accept)
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
    uiModel.error.ifPresent(this::toast);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    disposables.clear();
  }

  private void toast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
