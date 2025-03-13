package be.kuleuven.gt.ballotapp;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AddBoxFragment extends Fragment {

    private EditText editBoxId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_box, container, false);

        // Find UI elements
        Button btnScanQr = view.findViewById(R.id.btn_scan_qr);
        Button btnAddBox = view.findViewById(R.id.btn_add_box);
        editBoxId = view.findViewById(R.id.edit_box_id);

        // Open QR Scanner
        btnScanQr.setOnClickListener(v -> startQrScanner());

        // Add Box Manually
        btnAddBox.setOnClickListener(v -> addBox());

        return view;
    }

    // Open QR Scanner
    private void startQrScanner() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan the QR code of the Ballot Box");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    // Handle QR Scan Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Set scanned QR code to EditText
                editBoxId.setText(result.getContents());
                Toast.makeText(getContext(), "QR Code Scanned!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "QR Scan Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Add Box with ID (Either from QR Code or Manual Input)
    private void addBox() {
        String boxId = editBoxId.getText().toString().trim();

        if (boxId.isEmpty()) {
            Toast.makeText(getContext(), "Please enter or scan a Box ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Store box ID in database or process it
        Toast.makeText(getContext(), "Box Added: " + boxId, Toast.LENGTH_SHORT).show();
    }
}

