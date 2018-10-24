package io.mehow.luckystrike.game.play;

import io.mehow.luckystrike.card.CardSequenceDetector;
import io.mehow.luckystrike.card.Dealer;
import io.mehow.luckystrike.card.Dealer.DrawResult;
import io.mehow.luckystrike.card.Hand;
import io.reactivex.Observable;
import javax.inject.Inject;

final class PlayGamePresenter {
  private final Dealer dealer;
  private final CardSequenceDetector detector;

  @Inject PlayGamePresenter(Dealer dealer, CardSequenceDetector detector) {
    this.dealer = dealer;
    this.detector = detector;
  }

  Observable<UiModel> accept(Observable<DrawEvent> events) {
    return events.switchMap(event -> dealer.draw(event.count)
        .map(this::analyzeSequences)
        .toObservable())
        .scan(UiModel.idle(), this::mapResultToModel);
  }

  private DrawResult analyzeSequences(DrawResult result) {
    if (result.hand != null) {
      Hand analyzedHand = detector.detectSequence(result.hand);
      return DrawResult.success(analyzedHand);
    }
    return result;
  }

  private UiModel mapResultToModel(UiModel uiModel, DrawResult result) {
    if (result.hand != null) {
      return uiModel.withHand(result.hand);
    } else if (result.error != null) {
      return uiModel.withError(result.error);
    } else {
      throw new IllegalStateException("Draw result must have one value!");
    }
  }
}
