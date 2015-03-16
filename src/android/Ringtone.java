package com.hiraqui.ringtone;

import java.io.File;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.hiraqui.myApplication.R;

/**
 * This class echoes a string called from JavaScript.
 */
public class Ringtone extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args,
            CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            return this.echo(args.getString(0), args.getString(1),
                    args.getString(2), callbackContext);
        }
        return false;
    }

    private boolean echo(final String file, final String title,
            final String tipo, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                if (tipo.equals("alarm") || tipo.equals("notification")
                        || tipo.equals("ringtone")) {
                    CopyAssets(tipo, file, title, callbackContext);
                } else {
                    callbackContext.error("tipo " + tipo + "not valid");
                }
            }
        });
        return true;
    }

    private void CopyAssets(String type, String file, String title,
            CallbackContext callbackContext) {
        File soundFile = new File(file.replaceAll("file:", ""));
        setRingtone(type, soundFile, callbackContext, title);
        callbackContext.success("reconexion correcta");
    }
    
    private void setRingtone(String type, File newSoundFile,
            CallbackContext callbackContext, String title) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, newSoundFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, title.replaceAll("[^\\\\dA-Za-z0-9_]", ""));
        String filenameArray[] = newSoundFile.getName().split("\\.");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/"
                + filenameArray[filenameArray.length - 1]);
        values.put(MediaStore.MediaColumns.SIZE, newSoundFile.length());
        values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(newSoundFile
                .getAbsolutePath());
        cordova.getActivity()
                .getContentResolver()
                .delete(uri,
                        MediaStore.MediaColumns.DATA + "=\""
                        + newSoundFile.getAbsolutePath() + "\"", null);
        Uri newUri = cordova.getActivity().getContentResolver()
                .insert(uri, values);
        try {
            if (type.equals("alarm")) {
                RingtoneManager.setActualDefaultRingtoneUri(
                        cordova.getActivity(), RingtoneManager.TYPE_ALARM,
                        newUri);
            } else if (type.equals("notification")) {
                RingtoneManager.setActualDefaultRingtoneUri(
                        cordova.getActivity(),
                        RingtoneManager.TYPE_NOTIFICATION, newUri);
            } else if (type.equals("ringtone")) {
                RingtoneManager.setActualDefaultRingtoneUri(
                        cordova.getActivity(), RingtoneManager.TYPE_RINGTONE,
                        newUri);
            } else {
                callbackContext.error("tipo " + type + "not valid");
            }
        } catch (Throwable t) {
            Log.d("tag", "catch exception");
            callbackContext.error(t.getMessage());
        }
    }
}
