# cordova-plugin-ringtone

Ringtone
======

A Cordova / Phonegap ringtone plugin for Android

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

