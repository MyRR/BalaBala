package com.jude.easyrecyclerview.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.R;

/**
 * M为这个itemView对应的model。
 * 使用RecyclerArrayAdapter就一定要用这个ViewHolder。
 * 这个ViewHolder将ItemView与Adapter解耦。
 * 推荐子类继承第二个构造函数。并将子类的构造函数设为一个ViewGroup parent。
 * 然后这个ViewHolder就完全独立。adapter在new的时候只需将parentView传进来。View的生成与管理由ViewHolder执行。
 * 实现setData来实现UI修改。Adapter会在onCreateViewHolder里自动调用。
 *
 * 在一些特殊情况下，只能在setData里设置监听。
 * @param <M>
 */
abstract public class BaseViewHolder<M> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    public void setData(M data) {
    }

    protected <T extends View> T $(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    protected Context getContext(){
        return itemView.getContext();
    }


    /**
     * 转跳 没有finish
     * @param classObj
     */
    public void overlay(Class<?> classObj) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(getContext(), classObj);
        getContext().startActivity(intent);
        ((Activity)itemView.getContext()).overridePendingTransition(R.anim.push_right_in,R.anim.push_left_out);
    }

    /**
     * 转跳 没有finish
     * @param classObj
     * @param params
     */
    public void overlay(Class<?> classObj, Bundle params) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(getContext(), classObj);
        intent.putExtras(params);
        getContext().startActivity(intent);
        ((Activity)itemView.getContext()).overridePendingTransition(R.anim.push_right_in,R.anim.push_left_out);
    }
}