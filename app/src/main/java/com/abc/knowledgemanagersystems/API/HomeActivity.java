//package com.abc.knowledgemanagersystems.API;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.activity.OnBackPressedCallback;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//
//import com.abc.knowledgemanagersystems.R;
//import com.abc.knowledgemanagersystems.controller.CreateExperimentActivity;
//import com.abc.knowledgemanagersystems.controller.EquipmentDetailActivity;
//import com.google.android.material.appbar.MaterialToolbar;
//import com.google.android.material.navigation.NavigationView;
//import com.google.android.material.bottomnavigation.BottomNavigationView; // 📢 Thêm Import này
//import com.google.android.material.navigation.NavigationBarView; // 📢 Thêm Import này
//
//public class HomeActivity extends AppCompatActivity {
//
//    private DrawerLayout drawerLayout;
//    private MaterialToolbar toolbar;
//    private NavigationView navigationView;
//
//    private BottomNavigationView bottomNavigationView; // 📢 Khai báo Bottom Nav
//
//    // Đảm bảo bạn đã khai báo Activity này trong Manifest
//    private static final Class<?> EXPERIMENT_ACTIVITY = CreateExperimentActivity.class;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // 1. Nạp Layout
//        setContentView(R.layout.activity_main);
//
//        // 2. Ánh xạ các View
//        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.navigation_view);
//        toolbar = findViewById(R.id.toolbar);
//        bottomNavigationView = findViewById(R.id.bottom_navigation_view); // 📢 Ánh xạ Bottom Nav
//
//        // 3. Cài đặt Toolbar để mở Menu (Navigation Drawer)
//        setupToolbarMenu();
//
//        // 4. Cài đặt xử lý click cho Navigation Drawer
//        setupDrawerMenu();
//
//        // 5. Cài đặt xử lý click cho Bottom Navigation View 📢 PHẦN MỚI
//        setupBottomNavigationView();
//
//        // 6. Xử lý nút Back
//        setupOnBackPressed();
//    }
//
//    // -----------------------------------------------------------
//    // 📢 PHƯƠNG THỨC MỚI: Xử lý Bottom Navigation View
//    // -----------------------------------------------------------
//
//    private void setupBottomNavigationView() {
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                int itemId = item.getItemId();
//
//                //  Kiểm tra ID của mục Experiment trong bottom_nav_manu.xml
//                if (itemId == R.id.nav_experiment) {
//                    // Chuyển sang màn hình Tạo Thí nghiệm
//                    Intent intent = new Intent(HomeActivity.this, EXPERIMENT_ACTIVITY);
//                    startActivity(intent);
//
//                    // Bạn có thể không muốn kết thúc HomeActivity,
//                    // nhưng nếu bạn muốn nó trở lại màn hình Home, hãy giữ nó.
//                    // finish();
//
//                    return true;
//                }
//
//                // 📢 Xử lý mục Home (ví dụ: cuộn lên đầu hoặc không làm gì)
//                else if (itemId == R.id.nav_home) {
//                    Toast.makeText(HomeActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//
//                // 📢 Xử lý mục Inventory (ví dụ)
//                else if (itemId == R.id.nav_inventory) {
//                    Toast.makeText(HomeActivity.this, "Mở Inventory từ Footer", Toast.LENGTH_SHORT).show();
//                    // Intent intent = new Intent(HomeActivity.this, InventoryActivity.class);
//                    // startActivity(intent);
//                    return true;
//                }
//
//                // Đảm bảo ID nav_experiment đã có trong file res/menu/bottom_nav_manu.xml
//
//                return false;
//            }
//        });
//
//        // Đặt mục Home được chọn mặc định khi Activity khởi tạo
//        bottomNavigationView.setSelectedItemId(R.id.nav_home);
//    }
//
//    // -----------------------------------------------------------
//    // 📢 CÁC PHƯƠNG THỨC GỐC ĐỂ DỄ QUẢN LÝ
//    // -----------------------------------------------------------
//
//    private void setupToolbarMenu() {
//        toolbar.setOnMenuItemClickListener(item -> {
//            if (item.getItemId() == R.id.action_menu) {
//                drawerLayout.openDrawer(GravityCompat.END);
//                return true;
//            }
//            return false;
//        });
//    }
//
//    private void setupDrawerMenu() {
//        navigationView.setNavigationItemSelectedListener(item -> {
//            int id = item.getItemId();
//
//            // Logic xử lý Navigation Drawer... (Giữ nguyên logic cũ của bạn)
//            if (id == R.id.navigation_home) {
//                Toast.makeText(HomeActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();
//            } else if (id == R.id.navigation_inventory) {
//                Intent intent = new Intent(HomeActivity.this, EquipmentDetailActivity.class);
//                startActivity(intent);
//            } else if (id == R.id.navigation_experiment) {
//                // 📢 Đã có logic chuyển màn hình trong Bottom Nav, nhưng giữ ở đây nếu cần
//                Toast.makeText(HomeActivity.this, "Mở Experiment từ Drawer", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(HomeActivity.this, EXPERIMENT_ACTIVITY);
//                startActivity(intent);
//            }
//            // ... (các mục khác)
//
//            drawerLayout.closeDrawer(GravityCompat.END);
//            return true;
//        });
//    }
//
//    private void setupOnBackPressed() {
//        // Logic xử lý nút Back (Giữ nguyên logic cũ của bạn)
//        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
//            @Override
//            public void handleOnBackPressed() {
//                drawerLayout.closeDrawer(GravityCompat.END);
//            }
//        };
//        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
//
//        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                onBackPressedCallback.setEnabled(true);
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                onBackPressedCallback.setEnabled(false);
//            }
//        });
//    }
//}