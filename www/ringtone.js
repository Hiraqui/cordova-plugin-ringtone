var exec = require('cordova/exec');
/**
 * This represents the object to handle ringtone that will be exported to module
 * 
 * @constructor
 */
function RingtoneManager() {
	this.propTest = 'superTest';
}
/**
 * Set a sound file as default Ringtone, notification tone or alarm tone
 *  
 * @param {String}
 *            file The path to the audio file ("file:...")
 * @param {String}
 *            title The title shown on the ringtone selection screen
 * @param {String}
 *            type The type of sound you want to set ["ringtone"|"notification"|"alarm"]
 * @param {Function}
 *            successCallback The function to call when the heading data is
 *            available
 * @param {Function}
 *            errorCallback The function to call when there is an error getting
 *            the heading data. (OPTIONAL)
 */
RingtoneManager.prototype.setRingtone = function(file, title, type, successCallback,
		errorCallback) {
	exec(successCallback, errorCallback, "Ringtone", "echo", [ file, title,
			type ]);
};

/**
* Copy an asset sound file to the sdcard or app directory and set it as Ringtone, notification tone or alarm tone
* If you have a writable sdcard the new file is created in "sdcard/Ringtones/myFile.mp3"
* If the sdcard is not writable or if it is missing, the new file is created inside the application folder "data/com.hiraqui.myApplication/ringtones/myFile.mp3"
* 
* @param {String}
*            file The path to the audio file starting with "/android_asset/www.."
* @param {String}
*            title The title shown on the ringtone selection screen
* @param {String}
*            type The type of sound you want to set ["ringtone"|"notification"|"alarm"]
* @param {Function}
*            successCallback The function to call when the heading data is
*            available
* @param {Function}
*            errorCallback The function to call when there is an error getting
*            the heading data. (OPTIONAL)
*/
RingtoneManager.prototype.exportAssetAndSetRingtone = function(file, title, type, successCallback,
		errorCallback) {
	exec(successCallback, errorCallback, "Ringtone", "copy", [ file, title,
			type ]);
};
module.exports = new RingtoneManager();
