package gg.norisk.client.v2.modules.impl.motionblur

import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.Std140Builder
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.systems.RenderSystem
import gg.norisk.client.v2.modules.impl.MotionBlurModule
import kotlin.jdk7.AutoCloseableKt
import org.lwjgl.system.MemoryStack

public object MotionBlurUbo : AutoCloseable {
   public final val SIZE: Int = Std140SizeCalculator().putFloat().get()

   public final val buffer: GpuBuffer by LazyKt.lazy({ 
      RenderSystem.getDevice().createBuffer({ 
         "MotionBlur Settings UBO"
      }, 136, (long)SIZE)
   })
      public final get() {
         val var10000: Any = buffer$delegate.getValue()
         return var10000 as GpuBuffer
      }


   public fun set() {
      if (MotionBlurModule.INSTANCE.isEnabled()) {
         val var1: AutoCloseable = MemoryStack.stackPush() as AutoCloseable
         var var2: java.lang.Throwable = null

         try {
            RenderSystem.getDevice()
               .createCommandEncoder()
               .writeToBuffer(
                  INSTANCE.buffer.slice(),
                  Std140Builder.onStack(var1 as MemoryStack, SIZE)
                     .putFloat((float)Math.clamp(MotionBlurModule.INSTANCE.strength.doubleValue(), 0.01, 0.99))
                     .get()
               )
            } catch (var8: java.lang.Throwable) {
            var2 = var8
            throw var8
         } finally {
            AutoCloseableKt.closeFinally(var1, var2)
         }
      }
   }

   public override fun close() {
      this.buffer.close()
   }
}
