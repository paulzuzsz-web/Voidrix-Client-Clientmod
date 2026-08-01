package gg.norisk.client.v2.resourcepackorganizer;

public final class QuietPackMetaScope {
   private static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> Boolean.FALSE);

   private QuietPackMetaScope() {
   }

   public static boolean isActive() {
      return ACTIVE.get();
   }

   public static void enter() {
      ACTIVE.set(Boolean.TRUE);
   }

   public static void exit() {
      ACTIVE.set(Boolean.FALSE);
   }
}
