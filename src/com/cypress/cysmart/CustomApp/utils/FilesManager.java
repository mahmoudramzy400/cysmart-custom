package com.cypress.cysmart.CustomApp.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FilesManager {
    private static final String TAG = "FilesManager";
    private static FileWriter writer ;

    private static  String baseFileName = "Custom_Channels_Folder" ;
    private FilesManager (){

    }


    public static void saveChannelsData (Context context , String dataBody  ){

        if (isExternalStorageReadable() && isExternalStorageWritable()){
            File baseFile = context.getExternalFilesDir(baseFileName) ;
            if (!baseFile.exists())
                baseFile.mkdir() ;
            File channelDataFile = new File(baseFile, "channel_data_" +DateTime.getDate()) ;

            if (!channelDataFile.exists())
                channelDataFile.mkdir() ;

            // start write the data

            try {
                writer = new FileWriter(channelDataFile ,true );
                writer.write(dataBody );
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG , "Error in write the file ") ;
            }


        }else{
            return;
        }

    }


    public static void closeWriter (){
        if (writer != null ) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
