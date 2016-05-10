# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\mohan\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

###############Only Obfuscate the code, dont shrink and optimize
-dontoptimize
-dontshrink
-dontpreverify

# SearchView


#Verbis
#-keep class com.blackshift.verbis.BuildConfig { *; }

##### Retrofit
#-dontwarn retrofit2.**
#-keep class retrofit2.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions

#ViewpagerIndicator
#-dontwarn com.viewpagerindicator.LinePageIndicator
# or
# -dontwarn com.viewpagerindicator.**

# support design
#-dontwarn android.support.design.**
#-keep class android.support.design.** { *; }
#-keep interface android.support.design.** { *; }
#-keep public class android.support.design.R$* { *; }

# Support v7
#-keep class android.support.v7.internal.** { *; }
#-keep interface android.support.v7.internal.** { *; }

# Support v4
#-keep class android.support.v4.internal.** { *; }
#-keep interface android.support.v4.internal.** { *; }

# Glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}

# Glide transformations
#-dontwarn jp.co.cyberagent.android.gpuimage.**

#ButterKnife
 #-keep class **$$ViewBinder { *; }
 #-keep class butterknife.** { *; }
 #-dontwarn butterknife.internal.**
 #-keep class **$$ViewInjector { *; }
 #-keepnames class * { @butterknife.InjectView *;}
 #-keep public class * implements butterknife.internal.ViewBinder { public <init>(); }
 #-keepclasseswithmembernames class * { @butterknife.* <methods>; }
 #-keepclasseswithmembernames class * { @butterknife.* <fields>; }

 #OkHttp3
 #-keepattributes Signature
 #-keepattributes Annotation
 #-keep class okhttp3.** { ; }
 #-keep interface okhttp3.* { ; }
 #-dontwarn okhttp3.*