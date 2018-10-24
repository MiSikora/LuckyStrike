package io.mehow.luckystrike;

import io.mehow.luckystrike.PlayGamePresenter.DrawEvent;
import io.mehow.luckystrike.PlayGamePresenter.DrawUiModel;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static io.mehow.luckystrike.CardSequence.GEMINI;

public final class PlayGamePresenterTest {
  static final DrawUiModel IDLE_MODEL = DrawUiModel.idle();

  TestObserver<DrawUiModel> uiModels;
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

  @Test public void apiErrorsAreHandledGracefully() {
    deckApi.enableErrors(true);
    DrawUiModel expected = IDLE_MODEL.withError("Network failure!");

    eventSubject.onNext(new DrawEvent(10));

    uiModels.assertValueAt(1, expected);
  }

  @Test public void success() {
    List<Card> cards = deckApi.getDeck(deckId).subList(0, 10);
    DrawUiModel expected = IDLE_MODEL.withHand(new Hand(cards).addSequence(GEMINI));

    eventSubject.onNext(new DrawEvent(10));

    uiModels.assertValueAt(1, expected);
  }

  @Test public void handIsPreservedOnError() {
    List<Card> cards = deckApi.getDeck(deckId).subList(0, 10);
    DrawUiModel expected = IDLE_MODEL
        .withHand(new Hand(cards).addSequence(GEMINI))
        .withError("Network failure!");

    eventSubject.onNext(new DrawEvent(10));
    deckApi.enableErrors(true);
    eventSubject.onNext(new DrawEvent(10));

    uiModels.assertValueAt(2, expected);
  }
}
