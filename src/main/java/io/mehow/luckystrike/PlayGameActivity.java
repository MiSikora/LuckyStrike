package io.mehow.luckystrike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public final class PlayGameActivity extends Activity {
  static final String DECK_ID_KEY = "DECK_ID_KEY";

  @Inject PlayGamePresenter presenter;

  @Override protected void onCreate(@Nullable Bundle inState) {
    super.onCreate(inState);
    AndroidInjection.inject(this);
    setContentView(R.layout.play_game);
  }

  static Intent show(Context context, String deckId) {
    Intent show = new Intent(context, PlayGameActivity.class);
    show.putExtra(DECK_ID_KEY, deckId);
    return show;
  }

  @Module abstract static class PlayGameModule {

    @Provides static String provideDeckId(PlayGameActivity activity) {
      return activity.getIntent().getStringExtra(DECK_ID_KEY);
    }

    @Provides static CardSequenceDetector provideCardSequenceDetector() {
      int sequenceMatch = 3;
      return new ChainedSequenceDetector.Builder()
          .add(new SuitSequenceDetector(sequenceMatch))
          .add(new FigureSequenceDetector(sequenceMatch))
          .add(new GeminiSequenceDetector(sequenceMatch))
          .add(new StairSequenceDetector(sequenceMatch))
          .build();
    }
  }
}
