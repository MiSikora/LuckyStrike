package io.mehow.luckystrike;

import com.squareup.moshi.Json;

final class Card {
  @Json(name = "value") final Rank rank;
  final Suit suit;
  @Json(name = "image") final String imageUrl;

  Card(Rank rank, Suit suit, String imageUrl) {
    this.rank = rank;
    this.suit = suit;
    this.imageUrl = imageUrl;
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
