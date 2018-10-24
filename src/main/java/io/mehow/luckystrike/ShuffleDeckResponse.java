package io.mehow.luckystrike;

import com.squareup.moshi.Json;

final class ShuffleDeckResponse {
  @Json(name = "deck_id") final String deckId;
  final int remaining;

  ShuffleDeckResponse(String deckId, int remaining) {
    this.deckId = deckId;
    this.remaining = remaining;
  }
}
