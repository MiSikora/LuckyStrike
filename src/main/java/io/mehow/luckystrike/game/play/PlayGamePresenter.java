package io.mehow.luckystrike.game.play;

import io.mehow.luckystrike.card.CardSequenceDetector;
import io.mehow.luckystrike.card.Dealer;
import io.mehow.luckystrike.card.Dealer.DrawResult;
import io.mehow.luckystrike.card.Hand;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javax.inject.Inject;

final class PlayGamePresenter {
  private final Subject<DrawEvent> events = PublishSubject.create();
  private final Observable<UiModel> uiModels;

  @Inject PlayGamePresenter(Dealer dealer, CardSequenceDetector detector) {
    uiModels = events.switchMap(event -> dealer.draw(event.count)
        .map(result -> analyzeSequences(result, detector))
        .toObservable())
        .scan(UiModel.idle(), this::mapResultToModel)
        .replay(1)
        .autoConnect(0);
  }

  Disposable accept(Observable<DrawEvent> events) {
    return events.subscribe(this.events::onNext);
  }

  Observable<UiModel> uiModels() {
    return uiModels;
  }

  private DrawResult analyzeSequences(DrawResult result, CardSequenceDetector detector) {
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
