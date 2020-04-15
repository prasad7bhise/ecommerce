package com.example.notifyapp.adapter;

import android.content.Context;
import android.drm.DrmStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notifyapp.R;
import com.example.notifyapp.model.Product;
import com.example.notifyapp.utils.Utils;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    public interface ActionListner{
        void onClick(int position);
    }
    private final Context context;
    private final ArrayList<Product> products;
    private final ActionListner listner;

    public ProductListAdapter(Context context, ArrayList<Product> products, ActionListner listner) {
        this.context = context;
        this.products = products;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.recycler_item_product,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Product product = products.get(position);

        holder.textTitle.setText(product.getTitle());
        holder.textDescription.setText(product.getDescription());
        holder.textPrice.setText("₹ " + product.getPrice());

        String url = Utils.getUrl("/" +product.getImage());
        Ion.with(context)
                .load(url)
                .withBitmap()
                .intoImageView(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listner.onClick(position);
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
        TextView textTitle, textDescription, textPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            imageView = itemView.findViewById(R.id.imageView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            textPrice = itemView.findViewById(R.id.textPrice);



        }
    }
}
