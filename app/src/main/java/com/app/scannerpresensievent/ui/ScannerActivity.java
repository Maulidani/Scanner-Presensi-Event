package com.app.scannerpresensievent.ui;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.scannerpresensievent.R;
import com.app.scannerpresensievent.model.DataModel;
import com.app.scannerpresensievent.model.ResponseModel;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.skripsi.traditionalfood.network.ApiClient;
import com.skripsi.traditionalfood.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerActivity extends AppCompatActivity {

    private CodeScanner codeScanner;
    private ProgressBar pbLoading;
    private static final int REQUEST_CODE_CAMERA = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        getSupportActionBar().hide();

        ConstraintLayout layoutScanner = findViewById(R.id.layoutScanner);
        CodeScannerView scannerView = findViewById(R.id.scanner);
        pbLoading = findViewById(R.id.pbLoading);

        codeScanner = new CodeScanner(this, scannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scanner(result.getText());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });

//        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void scanner(String idKegiatan) {

        pbLoading.setVisibility(View.VISIBLE);

        ApiService api = ApiClient.INSTANCE.getInstances();
        Call<ResponseModel> call = api.scanner(idKegiatan);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                if (response.isSuccessful()) {
                    String message = response.body().getMessage();

                    if (message.equals("Success")) {
                        DataModel data = response.body().getData();
                        showDialog(data.getStatus());
                    } else {
                        showDialog("Gagal");
                    }
                    Log.e("onResponse: ", response.body().toString());
                } else {

                    showDialog("Gagal");
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Periksa koneksi internet", Toast.LENGTH_LONG).show();
                Log.e("onResponse: ", t.toString());
                showDialog("Gagal");
            }
        });
    }

    private void showDialog(String status) {

        pbLoading.setVisibility(View.INVISIBLE);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Status");

        // set pesan dari dialog

        if (status.equals("Gagal")) {
            alertDialogBuilder
                    .setMessage("Gagal / Event tidak ada")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // do something
                            codeScanner.startPreview();

                        }
                    });
        } else {
            alertDialogBuilder
                    .setMessage(status+" / Berhasil Scan")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // do something
                            codeScanner.startPreview();

                        }
                    });
        }

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}