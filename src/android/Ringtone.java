package com.hiraqui.ringtone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */
public class Ringtone extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (action.equals("echo")) {
			return this.echo(args.getString(0), args.getString(1),
					args.getString(2), args.getString(3), callbackContext);
		} else if (action.equals("copy")) {
			return this.copy(args.getString(0), args.getString(1),
					args.getString(2), args.getString(3), callbackContext);
		}
		return false;
	}

	private boolean echo(final String file, final String title,
			final String artist, final String tipo,
			final CallbackContext callbackContext) {
		cordova.getThreadPool().execute(new Runnable() {
			public void run() {
				if (tipo.equals("alarm") || tipo.equals("notification")
						|| tipo.equals("ringtone")) {
					setAssets(tipo, file, title, artist, callbackContext);
				} else {
					callbackContext.error("tipo " + tipo + "not valid");
				}
			}
		});
		return true;
	}

	private boolean copy(final String file, final String title,
			final String artist, final String tipo,
			final CallbackContext callbackContext) {
		cordova.getThreadPool().execute(new Runnable() {
			public void run() {
				if (file.contains("/android_asset/")) {
					String tmpFile = file.replaceFirst("/android_asset/", "");
					if (tipo.equals("alarm") || tipo.equals("notification")
							|| tipo.equals("ringtone")) {
						copyAssets(tipo, tmpFile, title, artist,
								callbackContext);
					} else {
						callbackContext.error("tipo " + tipo + "not valid");
					}
				} else
					callbackContext
							.error("the file must be in the \"www\" folder or subfolder and the path should start with \"/android_asset/www\"");
			}
		});
		return true;
	}

	private void copyAssets(String type, String file, String title,
			String artist, CallbackContext callbackContext) {
		AssetManager assetManager = cordova.getActivity().getAssets();
		File newSoundFile = null;
		String[] arrayName = file.split("/");
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)
				&& Environment.getExternalStorageDirectory().canWrite()) {
			File wallpaperDirectory = new File(Environment
					.getExternalStorageDirectory().toString() + "/Ringtones/");
			wallpaperDirectory.mkdirs();
			newSoundFile = new File(Environment.getExternalStorageDirectory()
					.toString()
					+ "/Ringtones/"
					+ arrayName[arrayName.length - 1]);
		} else {
			File wallpaperDirectory = new File(Environment.getDataDirectory()
					.toString()
					+ "/data/"
					+ cordova.getActivity().getApplicationInfo().packageName
					+ "/ringtones/");
			wallpaperDirectory.mkdirs();
			newSoundFile = new File(Environment.getDataDirectory().toString()
					+ "/data/"
					+ cordova.getActivity().getApplicationInfo().packageName
					+ "/ringtones/" + arrayName[arrayName.length - 1]);
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(file);
			out = new FileOutputStream(newSoundFile);
			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
			setRingtone(type, newSoundFile, callbackContext, title, artist);
			callbackContext.success("success, new file is "
					+ newSoundFile.getPath());
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
			callbackContext.error("Error copiando: " + e.getMessage()
					+ ". Destino: " + newSoundFile.getPath());
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	private void setAssets(String type, String file, String title,
			String artist, CallbackContext callbackContext) {
		File soundFile = new File(file.replaceAll("file:", ""));
		setRingtone(type, soundFile, callbackContext, title, artist);
	}

	private void setRingtone(String type, File newSoundFile,
			CallbackContext callbackContext, String title, String artist) {
		if (artist.equals("")) {
			artist = cordova
					.getActivity()
					.getPackageManager()
					.getApplicationLabel(
							cordova.getActivity().getApplicationInfo())
					.toString();
		}
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, newSoundFile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE,
				title.replaceAll("[^\\\\dA-Za-z0-9_]", ""));
		String filenameArray[] = newSoundFile.getName().split("\\.");
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/"
				+ filenameArray[filenameArray.length - 1]);
		values.put(MediaStore.MediaColumns.SIZE, newSoundFile.length());
		values.put(MediaStore.Audio.Media.ARTIST,
				artist.replaceAll("[^\\\\dA-Za-z0-9_]", ""));
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
				callbackContext.success("success setting "
						+ newSoundFile.getAbsolutePath() + " as " + type);
			} else if (type.equals("notification")) {
				RingtoneManager.setActualDefaultRingtoneUri(
						cordova.getActivity(),
						RingtoneManager.TYPE_NOTIFICATION, newUri);
				callbackContext.success("success setting "
						+ newSoundFile.getAbsolutePath() + " as " + type);
			} else if (type.equals("ringtone")) {
				RingtoneManager.setActualDefaultRingtoneUri(
						cordova.getActivity(), RingtoneManager.TYPE_RINGTONE,
						newUri);
				callbackContext.success("success setting "
						+ newSoundFile.getAbsolutePath() + " as " + type);
			} else {
				callbackContext.error("tipo " + type + "not valid");
			}
		} catch (Throwable t) {
			Log.d("tag", "catch exception");
			callbackContext.error("Error setting as " + type + t.getMessage()
					+ ". Destino: " + newSoundFile.getPath());
		}
	}
}