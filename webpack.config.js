const path = require('path');
var webpack = require("webpack");

// multi site configuration
module.exports = {
	entry : {
		site : './src/main/js/main.jsx',
	},
	output : {
		filename : '[name].js',
		path : path.resolve(__dirname
				+ '/generatedsrc/main/resources/template/')
	},
	module : {
		rules : [ {
			test : /\.(js|jsx)$/,
			exclude : /node_modules/,
			use : [ 'babel-loader' ]
		}, {
			test : /\.css$/,
			use : [ 'style-loader', 'css-loader' ]
		} ]
	},
	resolve : {
		extensions : [ '*', '.js', '.jsx' ]
	}
};