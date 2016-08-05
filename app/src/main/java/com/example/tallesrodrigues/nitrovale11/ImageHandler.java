package com.example.tallesrodrigues.nitrovale11;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by TallesRodrigues on 5/31/2016.
 */
public class ImageHandler extends AppCompatActivity{

    private Bitmap contactImage;
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String DATA_FILE_SUFFIX = ".txt";
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private EditText realSpad;
    private String mCurrentPhotoPath,mCurrentFilePath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private Uri imageToUploadUri;

    SPAD mySPAD;

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    // create File for image: na  me.jpg
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);

        return imageF;
    }

    // create File for image: name.jpg
    private File createDatalogFile() throws IOException {
        // Create data file
        File albumF = getAlbumDir();
        File imageF = File.createTempFile("datalog", DATA_FILE_SUFFIX, albumF);
        return imageF;
    }

    //Save datalog
    private void saveToFile() throws IOException {
        File f;
        String txtData=  mySPAD.getR()+"\t"+mySPAD.getG()+"\t"+mySPAD.getB()+"\t"+realSpad.getText()+"\n";

        DatabaseController crud = new DatabaseController(getBaseContext());

        Log.e("R",String.valueOf(mySPAD.getR()));
        Log.e("G",String.valueOf(mySPAD.getG()));
        Log.e("B",String.valueOf(mySPAD.getB()));
        Log.e("INt",String.valueOf(mySPAD.getIntensity()));
        Log.e("SPAD",realSpad.getText().toString());

        crud.insertData(mySPAD.getR(),
                        mySPAD.getG(),
                        mySPAD.getB(),
                        mySPAD.getIntensity(),
                        Float.parseFloat(realSpad.getText().toString()));

        Toast.makeText(getBaseContext(),
                "Done Saving to Database",
                Toast.LENGTH_SHORT).show();

        return;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap  */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (actionCode) {
            case ACTION_TAKE_PHOTO_B:
                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    imageToUploadUri = Uri.fromFile(f);
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        } // switch

        startActivityForResult(takePictureIntent, actionCode);
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            ImageHandler.BicubicReduction(mCurrentPhotoPath, 800, 640); // reduce image before showing and saving it
            mySPAD = new SPAD(mCurrentPhotoPath);
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;

            realSpad.setVisibility(View.VISIBLE);

        }

    }

    public static void BicubicReduction(String mCurrentPhotoPath, int mDstWidth, int mDstHeight) {
        File sourceFile = new File(mCurrentPhotoPath);
        Bitmap b = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());

        // create scaled image
        Bitmap b2 = Bitmap.createScaledBitmap(b, mDstWidth, mDstHeight, false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        b2.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        try {
            FileOutputStream fo = new FileOutputStream(sourceFile);
            fo.write(outputStream.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            Log.e("File not Found ", e.toString());
        } catch (IOException io) {
            Log.e("Couldn't write to file ", io.toString());
        }
        return;
    }

    public boolean isContactImage(String mCurrentPhotoPath) {
        File sourceFile = new File(mCurrentPhotoPath);
        Bitmap b = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());

        // Get the intensity of a bitmap
      /*  private double getFrameIntensity(Bitmap frame) {
            int[] pixels = new int[frame.getHeight()*frame.getWidth()];
            frame.getPixels(pixels,0,frame.getWidth(),0,0,frame.getWidth(),frame.getHeight());
            long red = 0;
            long green = 0;
            long blue = 0;
            for (int i = 0; i < pixels.length; i++) {
                red += Color.red(pixels[i]);
                green += Color.green(pixels[i]);
                blue += Color.blue(pixels[i]);
            }
            return (red + green + blue) / pixels.length;
        } */
        return  false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagehandler);

        mImageView = (ImageView) findViewById(R.id.imageView1);
        mImageBitmap = null;
        mCurrentFilePath = null;




        realSpad = (EditText)findViewById(R.id.realSpad);
        realSpad.setVisibility(View.INVISIBLE);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e("ENtered value",realSpad.getText().toString());
                    saveToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
            Log.i("mAlbumStorageDirFactory", mAlbumStorageDirFactory.toString());
        }

        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {

                    handleBigCameraPhoto();
                }
                break;
            }
            case ACTION_TAKE_PHOTO_S: {
                break;
            }
        } // switch
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
    }

    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

}
