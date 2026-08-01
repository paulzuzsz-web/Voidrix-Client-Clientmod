package gg.voidrix.client.v2.moderation.ui

import com.mojang.authlib.GameProfile
import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.kotlin.ExtensionsKt
import gg.voidrix.compat.scale.IVoidrixScreen
import gg.voidrix.compat.task.CoroutineScopesKt
import gg.voidrix.compat.task.CoroutineTask
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.text.TextKt
import gg.voidrix.cosmetics.v2.ui.preview.CosmeticPlayerPreviewProvider
import gg.voidrix.cosmetics.v2.ui.preview.CosmeticShowcasePlayer
import gg.voidrix.owolib.owo.ui.base.BaseOwoScreen
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.component.entity.EntityComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.OverlayContainer
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.OwoUIAdapter
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.Surface
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.components.voidrix.VoidrixDialog
import gg.voidrix.ui.components.voidrix.VoidrixInputField
import gg.voidrix.ui.components.voidrix.VoidrixLabelButton
import gg.voidrix.ui.theme.ThemeModule
import java.util.Arrays
import java.util.UUID
import kotlin.enums.EnumEntries
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import kotlinx.coroutines.BuildersKt
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.Entity
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nReportScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ReportScreen.kt\ngg/voidrix/client/v2/moderation/ui/ReportScreen\n+ 2 MCLogger.kt\ngg/voidrix/compat/client/MCLoggerKt\n+ 3 CoroutineTask.kt\ngg/voidrix/compat/task/CoroutineTaskKt\n+ 4 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 5 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 6 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 7 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 8 TextBuilder.kt\ngg/voidrix/compat/text/LiteralTextBuilder\n*L\n1#1,201:1\n60#2:202\n18#3,12:203\n355#4:215\n40#4:216\n356#4:217\n355#4:247\n40#4:248\n356#4:249\n355#4:266\n40#4:267\n356#4:268\n67#5:218\n67#5:219\n67#5:254\n67#5:269\n67#5:270\n1#6:220\n8#7,4:221\n8#7,4:250\n78#8,6:225\n72#8,4:231\n87#8:235\n78#8,6:236\n72#8,4:242\n87#8:246\n90#8,6:255\n72#8,4:261\n99#8:265\n*S KotlinDebug\n*F\n+ 1 ReportScreen.kt\ngg/voidrix/client/v2/moderation/ui/ReportScreen\n*L\n37#1:202\n169#1:203,12\n108#1:215\n108#1:216\n108#1:217\n146#1:247\n146#1:248\n146#1:249\n158#1:266\n158#1:267\n158#1:268\n58#1:218\n83#1:219\n151#1:254\n122#1:269\n138#1:270\n95#1:221,4\n151#1:250,4\n96#1:225,6\n96#1:231,4\n96#1:235\n99#1:236,6\n99#1:242,4\n99#1:246\n151#1:255,6\n151#1:261,4\n151#1:265\n*E\n"])
public class ReportScreen(targetUuid: UUID) : BaseOwoScreen(null, 1), IVoidrixScreen {
   private final val targetUuid: UUID

   private final val log: Logger
      private final get() {
         return this.log$delegate.getValue() as Logger
      }


   protected open val useBlurredBackground: Boolean
   private final var isSubmitting: Boolean

   init {
      ((ReportScreen)this).targetUuid = targetUuid
      ((ReportScreen)this).log$delegate = LazyKt.lazy(ReportScreen$special$$inlined$lazyLogger$1.INSTANCE)
      this.useBlurredBackground = true
   }

