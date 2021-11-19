package com.example.ad_btl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ad_btl.Adapter.ItemAdapter;
import com.example.ad_btl.Characters.Accounts;
import com.example.ad_btl.Fragment.Home;
import com.example.ad_btl.Fragment.Person;
import com.example.ad_btl.Fragment.UpdatePost;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 19;
    ItemAdapter mitemAdapter;
    Accounts accounts;
    Home home;
    Bundle bundle;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accounts=SignUpReceive();
        Toast.makeText(this, accounts.getUsername(), Toast.LENGTH_SHORT).show();
        //mặc định fragment
        LoadFragment(new Home());
        //thiết lập bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListenerga);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListenerga =
            new BottomNavigationView.OnNavigationItemSelectedListener(){

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    Bundle bundla = new Bundle();
                    switch (item.getItemId()) {
                        case R.id.id_nv_home:
                            fragment = new Home();
                            bundla.putSerializable("acc", accounts);
                            fragment.setArguments(bundla);
                            break;
                        case R.id.id_nv_account:
                            fragment = new Person();
                            bundla.putSerializable("acc", accounts);
                            fragment.setArguments(bundla);
                            break;
                        case R.id.id_nv_add:
                            fragment = new UpdatePost();
                            bundla.putSerializable("acc", accounts);
                            fragment.setArguments(bundla);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;
                }
            };
    public Accounts SignUpReceive() {
        Accounts account = null;
        Intent iSu = getIntent();
        Bundle bunrcv = iSu.getExtras();
        if(bunrcv != null) {
            account = (Accounts) bunrcv.get("acc");
        }
        return account;
    }
    private void LoadFragment(Fragment afragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,afragment);
        Bundle bundle= new Bundle();
        bundle.putSerializable("acc", accounts);
        afragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commit();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.id_nv_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mitemAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                home.mItemAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }*/
}
