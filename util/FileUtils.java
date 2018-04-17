package com.haybankz.medmanager.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by LENOVO on 3/2/2018.
 */

public class FileUtils {

    private static final String TAG = "FileUtils";
    public static String external_path = Environment.getExternalStorageDirectory() + File.separator +".medmanager";


    //get file path from provided uri
    public static String getPath(Context context, Uri uri){
        String path = null;

        if("content".equalsIgnoreCase(uri.getScheme())){
            String[] projection = {"_data"};
            Cursor cursor = null;


            try{
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if(cursor.moveToFirst()){
                    path =  cursor.getString(column_index);
                }
                cursor.close();
            }catch (Exception e){
                Log.e("getPathError", "getPath: " +e.getMessage() );
            }
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            path =  uri.getPath();
        }

        return "file:" + path;
    }



    //check if provided file path is an image or not
    public static boolean isImageFile(String filePath){
        return ("jpg".equalsIgnoreCase(getFileExtension(filePath))
                || "png".equalsIgnoreCase(getFileExtension(filePath)) || "jpeg".equalsIgnoreCase(getFileExtension(filePath)));
    }

    public static File saveImage(Context context, Bitmap image){

        createFolderIfNotExist();

        String file_path = external_path+File.separator +"medmnger_"+ getDateString() + ".png";
        File file = null;
        try {
            file = new File(file_path);

//            file.createNewFile();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 0, baos);
            byte[] bitmapData = baos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();


//            Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show();

            Log.e("FileUtilsClass", "saveStampedImage: "+file.getPath());

        }catch (Exception e){
            Log.e("FileUtilsClass", "saveStampedImage: ", e );
        }

        return file;
    }

    public static String getDateString() {
        Date date = Calendar.getInstance().getTime();


        String format = "ddMMyyyy_HHmmss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);


        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));


        return String.format("%s%s", "", dateFormat.format(date));
    }


    private static void createFolderIfNotExist(){
        File folder = new File(external_path);

//        boolean success = true;

        if(!folder.exists()){
//            success = folder.mkdir();
            folder.mkdir();
        }
    }

    //get file extension/format from filepath
    @Nullable
    public static String getFileExtension( @NonNull String filePath){
        int strLength = filePath.lastIndexOf(".");
        if(strLength > 0) {
            String ext = filePath.substring(strLength + 1).toLowerCase();
            return ext;
        }else {
            return null;
        }
    }

    public static void addPicToGallery(Context context, String filePath){

        MediaScannerConnection.scanFile(context, new String[]{filePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {

            }
        });

    }

    public static Intent shareFile(String absoluteFilePath) {



        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        Uri fileUri = Uri.parse(absoluteFilePath);

        File file = new File(fileUri.getPath());

        Log.e(TAG, "shareFile: " + file.getPath());

        shareIntent.setData(fileUri);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);


        return shareIntent;
    }



}
