package com.example.tallesrodrigues.nitrovale11;

import android.os.Environment;

import java.io.File;

/**
 * Created by TallesRodrigues on 5/29/2016.
 */
public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ), albumName
        );
    }

}