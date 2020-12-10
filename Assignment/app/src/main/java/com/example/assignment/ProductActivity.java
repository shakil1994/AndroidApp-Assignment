package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.assignment.Adapter.MyCategoryAdapter;
import com.example.assignment.Common.Common;
import com.example.assignment.Model.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    RecyclerView recycler_category;
    List<CategoryModel> categoryModelList;
    MyCategoryAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recycler_category = findViewById(R.id.recycler_category);
        recycler_category.setLayoutManager(new LinearLayoutManager(this));
        recycler_category.setHasFixedSize(true);
        categoryModelList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Common.CATEGORY_REF);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    CategoryModel categoryModel = dataSnapshot.getValue(CategoryModel.class);
                    categoryModel.setMenu_id(dataSnapshot.getKey());
                    categoryModelList.add(categoryModel);
                }
                adapter = new MyCategoryAdapter(getApplicationContext(), categoryModelList);
                recycler_category.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
