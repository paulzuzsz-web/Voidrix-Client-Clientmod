package gg.voidrix.client.v2.waypoints.xaero

import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nXaeroReflect.kt\nKotlin\n*S Kotlin\n*F\n+ 1 XaeroReflect.kt\ngg/voidrix/client/v2/waypoints/xaero/XaeroWaypoint\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,774:1\n1#2:775\n*E\n"])
public class XaeroWaypoint internal constructor(raw: Any) {
   internal final val raw: Any

   init {
      this.raw = raw
   }

   public final val name: String
      public final get() {
         return XaeroReflect.INSTANCE.wpName$voidrix_client(this.raw)
      }


   public final val initials: String
      public final get() {
         return XaeroReflect.INSTANCE.wpInitials$voidrix_client(this.raw)
      }


   public final val x: Int
      public final get() {
         return XaeroReflect.INSTANCE.wpX$voidrix_client(this.raw)
      }


   public final val y: Int
      public final get() {
         return XaeroReflect.INSTANCE.wpY$voidrix_client(this.raw)
      }


   public final val z: Int
      public final get() {
         return XaeroReflect.INSTANCE.wpZ$voidrix_client(this.raw)
      }


   public final val color: Int
      public final get() {
         return XaeroReflect.INSTANCE.wpColor$voidrix_client(this.raw)
      }


   public final val disabled: Boolean
      public final get() {
         return XaeroReflect.INSTANCE.wpDisabled$voidrix_client(this.raw)
      }


   public final val yIncluded: Boolean
      public final get() {
         return XaeroReflect.INSTANCE.wpYIncluded$voidrix_client(this.raw)
      }


   public final val createdAt: Long
      public final get() {
         return XaeroReflect.INSTANCE.wpCreatedAt$voidrix_client(this.raw)
      }


   public final val purposeName: String
      public final get() {
         return XaeroReflect.INSTANCE.wpPurposeName$voidrix_client(this.raw)
      }


   public final val visibilityName: String
      public final get() {
         return XaeroReflect.INSTANCE.wpVisibilityName$voidrix_client(this.raw)
      }


   public fun applyUpdate(
      name: String,
      initials: String,
      x: Int,
      y: Int,
      z: Int,
      color: Int,
      disabled: Boolean,
      yIncluded: Boolean,
      purposeName: String? = null,
      visibilityName: String? = null
   ) {
      var var10000: XaeroReflect = XaeroReflect.INSTANCE
      var var10001: Any = this.raw
      var var10002: java.lang.String = name
      var var10003: java.lang.String = initials
      var var10004: Int = x
      var var10005: Int = y
      var var10006: Int = z
      var var10007: Int = color
      var var10008: Boolean = disabled
      var var10009: Boolean = yIncluded
      val var10010: Any
      if (purposeName != null) {
         val var14: Any = this.raw
         val var13: XaeroReflect = XaeroReflect.INSTANCE
         val var23: Any = XaeroReflect.INSTANCE.purposeFromName$voidrix_client(purposeName)
         var10000 = var13
         var10001 = var14
         var10002 = name
         var10003 = initials
         var10004 = x
         var10005 = y
         var10006 = z
         var10007 = color
         var10008 = disabled
         var10009 = yIncluded
         var10010 = var23
      } else {
         var10010 = null
      }

      var10000.applyUpdate$voidrix_client(
         var10001,
         var10002,
         var10003,
         var10004,
         var10005,
         var10006,
         var10007,
         var10008,
         var10009,
         var10010,
         if (visibilityName != null) XaeroReflect.INSTANCE.visibilityFromName$voidrix_client(visibilityName) else null
      )
   }

   public companion object {
      public fun create(x: Int, y: Int, z: Int, name: String, initials: String, color: Int, purposeName: String = "NORMAL", yIncluded: Boolean = true): XaeroWaypoint? {
         val var10000: Any = XaeroReflect.INSTANCE
            .newWaypoint$voidrix_client(x, y, z, name, initials, color, XaeroReflect.INSTANCE.purposeFromName$voidrix_client(purposeName), yIncluded)
            return if (var10000 == null) null else XaeroWaypoint(var10000)
      }
   }
}
