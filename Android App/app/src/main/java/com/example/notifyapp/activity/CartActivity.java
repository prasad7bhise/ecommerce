package com.example.notifyapp.activity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notifyapp.R;
import com.example.notifyapp.adapter.ProductCartAdapter;
import com.example.notifyapp.adapter.ProductListAdapter;
import com.example.notifyapp.model.CartItem;
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

public class CartActivity extends BaseActivity implements ProductCartAdapter.ActionListener{


    RecyclerView recyclerView;
    ProductCartAdapter adapter;
    ArrayList<CartItem> products = new ArrayList<>();

    @BindView(R.id.textViewPrice) TextView textViewPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
         ButterKnife.bind(this);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ProductCartAdapter(this, products, this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);



    }
    @Override
    protected void onResume(){
        super.onResume();
        loadProducts();
    }

    private void updatePrice(){
        float price = 0;
        for (CartItem item: products){
            price += (item.getPrice() * item.getQuantity());
        }
        textViewPrice.setText("₹ "+ price);
    }

    private void loadProducts() {
        products.clear();

        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        int id = preferences1.getInt("id", 0);

        String url = Utils.getUrl(Constants.PATH_CART + id);
        Log.e("CartActivity", "url: " + url);

        // send GET HTTP request
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

                                CartItem product = new CartItem();
                                product.setProductId(object.get("productId").getAsString());
                                product.setTitle(object.get("title").getAsString());
                                product.setDescription(object.get("description").getAsString());
                                product.setPrice(object.get("price").getAsFloat());
                                product.setCategory_id(object.get("categoryId").getAsString());
                                product.setImage(object.get("image").getAsString());
                                product.setCartId(object.get("cartId").getAsInt());
                                product.setQuantity(object.get("quantity").getAsInt());

                                products.add(product);
                              //  products.add(new CartItem(productId, title, description, price, categoryId, image, cartId, quantity));
                            }

                            adapter.notifyDataSetChanged();
                            updatePrice();
                        }

                    }

                });

    }



    @Override
    public void onDecrement(int position) {

        CartItem item = products.get(position);
        int quantity = item.getQuantity()-1;
        if (quantity == 0){
            removeProductFromCart(item.getCartId());
        }else {

            updateQuantity(item.getCartId(), quantity);
        }
    }

    @Override
    public void onIncrement(int position) {

        CartItem item = products.get(position);
        int quantity = item.getQuantity()+1;

        updateQuantity(item.getCartId(), quantity);
    }

    private void updateQuantity(int cartItemId, int quantity){

        String url = Utils.getUrl(Constants.PATH_CART+ cartItemId);
        Log.e("CartActivity", "url: " + url);
        JsonObject body = new JsonObject();
        body.addProperty("quantity", quantity);

        Ion.with(this)
                .load("PUT", url)
                .setJsonObjectBody(body)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String status = result.get("status").getAsString();
                        if(status.equals("success")){
                            Toast.makeText(CartActivity.this, "successfully updated quantity", Toast.LENGTH_SHORT).show();

                            loadProducts();
                        }

                    }
                });
    }

    private void removeProductFromCart(int cartItemId){

        String url = Utils.getUrl(Constants.PATH_CARTOP+ cartItemId);
        Log.e("CartActivity", "url: " + url);

        Ion.with(this)
                .load("DELETE", url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String status = result.get("status").getAsString();
                        if(status.equals("success")){
                            Toast.makeText(CartActivity.this, "successfully removed product", Toast.LENGTH_SHORT).show();

                            loadProducts();
                        }

                    }
                });
    }
}
