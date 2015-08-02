package com.github.glomadrian.codeinput;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.github.glomadrian.codeinputlib.CodeInput;

public class MainActivity extends AppCompatActivity {

  private CodeInput codeInput;
  private Button button;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    codeInput = (CodeInput) findViewById(R.id.pairing);
    button = (Button) findViewById(R.id.button);

    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        StringBuffer buffer = new StringBuffer();
        for (Character character : codeInput.getCode()) {
          buffer.append(character);
          buffer.append(" | ");
        }

        Toast.makeText(MainActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
      }
    });
  }
}
