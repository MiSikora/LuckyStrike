package io.mehow.luckystrike;

import android.app.Activity;
import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import javax.inject.Singleton;

@Singleton @Component(modules = { AndroidInjectionModule.class, LuckyStrikeModule.class})
interface LuckyStrikeComponent extends AndroidInjector<LuckyStrikeApplication> {
  DispatchingAndroidInjector<Activity> activityInjector();

  @Component.Builder interface Builder {
    @BindsInstance Builder application(Application application);

    LuckyStrikeComponent build();
  }

  static LuckyStrikeComponent create(Application application) {
    return DaggerLuckyStrikeComponent.builder()
        .application(application)
        .build();
  }
}
