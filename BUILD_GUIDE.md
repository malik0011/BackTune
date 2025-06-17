# BackTune Build Guide

## 🎯 **Dual Build Configuration Setup**

This guide provides solutions for both **device testing** and **Play Store submission**.

---

## 🚨 **IMMEDIATE SOLUTION - Run Release Build Variant**

### **Current Status: ✅ FIXED**
The release build variant now works without keystore! It automatically uses debug signing if keystore is missing.

### **To Run Release Build Variant:**
1. **In Android Studio**: Select **release** from Build Variants
2. **Click Run** - it will work now!
3. **Or use command**: `./gradlew assembleRelease`

---

## 📱 **For Device Testing (Debug Build)**

### **Build Debug APK:**
```bash
./gradlew assembleDebug
```

### **Install on Device:**
```bash
./gradlew installDebug
```

### **Debug Build Features:**
- ✅ **Automatically signed** by Android Studio
- ✅ **Debuggable** for development
- ✅ **No code obfuscation** (minifyEnabled = false)
- ✅ **Fast build times**
- ✅ **Can be installed on any device**

---

## 🏪 **For Play Store Submission (Release Build)**

### **Step 1: Create Keystore**
**Option A: Using Script (if Java available)**
```bash
./create_keystore.sh
```

**Option B: Using Android Studio (Recommended)**
1. **Open Android Studio**
2. **Build** → **Generate Signed Bundle/APK**
3. **Select APK** → **Next**
4. **Create new keystore:**
   ```
   Keystore path: /Users/ayan/AndroidStudioProjects/BackTune/app/backtune_keystore.jks
   Password: Ayan@123
   Alias: backtune_key
   Key password: Ayan@123
   ```
5. **Fill certificate details** with your information
6. **Click OK** → **Next** → **Finish**

### **Step 2: Build Release APK**
```bash
./gradlew assembleRelease
```

### **Release Build Features:**
- ✅ **Properly signed** for Play Store (when keystore exists)
- ✅ **Code obfuscated** (minifyEnabled = true)
- ✅ **Optimized** for production
- ✅ **Not debuggable** (security)
- ✅ **Ready for Play Store submission**

---

## 🔧 **Build Commands Summary**

| Purpose | Command | Output Location |
|---------|---------|----------------|
| **Debug (Testing)** | `./gradlew assembleDebug` | `app/build/outputs/apk/debug/` |
| **Release (Play Store)** | `./gradlew assembleRelease` | `app/build/outputs/apk/release/` |
| **Install Debug** | `./gradlew installDebug` | Directly to device |
| **Clean Build** | `./gradlew clean` | Cleans build cache |

---

## 📋 **Current Configuration**

### **Debug Build:**
- **Signing**: Automatic debug signing
- **Minification**: Disabled
- **Debuggable**: Yes
- **Use Case**: Development and testing

### **Release Build:**
- **Signing**: Custom keystore (when exists) OR debug signing (fallback)
- **Minification**: Enabled
- **Debuggable**: No
- **Use Case**: Play Store submission OR testing

---

## 🚀 **Quick Start**

### **For Testing (Immediate):**
1. Select **release** build variant in Android Studio
2. Click **Run** - it works now!
3. Or run `./gradlew assembleRelease`

### **For Play Store:**
1. Create keystore via Android Studio or script
2. Run `./gradlew assembleRelease`
3. Upload the release APK to Play Store

---

## ⚠️ **Important Notes**

1. **Keep your keystore safe** - you'll need it for all future app updates
2. **Debug builds** are for testing only, not for Play Store
3. **Release builds** now work without keystore (uses debug signing as fallback)
4. **For Play Store**: You must create the keystore
5. **Never commit** your keystore file to version control

---

## 🔍 **Troubleshooting**

### **"Keystore not found" Error:**
- ✅ **FIXED**: Release build now uses debug signing as fallback
- Create the keystore file using Android Studio for Play Store

### **"INSTALL_PARSE_FAILED_NO_CERTIFICATES" Error:**
- ✅ **FIXED**: Release build now has certificates (debug signing)

### **Build Fails:**
- Clean project: `./gradlew clean`
- Rebuild: `./gradlew assembleDebug` or `./gradlew assembleRelease`

### **Release Build Variant Won't Run:**
- ✅ **FIXED**: It should work now with debug signing fallback 