package io.mehow.luckystrike;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import io.mehow.luckystrike.Card.Rank;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class RankJsonAdapter extends JsonAdapter<Rank> {
  private static final Map<String, Rank> RANK_MAP = new HashMap<String, Rank>() {{
    put("ACE", Rank.ACE);
    put("2", Rank.TWO);
    put("3", Rank.THREE);
    put("4", Rank.FOUR);
    put("5", Rank.FIVE);
    put("6", Rank.SIX);
    put("7", Rank.SEVEN);
    put("8", Rank.EIGHT);
    put("9", Rank.NINE);
    put("10", Rank.TEN);
    put("JACK", Rank.JACK);
    put("QUEEN", Rank.QUEEN);
    put("KING", Rank.KING);
  }};

  @Override public Rank fromJson(JsonReader reader) throws IOException {
    String value = reader.nextString();
    Rank rank = RANK_MAP.get(value);
    if (rank == null) {
      throw new JsonDataException("Unknown card rank: " + value);
    }
    return rank;
  }

  @Override public void toJson(JsonWriter writer, Rank value) throws IOException {
    // Don't care about API symmetry since we only deserialize JSON.
    writer.value(value.name());
  }
}
