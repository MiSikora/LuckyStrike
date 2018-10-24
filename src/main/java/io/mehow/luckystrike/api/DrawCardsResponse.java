package io.mehow.luckystrike.api;

import io.mehow.luckystrike.card.Card;
import java.util.List;

public final class DrawCardsResponse {
  public final List<Card> cards;

  DrawCardsResponse(List<Card> cards) {
    this.cards = cards;
  }
}
