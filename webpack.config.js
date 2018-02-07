const path = require('path');
var webpack = require("webpack");

//multi site configuration
module.exports = {
	entry: {
		site: './src/main/js/main.jsx',
	},
	output: {
		filename: '[name].js',
		path: path.resolve(__dirname  + '/generatedsrc/main/resources/template/')
	},
	module: {
		loaders: [
			{
				test: /.jsx$/,
				loaders: 'buble-loader',
				include: path.join(__dirname, 'src/main/js'),
				query: {
					//objectAssign: 'Object.assign'
				}
			},
			{
			    test: /\.css$/,
			    loaders: [
			      'style-loader',
			      'css-loader'
			    ]
			  }
		]
	}
};