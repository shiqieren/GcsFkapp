# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class net.oschina.app.improve.bean.** { *; }

-keepattributes EnclosingMethod

##---------------End: proguard configuration for Gson  ----------

-dontwarn com.thoughtworks.xstream.**
-keep class com.thoughtworks.xstream.** { *; }

-dontwarn com.makeramen.roundedimageview.**
-keep class com.makeramen.roundedimageview.RoundedTransformationBuilder

-dontwarn com.tencent.weibo.sdk.android.**
-keep class com.tencent.weibo.sdk.android.** { *; }

-dontwarn com.squareup.leakcanary.DisplayLeakService
-keep class com.squareup.leakcanary.DisplayLeakService

-dontwarn android.widget.**
-keep class android.widget.** {*;}

-dontwarn android.support.v7.widget.**
-keep class android.support.v7.widget.**{*;}

-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public class javax.**
-keep public class android.webkit.**

#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}


#okio
-dontwarn okio.**
-keep class okio.**{*;}


# baidu map sdk
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**


# baiqishi ------------------- start ---------------------------
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#不混淆Serializable和它的实现子类、其成员变量
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepnames class com.bqs.crawler.cloud.sdk.view.LoginView$* {
    public <fields>;
    public <methods>;
    public *;#不混淆public方法
}
-keep class com.bqs.crawler.cloud.sdk.view.LoginView {#不混淆LoginView类
	public *;#不混淆public方法
}
-keep class com.bqs.crawler.cloud.sdk.view.StateView {#不混淆StateView类
	public *;#不混淆public方法
}

-keep class com.bqs.crawler.cloud.sdk.view.dialog.BackgroundLayout {#不混淆BackgroundLayout类
	public *;#不混淆public方法
}

-keep class com.bqs.crawler.cloud.sdk.view.H5LoginWebView {#不混淆H5LoginWebView类
	public *;#不混淆public方法
}

#不混淆自定义view
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
    public *;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public *;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-dontpreverify
-dontwarn

#枚举类不能被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留自定义控件(继承自View)不能被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
    *** get* ();
}

# baiqishi ------------------- end ---------------------------

