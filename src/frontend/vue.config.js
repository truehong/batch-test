const path = require('path')
module.exports = {
    outputDir: path.resolve(__dirname, "../" + "main/resources/static"),

    devServer: {
        port: 8087,
        proxy: {
            '/api': {
                target: 'http://localhost:18080',
                ws: true,
                changeOrigin: true
            },
            '/schedulers': {
                target: 'http://localhost:18080',
                ws: true,
                changeOrigin: true
            }
        }
    },
    transpileDependencies: [
        'vuetify'
    ]
}
