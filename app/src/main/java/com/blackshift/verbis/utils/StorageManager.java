package com.blackshift.verbis.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Package com.blackshift.verbis.utils
 * <p>
 * Created by Prashant on 4/10/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class StorageManager {
    //TODO: Permissions
    public static String ROOT_VERBIS = Environment.getExternalStorageDirectory().toString().concat("/.verbis");
    public static String FONTS = ROOT_VERBIS.concat("/fonts/");

    public List<File> getFonts(){
        List<File> fonts = new ArrayList<>();
        File dirFonts = new File(FONTS);
        Log.d("Fonts dir",dirFonts.toString());
        if(dirFonts.exists()){
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    Log.d("File Path",pathname.toString());
                    return pathname.toString().endsWith(".ttf")||pathname.toString().endsWith(".otf");
                }
            };
            if(dirFonts.listFiles(filter)!=null)
            for(File font:dirFonts.listFiles(filter)) {
                if(font.isFile()){
                        fonts.add(font);
                }
            }
        }
        return fonts;
    }

}
