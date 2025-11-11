package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.config.AuthPreferences;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private AuthPreferences authPreferences;

    // Khai báo các Activity đích
    // ✅ THAY THẾ bằng Activity Danh sách
    private static final Class<?> EXPERIMENT_ACTIVITY = ExperimentListActivity.class;
    private static final Class<?> HOME_ACTIVITY = HomeActivity.class;
    private static final Class<?> EQUIPMENT_ACTIVITY = EquipmentListActivity.class;
    private static final Class<?> SOPS_ACTIVITY = SDSLookupActivity.class;
    private static final Class<?> LOGIN_ACTIVITY = LoginController.class;
    private static final Class<?> PROTOCOL_ACTIVITY = ProtocolActivity.class;
    private static final Class<?> ADMIN_USER_ACTIVITY = AdminUserController.class;
    private static final Class<?> USER_PROFILE_ACTIVITY = UserProfileActivity.class;
    private static final Class<?> INVENTORY_ACTIVITY = InventoryManagementActivity.class;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. Ánh xạ các View và Khởi tạo Auth
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        authPreferences = new AuthPreferences(this);

        updateNavHeader();
        setupToolbarMenu();
        setupDrawerMenu();
        applyAuthorization();
        setupBottomNavigationView();

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        setupOnBackPressed();
    }

    // --- CÁC PHƯƠNG THỨC XỬ LÝ CHỨC NĂNG ---

    private void applyAuthorization() {
        String userRole = authPreferences.getRole();
        if (userRole == null) userRole = "";

        Menu drawerMenu = navigationView.getMenu();
        drawerMenu.findItem(R.id.nav_team).setVisible(false);
        drawerMenu.findItem(R.id.nav_protocols).setVisible(false);

        if (userRole.equals("MANAGER")) {
            drawerMenu.findItem(R.id.nav_team).setVisible(true);
            drawerMenu.findItem(R.id.nav_protocols).setVisible(true);
        }
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                if (HomeActivity.this.getClass() != HOME_ACTIVITY) {
                    startActivity(new Intent(HomeActivity.this, HOME_ACTIVITY));
                }
                return true;
            } else if (itemId == R.id.navigation_experiment) {
                // ✅ Chuyển đến màn hình Danh sách Thí nghiệm
                startActivity(new Intent(HomeActivity.this, EXPERIMENT_ACTIVITY));
                return true;
            } else if (itemId == R.id.navigation_equipment) {
                startActivity(new Intent(HomeActivity.this, EQUIPMENT_ACTIVITY));
                return true;
            } else if (itemId == R.id.navigation_inventory) {
                startActivity(new Intent(HomeActivity.this, INVENTORY_ACTIVITY));
                return true;
            } else if (itemId == R.id.navigation_sops) {
                startActivity(new Intent(HomeActivity.this, PROTOCOL_ACTIVITY));
                return true;
            }

            return false;
        });
    }

    private void setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_menu) {
                drawerLayout.openDrawer(GravityCompat.END);
                return true;
            }
            return false;
        });
    }

    private void setupDrawerMenu() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, USER_PROFILE_ACTIVITY));

            } else if (id == R.id.nav_team) {
                startActivity(new Intent(HomeActivity.this, ADMIN_USER_ACTIVITY));

            } else if (id == R.id.nav_protocols) {
                startActivity(new Intent(HomeActivity.this, PROTOCOL_ACTIVITY));

            } else if (id == R.id.nav_sds) {
                startActivity(new Intent(HomeActivity.this, SOPS_ACTIVITY));
                Toast.makeText(HomeActivity.this, "Mở SDS Lookup", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_setting) {
                startActivity(new Intent(HomeActivity.this, PROTOCOL_ACTIVITY));
            } else if(id == R.id.nav_Logout){

                authPreferences.clearAuthData();
                Intent intent = new Intent(HomeActivity.this, LOGIN_ACTIVITY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void setupOnBackPressed() {
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        };

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                onBackPressedCallback.setEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                onBackPressedCallback.setEnabled(false);
            }
        });
    }

    private void updateNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.nav_header_username);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);

        String userEmail = authPreferences.getUserEmail();
        String fullName = "User Name Loading...";

        if (userEmail != null && !userEmail.isEmpty()) {
            if (userEmail.contains("admin") || (authPreferences.getRole() != null && authPreferences.getRole().equals("MANAGER"))) {
                fullName = "Admin/Manager";
            } else {
                fullName = "Nhà Nghiên Cứu";
            }
        }

        userNameTextView.setText(fullName);
        emailTextView.setText(userEmail != null ? userEmail : "Chưa đăng nhập");
    }
}