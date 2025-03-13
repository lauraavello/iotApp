package be.kuleuven.gt.ballotapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import be.kuleuven.gt.ballotapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();  // Return the root view
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Example data
        int totalVoters = 1000;
        int party1Votes = 400;   // Blue
        int party2Votes = 300;   // Red
        int voted = party1Votes + party2Votes;
        int notVoted = totalVoters - voted; // Grey part

        // Update progress bar values
        binding.stackedProgressBar.setMax(totalVoters);
        binding.stackedProgressBar.setProgress(party2Votes);  // Red (top layer)
        binding.stackedProgressBar.setSecondaryProgress(voted);  // Blue + Red
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
