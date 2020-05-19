package com.example.grocerylisting.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerylisting.Adapters.EditIngrListAdapter;
import com.example.grocerylisting.CommonMethods.Common;
import com.example.grocerylisting.ModelManagers.IngredientManager;
import com.example.grocerylisting.ModelManagers.MyProductsDbManager;
import com.example.grocerylisting.ModelManagers.RecipeManager;
import com.example.grocerylisting.Models.Product;
import com.example.grocerylisting.Models.Recipe;
import com.example.grocerylisting.R;
import com.example.grocerylisting.Adapters.ViewPagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    ViewPagerAdapter adapter;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton fabRecipe, fabProduct;

    RecipeManager recipeManager;
    IngredientManager ingredientManager;
    boolean ingrCheck = false;
    boolean recCheck = false;

    Dialog popupAddRecipe;
    Uri pickedImgUri = null;
    ListView ingrList;
    ImageView addRecipeBtn;
    ImageView addedRecipeImage;
    Button addIngrAndUomBtn;
    TextView addRecipeTitle, addRecipeInstr, addRecipeAuthor;
    TextView addIngr, addUom;
    ProgressBar addClickProgress;
    EditIngrListAdapter editIngrListAdapter;
    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;

    Dialog popupAddProduct;
    Button addByView, addByText, addByBarcode, addManually, closeAddProduct;

    Dialog popupAddProductManually;
    Button acceptManualProduct, closeAddManuallyProduct;
    EditText inptManualProduct;

    MyProductsDbManager myProductsDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        fabRecipe = (FloatingActionButton) findViewById(R.id.adds_recipe_btn);
        fabProduct = (FloatingActionButton) findViewById(R.id.add_product_btn);

        initiatePopup();
        setClickOnAddBtn();
        initiateAddProductPopup();
        initiateAddProductManually();

        fabRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FloatingActionButton) view.findViewById(R.id.adds_recipe_btn)).hide();
                popupAddRecipe.show();
            }
        });

        fabProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabProduct.setVisibility(View.INVISIBLE);
                popupAddProduct.show();
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch ((tab.getPosition()))
                {
                    case 0:
                        fabRecipe.setVisibility(View.VISIBLE);
                        fabProduct.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        adapter.getSelectedRecipes().updateSelectedRecipes();
                        fabRecipe.setVisibility(View.INVISIBLE);
                        fabProduct.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        fabRecipe.setVisibility(View.INVISIBLE);
                        fabProduct.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        fabRecipe.setVisibility(View.INVISIBLE);
                        fabProduct.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // Add Recipe dialog

    private void initiatePopup() {
        popupAddRecipe = new Dialog(MainActivity.this, R.style.MyDialogTheme);
        popupAddRecipe.setContentView(R.layout.popup_add_recipe);
        popupAddRecipe.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        popupAddRecipe.getWindow().getAttributes().gravity = Gravity.TOP;


        ingrList = popupAddRecipe.findViewById(R.id.editIngredients);
        editIngrListAdapter = new EditIngrListAdapter(MainActivity.this);
        ingrList.setAdapter(editIngrListAdapter);

        addIngr = popupAddRecipe.findViewById(R.id.editIngr);
        addUom = popupAddRecipe.findViewById(R.id.editUom);
        addIngrAndUomBtn = (Button) popupAddRecipe.findViewById(R.id.addIngrAndUomBtn);

        addIngrAndUomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(MainActivity.this);
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
                    final StorageReference imagePath = store.child(Common.getFileName(MainActivity.this,pickedImgUri));
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
                                                Toast.makeText(MainActivity.this, "Рецепт добавлен успешно", Toast.LENGTH_LONG).show();
                                                addRecipeBtn.setVisibility(View.VISIBLE);
                                                addRecipeBtn.bringToFront();
                                                addClickProgress.setVisibility(View.INVISIBLE);
                                                popupAddRecipe.dismiss();
                                            }
                                        });
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, "Возникла ошибка. Обратитесь к администратору", Toast.LENGTH_LONG).show();
                                        addRecipeBtn.setVisibility(View.VISIBLE);
                                        addClickProgress.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                    addRecipeBtn.setVisibility(View.VISIBLE);
                                    addClickProgress.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Заполните поля и загрузите изображение",Toast.LENGTH_LONG).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult res = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (res != null) {
            if (res.getContents() != null) {

                // Get EAN13 of product
                String ean13 = res.getContents();
                // Get Name of product
                String addedProductTitle = getTitleByEan();
                // Add product
                myProductsDbManager.addProduct(new Product(addedProductTitle));
                popupAddProduct.dismiss();
                Toast.makeText(MainActivity.this,"Продукт успешно добавлен", Toast.LENGTH_SHORT).show();
                adapter.getMyProducts().updateListOfProducts();
                fabProduct.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == 140 && data != null)
            {
                String addedProductTitle = data.getStringExtra("prodName");
                myProductsDbManager = new MyProductsDbManager(MainActivity.this);
                myProductsDbManager.addProduct(new Product(addedProductTitle));
                popupAddProduct.dismiss();
                Toast.makeText(MainActivity.this,"Продукт успешно добавлен", Toast.LENGTH_SHORT).show();
                adapter.getMyProducts().updateListOfProducts();
                fabProduct.setVisibility(View.VISIBLE);
            }
            else if (resultCode == MainActivity.this.RESULT_OK && requestCode == REQUESCODE && data != null)
            {
                pickedImgUri = data.getData();
                addedRecipeImage.setImageURI(pickedImgUri);
            }
        }
    }

    private String getTitleByEan() {
        return "empty";
    }

    private void checkImagePermission() {


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(MainActivity.this,"Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,
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

    // Add Product dialog

    private void initiateAddProductPopup() {
        popupAddProduct = new Dialog(MainActivity.this);
        popupAddProduct.setContentView(R.layout.popup_add_product);
        popupAddProduct.getWindow().getAttributes().gravity = Gravity.CENTER;


        addByView = popupAddProduct.findViewById(R.id.add_product_view_btn);
        addByText = popupAddProduct.findViewById(R.id.add_product_text_btn);
        addByBarcode = popupAddProduct.findViewById(R.id.add_product_barcode_btn);
        addManually = popupAddProduct.findViewById(R.id.add_product_manually_btn);
        closeAddProduct = popupAddProduct.findViewById(R.id.close_popup_product);

        closeAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddProduct.dismiss();
                fabProduct.setVisibility(View.VISIBLE);
            }
        });

        addByView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detector = new Intent(MainActivity.this, DetectorActivity.class);
                startActivityForResult(detector, 176);
            }
        });

        addByText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addByBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanEanCode();
            }
        });

        addManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddProduct.dismiss();
                popupAddProductManually.show();
            }
        });

    }

    private  void initiateAddProductManually() {

        popupAddProductManually = new Dialog(MainActivity.this);
        popupAddProductManually.setContentView(R.layout.popup_add_product_manual);
        popupAddProductManually.getWindow().getAttributes().gravity = Gravity.CENTER;


        acceptManualProduct = popupAddProductManually.findViewById(R.id.accepy_manual_btn);
        closeAddManuallyProduct = popupAddProductManually.findViewById(R.id.close_popup_manual_product);
        inptManualProduct = popupAddProductManually.findViewById(R.id.manual_title_edit);
        inptManualProduct.setText("");

        acceptManualProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productTitle = inptManualProduct.getText().toString();
                if (productTitle.isEmpty()) {
                    Toast.makeText(MainActivity.this,"Введите название продукта", Toast.LENGTH_SHORT).show();
                }
                else {
                    myProductsDbManager = new MyProductsDbManager(MainActivity.this);
                    myProductsDbManager.addProduct(new Product(productTitle));
                    popupAddProductManually.dismiss();
                    Toast.makeText(MainActivity.this,"Продукт успешно добавлен", Toast.LENGTH_SHORT).show();
                    adapter.getMyProducts().updateListOfProducts();
                    fabProduct.setVisibility(View.VISIBLE);
                }
            }
        });

        closeAddManuallyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddProduct.show();
                popupAddProductManually.dismiss();
                fabProduct.setVisibility(View.VISIBLE);
            }
        });
    }

    public void scanEanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setCaptureActivity(CaptureBarcodeActivity.class);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13);
        intentIntegrator.setPrompt("Сканирование штрих-кода");
        intentIntegrator.initiateScan();
    }

}
