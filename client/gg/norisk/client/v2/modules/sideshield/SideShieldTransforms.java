package gg.norisk.client.v2.modules.sideshield;

import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

public final class SideShieldTransforms {
   private static final float U = 0.0625F;
   private static final ItemTransform IDLE_FIRST_PERSON_RIGHT = new ItemTransform(
      new Vector3f(0.0F, 90.0F, 90.0F), new Vector3f(0.9375F, 0.0F, 0.625F), new Vector3f(1.25F, 1.25F, 1.25F)
   );
   private static final ItemTransform IDLE_FIRST_PERSON_LEFT = new ItemTransform(
      new Vector3f(0.0F, 90.0F, 90.0F), new Vector3f(0.9375F, -1.25F, 0.625F), new Vector3f(1.25F, 1.25F, 1.25F)
   );
   private static final ItemTransform BLOCKING_FIRST_PERSON_RIGHT = new ItemTransform(
      new Vector3f(0.0F, 180.0F, -5.0F), new Vector3f(-0.9375F, 0.046875F, -0.6875F), new Vector3f(1.25F, 1.25F, 1.25F)
   );
   private static final ItemTransform BLOCKING_FIRST_PERSON_LEFT = new ItemTransform(
      new Vector3f(0.0F, 180.0F, -5.0F), new Vector3f(0.3125F, 0.15625F, -0.6875F), new Vector3f(1.25F, 1.25F, 1.25F)
   );

   private SideShieldTransforms() {
   }

   public static ItemTransform lookup(boolean blocking, ItemDisplayContext ctx) {
      if (blocking) {
         return switch (ctx) {
            case FIRST_PERSON_RIGHT_HAND -> BLOCKING_FIRST_PERSON_RIGHT;
            case FIRST_PERSON_LEFT_HAND -> BLOCKING_FIRST_PERSON_LEFT;
            default -> null;
         };
      } else {
         return switch (ctx) {
            case FIRST_PERSON_RIGHT_HAND -> IDLE_FIRST_PERSON_RIGHT;
            case FIRST_PERSON_LEFT_HAND -> IDLE_FIRST_PERSON_LEFT;
            default -> null;
         };
      }
   }
}
