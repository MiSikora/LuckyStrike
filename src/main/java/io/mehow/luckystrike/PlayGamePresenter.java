package io.mehow.luckystrike;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;

final class PlayGamePresenter {
  private final Dealer dealer;
  private final CardSequenceDetector detector;

  @Inject PlayGamePresenter(Dealer dealer, CardSequenceDetector detector) {
    this.dealer = dealer;
    this.detector = detector;
  }

  Observable<DrawUiModel> accept(Observable<DrawEvent> events) {
    return events.switchMap(event -> dealer.draw(event.count)
        .map(result -> processDrawResult(result, event.count))
        .toObservable()
        .onErrorReturnItem(DrawResult.error("Network failure!")))
        .scan(DrawUiModel.idle(), this::mapResultToModel);
  }

  private DrawResult processDrawResult(Hand hand, int minCardCount) {
    if (hand.getCards().size() < minCardCount) {
      return DrawResult.error("¿Que passa?");
    } else {
      Hand analyzedHand = detector.detectSequence(hand);
      return DrawResult.success(analyzedHand);
    }
  }

  private DrawUiModel mapResultToModel(DrawUiModel uiModel, DrawResult result) {
    if (result.hand != null) {
      return uiModel.withHand(result.hand);
    } else if (result.error != null) {
      return uiModel.withError(result.error);
    } else {
      throw new IllegalStateException("Draw result must have one value!");
    }
  }

  static final class DrawEvent {
    final int count;

    DrawEvent(int count) {
      this.count = count;
    }
  }

  // Should use i.e. visitor pattern but would be too over-engineered for this sample.
  private static final class DrawResult {
    @Nullable final Hand hand;
    @Nullable final String error;

    private DrawResult(Hand hand, String error) {
      this.hand = hand;
      this.error = error;
    }

    static DrawResult success(Hand hand) {
      return new DrawResult(hand, null);
    }

    static DrawResult error(String error) {
      return new DrawResult(null, error);
    }
  }

  static final class DrawUiModel {
    final Hand hand;
    final Optional<String> error;

    private DrawUiModel(Hand hand, String error) {
      this.hand = hand;
      this.error = Optional.ofNullable(error);
    }

    static DrawUiModel idle() {
      return new DrawUiModel(new Hand(Collections.emptyList()), null);
    }

    DrawUiModel withError(String error) {
      return new DrawUiModel(hand, error);
    }

    DrawUiModel withHand(Hand hand) {
      return new DrawUiModel(hand, null);
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      DrawUiModel that = (DrawUiModel) o;
      return Objects.equals(hand, that.hand)
          && Objects.equals(error, that.error);
    }

    @Override public int hashCode() {
      return Objects.hash(hand, error);
    }

    @Override public String toString() {
      return "DrawUiModel{" +
          "hand=" + hand +
          ", error=" + error +
          '}';
    }
  }
}
