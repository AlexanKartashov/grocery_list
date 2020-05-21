package com.example.grocerylisting.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerylisting.CustomView.GraphicOverlay;
import com.example.grocerylisting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.Arrays;
import java.util.List;

public class TextRecognitionActivity extends AppCompatActivity {

    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    Button recText;
    ImageView blackOverlay;

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
        recText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_rec_activity);

        cameraView = (CameraView)findViewById(R.id.camera_view);
        graphicOverlay = (GraphicOverlay)findViewById(R.id.graphic_overlay);
        recText = (Button)findViewById(R.id.btn_capture_text);
        blackOverlay = findViewById(R.id.imageView278);
        blackOverlay.setVisibility(View.INVISIBLE);
        recText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = cameraKitImage.getBitmap();
                recText.setVisibility(View.INVISIBLE);
                blackOverlay.setVisibility(View.VISIBLE);
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(),
                        cameraView.getHeight(), false);
                Bitmap destBmp;
                if (bitmap.getWidth() >= bitmap.getHeight()){

                    destBmp = Bitmap.createBitmap(
                            bitmap,
                            bitmap.getWidth()/2 - bitmap.getHeight()/2,
                            0,
                            bitmap.getHeight(),
                            bitmap.getHeight()
                    );

                }else{

                    destBmp = Bitmap.createBitmap(
                            bitmap,
                            0,
                            bitmap.getHeight()/2 - bitmap.getWidth()/2,
                            bitmap.getWidth(),
                            bitmap.getWidth()
                    );
                }
                recognizeText(destBmp);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    private void recognizeText(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("ru"))
                .build();

        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getCloudTextRecognizer(options);

        textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        getTextResult(firebaseVisionText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("EDMT_ERROR", e.getMessage());
            }
        });
    }

    private void getTextResult(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();

        if (blocks.size() == 0) {
            Toast.makeText(this, "Текст не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        graphicOverlay.clear();


        StringBuilder text = new StringBuilder();

        for (int i=0;i<blocks.size();i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();

            for (int j=0; j<lines.size();j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();

                for (int k=0;k<elements.size();k++) {
                    String elText = elements.get(k).getText();
                    if (!(anotherCheck(elText))) {
                        text.append(elText + " ");
                    }
                }
            }
        }

        Intent data = new Intent();
        data.putExtra("prodName",text.toString());
        setResult(130, data);
        finish();
    }

    public static boolean anotherCheck(String x)
    {
        if (x.length() < 3)
            return true;
        for (int i = 0; i < x.length(); i++)
        {
            char c = x.charAt(i);
            if (c == '(' || c == ')' || c == '/' || c == '.' || c == '~')
                return true;
            if ((c >= 'A') && (c <= 'Z'))
                return true;
            if ((c >= '0') && (c <= '9'))
                return true;
            if ((c >= 'a') && (c <= 'z'))
                return true;
        }
        return false;
    }

}
