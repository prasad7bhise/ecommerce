package com.example.notifyapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.notifyapp.R;
import com.example.notifyapp.adapter.ProductListAdapter;
import com.example.notifyapp.model.Product;
import com.example.notifyapp.utils.Constants;
import com.example.notifyapp.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListActivity extends AppCompatActivity implements ProductListAdapter.ActionListner {

    ArrayList<Product>products = new ArrayList<>();
    @BindView(R.id.recycleView) RecyclerView recyclerView;
    ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);

        adapter = new ProductListAdapter(this, products, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
    }

    @Override
    protected  void onResume(){
        super.onResume();
        loadProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuRefresh){
            loadProducts();
        } else if (item.getItemId() == R.id.menuLogout){

            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(ProductListActivity.this);

            preferences1.edit()
                    .putInt("id",0)
                    .putString("name", "")
                    .putString("email","")
                    .putBoolean("login_status",false)
                    .commit();

            Intent intent =  new Intent(this,LoginActivity.class);
            startActivity(intent);

            finish();
        } else if (item.getItemId() == R.id.menuCart){

            Intent intent =  new Intent(this,CartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    void loadProducts(){
        products.clear();
        final String url = Utils.getUrl(Constants.PATH_PRODUCT);
        Log.e("MainActivity",url);
        Ion.    with(this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                       String status = result.get("status").getAsString();
                       if(status.equals("success")){
                           JsonArray tempProducts = result.get("data").getAsJsonArray();
                           for (int index = 0; index < tempProducts.size(); index++) {
                               JsonObject object = tempProducts.get(index).getAsJsonObject();

                               Product product = new Product();
                               product.setId(object.get("id").getAsInt());
                               product.setTitle(object.get("title").getAsString());
                               product.setDescription(object.get("description").getAsString());
                               product.setPrice(object.get("price").getAsFloat());
                               product.setImage(object.get("image").getAsString());
                               product.setCategory_id(object.get("category_id").getAsInt());
                               products.add(product);
                           }
                           adapter.notifyDataSetChanged();
                       }
                    }
                });
    }

    @Override
    public void onClick(int position) {

        Product product = products.get(position);
        Intent intent = new Intent(this,ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }
}

