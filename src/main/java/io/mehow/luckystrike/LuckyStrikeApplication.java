package io.mehow.luckystrike;

import android.app.Activity;
import android.app.Application;
import dagger.android.AndroidInjector;
import dagger.android.HasActivityInjector;

public final class LuckyStrikeApplication extends Application implements HasActivityInjector {
  private LuckyStrikeComponent appComponent;

  @Override public void onCreate() {
    super.onCreate();
    appComponent = LuckyStrikeComponent.create(this);
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return appComponent.activityInjector();
  }
}
