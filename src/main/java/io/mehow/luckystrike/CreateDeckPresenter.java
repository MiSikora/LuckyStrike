package io.mehow.luckystrike;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import javax.inject.Inject;

final class CreateDeckPresenter {
  private final DeckApi deckApi;

  @Inject CreateDeckPresenter(DeckApi deckApi) {
    this.deckApi = deckApi;
  }

  Observable<CreateDeckResult> accept(Observable<CreateDeckEvent> events) {
    return events.compose(RxUtil.ignoreWhileBusyMap(event -> deckApi.createDeck(event.deckCount)
        .map(result -> CreateDeckResult.success(result.deckId))
        .onErrorReturnItem(CreateDeckResult.error("Network failure!"))
        .toObservable()
    ));
  }

  static class CreateDeckEvent {
    final int deckCount;

    CreateDeckEvent(int deckCount) {
      this.deckCount = deckCount;
    }
  }

  // Should use i.e. visitor pattern but would be too over-engineered for this sample.
  final static class CreateDeckResult {
    @Nullable final String deckId;
    @Nullable final String error;

    private CreateDeckResult(String deckId, String error) {
      this.deckId = deckId;
      this.error = error;
    }

    static CreateDeckResult success(String deckId) {
      return new CreateDeckResult(deckId, null);
    }

    static CreateDeckResult error(String error) {
      return new CreateDeckResult(null, error);
    }
  }
}
