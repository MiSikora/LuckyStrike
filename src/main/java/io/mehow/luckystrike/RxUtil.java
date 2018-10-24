package io.mehow.luckystrike;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.functions.Function;

import static io.reactivex.BackpressureStrategy.DROP;
import static io.reactivex.BackpressureStrategy.ERROR;

final class RxUtil {
  private RxUtil() {
    throw new AssertionError("No instances allowed!");
  }

  static <T, R> ObservableTransformer<T, R> ignoreWhileBusyMap(Function<T, Observable<R>> mapper) {
    return upstream -> upstream.toFlowable(DROP)
        .flatMap(it -> mapper.apply(it).toFlowable(ERROR), 1)
        .toObservable();
  }

  static void rethrow(Throwable throwable) {
    throw new OnErrorNotImplementedException("Source!", throwable);
  }
}
