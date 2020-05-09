package com.example.grocerylisting.Fragments;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylisting.Adapters.EditIngrListAdapter;
import com.example.grocerylisting.Adapters.RecipeInListAdapter;
import com.example.grocerylisting.CommonMethods.Common;
import com.example.grocerylisting.ModelManagers.IngredientManager;
import com.example.grocerylisting.ModelManagers.RecipeManager;
import com.example.grocerylisting.Models.IngrAndUom;
import com.example.grocerylisting.Models.Recipe;
import com.example.grocerylisting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class FragmentRecipes extends Fragment {

    View view;
    Dialog popupAddRecipe;
    ImageView addedRecipeImage, addRecipeBtn;
    Button addIngrAndUomBtn;
    TextView addRecipeTitle, addRecipeInstr, addRecipeAuthor;
    TextView addIngr, addUom;
    ProgressBar addClickProgress;
    Uri pickedImgUri = null;
    ListView ingrList;
    EditIngrListAdapter editIngrListAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Recipe> recipeList;

    RecipeManager recipeManager;
    IngredientManager ingredientManager;
    boolean ingrCheck = false;
    boolean recCheck = false;


    RecyclerView recyclerView;
    RecipeInListAdapter recipeAdapter;

    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;

    public FragmentRecipes() {
        this.recipeManager = new RecipeManager();
        this.ingredientManager = new IngredientManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        lin.setStackFromEnd(true);
        lin.setReverseLayout(true);
        view = inflater.inflate(R.layout.recipes_fragment,container,false);
        recyclerView =  view.findViewById(R.id.recipeList);
        recyclerView.setLayoutManager(lin);
        recyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Recipes");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get list

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList = new ArrayList<>();
                for (DataSnapshot recipesnap: dataSnapshot.getChildren()) {
                    Recipe recipe = recipesnap.getValue(Recipe.class);
                    recipeList.add(recipe);
                }

                recipeAdapter = new RecipeInListAdapter(getActivity(), recipeList);
                recyclerView.setAdapter(recipeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);

        initiatePopup();
        setClickOnAddBtn();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FloatingActionButton) view.findViewById(R.id.floatingActionButton)).hide();
                popupAddRecipe.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == REQUESCODE && data != null)
        {
            pickedImgUri = data.getData();
            addedRecipeImage.setImageURI(pickedImgUri);

        }
    }

    private void initiatePopup() {
        popupAddRecipe = new Dialog(getContext(), R.style.MyDialogTheme);
        popupAddRecipe.setContentView(R.layout.popup_add_recipe);
        popupAddRecipe.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        popupAddRecipe.getWindow().getAttributes().gravity = Gravity.TOP;


        ingrList = popupAddRecipe.findViewById(R.id.editIngredients);
        editIngrListAdapter = new EditIngrListAdapter(getActivity());
        ingrList.setAdapter(editIngrListAdapter);

        addIngr = popupAddRecipe.findViewById(R.id.editIngr);
        addUom = popupAddRecipe.findViewById(R.id.editUom);
        addIngrAndUomBtn = (Button) popupAddRecipe.findViewById(R.id.addIngrAndUomBtn);

        addIngrAndUomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(getActivity());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        ((ScrollView)popupAddRecipe.findViewById(R.id.addRecipe_fragment)).fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 500);
                editIngrListAdapter.addIngredient(addIngr, addUom);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) ingrList.getLayoutParams();
                lp.height = 150*editIngrListAdapter.getCount();
                ingrList.setLayoutParams(lp);
                addIngr.setText("");
                addUom.setText("");
            }
        });


        addedRecipeImage = popupAddRecipe.findViewById(R.id.editRecipeImage);
        addRecipeBtn = popupAddRecipe.findViewById(R.id.addRecipeBtn);
        addRecipeTitle = popupAddRecipe.findViewById(R.id.editRecTitle);
        addRecipeAuthor = popupAddRecipe.findViewById(R.id.editRecAuthor);
        addRecipeInstr = popupAddRecipe.findViewById(R.id.editInstr);
        addClickProgress = popupAddRecipe.findViewById(R.id.editProgressBar);

        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipeBtn.setVisibility(View.INVISIBLE);
                addClickProgress.setVisibility(View.VISIBLE);

                if(!addRecipeTitle.getText().toString().isEmpty()
                    && !addRecipeInstr.getText().toString().isEmpty()
                    && !addRecipeAuthor.getText().toString().isEmpty()
                    && pickedImgUri != null)
                {
                    StorageReference store = FirebaseStorage.getInstance()
                            .getReference().child("recipe_images");
                    final StorageReference imagePath = store.child(Common.getFileName(getActivity(),pickedImgUri));
                    imagePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageLink = uri.toString();
                                    Recipe newRecipe = new Recipe(addRecipeTitle,
                                            addRecipeInstr, addRecipeAuthor, imageLink);
                                    DatabaseReference recipeRef = recipeManager.getRecipeKey();
                                    ingrCheck = ingredientManager.addIngredients(editIngrListAdapter.getIngredients(), recipeRef);
                                    if (ingrCheck) {
                                        recipeManager.addRecipeToFirebase(newRecipe,recipeRef).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Рецепт добавлен успешно", Toast.LENGTH_LONG).show();
                                                addRecipeBtn.setVisibility(View.VISIBLE);
                                                addClickProgress.setVisibility(View.INVISIBLE);
                                                popupAddRecipe.dismiss();
                                            }
                                        });
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Возникла ошибка. Обратитесь к администратору", Toast.LENGTH_LONG).show();
                                        addRecipeBtn.setVisibility(View.VISIBLE);
                                        addClickProgress.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                                    addRecipeBtn.setVisibility(View.VISIBLE);
                                    addClickProgress.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(),"Заполните поля и загрузите изображение",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setClickOnAddBtn() {

        addedRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkImagePermission();
            }
        });
    }


    private void checkImagePermission() {


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getActivity(),"Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

}
