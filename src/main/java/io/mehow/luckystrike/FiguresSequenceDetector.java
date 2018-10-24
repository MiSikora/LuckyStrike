package io.mehow.luckystrike;

import static io.mehow.luckystrike.CardSequence.FIGURE;

final class FiguresSequenceDetector implements CardSequenceDetector {
  private final int sufficientCount;

  FiguresSequenceDetector(int sufficientCount) {
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

    if (figuresCount == sufficientCount) {
      return hand.addSequence(FIGURE);
    }
    return hand;
  }
}
