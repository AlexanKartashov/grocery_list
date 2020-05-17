package com.example.grocerylisting.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.example.grocerylisting.Adapters.RecipeDetailIngrListAdapter;
import com.example.grocerylisting.ModelManagers.SelectedIngredientsDbManager;
import com.example.grocerylisting.ModelManagers.SelectedRecipeDbManager;
import com.example.grocerylisting.Models.*;
import com.example.grocerylisting.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
public class RecipeDetailActivity extends AppCompatActivity {

    ImageView imgRecipe;
    TextView recTitle, recAuthor;
    TextView recInstr;
    ListView ingrList;
    Button addToSelectedBtn;
    Button expInstr;
    Button recdet_back;
    NestedScrollView nest_scrl_view;

    RecipeDetailIngrListAdapter ingrListAdapter;
    ArrayList<Ingredient> ingredients;
    ArrayList<Uom> uoms;
    String recipeKey, recipeTitle;

    boolean isTextViewClicked = false;

    @Override
    protected void onResume() {
        super.onResume();
        SelectedRecipeDbManager dataBaseManager = new SelectedRecipeDbManager(RecipeDetailActivity.this);
        if (dataBaseManager.contains(recipeKey)) {
            addToSelectedBtn.setText("Unselect");
        }
        else {
            addToSelectedBtn.setText("Add to selected");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recipeKey = getIntent().getExtras().getString("recipeKey");
        initViews();

        recipeTitle = getIntent().getExtras().getString("title");
        recTitle.setText(recipeTitle);

        String recipeImage = getIntent().getExtras().getString("recipeImage");
        Glide.with(this).load(recipeImage).into(imgRecipe);

        String author = getIntent().getExtras().getString("author");
        recAuthor.setText("Автор рецепта: "+author);

        String instruction = getIntent().getExtras().getString("instruction");
        recInstr.setText(instruction);
        recInstr.post(new Runnable() {
            @Override
            public void run() {
                if (recInstr.getLineCount()>8)
                {
                    expInstr.setVisibility(View.INVISIBLE);
                }
                else
                {
                    expInstr.setVisibility(View.VISIBLE);
                };
            }
        });

        expInstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTextViewClicked){
                    recInstr.setMaxLines(8);
                    expInstr.setText("See More");
                    isTextViewClicked = false;
                } else {
                    recInstr.setMaxLines(Integer.MAX_VALUE);
                    expInstr.setText("See Less");
                    isTextViewClicked = true;
                }
            }
        });

        addToSelectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedRecipe selectedRecipe;
                selectedRecipe = new SelectedRecipe(recipeKey, recipeTitle);

                SelectedRecipeDbManager dataBaseManager = new SelectedRecipeDbManager(RecipeDetailActivity.this);
                SelectedIngredientsDbManager dbIngredients = new SelectedIngredientsDbManager(RecipeDetailActivity.this);

                if (dataBaseManager.contains(recipeKey)) {
                    try {
                        dataBaseManager.unselectRecipe(selectedRecipe);
                        dbIngredients.deleteAllIngredientsOfRecipe(recipeKey);
                        Toast.makeText(RecipeDetailActivity.this, "Рецепт удален из списка выбранных", Toast.LENGTH_SHORT).show();
                        addToSelectedBtn.setText("Add to selected");
                    }
                    catch (Exception ex) {
                        Toast.makeText(RecipeDetailActivity.this, "Ошибка удаления рецепты из выбранных", Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    try {
                        dataBaseManager.addSelectedRecipe(selectedRecipe);
                        for (Ingredient ingr:
                             ingredients) {
                            dbIngredients.addIngredient(new IngrOfRecipe(recipeKey, ingr.getIngrKey(), ingr.getIngrName()));
                        }
                        Toast.makeText(RecipeDetailActivity.this, "Рецепт добавлен для списка покупок", Toast.LENGTH_SHORT).show();
                        addToSelectedBtn.setText("Unselect");
                    } catch (Exception ex) {
                        Toast.makeText(RecipeDetailActivity.this, "Ошибка добавления рецепты в выбранные", Toast.LENGTH_SHORT).show();
                        selectedRecipe = new SelectedRecipe("-1", "fail");
                        dataBaseManager.addSelectedRecipe(selectedRecipe);
                    }
                }
            }
        });

        recdet_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        nest_scrl_view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                nest_scrl_view.scrollTo(0,0);
            }
        });

        initIngredients();
        addIngredients();
    }

    private void initViews() {
        imgRecipe = findViewById(R.id.recipe_detail_img);
        recTitle = findViewById(R.id.recdet_titile);
        recAuthor = findViewById(R.id.recdet_authLabel);
        recInstr = findViewById(R.id.recdet_instr);

        ingrList = findViewById(R.id.recdet_ingrList);
        addToSelectedBtn = findViewById(R.id.addToSelectedBtn);

        expInstr = findViewById(R.id.expandInstr);
        recdet_back = findViewById(R.id.recdet_back);
        nest_scrl_view = (NestedScrollView) findViewById(R.id.activity_recipe_detail);
    }

    private void initIngredients() {
        ingredients = new ArrayList<Ingredient>();
        uoms = new ArrayList<Uom>();
        ingrListAdapter = new RecipeDetailIngrListAdapter(this,ingredients, uoms);
        ingrList.setAdapter(ingrListAdapter);
    }

    private void addIngredients()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ingrUomsRef = firebaseDatabase.getReference("IngredientsUom");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    RecipeWithIngr recipeWithIngr = ds.getValue(RecipeWithIngr.class);
                    String fRecipeKey = recipeWithIngr.getRecipeKey();
                    String fIngrKey = recipeWithIngr.getIngrKey();
                    String fuomKey = recipeWithIngr.getUomKey();
                    if (fRecipeKey.equals(recipeKey)) {
                        DatabaseReference ingrRef = FirebaseDatabase.getInstance().getReference("Ingredient").child(fIngrKey);
                        ValueEventListener ingrEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Ingredient newIngr = dataSnapshot.getValue(Ingredient.class);
                                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) ingrList.getLayoutParams();
                                lp.height = 120*ingrListAdapter.getCount();
                                ingrList.setLayoutParams(lp);
                                ingredients.add(newIngr);
                                ingrListAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        ingrRef.addValueEventListener(ingrEventListener);

                        DatabaseReference uomRef = FirebaseDatabase.getInstance().getReference("Uom").child(fuomKey);
                        ValueEventListener uomEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Uom newUom = dataSnapshot.getValue(Uom.class);
                                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) ingrList.getLayoutParams();
                                lp.height = 120*ingrListAdapter.getCount();
                                ingrList.setLayoutParams(lp);
                                uoms.add(newUom);
                                ingrListAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        uomRef.addValueEventListener(uomEventListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ingrUomsRef.addValueEventListener(valueEventListener);

    }

}
