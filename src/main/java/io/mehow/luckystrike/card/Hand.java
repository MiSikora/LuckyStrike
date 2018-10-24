package io.mehow.luckystrike.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Hand {
  private final List<Card> cards;
  private final Set<CardSequence> sequences;

  public Hand(List<Card> cards) {
    this(cards, Collections.emptySet());
  }

  private Hand(List<Card> cards, Set<CardSequence> sequences) {
    this.cards = new ArrayList<>(cards);
    this.sequences = new LinkedHashSet<>(sequences);
  }

  public List<Card> getCards() {
    return new ArrayList<>(cards);
  }

  public Set<CardSequence> getSequences() {
    return new LinkedHashSet<>(sequences);
  }

  public Hand addSequence(CardSequence sequence) {
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
