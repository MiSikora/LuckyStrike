package io.mehow.luckystrike.card;

import io.mehow.luckystrike.card.Card.Suit;
import java.util.HashMap;
import java.util.Map;

import static io.mehow.luckystrike.card.CardSequence.SUIT;

public final class SuitSequenceDetector implements CardSequenceDetector {
  private final int sufficientCount;

  public SuitSequenceDetector(int sufficientCount) {
    if (sufficientCount <= 0) {
      throw new IllegalArgumentException("sufficientCount must be positive!");
    }
    this.sufficientCount = sufficientCount;
  }

  @Override public Hand detectSequence(Hand hand) {
    Map<Suit, Integer> rankCountMap = new HashMap<>();
    hand.getCards().forEach(card -> incrementOccurrenceCount(rankCountMap, card.suit));

    boolean hasSequence = rankCountMap.values()
        .stream()
        .anyMatch(it -> it >= sufficientCount);

    if (hasSequence) {
      return hand.addSequence(SUIT);
    }
    return hand;
  }

  private void incrementOccurrenceCount(Map<Suit, Integer> map, Suit suit) {
    map.merge(suit, 1, (cached, inc) -> cached + inc);
  }
}
