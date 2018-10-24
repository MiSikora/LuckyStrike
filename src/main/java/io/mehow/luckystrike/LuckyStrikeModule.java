package io.mehow.luckystrike;

import com.squareup.moshi.Moshi;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import io.mehow.luckystrike.Card.Rank;
import io.mehow.luckystrike.PlayGameActivity.PlayGameModule;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module abstract class LuckyStrikeModule {
  @Provides @Singleton static DeckApi provideDeckApi() {
    Moshi moshi = new Moshi.Builder()
        .add(Rank.class, new RankJsonAdapter())
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://deckofcardsapi.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .validateEagerly(BuildConfig.DEBUG)
        .build();

    return retrofit.create(DeckApi.class);
  }

  @ContributesAndroidInjector abstract CreateDeckActivity contributeCreateDeckActivity();

  @ContributesAndroidInjector(modules = PlayGameModule.class)
  abstract PlayGameActivity contributePlayGameActivity();
}
