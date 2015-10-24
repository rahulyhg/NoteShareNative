package com.tilak.noteshare;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tilak.db.NoteElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CameraImage extends Activity {

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_image);

        // Retrieving image Uri from Bundle
        Bundle b = getIntent().getExtras();
        Uri mediaUri = Uri.parse(b.getString("image"));
        ImageView image = (ImageView)findViewById(R.id.camera_image);

        if(getIntent().hasExtra("image")) {
            try {
                // Converting Uri to Bitmap
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mediaUri);
                int deviceWidth = getWindowManager().getDefaultDisplay().getWidth();
                int deviceHeight = getWindowManager().getDefaultDisplay().getHeight();
                BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mediaUri.getPath(), bounds);

                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(mediaUri.getPath(), opts);
                ExifInterface exif = new ExifInterface(mediaUri.getPath());
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
                /*int imageHeight = bounds.outHeight;
                int imageWidth = bounds.outWidth;
                Bitmap scale = null;
                if(imageWidth > imageHeight)
                    scale = bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, false);
                else if (imageWidth < imageHeight)
                    scale = bitmap.createScaledBitmap(bitmap, deviceWidth, deviceHeight, false);
                // Setting bitmap to ImageView;*/
                image.setImageBitmap(rotatedBitmap);
            } catch (Exception e) {}
        }
    }

    public void done(View v) {
        finish();

        try {
            // Saving Image file
            String timestamp = String.valueOf(System.currentTimeMillis());
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/NoteShare/NoteShare Images/" + "IMG-" + timestamp + ".jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(mediaStorageDir));

            // Refreshing Gallery to view Image in Gallery
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, mediaStorageDir.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Toast to display image path
            Toast.makeText(getApplication(), mediaStorageDir.toString(), Toast.LENGTH_LONG).show();

            // Saving to database
            NoteElement noteElement = new NoteElement(1L, 1, "yes", "image", "IMG-" + timestamp + ".jpg");
            noteElement.save();
        } catch (FileNotFoundException e) {}
    }

    public void crop(View v){}

    public void cancel(View v){
        finish();
        Toast.makeText(getApplication(), "Photo Discarded", Toast.LENGTH_LONG).show();
    }
}
