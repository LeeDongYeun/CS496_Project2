var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var userSchema = new Schema({
	id: String,
	password: String,
	userName: String,
	contact : [{name: String, number: String}],
	photo : [String]
});

module.exports = mongoose.model('user', userSchema);