package com.example.wfc;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllcocktailActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<FirebaseData> cocktails = new ArrayList<>();
    private CocktailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcocktail);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCocktails);

        db.collection("cocktails").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<FirebaseData> cocktails = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    FirebaseData data = document.toObject(FirebaseData.class);
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

/*        View mainLayout = findViewById(R.id.mainLayout);  // 이 뷰는 activity_allcocktail.xml 파일에서 가장 바깥쪽에 있는 레이아웃의 id여야 합니다.
        mainLayout.setOnTouchListener((v, event) -> {
            *//*editTextSearch.setText("");  // 검색창 초기화*//* //이 코드드 사용 시 검색창이 아닌 화면을 레이아웃을 눌렀을 때 검색창이 초기화 되고 키보드가 내려감
            hideKeyboard(v);
            return true;
        });*/

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
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

/*    private void filter(String text) {
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
                    .whereEqualTo("cocktailName", text)  // "cocktailName" 필드가 검색어와 일치하는 문서만 선택
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
    }*/
}

class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {

    private List<FirebaseData> originalCocktails;
    private List<FirebaseData> filteredCocktails;

    public CocktailAdapter(List<FirebaseData> cocktails) {
        this.originalCocktails = cocktails;
        this.filteredCocktails = new ArrayList<>(cocktails);
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

        holder.textViewCocktailNum.setText(String.valueOf(cocktail.getCocktailNum()));
        holder.textViewCocktailName.setText(cocktail.getCocktailName());
        holder.textViewCockSimpleExplan.setText(cocktail.getCockSimpleExplan());

    }

    @Override
    public int getItemCount() {
        return filteredCocktails.size();
    }

    static class CocktailViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCocktailNum;
        TextView textViewCocktailName;
        TextView textViewCockSimpleExplan;

        public CocktailViewHolder(View itemView) {
            super(itemView);
            textViewCocktailNum = itemView.findViewById(R.id.textViewCocktailNum);
            textViewCocktailName = itemView.findViewById(R.id.textViewCocktailName);
            textViewCockSimpleExplan = itemView.findViewById(R.id.textViewCockSimpleExplan);
        }
    }

    // Update the adapter to filter the list

/*    public void filterList(List<FirebaseData> queryList) {
        filteredCocktails.clear();
        if (queryList.isEmpty()) {
            filteredCocktails.addAll(originalCocktails);
        } else {
            filteredCocktails.addAll(queryList);
        }
        notifyDataSetChanged();  // Notify the adapter that data set has changed which triggers UI update.
    }
}*/


/*
class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {

    private List<FirebaseData> cocktails;

    public CocktailAdapter(List<FirebaseData> cocktails) {
        this.cocktails = cocktails;
    }

    @NonNull
    @Override
    public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cocktail, parent, false);
        return new CocktailViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CocktailViewHolder holder, int position) {

        FirebaseData cocktail = cocktails.get(position);

        holder.textViewCocktailNum.setText(String.valueOf(cocktail.getCocktailNum()));
        holder.textViewCocktailName.setText(cocktail.getCocktailName());
        holder.textViewCockSimpleExplan.setText(cocktail.getCockSimpleExplan());

    }

    @Override
    public int getItemCount() {
        return cocktails.size();
    }

    static class CocktailViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCocktailNum;
        TextView textViewCocktailName;
        TextView textViewCockSimpleExplan;

        public CocktailViewHolder(View itemView) {
            super(itemView);
            textViewCocktailNum = itemView.findViewById(R.id.textViewCocktailNum);
            textViewCocktailName = itemView.findViewById(R.id.textViewCocktailName);
            textViewCockSimpleExplan = itemView.findViewById(R.id.textViewCockSimpleExplan);
        }
    }
    public void filterList(List<FirebaseData> filteredList) {
        cocktails = filteredList;
        notifyDataSetChanged();
    }

}*/
