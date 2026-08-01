package gg.voidrix.client.v2.waypoints.xaero

public object InWorldRenderState {
   private final val depth: ThreadLocal<Int> = ThreadLocal.withInitial({ 
      0
   })

   @JvmStatic
   public fun push() {
      depth.set(depth.get().intValue() + 1)
   }

   @JvmStatic
   public fun pop() {
      val next: Int = depth.get().intValue() - 1
      depth.set(if (next < 0) 0 else next)
   }

   public final val isActive: Boolean
      public final get() {
         return depth.get().intValue() > 0
      }

}
