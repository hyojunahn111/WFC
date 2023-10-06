package com.example.wfc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.wfc.databinding.ActivityAddNewCocktailBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewCocktail extends AppCompatActivity {
    private ActivityAddNewCocktailBinding binding;

    private int cocktailNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewCocktailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cocktailName = binding.editTextCocktailName.getText().toString();
                String cockSimpleExplan = binding.editTextCockSimpleExplan.getText().toString();
                String techniques = binding.editTextTechniques.getText().toString();
                String glassName = binding.editTextGlassName.getText().toString();
                String garnish = binding.editTextGarnish.getText().toString();
                String recipe = binding.editTextRecipe.getText().toString();

                FirebaseData data = new FirebaseData(cocktailName, cockSimpleExplan, techniques, glassName, garnish, recipe, ++cocktailNum);

                setDocument(data);
            }
        });
    }

    private void setDocument(FirebaseData data) {
        FirebaseFirestore.getInstance()
                .collection("cocktail")
                .document(String.valueOf(data.getCocktailNum()))
                .set(data)
                .addOnSuccessListener(aVoid -> binding.textResult.setText("success!"))
                .addOnFailureListener(e -> binding.textResult.setText("fail!"));
    }
}