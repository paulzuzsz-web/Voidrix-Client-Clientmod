package gg.voidrix.client.v2.modules.chatheads

import gg.voidrix.compat.chat.AddVisibleMessageEvent
import gg.voidrix.compat.chat.BreakChatLinesEvent
import gg.voidrix.compat.chat.ChatEvents
import gg.voidrix.compat.chat.SeenPlayersCache
import gg.voidrix.compat.chat.VisitFormattedData
import gg.voidrix.compat.chat.AddVisibleMessageEvent.Injection
import gg.voidrix.compat.chat.SeenPlayersCache.MinecraftPlayer
import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.kotlin.ExtensionsKt
import gg.voidrix.compat.task.CoroutineScopesKt
import gg.voidrix.compat.task.CoroutineTask
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import java.util.ArrayList
import java.util.Optional
import java.util.UUID
import java.util.function.Supplier
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import kotlinx.coroutines.BuildersKt
import net.minecraft.client.multiplayer.chat.GuiMessage
import net.minecraft.client.multiplayer.chat.GuiMessage.Line
import net.minecraft.client.resources.DefaultPlayerSkin
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentContents
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.ClickEvent.SuggestCommand
import net.minecraft.network.chat.contents.ObjectContents
import net.minecraft.network.chat.contents.objects.PlayerSprite
import net.minecraft.world.entity.player.PlayerSkin
import org.slf4j.Logger
import org.spongepowered.asm.mixin.injection.At.Shift

@SourceDebugExtension(["SMAP\nChatHeads.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ChatHeads.kt\ngg/voidrix/client/v2/modules/chatheads/ChatHeads\n+ 2 CoroutineTask.kt\ngg/voidrix/compat/task/CoroutineTaskKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 MCLogger.kt\ngg/voidrix/compat/client/MCLoggerKt\n+ 5 Strings.kt\nkotlin/text/StringsKt__StringsKt\n*L\n1#1,347:1\n18#2,12:348\n1761#3,3:360\n63#4:363\n63#4:364\n63#4:365\n108#5:366\n80#5,22:367\n*S KotlinDebug\n*F\n+ 1 ChatHeads.kt\ngg/voidrix/client/v2/modules/chatheads/ChatHeads\n*L\n100#1:348,12\n215#1:360,3\n242#1:363\n45#1:364\n66#1:365\n195#1:366\n195#1:367,22\n*E\n"])
public object ChatHeads : Module("Chat Heads", ModuleCategory.VISUAL, false, true, false, 20) {
   public final val mode: HeadMode by ValueApiKt.enum$default(HeadMode.BEFORE_NAME, null, null, null, null, 30, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return mode$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as HeadMode
      }


   public final var startHeadApply: Boolean
      internal set

   public final var currentMessage: GuiMessage?
      internal set

   public final var currentChatMessage: GuiMessage?
      internal set

   public final var currentMessageOwner: Pair<UUID, Supplier<PlayerSkin>>?
      internal set

   public final var currentMessageTellReceiver: String?
      internal set

   public final var currentSkinTextures: PlayerSkin?
      internal set

   public final var prefixApplied: Boolean
      internal set

   public fun setCurrentSkinTextures(line: Line) {
      val dummy: ChatHudLineVisibleExt = line as ChatHudLineVisibleExt
      val var10000: Supplier = (line as ChatHudLineVisibleExt).voidrix_skinTextures
      currentSkinTextures = if (var10000 != null) var10000.get() as PlayerSkin else null
      val uuid: UUID = (line as ChatHudLineVisibleExt).voidrix_messageSender
      if (currentSkinTextures == null && (line as ChatHudLineVisibleExt).voidrix_tellReceiver != null) {
         currentSkinTextures = DefaultPlayerSkin.get(UUID(0L, 0L))
         val `client$iv`: Boolean = true
         BuildersKt.launch$default(
            CoroutineScopesKt.getBackgroundCoroutineScope(),
            null,
            null,
            ChatHeads$setCurrentSkinTextures$$inlined$mcCoroutineTask-ML416i8$default$1(
               Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null, line
            ),
            3,
            null
         )
      } else if (currentSkinTextures == null && uuid != null) {
         currentSkinTextures = DefaultPlayerSkin.get(uuid)
      }
   }

