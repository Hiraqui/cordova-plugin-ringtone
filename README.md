# com.hiraqui.ringtone v0.5.0

Ringtone
======

A Cordova / Phonegap ringtone plugin for Android. Set an audio file as ringtone, notification tone or alarm tone.

	cordova plugin add https://github.com/Hiraqui/cordova-plugin-ringtone
	
	or

    cordova plugin add com.hiraqui.ringtone

Methods
-------

- window.ringtone.setRingtone

Instructions
-------

Change "import com.hiraqui.myApplication.R" to your own R resource file on Ringtone.java

window.ringtone.setRingtone
=================

Set a sound file as default Ringtone, notification tone or alarm tone

    param {String}
        file The path to the audio file ("file:..." or "/android_asset/www/...")
    param {String}
        title The title shown on the ringtone selection screen
    param {String}
        type The type of sound you want to set ["ringtone"|"notification"|"alarm"]
    param {Function}
        successCallback The function to call when the heading data is available
    param {Function}
        errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
    
    window.ringtone.setRingtone(file, title, type, successCallback, errorCallback)

window.ringtone.exportAssetAndSetRingtone
=================

Copy an asset sound file to the sdcard or app directory and set it as Ringtone, notification tone or alarm tone
*If you have a writable sdcard the new file is created in "sdcard/Ringtones/myFile.mp3"
*If the sdcard is not writable or if it is missing, the new file is created inside the application folder "data/com.hiraqui.myApplication/ringtones/myFile.mp3"

    param {String}
        file The path to the asset audio file ("/android_asset/www/...")
    param {String}
        title The title shown on the ringtone selection screen
    param {String}
        type The type of sound you want to set ["ringtone"|"notification"|"alarm"]
    param {Function}
        successCallback The function to call when the heading data is available
    param {Function}
        errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
    
    window.ringtone.exportAssetAndSetRingtone(file, title, type, successCallback, errorCallback)

Supported Platforms
-------------------

- Android

Example 1
---------

//Seting a sound file from the "www" folder as ringtone

window.ringtone.setRingtone("/android_asset/www/img/beep.wav",
			    "beep",
			    "ringtone",
			    function(success) {
			    alert(success);
			    },
			    function(err) {
			    alert(err);
			    })
			    
Example 2
---------

//Seting a sound file from the SD card as notification tone

window.ringtone.setRingtone("file:///storage/sdcard/Android/data/com.hiraqui.myApplication/files/beep.mp3",
			    "beep",
			    "notification",
			    function(success) {
			    alert(success);
			    },
			    function(err) {
			    alert(err);
			    })

Example 3
---------

//Seting a sound file from the "www/sounds" folder as ringtone after copying it to another folder

window.ringtone.exportAssetAndSetRingtone("/android_asset/www/sounds/beep.wav",
			    "beep",
			    "ringtone",
			    function(success) {
			    alert(success);
			    },
			    function(err) {
			    alert(err);
			    })

Permissions
-----------

android.permission.WRITE_SETTINGS
android.permission.READ_EXTERNAL_STORAGE
android.permission.WRITE_EXTERNAL_STORAGE
