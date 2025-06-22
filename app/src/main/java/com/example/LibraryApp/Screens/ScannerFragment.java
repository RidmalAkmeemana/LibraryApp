package com.example.LibraryApp.Screens;

import android.Manifest;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

public class ScannerFragment extends AppCompatActivity {

    private DecoratedBarcodeView barcodeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        barcodeScannerView = new DecoratedBarcodeView(this);
        setContentView(barcodeScannerView);

        // Request camera permission using Dexter
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        barcodeScannerView.decodeContinuous(callback);
                        barcodeScannerView.resume();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ScannerFragment.this, "Camera permission is required to scan QR codes", Toast.LENGTH_LONG).show();
                        finish(); // Close activity if permission denied
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result == null) {
                return;
            }
            // Stop scanning after first result
            barcodeScannerView.pause();

            // Send result back or update UI
            String scannedText = result.getText();
            if (scannedText != null) {
                // Example: update static variable or use Intent extras
                SubmitFragment.scan_code.setText(scannedText);
            }

            // Close this scanner activity
            finish();
        }

        @Override
        public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {
            // Optional: can be left empty
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barcodeScannerView.pauseAndWait();
    }
}