   public fun removeCurrentSkinTextures() {
      currentSkinTextures = null
   }

   public fun getTellReceiver(component: Component): Optional<String> {
      val var10000: Optional = component.visit(lambda_5@{ style: Style, string: java.lang.String ->
         if (style.clickEvent != null && style.clickEvent is SuggestCommand) {
            val cmd: java.lang.String = (style.clickEvent as SuggestCommand).command()
            if (cmd != null && StringsKt.startsWith$default(cmd, "/tell ", false, 2, null)) {
               val var10000: java.lang.String = cmd.substring(6)
               val `$this$trim$iv$iv`: java.lang.CharSequence = var10000
               var `startIndex$iv$iv`: Int = 0
               var `endIndex$iv$iv`: Int = `$this$trim$iv$iv`.length() - 1
               var `startFound$iv$iv`: Boolean = false

               while (`startIndex$iv$iv` <= `endIndex$iv$iv`) {
                  val var15: Boolean = Intrinsics.compare(`$this$trim$iv$iv`.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32)
                     <= 0
                     if (!`startFound$iv$iv`) {
                     if (!var15) {
                        `startFound$iv$iv` = true
                     } else {
                        `startIndex$iv$iv`++
                     }
                  } else {
                     if (!var15) {
                        break
                     }

                     `endIndex$iv$iv`--
                  }
               }

               return@lambda_5 Optional.of(`$this$trim$iv$iv`.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString())
            }
         }

         return@lambda_5 Optional.empty()
      }, Style.EMPTY)
      return var10000
   }

   public fun containsPlayerSprite(components: List<Component>?): Boolean {
      var var9: Boolean
      if (components != null) {
         val `$this$any$iv`: java.lang.Iterable = components
         if (components is java.util.Collection && (components as java.util.Collection).isEmpty()) {
            var9 = false
         } else {
            for (`element$iv` in `$this$any$iv`) {
               val component: Component = `element$iv` as Component
               if ((`element$iv` as Component).getContents() is ObjectContents) {
                  val var8: ComponentContents = component.getContents()
                  var9 = (if ((var8 as? ObjectContents) != null) (var8 as? ObjectContents).contents() else null) is PlayerSprite
               } else {
                  var9 = false
               }

               if (var9) {
                  return true
               }
            }

            var9 = false
         }
      } else {
         var9 = false
      }

      return var9
   }

   public fun Component.split(): List<Component> {
      val components: java.util.List = ArrayList()
      this.walkTree(`$this$split`, { c: Component ->
         val copy: MutableComponent = c.plainCopy().setStyle(c.getStyle())
         `$components`.add(copy)
         Unit.INSTANCE
      })
      return components
   }

   public fun Component.walkTree(consumer: (Component) -> Unit) {
      consumer(`$this$walkTree`)

      for (sibling in `$this$walkTree`.getSiblings()) {
         this.walkTree(sibling, consumer)
      }
   }

   public fun getMessagesOwner(string: String, content: Component): Pair<UUID, Supplier<PlayerSkin>>? {
      val `$this$voidrixDebug$iv`: Logger = this.getLogger().getValue() as Logger
      val player: java.lang.String = "Content: $content"
      if (MCLogger.IS_DEBUG) {
         `$this$voidrixDebug$iv`.info(player)
      }

      val var6: java.util.Iterator = SeenPlayersCache.INSTANCE.getSeenPlayersTrie().search(string).iterator()
      if (var6.hasNext()) {
         val var7: MinecraftPlayer = var6.next() as MinecraftPlayer
         return Pair(var7.getUuid(), var7.getSkinTextures())
      } else {
         return null
      }
   }

