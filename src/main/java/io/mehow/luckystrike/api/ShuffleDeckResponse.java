package io.mehow.luckystrike.api;

import com.squareup.moshi.Json;

public final class ShuffleDeckResponse {
  @Json(name = "deck_id") public final String deckId;

  ShuffleDeckResponse(String deckId) {
    this.deckId = deckId;
  }
}
