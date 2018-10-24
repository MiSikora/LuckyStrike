package io.mehow.luckystrike;

import io.mehow.luckystrike.Card.Rank;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

import static io.mehow.luckystrike.CardSequence.STAIR;

final class StairSequenceDetector implements CardSequenceDetector {
  private final int sufficientCount;

  StairSequenceDetector(int sufficientCount) {
    if (sufficientCount <= 0) {
      throw new IllegalArgumentException("sufficientCount must be positive!");
    }
    this.sufficientCount = sufficientCount;
  }

  @Override public Hand detectSequence(Hand hand) {
    List<Card> cards = hand.getCards();
    if (hasAscendingSequence(cards) || hasDescendingSequence(cards)) {
      return hand.addSequence(STAIR);
    }
    return hand;
  }

  private boolean hasAscendingSequence(List<Card> cards) {
    return pairIterate(cards, Rank::isNext);
  }

  private boolean hasDescendingSequence(List<Card> cards) {
    return pairIterate(cards, Rank::isPrevious);
  }

  private boolean pairIterate(List<Card> cards, BiPredicate<Rank, Rank> predicate) {
    Iterator<Card> iterator = cards.iterator();
    Rank currentRank = iterator.next().rank;
    int matchCount = 1;
    while (iterator.hasNext()) {
      Rank nextRank = iterator.next().rank;
      if (predicate.test(currentRank, nextRank)) {
        matchCount++;
      } else {
        matchCount = 1;
      }
      if (matchCount == sufficientCount) {
        break;
      }
      currentRank = nextRank;
    }
    return matchCount == sufficientCount;
  }
}
