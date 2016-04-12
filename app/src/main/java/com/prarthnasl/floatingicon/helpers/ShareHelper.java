package com.prarthnasl.floatingicon.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.prarthnasl.floatingicon.R;
import com.prarthnasl.floatingicon.constants.Constants;
import com.prarthnasl.floatingicon.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prarthnasl on 4/2/2016.
 */
public class ShareHelper {

    private Context context;

    public ShareHelper(Context context) {
        this.context = context;
    }

    public void shareImageWhatsApp(List<Product> products) {

        ArrayList<Uri> imageUriArray = new ArrayList<>();

        for (Product product : products) {
            Drawable drawable = context.getResources().getDrawable(product.getImage());
            try {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, product.getName(), null);
                Uri uri = Uri.parse(path);
                imageUriArray.add(uri);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, imageUriArray);
        share.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_products_text));

        if (isPackageInstalled(Constants.WHATSAPP_PACKAGE_NAME, context)) {
            share.setPackage(Constants.WHATSAPP_PACKAGE_NAME);
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(share);
        } else {
            Toast.makeText(context, context.getString(R.string.install_whatsapp_text), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
