package com.example.tallesrodrigues.nitrovale11;

import android.os.Environment;

import java.io.File;

/**
 * Created by TallesRodrigues on 5/29/2016.
 */
public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {
    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File(
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}
