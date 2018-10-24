package io.mehow.luckystrike;

final class Card {
  final Rank rank;
  final Suit suit;

  Card(Rank rank, Suit suit) {
    this.rank = rank;
    this.suit = suit;
  }

  enum Rank {
    ACE(1, true),
    TWO(2, false),
    THREE(3, false),
    FOUR(4, false),
    FIVE(5, false),
    SIX(6, false),
    SEVEN(7, false),
    EIGHT(8, false),
    NINE(9, false),
    TEN(10, false),
    JACK(11, true),
    QUEEN(12, true),
    KING(13, true);

    final int value;
    final boolean isFigure;

    Rank(int value, boolean isFigure) {
      this.value = value;
      this.isFigure = isFigure;
    }

    boolean isNext(Rank rank) {
      return this.value + 1 == rank.value;
    }

    boolean isPrevious(Rank rank) {
      return this.value - 1 == rank.value;
    }
  }

  enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;
  }
}
