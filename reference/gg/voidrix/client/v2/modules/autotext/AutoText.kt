package gg.voidrix.client.v2.modules.autotext

import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.event.KeyEventData
import gg.voidrix.compat.event.KeyEvents
import gg.voidrix.compat.event.MouseClickEventData
import gg.voidrix.compat.event.MouseEvents
import gg.voidrix.compat.resource.MCKey
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixLabelButton
import gg.voidrix.ui.modules.v3.V3Button
import gg.voidrix.ui.modules.v3.V3Theme
import java.awt.Color
import java.util.Arrays
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nAutoText.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AutoText.kt\ngg/voidrix/client/v2/modules/autotext/AutoText\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 4 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 5 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,149:1\n1869#2,2:150\n328#3:152\n40#3:153\n328#3:154\n40#3:155\n67#4:156\n269#4:157\n1#5:158\n*S KotlinDebug\n*F\n+ 1 AutoText.kt\ngg/voidrix/client/v2/modules/autotext/AutoText\n*L\n129#1:150,2\n45#1:152\n45#1:153\n62#1:154\n62#1:155\n99#1:156\n99#1:157\n*E\n"])
public object AutoText : Module("AutoText", ModuleCategory.QUALITY_OF_LIFE, false, true, false, 20) {
   public open val seoTags: Array<String>

   public final var keybinds: MutableList<AutoTextEntry> by ValueApiKt.list$default(
         { 
            CollectionsKt.mutableListOf(
               arrayOf(
                  AutoTextEntry(null, "voidrix ooooooon top", null, false, 13, null),
                  AutoTextEntry(null, "/command", null, false, 13, null),
                  AutoTextEntry(null, "/voidrix minigames tetris computer", null, false, 13, null),
                  AutoTextEntry(null, "/voidrix flappybird", null, false, 13, null)
               )
            )
         },
         AutoTextEntry.Companion.serializer(),
         null,
         null,
         null,
         28,
         null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return keybinds$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MutableList<AutoTextEntry>
      }

      public final set(<set-?>) {
         keybinds$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   private final val pressedKeys: CopyOnWriteArrayList<MCKey> = CopyOnWriteArrayList()

   public open fun onEnable() {
      super.onEnable()
      pressedKeys.clear()
   }

   public open fun onDisable() {
      super.onDisable()
      pressedKeys.clear()
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         settingsPanel.clearChildren()
         settingsPanel.gap(2)
         val list: AutoTextListComponent = AutoTextListComponent(null, null, 3, null)
         val `$this$asString$iv`: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
         `$this$asString$iv`.alignment(HorizontalAlignment.RIGHT, VerticalAlignment.CENTER)
         `$this$asString$iv`.padding(Insets.Companion.of(2, 4, 4, 4))
         val var11: Array<Any> = arrayOfNulls(0)
         val var10003: MutableComponent = Component.translatable("voidrix.autotext.button.add", Arrays.copyOf(var11, var11.length))
         val var19: java.lang.String = (var10003 as Component).getString()
         val var9: V3Button = V3Button("+ ${TextKt.toSmallCaps(var19)}", { var1: VoidrixLabelButton, var2: Double, var4: Double, var6: Int ->
            UISounds.playButtonSound()
            INSTANCE.addEntry(`$list`)
            Unit.INSTANCE
         })
         var9.sizing(Sizing.Companion.fixed(60), Sizing.Companion.fixed(13))
         var9.padding(Insets.Companion.of(0))
         var9.getLabel().scale(0.75F)
         var9.setForcedColor(Color(V3Theme.INSTANCE.accentAlpha(5, 110), true))
         var9.setForcedHoverColor(Color(V3Theme.INSTANCE.accentAlpha(7, 170), true))
         var9.setForcedBorderColor({ 
            V3Theme.INSTANCE.accent(8)
         })
         `$this$asString$iv`.child(var9 as UIComponent)
         settingsPanel.child(`$this$asString$iv` as UIComponent)
         list.build(INSTANCE.keybinds)
         settingsPanel.child(list as UIComponent)
         Unit.INSTANCE
      }
   }

   private fun addEntry(list: AutoTextListComponent) {
      this.keybinds.add(AutoTextEntry(null, null, null, false, 15, null))
      list.build(this.keybinds)
      this.updateProfile()
   }

   public fun removeEntry(entry: AutoTextEntry, list: AutoTextListComponent) {
      this.keybinds.removeIf({ p0: Any ->
         `$tmp0`(p0)
      })
      list.build(this.keybinds)
      this.updateProfile()
   }

   private fun checkAndExecuteKeybind() {
      var flag: Boolean = false

      for (`element$iv` in this.keybinds) {
         val keybind: AutoTextEntry = `element$iv` as AutoTextEntry
         if ((`element$iv` as AutoTextEntry).isEnabled && (`element$iv` as AutoTextEntry).hasValidKeys()) {
            val expected: java.util.List = keybind.toMCKeyList()
            if (!expected.isEmpty() && pressedKeys.size() >= expected.size() && CollectionsKt.takeLast(pressedKeys, expected.size()) == expected) {
               flag = true
               MCClient.sendChatOrCommand(StringsKt.trim(keybind.text).toString())
            }
         }
      }

      if (flag) {
         pressedKeys.clear()
      }
   }

   @JvmStatic
   fun {
      KeyEvents.INSTANCE.getKeyEvent().listen(lambda_1@{ event: KeyEventData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_1 Unit.INSTANCE
         } else {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() != null) {
               pressedKeys.clear()
               return@lambda_1 Unit.INSTANCE
            } else {
               val var3: MCKey = MCKey.Companion.ofKeyboard(event.getKey())
               if (var3.isUnknown()) {
                  return@lambda_1 Unit.INSTANCE
               } else {
                  if (event.isClicked()) {
                     pressedKeys.add(var3)
                     INSTANCE.checkAndExecuteKeybind()
                  } else if (event.isReleased()) {
                     pressedKeys.remove(var3)
                  }

                  return@lambda_1 Unit.INSTANCE
               }
            }
         }
      })
      MouseEvents.INSTANCE.getMouseClickEvent().listen(lambda_2@{ event: MouseClickEventData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_2 Unit.INSTANCE
         } else {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() != null) {
               pressedKeys.clear()
               return@lambda_2 Unit.INSTANCE
            } else {
               val var3: MCKey = MCKey.Companion.ofMouse(event.getButton())
               if (var3.isUnknown()) {
                  return@lambda_2 Unit.INSTANCE
               } else {
                  if (event.getAction() == 1) {
                     pressedKeys.add(var3)
                     INSTANCE.checkAndExecuteKeybind()
                  } else if (event.getAction() == 0) {
                     pressedKeys.remove(var3)
                  }

                  return@lambda_2 Unit.INSTANCE
               }
            }
         }
      })
   }
}
