package io.mehow.luckystrike;

import io.reactivex.Single;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface DeckApi {

  @POST("api/deck/new/shuffle")
  Single<ShuffleDeckResponse> createDeck(@Query("deck_count") int deckCount);

  @POST("api/dec/{deckId}/shuffle")
  Single<ShuffleDeckResponse> reshuffleDeck(@Path("deckId") String deckId);

  @POST("api/dec/{deckId}/draw")
  Single<DrawCardsResponse> draw(@Path("deckId") String deckId, @Query("count") int cardCount);
}
