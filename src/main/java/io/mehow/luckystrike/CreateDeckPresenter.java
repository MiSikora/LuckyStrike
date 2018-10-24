package io.mehow.luckystrike;

import io.reactivex.Observable;
import java.util.Optional;
import javax.inject.Inject;

final class CreateDeckPresenter {
  private final DeckApi deckApi;

  @Inject CreateDeckPresenter(DeckApi deckApi) {
    this.deckApi = deckApi;
  }

  Observable<CreateDeckUiModel> accept(Observable<CreateDeckEvent> events) {
    return events.compose(RxUtil.ignoreWhileBusyMap(event -> deckApi.createDeck(event.deckCount)
        .map(result -> CreateDeckUiModel.success(result.deckId))
        .toObservable()
        .startWith(CreateDeckUiModel.pending())
        .onErrorReturnItem(CreateDeckUiModel.error("Network failure!"))
    )).startWith(CreateDeckUiModel.idle());
  }

  static class CreateDeckEvent {
    final int deckCount;

    CreateDeckEvent(int deckCount) {
      this.deckCount = deckCount;
    }
  }

  // Should use i.e. visitor pattern but would be too over-engineered for this sample.
  final static class CreateDeckUiModel {
    final Optional<String> deckId;
    final Optional<String> error;
    final boolean pending;

    private CreateDeckUiModel(String deckId, String error, boolean pending) {
      this.deckId = Optional.ofNullable(deckId);
      this.error = Optional.ofNullable(error);
      this.pending = pending;
    }

    static CreateDeckUiModel idle() {
      return new CreateDeckUiModel(null, null, false);
    }

    static CreateDeckUiModel pending() {
      return new CreateDeckUiModel(null, null, true);
    }

    static CreateDeckUiModel success(String deckId) {
      return new CreateDeckUiModel(deckId, null, false);
    }

    static CreateDeckUiModel error(String error) {
      return new CreateDeckUiModel(null, error, false);
    }
  }
}