   protected open fun createAdapter(): OwoUIAdapter<FlowLayout> {
      return OwoUIAdapter.Companion.create(this as Screen, gg/voidrix/client/v2/moderation/ui/ReportScreen##Lambda_0_95())
   }

   protected open fun build(rootComponent: FlowLayout) {
      rootComponent.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      VoidrixDialog.Companion
         .openConfirmDialogNoEscape(
            rootComponent as ParentUIComponent,
            { dialog: VoidrixDialog, overlay: OverlayContainer ->
               overlay.surface(Surface.BLANK)
               dialog.horizontalSizing(Sizing.Companion.fixed(280))
               dialog.allowOverflow(true)
               val var10000: LabelComponent = dialog.getTitle()
               val `baseText$iv`: Array<Any> = arrayOfNulls(0)
               var var10001: MutableComponent = Component.translatable("voidrix.moderation.report.title", Arrays.copyOf(`baseText$iv`, `baseText$iv`.length))
               var10001 = var10001.withColor(ThemeModule.INSTANCE.getFontColor().getRGB())
               var10000.text(var10001 as Component)
               val var19: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
               var19.gap(10)
               var19.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
               val contentRow: FlowLayout = var19

               try {
                  val var28: EntityComponent = EntityComponent.Companion
                     .of(
                        Sizing.Companion.fixed(50),
                        CosmeticShowcasePlayer(CosmeticPlayerPreviewProvider.INSTANCE.withTextures(GameProfile(`this$0`.targetUuid, "ReportTarget"))) as Entity
                     )
                     var28.verticalSizing(Sizing.Companion.fixed(70))
                  var28.scaleToFit(true)
                  var28.allowMouseRotation(true)
                  var28.lookAtCursor(true)
                  contentRow.child(var28 as UIComponent)
               } catch (var18: java.lang.Throwable) {
               }

               val var24: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
               val var29: FlowLayout = var24
               var24.gap(5)
               var24.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
               val `args$ivx`: Array<Any> = arrayOfNulls(0)
               var var10003: MutableComponent = Component.translatable("voidrix.moderation.report.select_category", Arrays.copyOf(`args$ivx`, `args$ivx`.length))
               var10003 = var10003.withColor(ThemeModule.INSTANCE.getFontColor().getRGB())
               val `inheritStyle$iv`: LabelComponent = LabelComponent(var10003 as Component)
               `inheritStyle$iv`.shadow(false)
               var24.child(`inheritStyle$iv` as UIComponent)

               for (var35 in ReportScreen.ReportReason.getEntries()) {
                  val var38: VoidrixLabelButton = VoidrixLabelButton(
                     TextKt.toSmallCaps(var35.displayName), { var4: VoidrixLabelButton, var5: Double, var7: Double, var9: Int ->
                        UISounds.playButtonSound()
                        `this$0`.showReasonInput(`$rootComponent`, `$dialog`, `$reason`)
                        Unit.INSTANCE
                     }
                  )
                  var38.horizontalSizing(Sizing.Companion.fixed(100))
                  var29.child(var38 as UIComponent)
               }

               var19.child(var24 as UIComponent)
               dialog.content(var19 as UIComponent)
               dialog.getMain().gap(10)
               val var53: FlowLayout = dialog.getMain()
               val var32: LiteralTextBuilder = LiteralTextBuilder(null, true)
               var32.getAppendTasks().add(ReportScreen$build$lambda$8$lambda$5$$inlined$text$default$1(var32, "Reporting UUID:", true))
               var32.setColor(ThemeModule.INSTANCE.getFontColor().darker().getRGB())
               var32.newLine()
               var32.getAppendTasks()
                  .add(ReportScreen$build$lambda$8$lambda$5$$inlined$text$default$2(var32, java.lang.String.valueOf(`this$0`.targetUuid), true))
                  var32.setColor(ThemeModule.INSTANCE.getFontColor().darker().darker().getRGB())
               val var26: LabelComponent = LabelComponent(var32.build() as Component)
               var26.shadow(true)
               var26.maxWidth(260)
               var53.child(var26 as UIComponent)
               dialog.getButtonActionWrapper().clearChildren()
               dialog.getCloseButton().onClick({ var0: VoidrixLabelButton, var1: Double, var3: Double, var5: Int ->
                  val var10000: Minecraft = Minecraft.getInstance()
                  var10000.gui.setScreen(null)
                  Unit.INSTANCE
               })
               Unit.INSTANCE
            }
         )
      }

   private fun showReasonInput(root: FlowLayout, oldDialog: VoidrixDialog, reason: gg.voidrix.client.v2.moderation.ui.ReportScreen.ReportReason) {
      oldDialog.close()
      root.clearChildren()
      VoidrixDialog.Companion
         .openConfirmDialogNoEscape(
            root as ParentUIComponent,
            { dialog: VoidrixDialog, overlay: OverlayContainer ->
               overlay.surface(Surface.BLANK)
               dialog.horizontalSizing(Sizing.Companion.fixed(260))
               dialog.allowOverflow(true)
               val var10000: LabelComponent = dialog.getTitle()
               val var16: Array<Any> = arrayOf(`$reason`.displayName)
               var var10001: MutableComponent = Component.translatable("voidrix.moderation.report.title_with_reason", Arrays.copyOf(var16, var16.length))
               var10001 = var10001.withColor(ThemeModule.INSTANCE.getFontColor().getRGB())
               var10000.text(var10001 as Component)
               val inputField: VoidrixInputField = VoidrixInputField(230, "", 0.0F, false, 12, null)
               inputField.setOnEnter({ it: java.lang.String ->
                  val text: java.lang.String = StringsKt.trim(`$inputField`.getText()).toString()
                  if (text.length() > 0) {
                     UISounds.playButtonSound()
                     `this$0`.submitReport(`$reason`, text)
                  }

                  Unit.INSTANCE
               })
               val var15: LabelComponent = LabelComponent(TextKt.getLiteral("") as Component)
               var15.shadow(false)
               val var18: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
               var18.gap(3)
               val `args$ivx`: Array<Any> = arrayOfNulls(0)
               var var10003: MutableComponent = Component.translatable("voidrix.moderation.report.enter_reason", Arrays.copyOf(`args$ivx`, `args$ivx`.length))
               var10003 = var10003.withColor(ThemeModule.INSTANCE.getFontColor().getRGB())
               val var13: LabelComponent = LabelComponent(var10003 as Component)
               var13.shadow(false)
               var18.child(var13 as UIComponent)
               var18.child(inputField as UIComponent)
               var18.child(var15 as UIComponent)
               dialog.content(var18 as UIComponent)
               dialog.getCancelButton().onClick({ var1: VoidrixLabelButton, var2: Double, var4: Double, var6: Int ->
                  UISounds.playButtonSound()
                  val `screen$iv`: Screen = ReportScreen(`this$0`.targetUuid) as Screen
                  val var10000: Minecraft = Minecraft.getInstance()
                  var10000.gui.setScreen(`screen$iv`)
                  Unit.INSTANCE
               })
               dialog.getConfirmButton()
                  .onClick(
                     { var4: VoidrixLabelButton, var5: Double, var7: Double, var9: Int ->
                        val text: java.lang.String = StringsKt.trim(`$inputField`.getText()).toString()
                        if (text.length() == 0) {
                           val var13: LiteralTextBuilder = LiteralTextBuilder(null, true)
                           val `inheritStyle$iv`: Array<Any> = arrayOfNulls(0)
                           val var10000: MutableComponent = Component.translatable(
                              "voidrix.moderation.report.reason_required", Arrays.copyOf(`inheritStyle$iv`, `inheritStyle$iv`.length)
                           )
                           var13.getAppendTasks()
                              .add(ReportScreen$showReasonInput$lambda$16$lambda$14$lambda$13$$inlined$text$default$1(var13, var10000, true))
                              var13.setColor(16733525)
                           `$errorLabel`.text(var13.build() as Component)
                        } else {
                           UISounds.playButtonSound()
                           `this$0`.submitReport(`$reason`, text)
                        }

                        Unit.INSTANCE
                     }
                  )
                  dialog.getCloseButton().onClick({ var0: VoidrixLabelButton, var1: Double, var3: Double, var5: Int ->
                  val var10000: Minecraft = Minecraft.getInstance()
                  var10000.gui.setScreen(null)
                  Unit.INSTANCE
               })
               Unit.INSTANCE
            }
         )
      }

   private fun submitReport(reason: gg.voidrix.client.v2.moderation.ui.ReportScreen.ReportReason, text: String) {
      if (!StringsKt.isBlank(text) && !this.isSubmitting) {
         this.isSubmitting = true
         var var10000: java.lang.String = MCClient.getCurrentServerIp()
         if (var10000 == null) {
            var10000 = "NO SERVER"
         }

         val `client$iv`: Boolean = true
         BuildersKt.launch$default(
            CoroutineScopesKt.getBackgroundCoroutineScope(),
            null,
            null,
            ReportScreen$submitReport$$inlined$mcCoroutineTask-ML416i8$default$1(
               Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(),
               1L,
               CoroutineTask(1L),
               ExtensionsKt.getTicks(1),
               null,
               reason,
               this,
               text,
               var10000
            ),
            3,
            null
         )
      }
   }

   public enum class ReportReason(backend: String, displayName: String) {
      VoiceChat("core/voicechat", "VoiceChat"),
      Nametag("cosmetics", "Nametag");

      public final val backend: String
      public final val displayName: String

      init {
         this.backend = backend
         this.displayName = displayName
      }

      @JvmStatic
      fun getEntries(): EnumEntries<ReportScreen.ReportReason> {
         $ENTRIES
      }
   }
}
