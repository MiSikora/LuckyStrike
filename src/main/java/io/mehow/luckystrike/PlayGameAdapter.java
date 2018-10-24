package io.mehow.luckystrike;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;

final class PlayGameAdapter extends RecyclerView.Adapter<PlayGameAdapter.ViewHolder> {
  private List<Card> cards = Collections.emptyList();

  private final LayoutInflater inflater;

  PlayGameAdapter(LayoutInflater inflater) {
    this.inflater = inflater;
  }

  void updateCards(List<Card> cards) {
    this.cards = cards;
    // Can get away with it due the use case.
    notifyDataSetChanged();
  }

  @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(viewType, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(cards.get(position));
  }

  @Override public int getItemCount() {
    return cards.size();
  }

  @Override public int getItemViewType(int position) {
    return R.layout.card_item;
  }

  static final class ViewHolder extends RecyclerView.ViewHolder {
    private final ImageView cardFace;

    ViewHolder(@NonNull View itemView) {
      super(itemView);
      cardFace = itemView.findViewById(R.id.card_face);
    }

    void bind(Card card) {
      Picasso.get()
          .load(card.imageUrl)
          .into(cardFace);
    }
  }
}
