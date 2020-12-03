package com.example.shareapk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareapk.BuildConfig;
import com.example.shareapk.R;
import com.example.shareapk.model.App;

import java.io.File;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    private Context context;
    private List<App> appList;

    public AppAdapter(Context context, List<App> appList) {
        this.context = context;
        this.appList = appList;
    }

    @NonNull
    @Override
    public AppAdapter.AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_row, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppAdapter.AppViewHolder holder, int position) {
        holder.app_name.setText(appList.get(position).getName());
        long apkSize = appList.get(position).getApkSize();
        holder.apk_size.setText(getReadableSize(apkSize));
        holder.ivAppIcon.setImageDrawable(appList.get(position).getIcon());

        holder.appRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                        context, BuildConfig.APPLICATION_ID + ".provider", new File(appList.get(position).getApkPath())
                ));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("application/vnd.android.package-archive");

                context.startActivity(Intent.createChooser(intent, "Share APK"));

            }
        });

    }

    private String getReadableSize(long apkSize) {
        String readableSize;
        if (apkSize < 1024) {
            readableSize = String.format(
                    context.getString(R.string.app_size_b),
                    (double) apkSize
            );
        } else if (apkSize < Math.pow(1024, 2)) {
            readableSize = String.format(
                    context.getString(R.string.app_size_kb),
                    (double) (apkSize / 1024)
            );
        } else if (apkSize < Math.pow(1024, 3)) {
            readableSize = String.format(
                    context.getString(R.string.app_size_mb),
                    (double) (apkSize / Math.pow(1024, 2))
            );
        } else {
            readableSize = String.format(
                    context.getString(R.string.app_size_gb),
                    (double) (apkSize / Math.pow(1024, 3))
            );
        }
        return readableSize;
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAppIcon;
        private TextView app_name;
        private TextView apk_size;
        private CardView appRow;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAppIcon = itemView.findViewById(R.id.ivAppIcon);
            app_name = itemView.findViewById(R.id.app_name);
            apk_size = itemView.findViewById(R.id.apk_size);
            appRow = itemView.findViewById(R.id.appRow);
        }
    }
}
