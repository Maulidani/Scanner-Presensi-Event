package com.app.scannerpresensievent.ui;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.app.scannerpresensievent.model.ResponseModel;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.skripsi.traditionalfood.network.ApiClient;
import com.skripsi.traditionalfood.network.ApiService;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
//                        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_LONG).show();

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


                if (response.isSuccessful() && response.code() == 200) {
                    String message = response.body().getMessage();

                    if (message.equals("Success")) {

                        showDialog(message);
                    } else {

                        showDialog(message);
                    }

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");

                        showDialog(message);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Periksa koneksi internet", Toast.LENGTH_LONG).show();
                Log.e("onResponse: ", t.toString());
                showDialog("Gagal, terjadi kesalahan");
            }
        });
    }

    private void showDialog(String status) {

        pbLoading.setVisibility(View.INVISIBLE);

        if (status.equals("Success")) {

            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            pDialog.setTitleText(status);
            pDialog.setContentText("Berhasil Scan");
            pDialog.setCancelable(false);
            pDialog.show();

        } else {

            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText(status);
            pDialog.setCancelable(false);
            pDialog.show();
        }

    }
}