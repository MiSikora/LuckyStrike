package io.mehow.luckystrike;

import java.util.List;

final class DrawCardsResponse {
  final int remaining;
  final List<Card> cards;

  DrawCardsResponse(int remaining, List<Card> cards) {
    this.remaining = remaining;
    this.cards = cards;
  }
}
