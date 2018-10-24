package io.mehow.luckystrike.card;

import androidx.annotation.Nullable;
import io.mehow.luckystrike.api.DeckApi;
import io.reactivex.Single;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

public final class Dealer {
  private final String deckId;
  private final DeckApi deckApi;

  @Inject public Dealer(String deckId, DeckApi deckApi) {
    this.deckId = deckId;
    this.deckApi = deckApi;
  }

  public Single<DrawResult> draw(int count) {
    return deckApi.draw(deckId, count)
        .flatMap(response -> redrawIfSmallCount(response.cards, count))
        .map(cards -> processDrawnCards(cards, count))
        .onErrorReturnItem(DrawResult.error("Network failure!"));
  }

  private Single<List<Card>> redrawIfSmallCount(List<Card> cards, int minCount) {
    if (cards.size() < minCount) {
      return deckApi.reshuffleDeck(deckId)
          .flatMap(response -> deckApi.draw(deckId, minCount))
          .map(response -> response.cards);
    }
    return Single.just(cards);
  }

  private DrawResult processDrawnCards(List<Card> cards, int minCount) {
    Hand hand = new Hand(cards);
    if (hand.getCards().size() < minCount) {
      return DrawResult.error("¿Que passa?");
    } else {
      return DrawResult.success(hand);
    }
  }

  // Should use i.e. visitor pattern but would be too over-engineered for this sample.
  public static final class DrawResult {
    @Nullable public final Hand hand;
    @Nullable public final String error;

    public static DrawResult success(Hand hand) {
      return new DrawResult(hand, null);
    }

    public static DrawResult error(String error) {
      return new DrawResult(null, error);
    }

    private DrawResult(Hand hand, String error) {
      this.hand = hand;
      this.error = error;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      DrawResult that = (DrawResult) o;
      return Objects.equals(hand, that.hand)
          && Objects.equals(error, that.error);
    }

    @Override public int hashCode() {
      return Objects.hash(hand, error);
    }

    @Override public String toString() {
      return "DrawResult{" +
          "hand=" + hand +
          ", error='" + error + '\'' +
          '}';
    }
  }
}
