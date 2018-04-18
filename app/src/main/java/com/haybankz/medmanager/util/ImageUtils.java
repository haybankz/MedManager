package com.haybankz.medmanager.util;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.haybankz.medmanager.data.user.UserContract;
import com.haybankz.medmanager.ui.LoginActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;

/**
 * Created by LENOVO on 3/5/2018.
 */

public class ImageUtils {


    //create a bitmap from a file path
    public static Bitmap getBitmapFromPath(Context context, String imageFilePath){

//File imgFile = new File(imageFilePath);
        Bitmap image = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        options.inJustDecodeBounds = false;

        if(FileUtils.isImageFile(imageFilePath)) {
//            image =  BitmapFactory.decodeFile(imageFilePath, options);
            try {
                image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imageFilePath));
            }catch (IOException e){
                e.printStackTrace();
            }

//            Log.e("FileUtils", "getBitmapFromPath: Height: "+image.getHeight() +" width: "+image.getWidth() );

//                return resizeImage(image, 50 , 50);
            return image;
        }else {
            return null;
        }
    }


    //get Bitmap from resource id
    public static Bitmap getBitmapFromResource(Context context, int resourceId){
        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }



    //resize a bitmap to provided width and height
    public static Bitmap resizeImage(Bitmap image, int width, int height){
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Target getTarget(final Context context, final int id){
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file= FileUtils.saveImage(context, bitmap);

                        ContentValues values = new ContentValues();
                        values.put(UserContract.UserEntry.COLUMN_USER_PHOTO_URL, "file:" + file.getPath());

                        UserDbUtils.updateUser(context, id, values);

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }
}
