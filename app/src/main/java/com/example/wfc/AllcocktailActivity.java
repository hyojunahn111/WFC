package com.example.wfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllcocktailActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcocktail);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCocktails);

        db.collection("cocktail").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<FirebaseData> cocktails = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    FirebaseData data = document.toObject(FirebaseData.class);
                    cocktails.add(data);
                }

                // 어댑터 생성 및 설정
                CocktailAdapter adapter = new CocktailAdapter(cocktails);
                recyclerView.setAdapter(adapter);
            } else {
                Log.d("Firestore", "Error getting documents.", task.getException());
            }
        });

        // 리사이클러뷰에 레이아웃 매니저 설정 (여기서는 세로 방향 리스트 사용)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

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
}