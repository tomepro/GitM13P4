package com.example.spotify;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivityMTJ extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    public Button biblioteca;

    private HomeFragmentMTJ fragmentHome;
    private SearchFragmentMTJ fragmentSearch;
    private ProfileFragmentMTJ fragmentProf;

    private static final int REQUEST_EXTERNAL_AUDIO = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_MEDIA_AUDIO
    };

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissionsResult -> {
                if (permissionsResult.containsValue(Boolean.TRUE)) {
                    performLogic();
                } else {
                    Toast.makeText(this, "Los permisos fueron denegados.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomnavigationnav);


        fragmentHome = new HomeFragmentMTJ();
        fragmentSearch = new SearchFragmentMTJ();
        fragmentProf = new ProfileFragmentMTJ();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragmentHome).commit();

        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment;

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                selectedFragment = fragmentHome;
            } else if (itemId == R.id.search) {
                selectedFragment = fragmentSearch;
            } else if (itemId == R.id.profile) {
                selectedFragment = fragmentProf;
            } else {
                selectedFragment = fragmentHome;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
            return true;
        });

        checkAndRequestStoragePermissions();
    }

    //WOW ES EL MILLOR MMORPG!!!

    private void checkAndRequestStoragePermissions() {
        if (checkStoragePermissions()) {
            performLogic();
        } else {
            requestPermissionLauncher.launch(PERMISSIONS_STORAGE);
        }
    }

    private boolean checkStoragePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void performLogic() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performLogic();
            } else {
                Toast.makeText(this, "Los permisos fueron denegados tu fruta madre.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}//HOLAHOLAHOLA
