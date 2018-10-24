package io.mehow.luckystrike;

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
        .map(Hand::getCards)
        .test()
        .assertValue(expected);
  }

  @Test
  public void drawingWhenLowOnCards() {
    List<Card> expected = new Hand(deckApi.getDeck(deckId)).getCards();
    dealer.draw(CARD_COUNT).subscribe();

    assertThat(deckApi.getDeck(deckId)).isEmpty();

    dealer.draw(CARD_COUNT)
        .map(Hand::getCards)
        .test()
        .assertValue(cards -> {
          assertThat(cards).containsOnlyElementsOf(expected);
          return true;
        });
  }
}
