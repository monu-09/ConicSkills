# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.synnapps.carouselview.** { *; }
-ignorewarnings

-keep class com.conicskill.app.data.** { *; }
-keep class com.yausername.youtubedl_android.** {*;}
-keep class com.google.gson.** { *; }
-keep public class com.google.gson.** {public private protected *;}
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

-keep class com.google.android.gms.** { *; }

-keep class org.simpleframework.xml.** { *; }

-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep class org.xmlpull.v1.** { *; }
-keep class com.conicskill.app.util.ytextractor.** { *; }