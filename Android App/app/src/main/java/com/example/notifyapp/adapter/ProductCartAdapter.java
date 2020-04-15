package com.example.notifyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notifyapp.R;
import com.example.notifyapp.model.CartItem;
import com.example.notifyapp.model.Product;
import com.example.notifyapp.utils.Utils;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ViewHolder> {

    public interface ActionListener{
        void onDecrement(int position);
        void onIncrement(int position);

    }

    private final Context context;
    private final ArrayList<CartItem> products;
    private final ActionListener listner;

    public ProductCartAdapter(Context context, ArrayList<CartItem> products, ActionListener listner) {
        this.context = context;
        this.products = products;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        LinearLayout layout =(LinearLayout) inflater.inflate(R.layout.recycler_item_cart,null);
        return new ProductCartAdapter.ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CartItem product =products.get(position);


        String url = Utils.getUrl("/" +product.getImage());
        Ion.with(context)
                .load(url)
                .withBitmap()
                .intoImageView(holder.imageView);

        holder.textTitle.setText(product.getTitle());
        holder.textPrice.setText("₹ "+product.getPrice());
        holder.textQuantity.setText(""+ product.getQuantity());

        holder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onIncrement(position);
            }
        });

        holder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onDecrement(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View itemView;
        ImageView imageView;
        TextView textTitle, textPrice, textQuantity;

        ImageButton buttonPlus, buttonMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
         this.itemView = itemView;

            imageView = itemView.findViewById(R.id.imageView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuanty);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);

        }
    }
}
