package io.mehow.luckystrike.game.play;

import io.mehow.luckystrike.card.Hand;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

final class UiModel {
  final Hand hand;
  final Optional<String> error;

  static UiModel idle() {
    return new UiModel(new Hand(Collections.emptyList()), null);
  }

  private UiModel(Hand hand, String error) {
    this.hand = hand;
    this.error = Optional.ofNullable(error);
  }

  UiModel withError(String error) {
    return new UiModel(hand, error);
  }

  UiModel withHand(Hand hand) {
    return new UiModel(hand, null);
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UiModel that = (UiModel) o;
    return Objects.equals(hand, that.hand)
        && Objects.equals(error, that.error);
  }

  @Override public int hashCode() {
    return Objects.hash(hand, error);
  }

  @Override public String toString() {
    return "UiModel{" +
        "hand=" + hand +
        ", error=" + error +
        '}';
  }
}
