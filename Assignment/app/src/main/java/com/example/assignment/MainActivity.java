package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CardView cardProduct, cardPayment, cardOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardProduct = findViewById(R.id.cardProduct);
        cardPayment = findViewById(R.id.cardPayment);
        cardOrder = findViewById(R.id.cardOrder);

        cardProduct.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProductActivity.class));
        });
    }
}
