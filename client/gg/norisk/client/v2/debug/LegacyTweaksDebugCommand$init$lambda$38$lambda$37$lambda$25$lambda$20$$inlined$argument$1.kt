package gg.norisk.client.v2.debug

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import java.util.UUID
import kotlin.jvm.functions.Function1
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.reflect.KClass
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.arguments.ComponentArgument
import net.minecraft.commands.arguments.IdentifierArgument
import net.minecraft.commands.arguments.UuidArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.phys.Vec3

// $VF: Class flags could not be determined
@SourceDebugExtension(["SMAP\nCommandBuilder.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CommandBuilder.kt\ngg/norisk/compat/command/CommandBuilder$argument$builder$1\n+ 2 ArgumentTypeUtils.kt\ngg/norisk/compat/command/ArgumentTypeUtilsKt\n*L\n1#1,445:1\n29#2,13:446\n55#2:459\n*S KotlinDebug\n*F\n+ 1 CommandBuilder.kt\ngg/norisk/compat/command/CommandBuilder$argument$builder$1\n*L\n133#1:446,13\n133#1:459\n*E\n"])
internal class `LegacyTweaksDebugCommand$init$lambda$38$lambda$37$lambda$25$lambda$20$$inlined$argument$1` : Function1<CommandBuildContext, ArgumentType<?>> {
   @JvmStatic
   public LegacyTweaksDebugCommand$init$lambda$38$lambda$37$lambda$25$lambda$20$$inlined$argument$1 INSTANCE = LegacyTweaksDebugCommand$init$lambda$38$lambda$37$lambda$25$lambda$20$$inlined$argument$1();

   fun invoke(ctx: CommandBuildContext): ArgumentType<*> {
      val var3: KClass = java.lang.Long::class
      val var4: ArgumentType
      if (var3 == java.lang.Boolean::class) {
         val var10000: BoolArgumentType = BoolArgumentType.bool()
         var4 = var10000 as ArgumentType
      } else if (var3 == Int::class) {
         val var5: IntegerArgumentType = IntegerArgumentType.integer()
         var4 = var5 as ArgumentType
      } else if (var3 == java.lang.Long::class) {
         val var6: LongArgumentType = LongArgumentType.longArg()
         var4 = var6 as ArgumentType
      } else if (var3 == java.lang.Float::class) {
         val var7: FloatArgumentType = FloatArgumentType.floatArg()
         var4 = var7 as ArgumentType
      } else if (var3 == java.lang.Double::class) {
         val var8: DoubleArgumentType = DoubleArgumentType.doubleArg()
         var4 = var8 as ArgumentType
      } else if (var3 == java.lang.String::class) {
         val var9: StringArgumentType = StringArgumentType.string()
         var4 = var9 as ArgumentType
      } else if (var3 == UUID::class) {
         val var10: UuidArgument = UuidArgument.uuid()
         var4 = var10 as ArgumentType
      } else if (var3 == Identifier::class) {
         val var11: IdentifierArgument = IdentifierArgument.id()
         var4 = var11 as ArgumentType
      } else if (var3 == BlockPos::class) {
         val var12: BlockPosArgument = BlockPosArgument.blockPos()
         var4 = var12 as ArgumentType
      } else if (var3 == Vec3::class) {
         val var13: Vec3Argument = Vec3Argument.vec3()
         var4 = var13 as ArgumentType
      } else {
         if (!(var3 == Component::class)) {
            throw IllegalArgumentException("No argument type mapping for ${(java.lang.Long::class).getQualifiedName()}")
         }

         val var14: ComponentArgument = ComponentArgument.textComponent(ctx)
         var4 = var14 as ArgumentType
      }

      var4
   }
}
