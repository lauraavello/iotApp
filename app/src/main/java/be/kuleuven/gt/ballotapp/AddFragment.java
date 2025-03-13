package be.kuleuven.gt.ballotapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AddFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Find buttons
        Button btnAddRemark = view.findViewById(R.id.btn_add_remark);
        Button btnAddBox = view.findViewById(R.id.btn_add_box);

        // Handle clicks to navigate to different fragments
        btnAddRemark.setOnClickListener(v -> navigateTo(new AddRemarkFragment()));
        btnAddBox.setOnClickListener(v -> navigateTo(new AddBoxFragment()));

        return view;
    }

    private void navigateTo(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_home, fragment);
        transaction.addToBackStack(null);  // Allows going back to this fragment
        transaction.commit();
    }
}
