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
 * Get device info
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
module.exports = new RingtoneManager();
