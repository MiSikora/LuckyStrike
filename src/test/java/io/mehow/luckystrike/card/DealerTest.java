package io.mehow.luckystrike.card;

import io.mehow.luckystrike.api.FakeDeckApi;
import io.mehow.luckystrike.card.Dealer.DrawResult;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class DealerTest {
  static final int DECK_COUNT = 1;
  static final int CARD_COUNT = DECK_COUNT * 52;

  Dealer dealer;
  FakeDeckApi deckApi;
  String deckId;

  @Before public void setUp() {
    deckApi = new FakeDeckApi();
    deckId = deckApi.createDeckSync(DECK_COUNT);
    dealer = new Dealer(deckId, deckApi);
  }

  @Test public void drawingCards() {
    List<Card> expected = new Hand(deckApi.getDeck(deckId)).getCards();

    dealer.draw(CARD_COUNT)
        .map(result -> result.hand.getCards())
        .test()
        .assertValue(expected);
  }

  @Test public void drawingWhenLowOnCards() {
    List<Card> expected = new Hand(deckApi.getDeck(deckId)).getCards();
    dealer.draw(CARD_COUNT).subscribe();

    assertThat(deckApi.getDeck(deckId)).isEmpty();

    List<Card> cards = dealer.draw(CARD_COUNT)
        .map(result -> result.hand.getCards())
        .blockingGet();

    assertThat(cards).containsOnlyElementsOf(expected);
  }

  @Test public void apiErrorsAreHandledGracefully() {
    deckApi.enableErrors(true);

    dealer.draw(CARD_COUNT)
        .test()
        .assertValue(DrawResult.error("Network failure!"));
  }

  @Test public void tooSmallCountAfterReshuffle() {
    dealer.draw(CARD_COUNT + 1)
        .test()
        .assertValue(DrawResult.error("¿Que passa?"));
  }
}
