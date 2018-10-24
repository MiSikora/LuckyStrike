package io.mehow.luckystrike;

import io.reactivex.Single;
import java.util.List;

final class Dealer {
  private final String deckId;
  private final DeckApi deckApi;

  Dealer(String deckId, DeckApi deckApi) {
    this.deckId = deckId;
    this.deckApi = deckApi;
  }

  Single<Hand> draw(int count) {
    return deckApi.draw(deckId, count)
        .flatMap(response -> redrawIfSmallCount(response.cards, count))
        .map(Hand::new);
  }

  private Single<List<Card>> redrawIfSmallCount(List<Card> cards, int count) {
    if (cards.size() < count) {
      return deckApi.reshuffleDeck(deckId)
          .flatMap(response -> deckApi.draw(deckId, count))
          .map(response -> response.cards);
    }
    return Single.just(cards);
  }
}
