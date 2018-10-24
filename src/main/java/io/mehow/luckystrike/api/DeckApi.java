package io.mehow.luckystrike.api;

import io.reactivex.Single;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeckApi {

  @POST("api/deck/new/shuffle")
  Single<ShuffleDeckResponse> createDeck(@Query("deck_count") int deckCount);

  @POST("api/deck/{deckId}/shuffle")
  Single<ShuffleDeckResponse> reshuffleDeck(@Path("deckId") String deckId);

  @POST("api/deck/{deckId}/draw")
  Single<DrawCardsResponse> draw(@Path("deckId") String deckId, @Query("count") int cardCount);
}
