const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
	entry: './src/index.tsx',
    devtool: "inline-source-map",
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: "ts-loader",
                include: path.resolve(__dirname, "src"),
                exclude: /node_modules/,
            }
        ]
    },
    resolve: {
        extensions: [ ".tsx", ".ts", ".js" ],
        modules: ["src", "node_modules"]
    },
    plugins: [
        new HtmlWebpackPlugin({
            title: "Little Maze",
            template: "public/index.html",
        })
    ],
    output: {
        filename: "bundle.js",
        path: path.resolve("dist"),
		publicPath: '/'
    },
    watch: true,
	target: "node",
    mode: "development",
    devServer: {
        contentBase: path.join(__dirname, 'public'),
		watchContentBase: true,
        host: '0.0.0.0',
        port: 2200,
        disableHostCheck: true,
		historyApiFallback: true, // on page refresh on eg "/game", falls back to "/" and react-router takes it from there
        headers: {
          'Cache-Control': 'no-cache, no-store',
          'Expires': '-1',
        },
        proxy: {
            '/littlemaze/*': {
				target: 'https://localhost:2222/', // <-- change 2222 to a different port if necessary
				secure: false, // accept self-signed certificates
			},
        }
    }
}