package io.mehow.luckystrike.game.play;

import dagger.Module;
import dagger.Provides;
import io.mehow.luckystrike.card.CardSequenceDetector;
import io.mehow.luckystrike.card.ChainedSequenceDetector;
import io.mehow.luckystrike.card.FigureSequenceDetector;
import io.mehow.luckystrike.card.GeminiSequenceDetector;
import io.mehow.luckystrike.card.StairSequenceDetector;
import io.mehow.luckystrike.card.SuitSequenceDetector;

@Module public abstract class PlayGameModule {

  @Provides static String provideDeckId(PlayGameActivity activity) {
    return activity.getIntent().getStringExtra(PlayGameActivity.DECK_ID_KEY);
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
