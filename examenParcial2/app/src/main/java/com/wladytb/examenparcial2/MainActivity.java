package com.wladytb.examenparcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.*;
import com.google.mlkit.vision.label.*;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnTomarFoto, btnSeleccionarFoto;
    ExtendedFloatingActionButton btnAccion;
    TextView txtNameTomarFoto, txtNameSeleccionaFoto, txtResul;
    Boolean bandera;
    ImageView imagen;
    ImageLabeler labeler;
    InputImage inputImage = null;
    Button btnMapa;
    String pais = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        setContentView(R.layout.activity_main);
        btnAccion = findViewById(R.id.btnAccion);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        imagen = (ImageView) findViewById(R.id.imagemId);
        txtResul = (TextView) findViewById(R.id.txtResul);
        txtNameTomarFoto = findViewById(R.id.txtNameTomarFoto);
        txtNameSeleccionaFoto = findViewById(R.id.txtNameSeleccionaFoto);
        btnSeleccionarFoto.setVisibility(View.GONE);
        btnTomarFoto.setVisibility(View.GONE);
        txtNameTomarFoto.setVisibility(View.GONE);
        txtNameSeleccionaFoto.setVisibility(View.GONE);

        btnMapa = (Button) findViewById(R.id.btnMapa);
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pais.equals("")) {
                    Toast.makeText(MainActivity.this, "No hay resultados de país.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, mapa.class);
                    intent.putExtra("pais", pais);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
        bandera = false;
        btnAccion.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!bandera)
                            flotarBtns();
                        else
                            closeBtns();
                    }
                });
        btnSeleccionarFoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selecionaImagen();
                    }
                });
        btnTomarFoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                tomarFoto();
                            } else
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        } else
                            tomarFoto();
                    }
                });

    }

    public void flotarBtns() {
        btnSeleccionarFoto.show();
        btnTomarFoto.show();
        txtNameTomarFoto.setVisibility(View.VISIBLE);
        txtNameSeleccionaFoto.setVisibility(View.VISIBLE);
        btnAccion.setIconResource(R.drawable.close_foreground);
        bandera = true;
    }

    public void closeBtns() {
        btnSeleccionarFoto.hide();
        btnTomarFoto.hide();
        txtNameTomarFoto.setVisibility(View.GONE);
        txtNameSeleccionaFoto.setVisibility(View.GONE);
        btnAccion.setIconResource(R.drawable.more_foreground);
        bandera = false;
    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
        closeBtns();
    }

    private void selecionaImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), 10);
        closeBtns();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                tomarFoto();
            else
                Toast.makeText(MainActivity.this, "Permiso de Cámara negado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imagen.setImageBitmap(bitmap);
            try {
                inputImage = InputImage.fromBitmap(bitmap, 0);
                processText(inputImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == 10) {
            Uri path = data.getData();
            imagen.setImageURI(path);
            try {
                inputImage = InputImage.fromFilePath(MainActivity.this, path);
                processText(inputImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processText(InputImage inputImage) {
        if (inputImage == null)
            return;
        TextRecognition.getClient().process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        if (visionText.getText().equals("")) {
                            txtResul.setText("");
                            txtResul.setText("No hay texto");
                        } else {
                            txtResul.setText("");
                            txtResul.setText(visionText.getText());
                            pais = visionText.getText();
                        }
                        Toast.makeText(getApplicationContext(), "Texto detectado!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                txtResul.setText("");
                                txtResul.setText("No hay texto");
                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                            }
                        });
    }
}