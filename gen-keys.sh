SERVER_KEYSTORE_DIR=./server
SERVER_KEYSTORE=$SERVER_KEYSTORE_DIR/server-key-store.p12
SERVER_TRUSTSTORE=$SERVER_KEYSTORE_DIR/server-trust-store.p12

CLIENT_KEYSTORE_DIR=./client
CLIENT_KEYSTORE=$CLIENT_KEYSTORE_DIR/client-key-store.p12
CLIENT_TRUSTSTORE=$CLIENT_KEYSTORE_DIR/client-trust-store.p12

mkdir $SERVER_KEYSTORE_DIR $CLIENT_KEYSTORE_DIR
# Generate a client and server RSA 2048 key pair
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -dname "CN=Server,OU=Server,O=Pivotal,L=London,S=LON,C=UK" -keypass changeme -keystore $SERVER_KEYSTORE -storetype PKCS12 -storepass changeme
keytool -genkeypair -alias client -keyalg RSA -keysize 2048 -dname "CN=Client,OU=Client,O=Pivotal,L=London,S=LON,C=UK" -keypass changeme -keystore $CLIENT_KEYSTORE -storetype PKCS12 -storepass changeme

# Export public certificates for both the client and server
keytool -exportcert -alias server -file $SERVER_KEYSTORE_DIR/server-public.cer -keystore $SERVER_KEYSTORE -storepass changeme
keytool -exportcert -alias client -file $CLIENT_KEYSTORE_DIR/client-public.cer -keystore $CLIENT_KEYSTORE -storepass changeme

# Import the client and server public certificates into each others keystore
keytool -importcert -keystore $SERVER_TRUSTSTORE -alias client-public-cert -file $CLIENT_KEYSTORE_DIR/client-public.cer -storepass changeme -noprompt
keytool -importcert -keystore $CLIENT_TRUSTSTORE -alias server-public-cert -file $SERVER_KEYSTORE_DIR/server-public.cer -storepass changeme -noprompt
