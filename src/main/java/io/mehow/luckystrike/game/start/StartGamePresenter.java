package io.mehow.luckystrike.game.start;

import io.mehow.luckystrike.api.DeckApi;
import io.mehow.luckystrike.util.RxUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javax.inject.Inject;

final class StartGamePresenter {
  private final Subject<CreateDeckEvent> events = PublishSubject.create();
  private final Observable<UiModel> uiModels;

  @Inject StartGamePresenter(DeckApi deckApi) {
    uiModels = events
        .compose(RxUtil.ignoreWhileBusyMap(event -> deckApi.createDeck(event.deckCount)
            .map(result -> UiModel.success(result.deckId))
            .toObservable()
            .startWith(UiModel.pending())
            .onErrorReturnItem(UiModel.error("Network failure!"))))
        .startWith(UiModel.idle())
        .replay(1)
        .autoConnect(0);
  }

  Disposable accept(Observable<CreateDeckEvent> events) {
    return events.subscribe(this.events::onNext);
  }

  Observable<UiModel> uiModels() {
    return uiModels;
  }
}
