package com.example.ratingbar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getRating = findViewById(R.id.getRating);
        final RatingBar ratingBar = findViewById(R.id.rating);
        getRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = "Rank é : " + ratingBar.getRating();
                Toast.makeText(MainActivity.this, rating, Toast.LENGTH_LONG).show();
            }
        });
    }
}
