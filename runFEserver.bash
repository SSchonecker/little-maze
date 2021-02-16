cd mazeFE/
npm install
mkcert localhost
export NODE_EXTRA_CA_CERTS="$(mkcert -CAROOT)/rootCA.pem"
node_modules/.bin/webpack-dev-server --https --key ./localhost-key.pem --cert ./localhost.pem
