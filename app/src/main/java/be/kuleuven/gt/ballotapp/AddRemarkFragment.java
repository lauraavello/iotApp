package be.kuleuven.gt.ballotapp;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import android.Manifest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class AddRemarkFragment extends Fragment {

    private EditText editRemarkTitle, editRemarkDetails;
    private Spinner spinnerBallotBox;
    private SeekBar seekBarUrgency;
    private TextView textUrgencyLevel;
    private ImageView imagePreview;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int CAMERA_PERMISSION_CODE = 100;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_remark, container, false);

        // Find UI elements
        editRemarkTitle = view.findViewById(R.id.edit_remark_title);
        editRemarkDetails = view.findViewById(R.id.edit_remark_details);
        spinnerBallotBox = view.findViewById(R.id.spinner_ballot_box);
        seekBarUrgency = view.findViewById(R.id.seekbar_urgency);
        textUrgencyLevel = view.findViewById(R.id.text_urgency_level);
        imagePreview = view.findViewById(R.id.image_preview);
        Button btnAttachImage = view.findViewById(R.id.btn_attach_image);
        Button btnSubmitRemark = view.findViewById(R.id.btn_submit_remark);

        // Populate the spinner with dummy ballot box names
        List<String> ballotBoxes = Arrays.asList("Box 1", "Box 2", "Box 3", "Box 4");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, ballotBoxes);
        spinnerBallotBox.setAdapter(adapter);

        // Update urgency level text when SeekBar is moved
        seekBarUrgency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textUrgencyLevel.setText("Urgency: " + (progress + 1));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Open image picker when attach image button is clicked
        btnAttachImage.setOnClickListener(v -> openImagePicker());

        // Submit the remark
        btnSubmitRemark.setOnClickListener(v -> submitRemark());

        return view;
    }

    // Open gallery to select an image
    private void openImagePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Image")
                .setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        openCamera();
                    } else {
                        openGallery();
                    }
                })
                .show();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                // Image picked from Gallery
                imageUri = data.getData();
                imagePreview.setImageURI(imageUri);
                imagePreview.setVisibility(View.VISIBLE);
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                // Image captured from Camera
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imagePreview.setImageBitmap(bitmap);
                imagePreview.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Submit Remark
    private void submitRemark() {
        String title = editRemarkTitle.getText().toString().trim();
        String box = spinnerBallotBox.getSelectedItem().toString();
        int urgency = seekBarUrgency.getProgress() + 1;
        String details = editRemarkDetails.getText().toString().trim();

        if (title.isEmpty() || details.isEmpty()) {
            Toast.makeText(getContext(), "Please enter title and details!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Remark Submitted!", Toast.LENGTH_SHORT).show();
    }
}

