package com.github.glomadrian.codeinput;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.glomadrian.codeinputlib.CodeInput;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    CodeInput cInput = (CodeInput) findViewById(R.id.pairing);
    cInput.setCodeReadyListener(new CodeInput.codeReadyListener() {
      @Override
      public void onCodeReady(Character[] code) {
        // Code has been entered ....
        Toast.makeText(MainActivity.this,"code entered is : "+ Arrays.toString(code),Toast.LENGTH_SHORT).show();
      }
    });
  }
}
