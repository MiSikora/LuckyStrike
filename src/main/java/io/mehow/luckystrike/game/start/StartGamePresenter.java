package io.mehow.luckystrike.game.start;

import io.mehow.luckystrike.api.DeckApi;
import io.mehow.luckystrike.util.RxUtil;
import io.reactivex.Observable;
import javax.inject.Inject;

final class StartGamePresenter {
  private final DeckApi deckApi;

  @Inject StartGamePresenter(DeckApi deckApi) {
    this.deckApi = deckApi;
  }

  Observable<UiModel> accept(Observable<CreateDeckEvent> events) {
    return events.compose(RxUtil.ignoreWhileBusyMap(event -> deckApi.createDeck(event.deckCount)
        .map(result -> UiModel.success(result.deckId))
        .toObservable()
        .startWith(UiModel.pending())
        .onErrorReturnItem(UiModel.error("Network failure!"))
    )).startWith(UiModel.idle());
  }
}
