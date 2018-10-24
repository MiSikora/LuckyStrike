package io.mehow.luckystrike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class Hand {
  private final List<Card> cards;
  private final Set<CardSequence> sequences;

  public Hand(List<Card> cards) {
    this(cards, Collections.emptySet());
  }

  Hand(List<Card> cards, Set<CardSequence> sequences) {
    this.cards = new ArrayList<>(cards);
    this.sequences = new LinkedHashSet<>(sequences);
  }

  public List<Card> getCards() {
    return new ArrayList<>(cards);
  }

  public Set<CardSequence> getSequences() {
    return new LinkedHashSet<>(sequences);
  }

  Hand addSequence(CardSequence sequence) {
    sequences.add(sequence);
    return new Hand(cards, sequences);
  }
}
