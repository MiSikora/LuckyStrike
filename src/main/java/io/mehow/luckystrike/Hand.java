package io.mehow.luckystrike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class Hand {
  private final List<Card> cards;
  private final Set<CardSequence> sequences;

  Hand(List<Card> cards) {
    this(cards, Collections.emptySet());
  }

  Hand(List<Card> cards, Set<CardSequence> sequences) {
    this.cards = new ArrayList<>(cards);
    this.sequences = new LinkedHashSet<>(sequences);
  }

  List<Card> getCards() {
    return new ArrayList<>(cards);
  }

  Set<CardSequence> getSequences() {
    return new LinkedHashSet<>(sequences);
  }

  Hand addSequence(CardSequence sequence) {
    sequences.add(sequence);
    return new Hand(cards, sequences);
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Hand hand = (Hand) o;
    return Objects.equals(cards, hand.cards)
        && Objects.equals(sequences, hand.sequences);
  }

  @Override public int hashCode() {
    return Objects.hash(cards, sequences);
  }

  @Override public String toString() {
    return "Hand{" +
        "cards=" + cards +
        ", sequences=" + sequences +
        '}';
  }
}
