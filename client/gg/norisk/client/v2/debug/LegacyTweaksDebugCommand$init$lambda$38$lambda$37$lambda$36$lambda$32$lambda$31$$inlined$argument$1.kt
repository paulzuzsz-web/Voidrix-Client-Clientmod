package gg.norisk.client.v2.debug

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import gg.norisk.compat.command.ArgumentCommandBuilder
import gg.norisk.compat.command.CommandBuilder
import java.util.UUID
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.reflect.KClass
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
@SourceDebugExtension(["SMAP\nCommandBuilder.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CommandBuilder.kt\ngg/norisk/compat/command/CommandBuilder$argument$2\n+ 2 ArgumentTypeUtils.kt\ngg/norisk/compat/command/ArgumentTypeUtilsKt\n*L\n1#1,445:1\n68#2,17:446\n*S KotlinDebug\n*F\n+ 1 CommandBuilder.kt\ngg/norisk/compat/command/CommandBuilder$argument$2\n*L\n151#1:446,17\n*E\n"])
internal class `LegacyTweaksDebugCommand$init$lambda$38$lambda$37$lambda$36$lambda$32$lambda$31$$inlined$argument$1` : Function0<java.lang.String> {
   fun `LegacyTweaksDebugCommand$init$lambda$38$lambda$37$lambda$36$lambda$32$lambda$31$$inlined$argument$1`(
      `$builder`: ArgumentCommandBuilder, `$name`: java.lang.String
   ) {
      this.$builder = `$builder`
      this.$name = `$name`
   }

   fun invoke() {
      var var10000: CommandContext = CommandBuilder.Companion.getActiveContext()
      if (var10000 == null) {
         var10000 = this.$builder.getCurrentContext()
      }

      val `name$iv`: java.lang.String = this.$name
      val var4: KClass = java.lang.String::class
      var var7: Any
      if (var4 == java.lang.Boolean::class) {
         var7 = BoolArgumentType.getBool(var10000, `name$iv`) as java.lang.String
      } else if (var4 == Int::class) {
         var7 = IntegerArgumentType.getInteger(var10000, `name$iv`) as java.lang.String
      } else if (var4 == java.lang.Long::class) {
         var7 = LongArgumentType.getLong(var10000, `name$iv`) as java.lang.String
      } else if (var4 == java.lang.Float::class) {
         var7 = FloatArgumentType.getFloat(var10000, `name$iv`) as java.lang.String
      } else if (var4 == java.lang.Double::class) {
         var7 = DoubleArgumentType.getDouble(var10000, `name$iv`) as java.lang.String
      } else if (var4 == java.lang.String::class) {
         var7 = StringArgumentType.getString(var10000, `name$iv`)
         if (var7 == null) {
            throw NullPointerException("null cannot be cast to non-null type kotlin.String")
         }
      } else {
         val var6: KClass = java.lang.String::class
         if (var6 == UUID::class) {
            var7 = UuidArgument.getUuid(var10000, `name$iv`)
            if (var7 == null) {
               throw NullPointerException("null cannot be cast to non-null type kotlin.String")
            }

            var7 = var7
         } else if (var6 == Identifier::class) {
            val var9: Identifier = IdentifierArgument.getId(var10000, `name$iv`)
            if (var9 == null) {
               throw NullPointerException("null cannot be cast to non-null type kotlin.String")
            }

            var7 = var9 as java.lang.String
         } else if (var6 == BlockPos::class) {
            val var10: BlockPos = BlockPosArgument.getBlockPos(var10000, `name$iv`)
            if (var10 == null) {
               throw NullPointerException("null cannot be cast to non-null type kotlin.String")
            }

            var7 = var10 as java.lang.String
         } else if (var6 == Vec3::class) {
            val var11: Vec3 = Vec3Argument.getVec3(var10000, `name$iv`)
            if (var11 == null) {
               throw NullPointerException("null cannot be cast to non-null type kotlin.String")
            }

            var7 = var11 as java.lang.String
         } else if (var6 == Component::class) {
            val var12: Component = ComponentArgument.getRawComponent(var10000, `name$iv`)
            if (var12 == null) {
               throw NullPointerException("null cannot be cast to non-null type kotlin.String")
            }

            var7 = var12 as java.lang.String
         } else {
            var7 = (java.lang.String)var10000.getArgument(`name$iv`, java.lang.String.class)
         }
      }

      var7
   }
}
