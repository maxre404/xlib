package com.ok.uiframe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.max.uiframe.R;
import com.ok.uiframe.data.Gift;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {
    private List<Gift> giftList;

    public GiftAdapter(List<Gift> giftList) {
        this.giftList = giftList;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        Gift gift = giftList.get(position);
        holder.giftText.setText(gift.getName());
        holder.giftImage.setImageResource(gift.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }

    public void addGift(Gift gift) {
        giftList.add(gift);
        notifyItemInserted(giftList.size() - 1);
    }

    public void removeGift(int position) {
        giftList.remove(position);
        notifyItemRemoved(position);
    }

    public static class GiftViewHolder extends RecyclerView.ViewHolder {
        ImageView giftImage;
        TextView giftText;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            giftImage = itemView.findViewById(R.id.gift_image);
            giftText = itemView.findViewById(R.id.gift_text);
        }
    }
}
