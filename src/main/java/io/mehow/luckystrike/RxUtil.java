package io.mehow.luckystrike;

import android.view.View;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.MainThreadDisposable;
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

  static Observable<Object> viewClicks(View view) {
    return Observable.create(emitter -> {
      MainThreadDisposable.verifyMainThread();
      view.setOnClickListener(v -> emitter.onNext(new Object()));
      emitter.setDisposable(new MainThreadDisposable() {
        @Override protected void onDispose() {
          view.setOnClickListener(null);
        }
      });
    });
  }
}
