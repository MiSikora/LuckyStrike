package io.mehow.luckystrike;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.Nullable;

public final class CreateDeckActivity extends Activity {
  private TextView deckCountLabel;
  private SeekBar deckCountSlider;

  @Override protected void onCreate(@Nullable Bundle inState) {
    super.onCreate(inState);
    setContentView(R.layout.create_deck);
    deckCountLabel = findViewById(R.id.deck_count_label);
    deckCountSlider = findViewById(R.id.deck_count_slider);
    deckCountSlider.setProgress(deckCountSlider.getMax());

    updateDeckCountProgress(deckCountSlider.getProgress());
    deckCountSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateDeckCountProgress(progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
  }

  private void updateDeckCountProgress(int progress) {
    int adjustment = deckCountSlider.getMax() / 4;
    deckCountLabel.setText(getString(R.string.deck_count, (adjustment + progress) / adjustment));
  }
}
