package com.example.wfc;

import static android.content.ContentValues.TAG;
import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AllcocktailActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<FirebaseData> cocktails = new ArrayList<>();
    private CocktailAdapter adapter;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcocktail);

        auth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCocktails);

        db.collection("cocktails").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<FirebaseData> cocktails = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    FirebaseData data = document.toObject(FirebaseData.class);
                    data.setDocumentId(document.getId());
                    cocktails.add(data);
                }

                // 어댑터 생성 및 설정
                adapter = new CocktailAdapter(cocktails);
                recyclerView.setAdapter(adapter);
            } else {
                Log.d("Firestore", "Error getting documents.", task.getException());
            }
        });

        // 리사이클러뷰에 레이아웃 매니저 설정 (여기서는 세로 방향 리스트 사용)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EditText editTextSearch = findViewById(R.id.editTextSearch);

        View mainLayout = findViewById(R.id.mainLayout);  // 이 뷰는 activity_allcocktail.xml 파일에서 가장 바깥쪽에 있는 레이아웃의 id여야 합니다.
        mainLayout.setOnTouchListener((v, event) -> {
            /*editTextSearch.setText("");*/  // 검색창 초기화 //이 코드드 사용 시 검색창이 아닌 화면을 레이아웃을 눌렀을 때 검색창이 초기화 되고 키보드가 내려감
            hideKeyboard(v);
            return true;
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }


            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.allmenubt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_menu1){

                            Intent intent = new Intent(AllcocktailActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            auth.signOut();

                            Toast.makeText(AllcocktailActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        }else if (menuItem.getItemId() == R.id.action_menu2){
                            Toast.makeText(AllcocktailActivity.this, "기능없음", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AllcocktailActivity.this, "기능없음", Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        ImageView goMainImage = findViewById(R.id.gomainim);
        goMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllcocktailActivity.this, LogoutActivity.class); // CurrentActivity는 현재 액티비티의 이름으로 대체해주세요.
                startActivity(intent);
            }
        });

    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void filter(String text) {
        if (text.isEmpty()) {
            // 검색창이 비었을 때는 모든 데이터를 다시 가져옵니다.
            db.collection("cocktails").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<FirebaseData> cocktails = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        FirebaseData data = document.toObject(FirebaseData.class);
                        cocktails.add(data);
                    }
                    adapter.filterList(cocktails);  // 어댑터에 새로운 리스트 전달
                } else {
                    Log.d("Firestore", "Error getting documents.", task.getException());
                }
            });
        } else {
            // 검색어가 있을 때는 해당하는 데이터만 가져옵니다.
            db.collection("cocktails")
                    .orderBy("cocktailName")  // "cocktailName" 필드 기준으로 정렬
                    .startAt(text)  // 검색어로 시작하는 문서 선택
                    .endAt(text + "\uf8ff")  // 검색어로 시작하고 어떤 문자든지 뒤따르는 문서 선택 ("\uf8ff"는 유니코드에서 가장 큰 값)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<FirebaseData> cocktails = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FirebaseData data = document.toObject(FirebaseData.class);
                                cocktails.add(data);
                            }
                            adapter.filterList(cocktails);  // 어댑터에 새로운 리스트 전달
                        } else {
                            Log.d("Firestore", "Error getting documents.", task.getException());
                        }
                    });
        }
    }

}

class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {

    private List<FirebaseData> originalCocktails;
    private List<FirebaseData> filteredCocktails;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public CocktailAdapter(List<FirebaseData> cocktails) {
        this.originalCocktails = cocktails;
        this.filteredCocktails = new ArrayList<>(cocktails);
        this.auth = auth;
    }


