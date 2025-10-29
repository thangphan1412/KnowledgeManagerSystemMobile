package com.abc.knowledgemanagersystems; // Đảm bảo tên package của bạn là chính xác

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. SỬA LỖI CRASH: Nạp đúng file layout chứa DrawerLayout
        // File activity_main.xml của bạn chứa layout_homepage.xml bên trong nó.
        setContentView(R.layout.activity_main);

        // 2. Ánh xạ các View
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // 3. Ánh xạ Toolbar
        // LƯU Ý: toolbar nằm BÊN TRONG layout_homepage.xml,
        // nhưng vì layout_homepage đã được <include> vào activity_main,
        // chúng ta vẫn có thể tìm thấy nó bình thường.
        toolbar = findViewById(R.id.toolbar);
        // (Hãy chắc chắn ID của toolbar trong layout_homepage.xml là "toolbar")

        // 4. Cài đặt Toolbar để mở Menu
        toolbar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Đảm bảo menu của toolbar có 1 item tên 'action_menu'
                if (item.getItemId() == R.id.action_menu) {
                    drawerLayout.openDrawer(GravityCompat.END); // Mở menu từ bên phải
                    return true;
                }
                return false;
            }
        });

        // 5. Cài đặt xử lý click cho các item trong Menu (NavigationView)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // Kiểm tra xem người dùng bấm vào item nào
                if (id == R.id.nav_home) {
                    Toast.makeText(HomeActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_equipment) {
                    // Mở màn hình Chi tiết Thiết bị
                    // (Yêu cầu bạn đã tạo EquipmentDetailActivity.java và khai báo trong Manifest)
                    Intent intent = new Intent(HomeActivity.this, EquipmentDetailActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_inventory) {
                    Toast.makeText(HomeActivity.this, "Mở Inventory", Toast.LENGTH_SHORT).show();
                    // TODO: Mở InventoryActivity (nếu có)

                } else if (id == R.id.nav_experiment) {
                    Toast.makeText(HomeActivity.this, "Mở Experiment", Toast.LENGTH_SHORT).show();
                    // TODO: Mở CreateExperimentActivity (nếu có)

                } else if (id == R.id.nav_sops) {
                    Toast.makeText(HomeActivity.this, "Mở SOPs", Toast.LENGTH_SHORT).show();
                    // TODO: MSopsActivity (nếu có)
                }

                // Đóng menu lại sau khi đã xử lý click
                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        // 6. Xử lý nút Back (Cách mới, tương thích cử chỉ vuốt)
        setupOnBackPressed();
    }

    /**
     * Cài đặt OnBackPressedDispatcher để thay thế cho onBackPressed() đã cũ.
     * Logic: Khi menu mở, bấm Back sẽ đóng menu. Khi menu đóng, bấm Back sẽ thoát app.
     */
    private void setupOnBackPressed() {
        // Tạo một Callback mới, ban đầu tắt (false)
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                // Khi callback này được BẬT, nó chỉ làm 1 việc: đóng menu
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        };

        // Thêm callback vào dispatcher
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        // Thêm listener vào DrawerLayout để BẬT/TẮT callback một cách linh động
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                // Khi menu MỞ, BẬT callback lên
                onBackPressedCallback.setEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Khi menu ĐÓNG, TẮT callback đi
                onBackPressedCallback.setEnabled(false);
            }
        });
    }

    // KHÔNG CẦN override onBackPressed() cũ nữa.
}