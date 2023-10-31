package com.example.wfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.wfc.databinding.ActivityAddNewCocktailBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

public class AddNewCocktail extends AppCompatActivity {

    Button imageaddbutton;
    final int GET_GALLERY_IMAGE = 200;

    private final int GALLERY_CODE = 10;
    ImageView addnewpicture;
    private FirebaseStorage storage;

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

        imageaddbutton = (Button) findViewById(R.id.imageaddbutton);
        imageaddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,GET_GALLERY_IMAGE);
            }
        });

        findViewById(R.id.addimv).setOnClickListener(onClickListener);
        addnewpicture=(ImageView)findViewById(R.id.addimv);
        storage=FirebaseStorage.getInstance();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.addimv:
                    loadAlbum();
                    break;
            }
        }
    };

    private void loadAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }

/*    private void setDocument(FirebaseData data) {
        FirebaseFirestore.getInstance()
                .collection("cocktail")
                .document(String.valueOf(data.getCocktailNum()))
                .set(data)
                .addOnSuccessListener(aVoid -> binding.textResult.setText("success!"))
                .addOnFailureListener(e -> binding.textResult.setText("fail!"));
    }
}*/

    private void setDocument(FirebaseData data) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cocktails")
                .orderBy("cocktailNum", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        FirebaseData lastCocktail = task.getResult().getDocuments().get(0).toObject(FirebaseData.class);
                        data.setCocktailNum(lastCocktail.getCocktailNum() + 1);
                    } else {
                        data.setCocktailNum(1);
                    }

                    db.collection("cocktails")
                            .document(String.valueOf(data.getCocktailNum()))
                            .set(data)
                            .addOnSuccessListener(aVoid -> binding.textResult.setText("success!"))
                            .addOnFailureListener(e -> binding.textResult.setText("fail!"));
                });
    }

}
