package io.mehow.luckystrike.card;

import java.util.LinkedHashSet;
import java.util.Set;

public final class ChainedSequenceDetector implements CardSequenceDetector {
  private final Set<CardSequenceDetector> detectors;

  ChainedSequenceDetector(Builder builder) {
    this.detectors = new LinkedHashSet<>(builder.detectors);
  }

  @Override public Hand detectSequence(Hand hand) {
    Hand result = hand;
    for (CardSequenceDetector detector : detectors) {
      result = detector.detectSequence(hand);
    }
    return result;
  }

  public static class Builder {
    private Set<CardSequenceDetector> detectors = new LinkedHashSet<>();

    public Builder add(CardSequenceDetector detector) {
      detectors.add(detector);
      return this;
    }

    public ChainedSequenceDetector build() {
      return new ChainedSequenceDetector(this);
    }
  }
}
