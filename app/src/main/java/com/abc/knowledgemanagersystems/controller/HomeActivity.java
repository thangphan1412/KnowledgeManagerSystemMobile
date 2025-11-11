package com.abc.knowledgemanagersystems.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.abc.knowledgemanagersystems.R;
import com.abc.knowledgemanagersystems.config.AuthPreferences;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private AuthPreferences authPreferences;

    // Khai báo các Activity đích
    private static final Class<?> EXPERIMENT_ACTIVITY = CreateExperimentActivity.class;
    private static final Class<?> HOME_ACTIVITY = HomeActivity.class;
    private static final Class<?> EQUIPMENT_ACTIVITY = EquipmentListActivity.class;
    private static final Class<?> SOPS_ACTIVITY = SDSLookupActivity.class;
    private static final Class<?> LOGIN_ACTIVITY = LoginController.class;
    private static final Class<?> EQUIPMENT_DETAIL_ACTIVITY = EquipmentDetailActivity.class;
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
        authPreferences = new AuthPreferences(this); // Khởi tạo AuthPreferences

        updateNavHeader();
        // 3. Cài đặt Toolbar
        setupToolbarMenu();

        // 4. Cài đặt xử lý click cho Navigation Drawer
        setupDrawerMenu();

        // ÁP DỤNG PHÂN QUYỀN (Ẩn/Hiện Menu)
        applyAuthorization();

        // 5. CÀI ĐẶT XỬ LÝ CLICK CHO BOTTOM NAVIGATION
        setupBottomNavigationView();

        // 6. Xử lý nút Back
        setupOnBackPressed();
    }

    // ------------------- CÁC PHƯƠNG THỨC XỬ LÝ CHỨC NĂNG -------------------

    /**
     * Phương thức MỚI: Áp dụng phân quyền cho cả Drawer và Bottom Nav.
     */
    private void applyAuthorization() {
        // Lấy vai trò của người dùng hiện tại
        String userRole = authPreferences.getRole();
        if (userRole == null) userRole = "";

        // 1. Phân quyền cho Navigation Drawer (Menu bên hông)
        Menu drawerMenu = navigationView.getMenu();

        // Mặc định ẩn các mục yêu cầu quyền đặc biệt (dựa trên file XML của Drawer)
        drawerMenu.findItem(R.id.nav_team).setVisible(false);
        drawerMenu.findItem(R.id.nav_protocols).setVisible(false);

        if (userRole.equals("MANAGER")) {
            drawerMenu.findItem(R.id.nav_team).setVisible(true);
            drawerMenu.findItem(R.id.nav_protocols).setVisible(true);
        }

        // 2. Phân quyền cho Bottom Navigation View (Thanh footer)
        // (Nếu cần thêm logic ẩn/hiện cho Bottom Nav, thêm ở đây)
    }


    /**
     * Xử lý sự kiện khi nhấn vào các mục của BottomNavigationView.
     */
    private void setupBottomNavigationView() {

        // Đặt mục Home được chọn mặc định
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                if (HomeActivity.this.getClass() != HOME_ACTIVITY) {
                    startActivity(new Intent(HomeActivity.this, HOME_ACTIVITY));
                }
                return true;
            } else if (itemId == R.id.navigation_experiment) {
                startActivity(new Intent(HomeActivity.this, EXPERIMENT_ACTIVITY));
                return true;
            } else if (itemId == R.id.navigation_equipment) {
                startActivity(new Intent(HomeActivity.this, EQUIPMENT_ACTIVITY));
                return true;
            } else if (itemId == R.id.navigation_inventory) {
                // Giả định có InventoryActivity
                startActivity(new Intent(HomeActivity.this, INVENTORY_ACTIVITY));
                return true;
            } else if (itemId == R.id.navigation_sops) {
                startActivity(new Intent(HomeActivity.this, PROTOCOL_ACTIVITY));
                return true;
            }

            return false;
        });
    }

    /**
     * Xử lý sự kiện khi nhấn nút Menu trên Toolbar để mở Navigation Drawer.
     */
    private void setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_menu) {
                drawerLayout.openDrawer(GravityCompat.END);
                return true;
            }
            return false;
        });
    }

    /**
     * Xử lý sự kiện khi nhấn vào các mục của Navigation Drawer.
     */
    private void setupDrawerMenu() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, USER_PROFILE_ACTIVITY));

            } else if (id == R.id.nav_team) {
                // Phân quyền đã được xử lý bằng applyAuthorization(), chỉ cần Intent
                startActivity(new Intent(HomeActivity.this, ADMIN_USER_ACTIVITY));

            } else if (id == R.id.nav_protocols) {
                startActivity(new Intent(HomeActivity.this, PROTOCOL_ACTIVITY)); // Đã sửa Intent đích

            } else if (id == R.id.nav_sds) {
                startActivity(new Intent(HomeActivity.this, SOPS_ACTIVITY));
                Toast.makeText(HomeActivity.this, "Mở SDS Lookup", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_setting) {
                startActivity(new Intent(HomeActivity.this, PROTOCOL_ACTIVITY));
            } else if(id == R.id.nav_Logout){

                authPreferences.clearAuthData(); // Xóa Token đã lưu
                Intent intent = new Intent(HomeActivity.this, LOGIN_ACTIVITY);
                // Dọn dẹp Activity Stack để người dùng không thể quay lại
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    /**
     * Xử lý nút Back.
     */
    private void setupOnBackPressed() {
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                // Khi menu mở, bấm Back sẽ đóng menu
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

    /**
     * Cập nhật thông tin người dùng trên Navigation Drawer Header.
     */
    private void updateNavHeader() {
        // 1. Lấy View của Header (index 0 vì thường chỉ có 1 header)
        View headerView = navigationView.getHeaderView(0);

        // 2. Ánh xạ các TextView trong Header View
        TextView userNameTextView = headerView.findViewById(R.id.nav_header_username);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);

        // 3. Truy vấn dữ liệu người dùng
        String userEmail = authPreferences.getUserEmail();
        String fullName = "User Name Loading...";

        if (userEmail != null && !userEmail.isEmpty()) {
            // Thay thế bằng logic truy vấn DB thực tế của bạn
            // Hiện tại, gán tên giả để kiểm tra:
            if (userEmail.contains("admin") || (authPreferences.getRole() != null && authPreferences.getRole().equals("MANAGER"))) {
                fullName = "Admin/Manager";
            } else {
                fullName = "Nhà Nghiên Cứu";
            }
        }

        // 4. Cập nhật giao diện
        userNameTextView.setText(fullName);
        emailTextView.setText(userEmail != null ? userEmail : "Chưa đăng nhập");
    }
}