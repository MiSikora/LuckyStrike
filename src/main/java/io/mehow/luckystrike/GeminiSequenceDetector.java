package io.mehow.luckystrike;

import io.mehow.luckystrike.Card.Rank;
import java.util.HashMap;
import java.util.Map;

import static io.mehow.luckystrike.CardSequence.GEMINI;

final class GeminiSequenceDetector implements CardSequenceDetector {
  private final int sufficientCount;

  GeminiSequenceDetector(int sufficientCount) {
    if (sufficientCount <= 0) {
      throw new IllegalArgumentException("sufficientCount must be positive!");
    }
    this.sufficientCount = sufficientCount;
  }

  @Override public Hand detectSequence(Hand hand) {
    Map<Rank, Integer> rankCountMap = new HashMap<>();
    hand.getCards().forEach(card -> incrementOccurrenceCount(rankCountMap, card.rank));

    return rankCountMap.values()
        .stream()
        .filter(it -> it >= sufficientCount)
        .findAny()
        .map(it -> hand.addSequence(GEMINI))
        .orElse(hand);
  }

  private void incrementOccurrenceCount(Map<Rank, Integer> map, Rank rank) {
    map.merge(rank, 1, (cached, inc) -> cached + inc);
  }
}