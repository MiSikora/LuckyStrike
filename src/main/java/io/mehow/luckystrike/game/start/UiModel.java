package io.mehow.luckystrike.game.start;

import java.util.Optional;

// Should use i.e. visitor pattern but would be too over-engineered for this sample.
final class UiModel {
  final Optional<String> deckId;
  final Optional<String> error;
  final boolean pending;

  private UiModel(String deckId, String error, boolean pending) {
    this.deckId = Optional.ofNullable(deckId);
    this.error = Optional.ofNullable(error);
    this.pending = pending;
  }

  static UiModel idle() {
    return new UiModel(null, null, false);
  }

  static UiModel pending() {
    return new UiModel(null, null, true);
  }

  static UiModel success(String deckId) {
    return new UiModel(deckId, null, false);
  }

  static UiModel error(String error) {
    return new UiModel(null, error, false);
  }
}