    @NonNull
    @Override
    public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cocktail, parent, false);
        return new CocktailViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CocktailViewHolder holder, int position) {
        FirebaseData cocktail = filteredCocktails.get(position);

        //holder.textViewCocktailNum.setText(String.valueOf(cocktail.getCocktailNum()));
        holder.textViewCocktailName.setText(cocktail.getCocktailName());
        holder.textViewCockSimpleExplan.setText(cocktail.getCockSimpleExplan());
        holder.textViewLikeCount.setText(String.valueOf(cocktail.getLikeCount()));
        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeClicked(db.collection("cocktails").document(cocktail.getDocumentId()));
                /*if (cocktail.getLikes().containsKey(auth.getCurrentUser().getUid())) {
                    holder.buttonLike.setImageResource(R.drawable.sun);
                } else {
                    holder.buttonLike.setImageResource(R.drawable.heartgood_im);
                }*/
            }
        });


        Glide.with(holder.itemView.getContext())
                .load(cocktail.getImageUrl())
                .placeholder(R.drawable.default_img)  // 이미지 로딩 중에 보여줄 이미지
                .error(R.drawable.default_img)  // 이미지 로딩 실패 시 보여줄 이미지
                .into(holder.imageViewCocktail);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("칵테일 상세정보");

                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View view = inflater.inflate(R.layout.modify_cocktail, null);

                ImageView addimv = view.findViewById(R.id.addimv_md);
                TextView TextCocktailName = view.findViewById(R.id.TextCocktailName_md);
                TextCocktailName.setTextColor(Color.WHITE);
                TextView TextCocktailExplan = view.findViewById(R.id.TextCockSimpleExplan_md);
                TextCocktailExplan.setTextColor(Color.WHITE);
                TextView TextCocktailTechniques = view.findViewById(R.id.TextTechniques_md);
                TextCocktailTechniques.setTextColor(Color.WHITE);
                TextView TextGlassName = view.findViewById(R.id.TextGlassName_md);
                TextGlassName.setTextColor(Color.WHITE);
                TextView TextGarnish = view.findViewById(R.id.TextGarnish_md);
                TextGarnish.setTextColor(Color.WHITE);
                TextView TextRecipe = view.findViewById(R.id.TextRecipe_md);
                TextRecipe.setTextColor(Color.WHITE);

                Glide.with(view.getContext())
                        .load(cocktail.getImageUrl())
                        .placeholder(R.drawable.default_img)
                        .error(R.drawable.default_img)
                        .into(addimv);
                TextCocktailName.setText(cocktail.getCocktailName());
                TextCocktailExplan.setText(cocktail.getCockSimpleExplan());
                TextCocktailTechniques.setText(cocktail.getTechniques());
                TextGlassName.setText(cocktail.getGlassName());
                TextGarnish.setText(cocktail.getGarnish());
                TextRecipe.setText(cocktail.getRecipe());

                builder.setView(view);

                builder.setPositiveButton("확인", null);
                builder.setNeutralButton("수정", new DialogInterface.OnClickListener(){
                   @Override
                   public void  onClick(DialogInterface dialog, int which){
                       Intent intent = new Intent(v.getContext(),Real_modify_cocktail.class);

                       intent.putExtra("DocumentId", cocktail.getDocumentId());
                       intent.putExtra("CocktailName", cocktail.getCocktailName());
                       intent.putExtra("CockSimpleExplan", cocktail.getCockSimpleExplan());
                       intent.putExtra("Techniques", cocktail.getTechniques());
                       intent.putExtra("GlassName", cocktail.getGlassName());
                       intent.putExtra("Garnish", cocktail.getGarnish());
                       intent.putExtra("Recipe", cocktail.getRecipe());


                       v.getContext().startActivity(intent);
                   }
                });

                builder.show();
            }
        });
    }

    private void onLikeClicked(DocumentReference cocktailRef) {
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @NonNull
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                FirebaseData cocktail = transaction.get(cocktailRef).toObject(FirebaseData.class);

                if (cocktail.getLikes().containsKey(auth.getCurrentUser().getUid())) {
                    cocktail.setLikeCount(cocktail.getLikeCount() - 1);
                    cocktail.getLikes().remove(auth.getCurrentUser().getUid());
                } else {
                    cocktail.setLikeCount(cocktail.getLikeCount() + 1);
                    cocktail.getLikes().put(auth.getCurrentUser().getUid(), true);
                }

                transaction.set(cocktailRef, cocktail);

                return null;
            }
        });
    }


    @Override
    public int getItemCount() {
        return filteredCocktails.size();
    }

    static class CocktailViewHolder extends RecyclerView.ViewHolder {

        ImageButton buttonLike;

        //TextView textViewCocktailNum;
        TextView textViewCocktailName;
        TextView textViewCockSimpleExplan;
        ImageView imageViewCocktail;

        TextView textViewLikeCount;  // 좋아요 수를 표시할 TextView 추가

        public CocktailViewHolder(View itemView) {
            super(itemView);
            //textViewCocktailNum = itemView.findViewById(R.id.textViewCocktailNum);
            textViewCocktailName = itemView.findViewById(R.id.textViewCocktailName);
            textViewCockSimpleExplan = itemView.findViewById(R.id.textViewCockSimpleExplan);
            buttonLike = itemView.findViewById(R.id.buttonLike);
            imageViewCocktail = itemView.findViewById(R.id.imageViewCocktail);
            textViewLikeCount = itemView.findViewById(R.id.textViewLikeCount); // 초기화
        }
    }



    // Update the adapter to filter the list

    public void filterList(List<FirebaseData> queryList) {
        filteredCocktails.clear();
        if (queryList.isEmpty()) {
            filteredCocktails.addAll(originalCocktails);
        } else {
            filteredCocktails.addAll(queryList);
        }
        notifyDataSetChanged();  // Notify the adapter that data set has changed which triggers UI update.
    }
}
