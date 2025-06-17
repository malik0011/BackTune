#!/bin/bash

# BackTune Keystore Creation Script
echo "🔑 Creating BackTune Keystore..."

# Check if Java is available
if ! command -v keytool &> /dev/null; then
    echo "❌ Java keytool not found. Please install Java or use Android Studio to create the keystore."
    echo ""
    echo "📱 Alternative: Use Android Studio"
    echo "1. Open Android Studio"
    echo "2. Build → Generate Signed Bundle/APK"
    echo "3. Select APK → Next"
    echo "4. Create new keystore with these details:"
    echo "   - Keystore path: /Users/ayan/AndroidStudioProjects/BackTune/app/backtune_keystore.jks"
    echo "   - Password: Ayan@123"
    echo "   - Alias: backtune_key"
    echo "   - Key password: Ayan@123"
    exit 1
fi

# Create keystore
keytool -genkey -v \
    -keystore app/backtune_keystore.jks \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -alias backtune_key \
    -storepass Ayan@123 \
    -keypass Ayan@123 \
    -dname "CN=Ayan Malik, OU=Development, O=BackTune, L=City, ST=State, C=US"

if [ $? -eq 0 ]; then
    echo "✅ Keystore created successfully!"
    echo "📁 Location: app/backtune_keystore.jks"
    echo "🔐 Password: Ayan@123"
    echo "🏷️  Alias: backtune_key"
    echo ""
    echo "🚀 You can now build release APK for Play Store!"
else
    echo "❌ Failed to create keystore"
    exit 1
fi 