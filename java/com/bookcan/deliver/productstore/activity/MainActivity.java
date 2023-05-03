package com.bookcan.deliver.productstore.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bookcan.deliver.productstore.interfaces.BookCanApiService;
import com.bookcan.deliver.productstore.model.Bookcanproduct;
import com.bookcan.deliver.productstore.model.ProductArr;
import com.bookcan.deliver.productstore.util.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.fragment.CategoryFragment;
import com.bookcan.deliver.productstore.fragment.HomeFragment;
import com.bookcan.deliver.productstore.fragment.MyOrderFragment;
import com.bookcan.deliver.productstore.fragment.NewProductFragment;
import com.bookcan.deliver.productstore.fragment.OffrersFragment;
import com.bookcan.deliver.productstore.fragment.PopularProductFragment;
import com.bookcan.deliver.productstore.fragment.ProfileFragment;
import com.bookcan.deliver.productstore.helper.Converter;
import com.bookcan.deliver.productstore.model.User;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener  {
    private static int cart_count = 0;
    User user;
    BottomNavigationView bottomNavigationView;
    @SuppressLint("ResourceAsColor")
    static void centerToolbarTitle(@NonNull final Toolbar toolbar) {
        final CharSequence title = toolbar.getTitle();
        final ArrayList<View> outViews = new ArrayList<>(1);
        toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
        if (!outViews.isEmpty()) {
            final TextView titleView = (TextView) outViews.get(0);
            titleView.setGravity(Gravity.CENTER);
            titleView.setTextColor(Color.parseColor("#243965"));
            final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            toolbar.requestLayout();
            //also you can use titleView for changing font: titleView.setTypeface(Typeface);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
//            HomeFragment myFragment = (HomeFragment)getSupportFragmentManager().findFragmentByTag("Home");
//            if (myFragment != null && myFragment.isVisible()) {
//                super.onBackPressed();
//                finishAffinity();
//                finish();
//            }else {
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
//                ft.replace(R.id.content_frame, new HomeFragment(),"Home");
//                ft.commit();
//            }
            super.onBackPressed();
            finishAffinity();
            finish();
        }
    }



    /*public void toggleCommunicationGroup(View button) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        MenuItem group = navigationView.getMenu().findItem(R.id.nav_communication_group);
        boolean isVisible = group.isVisible();
        group.setVisible(!isVisible);
        Button toggleButton = (Button)findViewById(R.id.main_toggle_button);
        if (isVisible) {
            toggleButton.setText("Enable communication group");
        } else {
            toggleButton.setText("Disable communication group");
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.ic_shopping_cart_black_24dp));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_action:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        centerToolbarTitle(toolbar);
        cart_count = cartCount();


        localStorage = new LocalStorage(getApplicationContext());
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(null); //or you can add icon
        //toggle.setHomeAsUpIndicator(R.id.icon);//add this for custom icon


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        TextView nav_user = hView.findViewById(R.id.nav_header_name);
        LinearLayout nav_footer = findViewById(R.id.footer_text);
        if (user != null) {
            nav_user.setText(user.getName());
        }
        nav_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localStorage.logoutUser();
                startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                // Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
            }
        });

        //displaySelectedScreen(0);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.product);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        String Tag = "empty";
        switch (item.getItemId()) {
            case R.id.product:
                Tag = "Home";
                fragment = new HomeFragment();
                ft.replace(R.id.content_frame, fragment,Tag);
                ft.commit();
                return true;

            case R.id.dashboard:
                fragment = new NewProductFragment();
                ft.replace(R.id.content_frame, fragment,Tag);
                ft.commit();
                return true;

            case R.id.account:
                DrawerLayout navDrawer = findViewById(R.id.drawer_layout);
                if (navDrawer.isDrawerOpen(GravityCompat.END)) {
                    navDrawer.closeDrawer(GravityCompat.END);
                }
                else {
                    navDrawer.openDrawer(GravityCompat.END);
                }
//                if(!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START);
//                else navDrawer.closeDrawer(GravityCompat.END);
                return true;
            default:
                displaySelectedScreen(item.getItemId());
                return true;
        }
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        String Tag = "empty";
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_dash:
                fragment = new NewProductFragment();
                break;
            case R.id.nav_home:
                fragment = new HomeFragment();
                Tag = "Home";
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_category:
                fragment = new CategoryFragment();
                break;
            case R.id.nav_popular_products:
                fragment = new PopularProductFragment();
                break;
            case R.id.nav_new_product:
               // fragment = new NewProductFragment();
                break;

            case R.id.nav_offers:
                fragment = new OffrersFragment();
                break;
            case R.id.nav_search:
                //fragment = new CategoryFragment();
                break;
            case R.id.nav_my_order:
                fragment = new MyOrderFragment();
                break;
            case R.id.nav_my_cart:
//                startActivity(new Intent(getApplicationContext(), CartActivity.class));
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
            ft.replace(R.id.content_frame, fragment,Tag);
            ft.commit();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        //calling the method displayselectedscreen and passing the id of selected menu
//        displaySelectedScreen(item.getItemId());
//        //make this method blank
//        return true;
//    }

    @Override
    public void onAddProduct() {
        super.onAddProduct();
        cart_count++;
        invalidateOptionsMenu();

    }

    @Override
    public void onRemoveProduct() {
        super.onRemoveProduct();
    }

}
