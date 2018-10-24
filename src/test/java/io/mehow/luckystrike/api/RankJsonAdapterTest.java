package io.mehow.luckystrike.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import io.mehow.luckystrike.api.RankJsonAdapter;
import io.mehow.luckystrike.card.Card.Rank;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public final class RankJsonAdapterTest {
  final Moshi moshi = new Moshi.Builder()
      .add(Rank.class, new RankJsonAdapter())
      .build();

  @Test public void invalidRank() {
    String json = "\"1\"";
    JsonAdapter<Rank> adapter = moshi.adapter(Rank.class);

    assertThatCode(() -> adapter.fromJson(json))
        .isInstanceOf(JsonDataException.class)
        .hasNoCause()
        .hasMessage("Unknown card rank: 1");
  }

  @Test public void validRank() throws IOException {
    String json = "["
        + "\"ACE\","
        + "\"2\","
        + "\"3\","
        + "\"4\","
        + "\"5\","
        + "\"6\","
        + "\"7\","
        + "\"8\","
        + "\"9\","
        + "\"10\","
        + "\"JACK\","
        + "\"QUEEN\","
        + "\"KING\""
        + "]";
    Type type = Types.newParameterizedType(List.class, Rank.class);
    JsonAdapter<List<Rank>> adapter = moshi.adapter(type);

    List<Rank> result = adapter.fromJson(json);

    assertThat(result).containsExactlyInAnyOrder(Rank.values());
  }
}
