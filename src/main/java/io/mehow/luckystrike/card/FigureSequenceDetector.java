package io.mehow.luckystrike.card;

import static io.mehow.luckystrike.card.CardSequence.FIGURE;

public final class FigureSequenceDetector implements CardSequenceDetector {
  private final int sufficientCount;

  public FigureSequenceDetector(int sufficientCount) {
    if (sufficientCount <= 0) {
      throw new IllegalArgumentException("sufficientCount must be positive!");
    }
    this.sufficientCount = sufficientCount;
  }

  @Override public Hand detectSequence(Hand hand) {
    long figuresCount = hand.getCards()
        .stream()
        .filter(it -> it.rank.isFigure)
        .count();

    if (figuresCount >= sufficientCount) {
      return hand.addSequence(FIGURE);
    }
    return hand;
  }
}
