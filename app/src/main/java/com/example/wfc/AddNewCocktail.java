package com.example.wfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wfc.databinding.ActivityAddNewCocktailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

public class AddNewCocktail extends AppCompatActivity {

/*    Button imageaddbutton;*/
    final int GET_GALLERY_IMAGE = 200;

    private final int GALLERY_CODE = 10;
    ImageView addnewpicture;
    private FirebaseStorage storage;
    private Uri selectedImageUri;

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

        /*imageaddbutton = (Button) findViewById(R.id.imageaddbutton);
        imageaddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,GET_GALLERY_IMAGE);
            }
        });*/

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

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE){
            selectedImageUri = data.getData();
/*            Uri file = data.getData();
            StorageReference storageReference = storage.getReference(); //Firebase의 Cloud Storage에 접근하기 위한 StorageReference 객체를 생성
            StorageReference riversReference = storageReference.child("phote/1.png"); //Cloud Storage의 "photo" 폴더 아래에 "1.png"라는 이름으로 파일을 저장하도록 설정
            UploadTask uploadTask = riversReference.putFile(file);*/

            /*addnewpicture.setVisibility(View.INVISIBLE); // 이미지를 업로드하면 기존 이미지를 보이지 않게 한다*/

            try{
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                addnewpicture.setImageBitmap(img);
            }catch(Exception e){
                e.printStackTrace();
            }

/*            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewCocktail.this, "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddNewCocktail.this, "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });*/
        }
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

                    if(selectedImageUri != null) {
                        // 이미지를 스토리지에 업로드
                        StorageReference storageReference = storage.getReference();
                        StorageReference riversReference = storageReference.child("photos/" + data.getCocktailNum() + ".png");
                        UploadTask uploadTask = riversReference.putFile(selectedImageUri);
                        uploadTask.continueWithTask(task1 -> {
                            if (!task1.isSuccessful()) {
                                throw task1.getException();
                            }
                            // Continue with the task to get the download URL
                            return riversReference.getDownloadUrl();
                        }).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Uri downloadUri = task1.getResult();
                                // 다운로드 URL을 데이터에 추가
                                data.setImageUrl(downloadUri.toString());

                                db.collection("cocktails")
                                        .document(String.valueOf(data.getCocktailNum()))
                                        .set(data)
                                        .addOnSuccessListener(aVoid -> {
                                            binding.textResult.setText("success!");
                                            // 데이터 저장이 성공하면 이전 액티비티(LogoutActivity)로 이동
                                            Intent intent = new Intent(AddNewCocktail.this, LogoutActivity.class);
                                            startActivity(intent);
                                            finish(); // 현재 액티비티 종료
                                        })
                                        .addOnFailureListener(e -> {
                                            binding.textResult.setText("fail!");
                                            Log.e("Firebase", "Error adding document", e); // Android Log 클래스를 사용하여 오류 메시지를 로그에 출력
                                        });
                            } else {
                                // Handle failures
                                // ...
                            }
                        });
                    }else{
                        data.setImageUrl("gs://whats-your-favorite-cocktail.appspot.com/default_img.png");
                        saveDataToFirestore(data, db);
                    }
                });
    }

    private void saveDataToFirestore(FirebaseData data, FirebaseFirestore db) {
        db.collection("cocktails")
                .document(String.valueOf(data.getCocktailNum()))
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    binding.textResult.setText("success!");
                    // 데이터 저장이 성공하면 이전 액티비티(LogoutActivity)로 이동
                    Intent intent = new Intent(AddNewCocktail.this, LogoutActivity.class);
                    startActivity(intent);
                    finish(); // 현재 액티비티 종료
                })
                .addOnFailureListener(e -> {
                    binding.textResult.setText("fail!");
                    Log.e("Firebase", "Error adding document", e); // Android Log 클래스를 사용하여 오류 메시지를 로그에 출력
                });
    }


}
