package com.ok.uiframe.adapter;

import android.animation.Animator;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class GiftItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        // 移除动画：滑出到左边屏幕
        holder.itemView.animate()
            .translationX(-holder.itemView.getWidth()) // 滑出到左边
            .setDuration(300)
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束时，调用父类的animateRemove，以确保RecyclerView更新状态
                    holder.itemView.setTranslationX(0);
                    superAnimateRemove(holder);
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        // 添加动画：从右边滑入
        holder.itemView.setTranslationX(holder.itemView.getWidth());
        holder.itemView.animate()
            .translationX(0) // 回到原位
            .setDuration(400)
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束时，调用父类的animateAdd，以确保RecyclerView更新状态
                    superAnimateAdd(holder);
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        return true;
    }

    private void superAnimateRemove(RecyclerView.ViewHolder holder) {
        super.animateRemove(holder);
    }

    private void superAnimateAdd(RecyclerView.ViewHolder holder) {
        super.animateAdd(holder);
    }
}
