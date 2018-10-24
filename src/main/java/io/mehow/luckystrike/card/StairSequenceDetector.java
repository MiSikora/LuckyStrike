package io.mehow.luckystrike.card;

import io.mehow.luckystrike.card.Card.Rank;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

import static io.mehow.luckystrike.card.CardSequence.STAIR;

public final class StairSequenceDetector implements CardSequenceDetector {
  private final int sufficientCount;

  public StairSequenceDetector(int sufficientCount) {
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
    return filterByPair(cards, Rank::isNext);
  }

  private boolean hasDescendingSequence(List<Card> cards) {
    return filterByPair(cards, Rank::isPrevious);
  }

  private boolean filterByPair(List<Card> cards, BiPredicate<Rank, Rank> predicate) {
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
