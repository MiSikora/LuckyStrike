package io.mehow.luckystrike.card;

import io.mehow.luckystrike.card.Card.Rank;
import java.util.HashMap;
import java.util.Map;

import static io.mehow.luckystrike.card.CardSequence.GEMINI;

public final class GeminiSequenceDetector implements CardSequenceDetector {
  private final int sufficientCount;

  public GeminiSequenceDetector(int sufficientCount) {
    if (sufficientCount <= 0) {
      throw new IllegalArgumentException("sufficientCount must be positive!");
    }
    this.sufficientCount = sufficientCount;
  }

  @Override public Hand detectSequence(Hand hand) {
    Map<Rank, Integer> rankCountMap = new HashMap<>();
    hand.getCards().forEach(card -> incrementOccurrenceCount(rankCountMap, card.rank));

    boolean hasSequence = rankCountMap.values()
        .stream()
        .anyMatch(it -> it >= sufficientCount);

    if (hasSequence) {
      return hand.addSequence(GEMINI);
    }
    return hand;
  }

  private void incrementOccurrenceCount(Map<Rank, Integer> map, Rank rank) {
    map.merge(rank, 1, (cached, inc) -> cached + inc);
  }
}
