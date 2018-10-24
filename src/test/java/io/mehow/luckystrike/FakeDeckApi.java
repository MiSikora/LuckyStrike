package io.mehow.luckystrike;

import io.mehow.luckystrike.Card.Rank;
import io.mehow.luckystrike.Card.Suit;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

final class FakeDeckApi implements DeckApi {
  private static final AtomicInteger DECK_ID_GENERATOR = new AtomicInteger();

  private final HashMap<String, Deck> decks = new HashMap<>();
  private final AtomicBoolean enableErrors = new AtomicBoolean();

  String createDeckSync(int deckCount) {
    String deckId = String.valueOf(DECK_ID_GENERATOR.getAndIncrement());
    Deck deck = Deck.generateDeck(deckCount);
    decks.put(deckId, deck);
    return deckId;
  }

  List<Card> getDeck(String deckId) {
    return new ArrayList<>(decks.get(deckId).cards);
  }

  void enableErrors(boolean enable) {
    enableErrors.set(enable);
  }

  @Override public Single<ShuffleDeckResponse> createDeck(int deckCount) {
    if (enableErrors.get()) {
      return Single.error(new RuntimeException());
    }
    return Single.fromCallable(() -> {
      String deckId = createDeckSync(deckCount);
      return new ShuffleDeckResponse(deckId, decks.get(deckId).cards.size());
    });
  }

  @Override public Single<ShuffleDeckResponse> reshuffleDeck(String deckId) {
    if (enableErrors.get()) {
      return Single.error(new RuntimeException());
    }
    return Single.fromCallable(() -> {
      Deck deck = decks.get(deckId);
      Deck reshuffledDeck = Deck.generateDeck(deck.originalDeckCount);
      decks.put(deckId, reshuffledDeck);
      return new ShuffleDeckResponse(deckId, reshuffledDeck.cards.size());
    });
  }

  @Override public Single<DrawCardsResponse> draw(String deckId, int cardCount) {
    if (enableErrors.get()) {
      return Single.error(new RuntimeException());
    }
    return Single.fromCallable(() -> {
      List<Card> cards = decks.get(deckId).cards;
      List<Card> drawnCards;
      if (cardCount > cards.size()) {
        drawnCards = new ArrayList<>(cards);
      } else {
        drawnCards = new ArrayList<>(cards.subList(0, cardCount));
      }
      cards.removeAll(drawnCards);
      return new DrawCardsResponse(cards.size(), drawnCards);
    });
  }

  private static final class Deck {
    final List<Card> cards;
    final int originalDeckCount;

    Deck(List<Card> cards, int originalDeckCount) {
      this.cards = cards;
      this.originalDeckCount = originalDeckCount;
    }

    static Deck generateDeck(int deckCount) {
      List<Card> cards = new ArrayList<>(52 * deckCount);
      for (int i = 0; i < deckCount; i++) {
        for (Rank rank : Rank.values()) {
          for (Suit suit : Suit.values()) {
            cards.add(new Card(rank, suit, ""));
          }
        }
      }
      Collections.shuffle(cards);
      return new Deck(cards, deckCount);
    }
  }
}
