package com.example.wfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Real_modify_cocktail extends AppCompatActivity {

    private EditText editTextCocktailName, editTextCockSimpleExplan, editTextTechniques, editTextGlassName, editTextGarnish, editTextRecipe;
    private Button buttonModify;
/*    private static final int REQUEST_CODE_IMAGE_PICK = 1;
    private ImageView imageViewCocktail;
    private String documentId;*/

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

                                Toast.makeText(Real_modify_cocktail.this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                Intent logoutIntent = new Intent(Real_modify_cocktail.this, LogoutActivity.class);
                                startActivity(logoutIntent);

                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Error updating document", e);
                            }
                        });
            }
        });

       /* imageViewCocktail = findViewById(R.id.modifyaddbt);

        imageViewCocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
            }


        });*/


    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imageViewCocktail.setImageURI(selectedImageUri);  // 이미지뷰에 선택한 이미지를 보여줍니다.

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference cocktailImagesRef = storageRef.child("cocktail_images/" + documentId);

            UploadTask uploadTask = cocktailImagesRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            cocktailImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // 업로드한 이미지의 URL을 Firestore에 저장합니다.
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("cocktails").document(documentId)
                                            .update("imageUrl", uri.toString())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Firestore", "Image URL successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Firestore", "Error updating image URL", e);
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseStorage", "Error uploading image", e);
                        }
                    });
        }
    }
*/
}