   @JvmStatic
   fun {
      ChatEvents.INSTANCE.getBreakRenderedChatMessageLinesEvent().listen({ event: BreakChatLinesEvent ->
         startHeadApply = event.getShift() === Shift.BEFORE
         Unit.INSTANCE
      })
      ChatEvents.INSTANCE.getVisitFormattedEvent().listen(lambda_1@{ data: VisitFormattedData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_1 Unit.INSTANCE
         } else {
            var currentString: java.lang.String = data.getString()
            if (currentMessage != null && startHeadApply) {
               var `$this$voidrixDebug$iv`: Logger
               var var14: java.lang.String
               run label111@{
                  `$this$voidrixDebug$iv` = INSTANCE.getLogger().getValue() as Logger
                  if (currentMessage != null) {
                     val var10001: Component = currentMessage.content()
                     if (var10001 != null) {
                        var14 = var10001.getString()
                        return@label111
                     }
                  }

                  var14 = null
               }

               val `message$iv`: java.lang.String = "Message: $currentString | Original $var14"
               if (MCLogger.IS_DEBUG) {
                  `$this$voidrixDebug$iv`.info(`message$iv`)
               }

               run label115@{
                  if (currentMessage != null) {
                     val var15: Component = currentMessage.content()
                     if (var15 != null) {
                        var16 = INSTANCE.split(var15)
                        return@label115
                     }
                  }

                  var16 = null
               }

               if (INSTANCE.containsPlayerSprite(var16)) {
                  return@lambda_1 Unit.INSTANCE
               }

               if (prefixApplied) {
                  return@lambda_1 Unit.INSTANCE
               }

               when (ChatHeads.WhenMappings.$EnumSwitchMapping$0[INSTANCE.mode.ordinal()]) {
                  1, 2 -> {
                     run label129@{
                        if (currentMessageOwner != null || currentMessageTellReceiver != null) {
                           var var13: Boolean
                           run label124@{
                              if (currentMessage != null) {
                                 val var10000: Component = currentMessage.content()
                                 if (var10000 != null) {
                                    val var12: java.lang.String = var10000.getString()
                                    if (var12 != null) {
                                       var13 = StringsKt.indexOf$default(var12, currentString, 0, false, 6, null) == 0
                                       return@label124
                                    }
                                 }
                              }

                              var13 = false
                           }

                           if (var13) {
                              currentString = "ꯟ $currentString"
                              return@label129
                           }
                        }

                        if (INSTANCE.mode === HeadMode.BEFORE_LINE_OFFSET) {
                           currentString = " $currentString"
                        }
                     }

                     prefixApplied = true
                     break
                  }
                  3 -> {
                     val var8: java.util.List = SeenPlayersCache.INSTANCE.getSeenPlayersTrie().search(currentString)
                     val `$this$voidrixDebug$ivx`: Logger = INSTANCE.getLogger().getValue() as Logger
                     val `message$ivx`: java.lang.String = "BEFORE_NAME -> $var8"
                     if (MCLogger.IS_DEBUG) {
                        `$this$voidrixDebug$ivx`.info(`message$ivx`)
                     }

                     val var9: java.util.Iterator = var8.iterator()
                     if (var9.hasNext()) {
                        val var11: java.lang.String = (var9.next() as MinecraftPlayer).getName()
                        currentString = StringsKt.replaceFirst$default(currentString, var11, "ꯟ $var11", false, 4, null)
                        prefixApplied = true
                     }
                     break
                  }
                  else -> throw NoWhenBranchMatchedException()
               }

               data.setString(currentString)
            }

            return@lambda_1 Unit.INSTANCE
         }
      })
      ChatEvents.INSTANCE.getAddVisibleMessageEvent().listen({ event: AddVisibleMessageEvent ->
         if (event.getInjection() === Injection.HEAD) {
            currentMessage = event.getChatHudLine()
            prefixApplied = false
         } else if (event.getInjection() === Injection.TAIL) {
            currentChatMessage = null
            currentMessage = null
         }

         Unit.INSTANCE
      })
   }
}
