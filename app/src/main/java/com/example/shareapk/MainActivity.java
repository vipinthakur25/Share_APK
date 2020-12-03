package com.example.shareapk;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareapk.adapter.AppAdapter;
import com.example.shareapk.model.App;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<App> appList;
    private RecyclerView rvApp;
    private TextView tvTotalCountApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        rvApp = findViewById(R.id.rvApp);
        tvTotalCountApps = findViewById(R.id.tvTotalCountApps);
        appList = new ArrayList<>();


        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;
        // Get installed applications
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(flags);

        // Remove system apps
        Iterator<ApplicationInfo> it = packages.iterator();
        while (it.hasNext()) {
            ApplicationInfo appInfo = it.next();
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                it.remove();
            }
        }

        for (ApplicationInfo packageInfo : packages) {
            String name;
            if ((name = String.valueOf(pm.getApplicationLabel(packageInfo))).isEmpty()) {
                name = packageInfo.packageName;
            }
            Drawable icon = pm.getApplicationIcon(packageInfo);
            String apkPath = packageInfo.sourceDir;
            long apksize = new File(packageInfo.sourceDir).length();

            appList.add(new App(name, icon, apkPath, apksize));
        }
        Collections.sort(appList, new Comparator<App>() {
            @Override
            public int compare(App o1, App o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvApp.setLayoutManager(linearLayoutManager);
        rvApp.setHasFixedSize(true);
        AppAdapter appAdapter = new AppAdapter(this, appList);
        rvApp.setAdapter(appAdapter);
        tvTotalCountApps.setText("" + appList.size());
    }
}