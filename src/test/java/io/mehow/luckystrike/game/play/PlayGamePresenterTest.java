package io.mehow.luckystrike.game.play;

import io.mehow.luckystrike.api.FakeDeckApi;
import io.mehow.luckystrike.card.Card;
import io.mehow.luckystrike.card.Dealer;
import io.mehow.luckystrike.card.Hand;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static io.mehow.luckystrike.card.CardSequence.GEMINI;

public final class PlayGamePresenterTest {
  static final UiModel IDLE_MODEL = UiModel.idle();

  TestObserver<UiModel> uiModels;
  Subject<DrawEvent> eventSubject;
  FakeDeckApi deckApi;
  String deckId;

  @Before public void setUp() {
    deckApi = new FakeDeckApi();
    deckId = deckApi.createDeckSync(100);
    Dealer dealer = new Dealer(deckId, deckApi);
    PlayGamePresenter presenter = new PlayGamePresenter(dealer, hand -> hand.addSequence(GEMINI));

    eventSubject = PublishSubject.create();
    uiModels = eventSubject.compose(presenter::accept).test();
  }

  @Test public void startsWithIdle() {
    uiModels.assertValue(IDLE_MODEL);
  }

  @Test public void success() {
    List<Card> cards = deckApi.getDeck(deckId).subList(0, 10);
    UiModel expected = IDLE_MODEL.withHand(new Hand(cards).addSequence(GEMINI));

    eventSubject.onNext(new DrawEvent(10));

    uiModels.assertValueAt(1, expected);
  }

  @Test public void handIsPreservedOnError() {
    List<Card> cards = deckApi.getDeck(deckId).subList(0, 10);
    UiModel expected = IDLE_MODEL
        .withHand(new Hand(cards).addSequence(GEMINI))
        .withError("Network failure!");

    eventSubject.onNext(new DrawEvent(10));
    deckApi.enableErrors(true);
    eventSubject.onNext(new DrawEvent(10));

    uiModels.assertValueAt(2, expected);
  }
}
