#!/bin/bash

BKS_PROVIDER="-providerclass org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath lib/bcprov-jdk15on-146.jar -storetype BKS"

# Generate client public/private key pair into private keystore
echo Generating client public private key pair
keytool -genkey -alias clientprivate -keystore client.private $BKS_PROVIDER

# Generate server public/private key pair
echo Generating server public private key pair
keytool -genkey -alias serverprivate -keystore server.private $BKS_PROVIDER

# Export client public key and import it into public keystore
echo Generating client public key file
keytool -export -alias clientprivate -keystore client.private -file temp.key $BKS_PROVIDER
keytool -import -noprompt -alias clientpublic -keystore client.public -file temp.key $BKS_PROVIDER
rm -f temp.key

# Export server public key and import it into public keystore
echo Generating server public key file
keytool -export -alias serverprivate -keystore server.private -file temp.key $BKS_PROVIDER
keytool -import -noprompt -alias serverpublic -keystore server.public -file temp.key $BKS_PROVIDER
rm -f temp.key
