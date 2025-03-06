# Keep Google Play Services Ads classes
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**
-keep class com.google.android.gms.** { *; }
-keep class com.google.ads.** { *; }
-keepattributes Signature

# Google Play Services rules
# -keep class com.google.android.gms.location.FusedLocationProviderClient { *; }

# Gson rules
-keepattributes *Annotation*
-dontwarn sun.misc.**

# -keep class * extends com.google.gson.TypeAdapter

# -keep class * implements com.google.gson.TypeAdapter
# -keep class * implements com.google.gson.TypeAdapterFactory
# -keep class * implements com.google.gson.JsonSerializer
# -keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers,allowobfuscation class * {
   #  @com.google.gson.annotations.SerializedName <fields>;
}

# -keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
# -keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Huawei rules
-dontwarn com.huawei.hms.ads.identifier.AdvertisingIdClient$Info
-dontwarn com.huawei.hms.ads.identifier.AdvertisingIdClient

# Parcelize rules
-dontwarn kotlinx.parcelize.Parcelize

# Retrofit rules
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# WorkManager rules
-keepclassmembers class * extends androidx.work.WorkRequest$Builder { public *; }