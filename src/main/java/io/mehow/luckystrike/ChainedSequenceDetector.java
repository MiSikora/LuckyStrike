package io.mehow.luckystrike;

import java.util.LinkedHashSet;
import java.util.Set;

final class ChainedSequenceDetector implements CardSequenceDetector {
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

  static class Builder {
    private Set<CardSequenceDetector> detectors = new LinkedHashSet<>();

    Builder add(CardSequenceDetector detector) {
      detectors.add(detector);
      return this;
    }

    ChainedSequenceDetector build() {
      return new ChainedSequenceDetector(this);
    }
  }
}
