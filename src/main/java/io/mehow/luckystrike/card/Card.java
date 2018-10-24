package io.mehow.luckystrike.card;

import com.squareup.moshi.Json;
import java.util.Objects;

public final class Card {
  @Json(name = "value") public final Rank rank;
  public final Suit suit;
  @Json(name = "image") public final String imageUrl;

  public Card(Rank rank, Suit suit, String imageUrl) {
    this.rank = rank;
    this.suit = suit;
    this.imageUrl = imageUrl;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Card card = (Card) o;
    return rank == card.rank
        && suit == card.suit
        && Objects.equals(imageUrl, card.imageUrl);
  }

  @Override public int hashCode() {
    return Objects.hash(rank, suit, imageUrl);
  }

  @Override public String toString() {
    return "Card{" +
        "rank=" + rank +
        ", suit=" + suit +
        ", imageUrl='" + imageUrl + '\'' +
        '}';
  }

  public enum Rank {
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

    public final int value;
    public final boolean isFigure;

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

  public enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;
  }
}
