package io.mehow.luckystrike;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.TestScheduler;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class RxUtilTest {
  @Test public void ignoreWhileBusyMap() {
    TestScheduler upstreamScheduler = new TestScheduler();
    TestScheduler downstreamScheduler = new TestScheduler();

    AtomicInteger upstreamCount = new AtomicInteger();
    AtomicInteger downstreamCount = new AtomicInteger();

    Function<Long, Observable<Integer>> longTask = tick -> Observable.just(1)
        .delay(10, SECONDS, downstreamScheduler)
        .doOnNext(tock -> downstreamCount.incrementAndGet());

    Disposable disposable = Observable.interval(10, SECONDS, upstreamScheduler)
        .doOnNext(tick -> upstreamCount.incrementAndGet())
        .compose(RxUtil.ignoreWhileBusyMap(longTask))
        .subscribe();

    upstreamScheduler.advanceTimeBy(50, SECONDS);
    downstreamScheduler.advanceTimeBy(20, SECONDS);
    upstreamScheduler.advanceTimeBy(50, SECONDS);
    downstreamScheduler.advanceTimeBy(20, SECONDS);

    disposable.dispose();

    assertThat(upstreamCount.get()).isEqualTo(10);
    assertThat(downstreamCount.get()).isEqualTo(2);
  }
}
