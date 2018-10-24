package io.mehow.luckystrike;

import io.reactivex.Observable;
import java.util.Optional;
import javax.inject.Inject;

final class CreateDeckPresenter {
  private final DeckApi deckApi;

  @Inject CreateDeckPresenter(DeckApi deckApi) {
    this.deckApi = deckApi;
  }

  Observable<CreateDeckResult> accept(Observable<CreateDeckEvent> events) {
    return events.compose(RxUtil.ignoreWhileBusyMap(event -> deckApi.createDeck(event.deckCount)
        .map(result -> CreateDeckResult.success(result.deckId))
        .toObservable()
        .startWith(CreateDeckResult.pending())
        .onErrorReturnItem(CreateDeckResult.error("Network failure!"))
    )).startWith(CreateDeckResult.idle());
  }

  static class CreateDeckEvent {
    final int deckCount;

    CreateDeckEvent(int deckCount) {
      this.deckCount = deckCount;
    }
  }

  // Should use i.e. visitor pattern but would be too over-engineered for this sample.
  final static class CreateDeckResult {
    final Optional<String> deckId;
    final Optional<String> error;
    final boolean pending;

    private CreateDeckResult(String deckId, String error, boolean pending) {
      this.deckId = Optional.ofNullable(deckId);
      this.error = Optional.ofNullable(error);
      this.pending = pending;
    }

    static CreateDeckResult idle() {
      return new CreateDeckResult(null, null, false);
    }

    static CreateDeckResult pending() {
      return new CreateDeckResult(null, null, true);
    }

    static CreateDeckResult success(String deckId) {
      return new CreateDeckResult(deckId, null, false);
    }

    static CreateDeckResult error(String error) {
      return new CreateDeckResult(null, error, false);
    }
  }
}
