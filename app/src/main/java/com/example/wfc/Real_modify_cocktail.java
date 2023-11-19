package com.example.wfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Real_modify_cocktail extends AppCompatActivity {

    private EditText editTextCocktailName, editTextCockSimpleExplan, editTextTechniques, editTextGlassName, editTextGarnish, editTextRecipe;
    private Button buttonModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_modify_cocktail);

        editTextCocktailName = findViewById(R.id.rmd_cocktailName);
        editTextCockSimpleExplan = findViewById(R.id.rmd_cockSimpleExplan);
        editTextTechniques = findViewById(R.id.rmd_techniques);
        editTextGlassName = findViewById(R.id.rmd_glassName);
        editTextGarnish = findViewById(R.id.rmd_garnish);
        editTextRecipe = findViewById(R.id.rmd_recipe);

        String documentId = getIntent().getStringExtra("DocumentId");
        if(documentId == null){
            Log.d("Firestore", "DocumentId is null!");
            return;
        }

        String cocktailName = getIntent().getStringExtra("CocktailName");
        editTextCocktailName.setText(cocktailName);
        String cockSimpleExplan = getIntent().getStringExtra("CockSimpleExplan");
        editTextCockSimpleExplan.setText(cockSimpleExplan);
        String techniques = getIntent().getStringExtra("Techniques");
        editTextTechniques.setText(techniques);
        String glassName = getIntent().getStringExtra("GlassName");
        editTextGlassName.setText(glassName);
        String Garnish = getIntent().getStringExtra("Garnish");
        editTextGarnish.setText(Garnish);
        String recipe = getIntent().getStringExtra("Recipe");
        editTextRecipe.setText(recipe);

        buttonModify = findViewById(R.id.buttonModify);

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCocktailName = editTextCocktailName.getText().toString();
                String newCockSimpleExplan = editTextCockSimpleExplan.getText().toString();
                String newTechniques = editTextTechniques.getText().toString();
                String newGlassName = editTextGlassName.getText().toString();
                String newGarnish = editTextGarnish.getText().toString();
                String newRecipe = editTextRecipe.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                FirebaseData updatedCocktail = new FirebaseData(); // FirebaseData 클래스가 Firestore의 documents를 매핑하는 클래스라고 가정합니다.
                updatedCocktail.setCocktailName(newCocktailName);
                updatedCocktail.setCockSimpleExplan(newCockSimpleExplan);
                updatedCocktail.setTechniques(newTechniques);
                updatedCocktail.setGlassName(newGlassName);
                updatedCocktail.setGarnish(newGarnish);
                updatedCocktail.setRecipe(newRecipe);

                db.collection("cocktails").document(documentId)
                        .set(updatedCocktail)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Firestore", "DocumentSnapshot successfully updated!");
                                // 업데이트 완료 후 수행할 작업들...
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Error updating document", e);
                            }
                        });
            }
        });
    }
}