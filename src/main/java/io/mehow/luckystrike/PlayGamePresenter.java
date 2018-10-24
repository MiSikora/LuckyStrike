package io.mehow.luckystrike;

import javax.inject.Inject;

final class PlayGamePresenter {
  private final Dealer dealer;
  private final CardSequenceDetector detector;

  @Inject PlayGamePresenter(Dealer dealer, CardSequenceDetector detector) {
    this.dealer = dealer;
    this.detector = detector;
  }
}
