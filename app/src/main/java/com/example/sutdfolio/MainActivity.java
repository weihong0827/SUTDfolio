package com.example.sutdfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sutdfolio.utils.NetworkManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
//    ActivityMainBinding binding;

    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = pref.getString("token", "");


//        binding = ActivityMainBinding.inflate(getLayoutInflater());
        NetworkManager.getInstance(this);
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_main);
        NavController navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.loginFragment){
                            if(token.equals("")){
                                navController.navigate(R.id.loginFragment);
                            }else{navController.navigate(R.id.profileFragment);}
                        }else if (item.getItemId() == R.id.homePage){
                            navController.navigate(R.id.homePage);}
                        else if(item.getItemId() == R.id.upload){
                            navController.navigate(R.id.upload);}
                        return true;
                    }
                });

    }
}

//NavigationUI.setupWithNavController(bottomNavigationView, navController);
//                item -> {
//                    item.getItemId() {
//
//                    }
//
//                    when(item.getItemId() ==)
//                    when(item.itemId) {
//                        R.id.item1 ->{
//                            // Respond to navigation item 1 click
//                            true
//                        }
//                        R.id.item2 ->{
//                            // Respond to navigation item 2 click
//                            true
//                        }
//        else ->false
//                    }
//                }



//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,homePage.newInstance(),null).commit();


