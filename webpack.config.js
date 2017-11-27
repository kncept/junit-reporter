const path = require('path');
var webpack = require("webpack");

//multi site configuration
module.exports = {
	entry: {
		site: './src/main/js/main.jsx',
	},
	output: {
		filename: '[name].js',
		path: path.resolve(__dirname  + '/src/generated/resources/template/')
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
			{ //needed for https://www.npmjs.com/package/react-rte
			    test: /\.css$/,
			    loaders: [
			      'style-loader',
			      'css-loader?modules'
			    ]
			  }
		]
	}
};