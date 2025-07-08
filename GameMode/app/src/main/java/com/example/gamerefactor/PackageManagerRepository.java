package com.example.gamerefactor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.List;

public class PackageManagerRepository {
    private final Context context;

    public PackageManagerRepository(Context context) {
        this.context = context;
    }

    public String findPackageInstalled(String pkgNameKeyword) {
        List<ApplicationInfo> packages = context.getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo pm : packages) {
            String gamePkgName = String.valueOf(pm.packageName);
            if (gamePkgName.contains(pkgNameKeyword)) {
                return gamePkgName;
            }
        }
        return null;
    }

    public List<ResolveInfo> getInstalledApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return context.getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    public void launchApp(String packageName) {
        Intent launchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        }
    }
}
