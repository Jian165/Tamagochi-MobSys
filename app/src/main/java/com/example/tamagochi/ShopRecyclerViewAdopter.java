package com.example.tamagochi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShopRecyclerViewAdopter extends RecyclerView.Adapter<ShopRecyclerViewAdopter.ShopViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ShopItemModel> shopItemModels;

    public ShopRecyclerViewAdopter(Context context, ArrayList<ShopItemModel> shopItemModels,RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.shopItemModels = shopItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public ShopRecyclerViewAdopter.ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_container_fooditem,parent,false);
        return new ShopRecyclerViewAdopter.ShopViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        holder.itemImg.setImageResource(shopItemModels.get(position).getItemImage());
        holder.itemNameLbl.setText(shopItemModels.get(position).getItemName());
        holder.itemPriceLbl.setText("$"+shopItemModels.get(position).getItemPrice());
        holder.healthStatLbl.setText("+"+shopItemModels.get(position).getHealthStat());
        holder.happyStatLbl.setText("+"+shopItemModels.get(position).getHappyStat());
    }

    @Override
    public int getItemCount() {
        return shopItemModels.size();
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImg;
        TextView itemNameLbl,itemPriceLbl,healthStatLbl,happyStatLbl;
        Button buyBtn;

        public ShopViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.imgItem);
            itemNameLbl = itemView.findViewById(R.id.lblFoodName);
            healthStatLbl = itemView.findViewById(R.id.healthStatsLbl);
            happyStatLbl = itemView.findViewById(R.id.happyStatsLbl);
            itemPriceLbl = itemView.findViewById(R.id.lblPrice);
            buyBtn = itemView.findViewById(R.id.btnBuy);
            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}

