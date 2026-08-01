package gg.norisk.client.v2.auth

import com.mojang.authlib.GameProfile
import gg.norisk.compat.auth.DevAuth
import gg.norisk.compat.auth.DevPinStore
import gg.norisk.compat.auth.SessionData
import gg.norisk.compat.auth.SessionHelper
import gg.norisk.compat.auth.SessionStatus
import gg.norisk.compat.auth.SessionSwapper
import gg.norisk.compat.auth.DevPinStore.Role
import gg.norisk.compat.auth.launcher.AuthHandback
import gg.norisk.compat.auth.launcher.Credentials
import gg.norisk.compat.auth.launcher.LocalRedirectServer
import gg.norisk.compat.auth.launcher.LoginOrchestrator
import gg.norisk.compat.auth.launcher.MinecraftAuthStore
import gg.norisk.compat.auth.launcher.TokenRefresh
import gg.norisk.compat.auth.launcher.DeviceCodeFlow.Challenge
import gg.norisk.compat.auth.launcher.LoginOrchestrator.LoginStep
import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.input.ClipboardHelper
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.ScrollContainer
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.container.ScrollContainer.Scrollbar
import gg.norisk.owolib.owo.ui.core.Color
import gg.norisk.owolib.owo.ui.core.CursorStyle
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.Insets
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics
import gg.norisk.owolib.owo.ui.core.ParentUIComponent
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.Surface
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.owolib.owo.ui.core.UIComponent.DismountReason
import gg.norisk.owolib.owo.ui.input.MouseButtonEvent
import gg.norisk.owolib.owo.ui.util.UISounds
import gg.norisk.ui.components.nrc.NrcLabelButton
import gg.norisk.ui.modules.v3.V3Border
import gg.norisk.ui.modules.v3.V3Button
import gg.norisk.ui.modules.v3.V3Surfaces
import gg.norisk.ui.modules.v3.V3Theme
import gg.norisk.ui.v2.toast.LoadingToastHandle
import gg.norisk.ui.v2.toast.components.PlayerHeadComponent
import java.util.ArrayList
import java.util.Arrays
import java.util.LinkedHashMap
import java.util.Locale
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.jvm.functions.Function1
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.Ref.BooleanRef
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nAccountSelectContent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AccountSelectContent.kt\ngg/norisk/client/v2/auth/AccountSelectContent\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 Text.kt\ngg/norisk/compat/text/TextKt\n+ 5 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 6 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,841:1\n1#2:842\n1563#3:843\n1634#3,3:844\n1563#3:847\n1634#3,3:848\n295#3,2:854\n774#3:856\n865#3,2:857\n295#3,2:873\n67#4:851\n67#4:852\n269#4:853\n67#4:859\n67#4:860\n67#4:861\n67#4:862\n67#4:863\n67#4:864\n67#4:865\n67#4:877\n269#4:878\n85#4:879\n204#4:880\n384#5,7:866\n369#6:875\n40#6:876\n*S KotlinDebug\n*F\n+ 1 AccountSelectContent.kt\ngg/norisk/client/v2/auth/AccountSelectContent\n*L\n101#1:843\n101#1:844,3\n106#1:847\n106#1:848,3\n248#1:854,2\n305#1:856\n305#1:857,2\n633#1:873,2\n137#1:851\n146#1:852\n146#1:853\n307#1:859\n368#1:860\n369#1:861\n370#1:862\n394#1:863\n473#1:864\n586#1:865\n839#1:877\n839#1:878\n596#1:879\n596#1:880\n612#1:866,7\n783#1:875\n783#1:876\n*E\n"])
public class AccountSelectContent(onClose: () -> Unit) : FlowLayout(Sizing.Companion.fill(100), Sizing.Companion.content(), Algorithm.VERTICAL) {
   private final val onClose: () -> Unit
   private final val logger: Logger
   private final var addState: gg.norisk.client.v2.auth.AccountSelectContent.AddState
   private final var refreshingUuid: String?
   private final var pickingRole: Role?
   private final val deviceCancel: AtomicBoolean
   private final val avatarCache: MutableMap<String, PlayerHeadComponent>
   private final val rowList: FlowLayout
   private final val rowScroll: ScrollContainer<FlowLayout>
   private final val footer: FlowLayout

   init {
      this.onClose = onClose
      this.logger = MCLogger.getLogger("Voidrix-AccountSelect")
      this.addState = AccountSelectContent.AddState.Idle.INSTANCE
      this.deviceCancel = AtomicBoolean(false)
      this.avatarCache = LinkedHashMap<>()
      var var2: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var2.gap(2)
      this.rowList = var2
      val var6: ScrollContainer = UIContainers.verticalScroll(Sizing.Companion.fill(100), Sizing.Companion.fixed(160), this.rowList as UIComponent)
      var6.scrollbar(Scrollbar.Companion.flat(V3Theme.INSTANCE.grayColorAlpha(8, 120)))
      this.rowScroll = var6
      var2 = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var2.verticalAlignment(VerticalAlignment.CENTER)
      var2.margins(Insets.Companion.top(6))
      this.footer = var2
      this.gap(5)
      this.padding(Insets.Companion.of(0, 0, 2, 2))
      this.child(this.rowScroll as UIComponent)
      this.child(this.footer as UIComponent)
      SessionAccountMemory.INSTANCE.rememberCurrent()
      this.rebuild()
   }

   private fun loadAccounts(): List<Credentials> {
      SessionAccountMemory.INSTANCE.rememberCurrent()
      val stored: java.util.List = CollectionsKt.toMutableList(MinecraftAuthStore.INSTANCE.getAllAccounts(MinecraftAuthStore.INSTANCE.load()))
      val `$i$f$map`: java.lang.Iterable = stored
      val `$this$mapTo$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(stored, 10))

      for (`item$iv$iv` in `$i$f$map`) {
         val var10000: java.lang.String = (`item$iv$iv` as Credentials).getId().toLowerCase(Locale.ROOT)
         `$this$mapTo$iv$iv`.add(var10000)
      }

      val storedIds: java.util.Set = CollectionsKt.toSet(`$this$mapTo$iv$iv` as java.util.List)

      for (var16 in SessionAccountMemory.INSTANCE.all().entrySet()) {
         val var18: java.lang.String = var16.getKey() as java.lang.String
         val var20: Credentials = var16.getValue() as Credentials
         val var10001: java.lang.String = var18.toLowerCase(Locale.ROOT)
         if (!storedIds.contains(var10001)) {
            stored.add(0, var20)
         }
      }

      val var15: java.lang.String = this.currentMcUserId()
      val var21: java.lang.Iterable = stored
      val `destination$iv$ivx`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(stored, 10))

      for (var25 in var21) {
         `destination$iv$ivx`.add(
            Credentials.copy$default(
               var25 as Credentials, null, null, null, null, null, null, StringsKt.equals((var25 as Credentials).getId(), var15, true), false, null, 447, null
            )
         )
      }

      return `destination$iv$ivx` as MutableList<Credentials>
   }

   private fun currentMcUserId(): String? {
      var var1: java.lang.String
      try {
         var1 = SessionHelper.INSTANCE.getCurrentSessionInfo().getProfileId().toString()
      } catch (var3: Exception) {
         var1 = null
      }

      return var1
   }

   private fun rebuild() {
      this.rowList.clearChildren()
      this.footer.clearChildren()
      if (this.addState is AccountSelectContent.AddState.Active) {
         this.renderDeviceCodePane()
      } else if (DevAuth.isEnabled && this.pickingRole != null) {
         val var10001: Role = this.pickingRole
         this.renderRolePicker(var10001)
      } else {
         if (DevAuth.isEnabled) {
            this.renderDevSection()
         }

         val all: java.util.List = this.loadAccounts()
         if (all.isEmpty()) {
            val var10000: FlowLayout = this.rowList
            val acc: Array<Any> = arrayOfNulls(0)
            val var10002: MutableComponent = Component.translatable("nrc.auth.select.empty", Arrays.copyOf(acc, acc.length))
            var10000.child(this.centeredLabel(var10002, 36) as UIComponent)
         } else {
            for (var6 in all) {
               this.rowList.child(this.buildRow(var6) as UIComponent)
            }
         }

         this.renderFooter(all)
      }
   }

   private fun renderFooter(all: List<Credentials>) {
      val var10000: java.lang.String
      if (all.size() == 1) {
         var10000 = this.t("nrc.auth.select.count.one")
      } else {
         val var14: Array<Any> = arrayOf(all.size())
         val var29: MutableComponent = Component.translatable("nrc.auth.select.count.many", Arrays.copyOf(var14, var14.length))
         var10000 = (var29 as Component).getString()
      }

      val var16: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(40), Sizing.Companion.content())
      var16.verticalAlignment(VerticalAlignment.CENTER)
      var16.padding(Insets.Companion.left(2))
      val var7: LabelComponent = LabelComponent(TextKt.getLiteral(TextKt.toSmallCaps(var10000)) as Component)
      this.liveColor(var7, { 
         V3Surfaces.INSTANCE.fontColorDisabled()
      })
      var16.child(var7 as UIComponent)
      this.footer.child(var16 as UIComponent)
      val var19: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(60), Sizing.Companion.content())
      var19.horizontalAlignment(HorizontalAlignment.RIGHT)
      var19.verticalAlignment(VerticalAlignment.CENTER)
      var19.gap(4)
      val var20: V3Button = V3Button(
         TextKt.toSmallCaps(this.t("nrc.auth.select.button.refresh")), { var1: NrcLabelButton, var2: Double, var4: Double, var6: Int ->
            UISounds.playButtonSound()
            `this$0`.onRefreshClicked()
            Unit.INSTANCE
         }
      )
      var20.horizontalSizing(Sizing.Companion.content())
      var19.child(var20 as UIComponent)
      val var21: V3Button = V3Button(TextKt.toSmallCaps(this.t("nrc.auth.select.button.add")), { var1: NrcLabelButton, var2: Double, var4: Double, var6: Int ->
         UISounds.playButtonSound()
         AccountSwitcherButton.INSTANCE.startDirectAddFlow({ it: Boolean ->
            rebuildOnMain$default(`this$0`, null, 1, null)
            Unit.INSTANCE
         })
         Unit.INSTANCE
      })
      var21.horizontalSizing(Sizing.Companion.fixed(110))
      var19.child(var21 as UIComponent)
      this.footer.child(var19 as UIComponent)
   }

   private fun buildRow(acc: Credentials): FlowLayout {
      val isRefreshingThis: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.fixed(34))
      isRefreshingThis.verticalAlignment(VerticalAlignment.CENTER)
      isRefreshingThis.padding(Insets.Companion.of(3, 3, 6, 6))
      isRefreshingThis.cursorStyle(CursorStyle.HAND)
      val var14: Boolean = this.refreshingUuid == acc.getId()
      val var15: Boolean = MinecraftAuthStore.INSTANCE.isExpired(acc)
      val var16: BooleanRef = BooleanRef()
      isRefreshingThis.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      isRefreshingThis.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      isRefreshingThis.surface(this.v3RowSurface(acc.getActive(), { 
         `$hovered`.element
      }))
      isRefreshingThis.mouseDown().subscribe({ var3: MouseButtonEvent, var4: Boolean ->
         UISounds.playButtonSound()
         if (!`$isRefreshingThis` && `this$0`.addState is AccountSelectContent.AddState.Idle) {
            if (`$acc`.getActive()) {
               `this$0`.verifyActive(`$acc`)
            } else {
               `this$0`.switchTo(`$acc`)
            }
         }

         true
      })
      var var6: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fixed(22), Sizing.Companion.fixed(22))
      var6.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var6.margins(Insets.Companion.right(6))
      var6.child(buildAvatar$default(this, acc, 20, null, 4, null) as UIComponent)
      isRefreshingThis.child(var6 as UIComponent)
      var6 = UIContainers.verticalFlow(Sizing.Companion.expand(100), Sizing.Companion.fill(95))
      var6.allowOverflow(true)
      var6.gap(0)
      var var9: LabelComponent = LabelComponent(TextKt.getLiteral(TextKt.toSmallCaps(acc.getUsername())) as Component)
      this.liveColor(var9, { 
         if (`$acc`.getActive()) V3Theme.INSTANCE.accentColor(12) else V3Theme.INSTANCE.grayColor(11)
      })
      var6.child(var9 as UIComponent)
      var9 = LabelComponent(this.rowSubText(acc, var14, var15) as Component)
      var9.scale(0.6F)
      var9.lineSpacing(0)
      this.liveColor(
         var9,
         { 
            if (`$isRefreshingThis`)
               V3Theme.INSTANCE.accentColor(11)
               else
               (if (`$expired`) Color.Companion.ofArgb(V3Theme.INSTANCE.getError()) else V3Surfaces.INSTANCE.fontColorSecondary())
            }
      )
      var6.child(var9 as UIComponent)
      isRefreshingThis.child(var6 as UIComponent)
      val var10000: FlowLayout = this.rowBadge(acc, var14, var15)
      if (var10000 != null) {
         isRefreshingThis.child(var10000 as UIComponent)
      }

      if (!acc.getActive()) {
         isRefreshingThis.child(this.buildRemoveButton(acc) as UIComponent)
      }

      return isRefreshingThis
   }

   private fun renderDevSection() {
      val all: java.util.List = this.loadAccounts()
      val cards: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      cards.gap(4)
      val var11: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var11.padding(Insets.Companion.of(2, 1, 2, 2))
      val var6: LabelComponent = LabelComponent(TextKt.getLiteral(TextKt.toSmallCaps("Dev Accounts")) as Component)
      this.liveColor(var6, { 
         V3Theme.INSTANCE.accentColor(11)
      })
      var6.scale(0.7F)
      var11.child(var6 as UIComponent)
      cards.child(var11 as UIComponent)
      val var14: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var14.gap(6)
      var14.verticalAlignment(VerticalAlignment.CENTER)
      var14.child(this.buildDevCard(Role.MAIN, all) as UIComponent)
      var14.child(this.buildDevCard(Role.ALT, all) as UIComponent)
      cards.child(var14 as UIComponent)
      this.rowList.child(cards as UIComponent)
   }

   private fun buildDevCard(role: Role, all: List<Credentials>): FlowLayout {
      val assignedId: java.lang.String = DevPinStore.INSTANCE.uuidFor(role)
      var var34: Credentials
      if (assignedId != null) {
         val hovered: java.lang.String = assignedId
         val var10: java.util.Iterator = all.iterator()

         while (true) {
            if (!var10.hasNext()) {
               var34 = null
               break
            }

            val `$this$buildDevCard_u24lambda_u2443_u24lambda_u2442`: Any = var10.next()
            if (StringsKt.equals((`$this$buildDevCard_u24lambda_u2443_u24lambda_u2442` as Credentials).getId(), hovered, true)) {
               var34 = (Credentials)`$this$buildDevCard_u24lambda_u2443_u24lambda_u2442`
               break
            }
         }

         var34 = var34
      } else {
         var34 = null
      }

      val var15: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.expand(50), Sizing.Companion.fixed(42))
      var15.verticalAlignment(VerticalAlignment.CENTER)
      var15.padding(Insets.Companion.of(4, 4, 8, 8))
      var15.cursorStyle(CursorStyle.HAND)
      val var16: BooleanRef = BooleanRef()
      var15.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      var15.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      var15.surface(this.v3RowSurface(var34 != null, { 
         `$hovered`.element
      }))
      var15.mouseDown().subscribe({ var2: MouseButtonEvent, var3: Boolean ->
         UISounds.playButtonSound()
         `this$0`.pickingRole = `$role`
         `this$0`.rebuild()
         true
      })
      var var18: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fixed(24), Sizing.Companion.fixed(24))
      var18.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var18.margins(Insets.Companion.right(7))
      var var10002: Credentials = var34
      if (var34 == null) {
         var10002 = Credentials(role.name(), role.name(), "", "", "", null, false, false, null, 480, null)
      }

      var var10004: java.lang.String
      run label59@{
         if (var34 != null) {
            var10004 = var34.getId()
            if (var10004 != null) {
               return@label59
            }
         }

         var10004 = role.name()
      }

      var var10001: LabelComponent
      var var10003: java.lang.String
      run label62@{
         var18.child(this.buildAvatar(var10002, 22, "devcard-$var10004") as UIComponent)
         var15.child(var18 as UIComponent)
         var18 = UIContainers.verticalFlow(Sizing.Companion.expand(100), Sizing.Companion.fill(100))
         var18.verticalAlignment(VerticalAlignment.CENTER)
         var18.allowOverflow(true)
         var18.gap(1)
         val var25: LabelComponent = LabelComponent(TextKt.getLiteral(TextKt.toSmallCaps(this.devRoleLabel(role))) as Component)
         this.liveColor(var25, { 
            if (`$assigned` != null) V3Theme.INSTANCE.accentColor(12) else V3Theme.INSTANCE.grayColor(11)
         })
         var18.child(var25 as UIComponent)
         var10001 = LabelComponent
         if (var34 != null) {
            var10003 = var34.getUsername()
            if (var10003 != null) {
               return@label62
            }
         }

         var10003 = "click to set"
      }

      var10001./* $VF: Unable to resugar constructor */<init>(TextKt.getLiteral(var10003) as Component)
      var10001.scale(0.6F)
      this.liveColor(var10001, { 
         V3Surfaces.INSTANCE.fontColorSecondary()
      })
      var18.child(var10001 as UIComponent)
      var15.child(var18 as UIComponent)
      return var15
   }

   private fun renderRolePicker(role: Role) {
      val pickable: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      pickable.gap(4)
      pickable.padding(Insets.Companion.of(0, 4, 2, 2))
      val pane: FlowLayout = pickable
      val var15: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var15.verticalAlignment(VerticalAlignment.CENTER)
      var15.gap(6)
      val var6: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      var6.cursorStyle(CursorStyle.HAND)
      val var10: LabelComponent = LabelComponent(TextKt.getLiteral("←") as Component)
      this.liveColor(var10, { 
         V3Theme.INSTANCE.grayColor(11)
      })
      var6.child(var10 as UIComponent)
      var6.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
         UISounds.playButtonSound()
         `this$0`.pickingRole = null
         `this$0`.rebuild()
         true
      })
      var15.child(var6 as UIComponent)
      val var27: LabelComponent = LabelComponent(TextKt.getLiteral(TextKt.toSmallCaps("Pick ${this.devRoleLabel(role)}")) as Component)
      this.liveColor(var27, { 
         V3Theme.INSTANCE.grayColor(12)
      })
      var27.scale(0.85F)
      var15.child(var27 as UIComponent)
      pickable.child(var15 as UIComponent)
      val var28: java.lang.Iterable = this.loadAccounts()
      val var32: java.util.Collection = ArrayList()

      for (var40 in var28) {
         if ((var40 as Credentials).getRefreshToken().length() > 0) {
            var32.add(var40)
         }
      }

      val var16: java.util.List = var32 as java.util.List
      if ((var32 as java.util.List).isEmpty()) {
         val var24: Array<Any> = arrayOfNulls(0)
         val var10002: MutableComponent = Component.translatable("nrc.auth.select.empty", Arrays.copyOf(var24, var24.length))
         pickable.child(this.centeredLabel(var10002, 36) as UIComponent)
      } else {
         for (var25 in var16) {
            pane.child(this.buildPickerRow(var25, role) as UIComponent)
         }
      }

      if (DevPinStore.INSTANCE.uuidFor(role) != null) {
         val var21: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
         var21.padding(Insets.Companion.of(4, 2, 2, 2))
         var21.cursorStyle(CursorStyle.HAND)
         val var33: LabelComponent = LabelComponent(TextKt.getLiteral(TextKt.toSmallCaps("✕ clear ${this.devRoleLabel(role)}")) as Component)
         var33.scale(0.6F)
         this.liveColor(var33, { 
            Color.Companion.ofArgb(V3Theme.INSTANCE.getError())
         })
         var21.child(var33 as UIComponent)
         var21.mouseDown().subscribe({ var2: MouseButtonEvent, var3: Boolean ->
            UISounds.playButtonSound()
            DevPinStore.INSTANCE.clear(`$role`)
            `this$0`.pickingRole = null
            `this$0`.rebuild()
            true
         })
         pane.child(var21 as UIComponent)
      }

      this.rowList.child(pane as UIComponent)
   }

   private fun buildPickerRow(acc: Credentials, role: Role): FlowLayout {
      val var10000: java.lang.String = DevPinStore.INSTANCE.uuidFor(role)
      val selected: Boolean = var10000 != null && StringsKt.equals(var10000, acc.getId(), true)
      val hovered: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.fixed(30))
      hovered.verticalAlignment(VerticalAlignment.CENTER)
      hovered.padding(Insets.Companion.of(2, 2, 6, 6))
      hovered.cursorStyle(CursorStyle.HAND)
      val var14: BooleanRef = BooleanRef()
      hovered.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      hovered.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      hovered.surface(this.v3RowSurface(selected, { 
         `$hovered`.element
      }))
      var var15: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fixed(20), Sizing.Companion.fixed(20))
      var15.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var15.margins(Insets.Companion.right(6))
      var15.child(buildAvatar$default(this, acc, 18, null, 4, null) as UIComponent)
      hovered.child(var15 as UIComponent)
      var15 = UIContainers.verticalFlow(Sizing.Companion.expand(100), Sizing.Companion.fill(95))
      var15.allowOverflow(true)
      val var9: LabelComponent = LabelComponent(TextKt.getLiteral(TextKt.toSmallCaps(acc.getUsername())) as Component)
      this.liveColor(var9, { 
         if (`$selected`) V3Theme.INSTANCE.accentColor(12) else V3Theme.INSTANCE.grayColor(11)
      })
      var15.child(var9 as UIComponent)
      hovered.child(var15 as UIComponent)
      if (selected) {
         val var17: LabelComponent = LabelComponent(TextKt.getLiteral("✓") as Component)
         this.liveColor(var17, { 
            V3Theme.INSTANCE.accentColor(12)
         })
         hovered.child(var17 as UIComponent)
      }

      hovered.mouseDown().subscribe({ var3: MouseButtonEvent, var4: Boolean ->
         UISounds.playButtonSound()
         DevPinStore.INSTANCE.assign(`$acc`.getId(), `$role`)
         `this$0`.pickingRole = null
         `this$0`.rebuild()
         true
      })
      return hovered
   }

   private fun devRoleLabel(role: Role): String {
      return if (role === Role.MAIN) "Main Account" else "Alt Account"
   }

   private fun rowSubText(acc: Credentials, isRefreshingThis: Boolean, expired: Boolean): MutableComponent {
      val var10000: MutableComponent
      if (isRefreshingThis) {
         val `args$iv`: Array<Any> = arrayOfNulls(0)
         var10000 = Component.translatable("nrc.auth.select.state.refreshing", Arrays.copyOf(`args$iv`, `args$iv`.length))
      } else if (expired) {
         val var9: Array<Any> = arrayOfNulls(0)
         var10000 = Component.translatable("nrc.auth.select.sub.expired", Arrays.copyOf(var9, var9.length))
      } else {
         val var10: Array<Any> = arrayOfNulls(0)
         var10000 = Component.translatable("nrc.auth.select.sub.signedIn", Arrays.copyOf(var10, var10.length))
      }

      return var10000
   }

   private fun rowBadge(acc: Credentials, isRefreshingThis: Boolean, expired: Boolean): FlowLayout? {
      return if (isRefreshingThis)
         this.badgeOf("↻", V3Theme.INSTANCE.accentAlpha(11, 220), V3Theme.INSTANCE.accentAlpha(4, 80))
         else
         (
            if (acc.getActive() && expired)
               this.badgeOf(this.t("nrc.auth.select.badge.expired"), V3Theme.INSTANCE.gray(12), V3Theme.INSTANCE.getError() and 16777215 or 1610612736)
               else
               (
                  if (acc.getActive())
                     this.badgeOf(this.t("nrc.auth.select.badge.active"), V3Theme.INSTANCE.accentAlpha(11, 220), V3Theme.INSTANCE.accentAlpha(4, 80))
                     else
                     null
               )
         )
      }

   private fun renderDeviceCodePane() {
      val `key$iv`: AccountSelectContent.AddState = this.addState
      val var10000: AccountSelectContent.AddState.Active = this.addState as? AccountSelectContent.AddState.Active
      if ((this.addState as? AccountSelectContent.AddState.Active) != null) {
         val var16: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
         var16.gap(6)
         var16.padding(Insets.Companion.of(0, 4, 2, 2))
         val var17: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
         var17.horizontalAlignment(HorizontalAlignment.LEFT)
         var17.verticalAlignment(VerticalAlignment.CENTER)
         var17.child(this.buildBackButton() as UIComponent)
         var16.child(var17 as UIComponent)
         if (var10000.phase != null) {
            var16.child(this.buildPhaseBox(var10000.phase) as UIComponent)
         } else if (var10000.challenge == null) {
            val var20: Array<Any> = arrayOfNulls(0)
            val var10002: MutableComponent = Component.translatable("nrc.auth.select.devicecode.requesting", Arrays.copyOf(var20, var20.length))
            var16.child(this.centeredLabel(var10002, 80) as UIComponent)
         } else {
            var16.child(this.buildChallengeBody(var10000.challenge, var10000.codeCopied) as UIComponent)
         }

         val var24: java.lang.String = var10000.error
         if (var24 != null) {
            val var7: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
            var7.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
            var7.margins(Insets.Companion.top(4))
            val var11: LabelComponent = LabelComponent(TextKt.getLiteral(var24) as Component)
            var11.color(Color.Companion.ofArgb(V3Theme.INSTANCE.getError()))
            var11.scale(0.65F)
            var7.child(var11 as UIComponent)
            var16.child(var7 as UIComponent)
         }

         this.rowList.child(var16 as UIComponent)
      }
   }

   private fun buildPhaseBox(phase: String): FlowLayout {
      val var2: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.fixed(100))
      var2.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var2.gap(6)
      var var5: LabelComponent = LabelComponent(TextKt.getLiteral(TextKt.toSmallCaps(this.t("nrc.auth.select.devicecode.title"))) as Component)
      this.liveColor(var5, { 
         V3Theme.INSTANCE.grayColor(12)
      })
      var2.child(var5 as UIComponent)
      var5 = LabelComponent(TextKt.getLiteral("↻") as Component)
      this.liveColor(var5, { 
         V3Theme.INSTANCE.accentColor(11)
      })
      var5.scale(1.4F)
      var2.child(var5 as UIComponent)
      var5 = LabelComponent(TextKt.getLiteral(phase) as Component)
      this.liveColor(var5, { 
         V3Surfaces.INSTANCE.fontColorSecondary()
      })
      var5.scale(0.8F)
      var2.child(var5 as UIComponent)
      return var2
   }

   private fun buildChallengeBody(challenge: Challenge, codeCopied: Boolean): FlowLayout {
      val var3: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      val `$this$buildChallengeBody_u24lambda_u2496`: FlowLayout = var3
      var3.alignment(HorizontalAlignment.CENTER, VerticalAlignment.TOP)
      var3.gap(6)
      val var6: LabelComponent = LabelComponent(TextKt.getLiteral(TextKt.toSmallCaps(this.t("nrc.auth.select.devicecode.title"))) as Component)
      this.liveColor(var6, { 
         V3Theme.INSTANCE.grayColor(12)
      })
      var6.scale(1.05F)
      var3.child(var6 as UIComponent)

      for (var21 in StringsKt.split$default(this.t("nrc.auth.select.devicecode.blurb"), arrayOf("\n"), false, 0, 6, null)) {
         val var29: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
         var29.horizontalAlignment(HorizontalAlignment.CENTER)
         val var12: LabelComponent = LabelComponent(TextKt.getLiteral(var21) as Component)
         this.liveColor(var12, { 
            V3Surfaces.INSTANCE.fontColorSecondary()
         })
         var12.scale(0.75F)
         var29.child(var12 as UIComponent)
         `$this$buildChallengeBody_u24lambda_u2496`.child(var29 as UIComponent)
      }

      val var17: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var17.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var17.child(this.buildReopenLink(challenge) as UIComponent)
      `$this$buildChallengeBody_u24lambda_u2496`.child(var17 as UIComponent)
      val var18: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.fixed(24))
      var18.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var18.gap(6)
      var18.margins(Insets.Companion.top(4))
      val var33: FlowLayout = this.boxedValue(challenge.getUserCode(), true, 1.2F)
      var33.verticalSizing(Sizing.Companion.fill(100))
      var18.child(var33 as UIComponent)
      val var34: V3Button = V3Button(
         TextKt.toSmallCaps(this.t(if (codeCopied) "nrc.auth.select.devicecode.copied" else "nrc.auth.select.devicecode.copy")),
         { var2: NrcLabelButton, var3: Double, var5: Double, var7: Int ->
            UISounds.playButtonSound()
            ClipboardHelper.INSTANCE.setString(`$challenge`.getUserCode())
            `this$0`.updateAddState({ it: AccountSelectContent.AddState.Active ->
               AccountSelectContent.AddState.Active.copy$default(it, null, null, true, null, 11, null)
            })
            Unit.INSTANCE
         }
      )
      var34.horizontalSizing(Sizing.Companion.content())
      var34.verticalSizing(Sizing.Companion.fill(100))
      var18.child(var34 as UIComponent)
      `$this$buildChallengeBody_u24lambda_u2496`.child(var18 as UIComponent)
      val var19: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var19.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var19.gap(4)
      var19.margins(Insets.Companion.top(4))
      val var35: LabelComponent = LabelComponent(TextKt.getLiteral("●") as Component)
      this.liveColor(var35, { 
         V3Theme.INSTANCE.accentColor(9)
      })
      var35.scale(0.7F)
      var19.child(var35 as UIComponent)
      val var41: Array<Any> = arrayOfNulls(0)
      val var10003: MutableComponent = Component.translatable("nrc.auth.select.devicecode.waiting", Arrays.copyOf(var41, var41.length))
      val var37: LabelComponent = LabelComponent(var10003 as Component)
      this.liveColor(var37, { 
         V3Surfaces.INSTANCE.fontColorSecondary()
      })
      var37.scale(0.7F)
      var19.child(var37 as UIComponent)
      `$this$buildChallengeBody_u24lambda_u2496`.child(var19 as UIComponent)
      return var3
   }

   private fun v3RowSurface(active: Boolean, hovered: () -> Boolean): Surface {
      return { ctx: OwoUIGraphics, c: ParentUIComponent ->
         val h: Boolean = `$hovered`() as java.lang.Boolean
         val border: Int = if (`$active` && h)
            V3Theme.INSTANCE.accentAlpha(7, 128)
            else
            (if (`$active`) V3Theme.INSTANCE.accentAlpha(6, 128) else (if (h) V3Theme.INSTANCE.grayAlpha(7, 128) else V3Theme.INSTANCE.grayAlpha(6, 128)))
            ctx.fill(
            c.x(),
            c.y(),
            c.x() + c.width(),
            c.y() + c.height(),
            if (`$active` && h)
               V3Theme.INSTANCE.accentAlpha(7, 90)
               else
               (if (`$active`) V3Theme.INSTANCE.accentAlpha(6, 60) else (if (h) V3Theme.INSTANCE.grayBg(3) else V3Theme.INSTANCE.grayBg(2)))
         )
         V3Border.INSTANCE.draw(ctx, c.x(), c.y(), c.width(), c.height(), border)
      }
   }

   private fun centeredLabel(text: MutableComponent, height: Int): FlowLayout {
      val var3: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.fixed(height))
      var3.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      val var6: LabelComponent = LabelComponent(text as Component)
      this.liveColor(var6, { 
         V3Surfaces.INSTANCE.fontColorDisabled()
      })
      var6.scale(0.85F)
      var3.child(var6 as UIComponent)
      return var3
   }

   private fun boxedValue(text: String, accent: Boolean, scale: Float): FlowLayout {
      val var4: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      var4.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var4.padding(Insets.Companion.of(5, 5, 14, 14))
      var4.surface({ ctx: OwoUIGraphics, c: ParentUIComponent ->
         val bg: Int = if (`$accent`) V3Theme.INSTANCE.accentAlpha(3, 140) else V3Theme.INSTANCE.grayBg(3)
         val border: Int = if (`$accent`) V3Theme.INSTANCE.accentAlpha(7, 180) else V3Theme.INSTANCE.grayAlpha(6, 160)
         ctx.fill(c.x(), c.y(), c.x() + c.width(), c.y() + c.height(), bg)
         V3Border.INSTANCE.draw(ctx, c.x(), c.y(), c.width(), c.height(), border)
      })
      val var7: LabelComponent = LabelComponent(TextKt.getLiteral(text) as Component)
      this.liveColor(var7, { 
         if (`$accent`) V3Theme.INSTANCE.accentColor(12) else V3Theme.INSTANCE.grayColor(12)
      })
      var7.scale(scale)
      var4.child(var7 as UIComponent)
      return var4
   }

   private fun badgeOf(text: String, fg: Int, bg: Int): FlowLayout {
      val var4: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      var4.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var4.padding(Insets.Companion.of(1, 1, 4, 4))
      var4.margins(Insets.Companion.left(4))
      var4.surface({ ctx: OwoUIGraphics, c: ParentUIComponent ->
         ctx.fill(c.x(), c.y(), c.x() + c.width(), c.y() + c.height(), `$bg`)
         V3Border.INSTANCE.draw(ctx, c.x(), c.y(), c.width(), c.height(), `$bg` or -16777216)
      })
      val var7: LabelComponent = LabelComponent(TextKt.getLiteral(text) as Component)
      var7.color(Color.Companion.ofArgb(fg))
      var7.scale(0.7F)
      var4.child(var7 as UIComponent)
      return var4
   }

   private fun buildBackButton(): FlowLayout {
      val var1: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fixed(18), Sizing.Companion.fixed(16))
      var1.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var1.cursorStyle(CursorStyle.HAND)
      val hovered: BooleanRef = BooleanRef()
      var1.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      var1.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      var1.surface({ ctx: OwoUIGraphics, c: ParentUIComponent ->
         if (`$hovered`.element) {
            ctx.fill(c.x(), c.y(), c.x() + c.width(), c.y() + c.height(), V3Theme.INSTANCE.grayAlpha(4, 140))
         }
      })
      val var5: LabelComponent = LabelComponent(TextKt.getLiteral("←") as Component)
      this.liveColor(var5, { 
         if (`$hovered`.element) V3Theme.INSTANCE.grayColor(12) else V3Theme.INSTANCE.grayColor(11)
      })
      var5.scale(0.9F)
      var1.child(var5 as UIComponent)
      var1.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
         UISounds.playButtonSound()
         `this$0`.cancelAdd()
         true
      })
      return var1
   }

   private fun buildRemoveButton(acc: Credentials): FlowLayout {
      val var2: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fixed(16), Sizing.Companion.fixed(16))
      var2.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var2.margins(Insets.Companion.left(6))
      var2.cursorStyle(CursorStyle.HAND)
      val hovered: BooleanRef = BooleanRef()
      var2.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      var2.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      var2.surface(
         { ctx: OwoUIGraphics, c: ParentUIComponent ->
            ctx.fill(
               c.x(),
               c.y(),
               c.x() + c.width(),
               c.y() + c.height(),
               if (`$hovered`.element) V3Theme.INSTANCE.getError() and 16777215 or Integer.MIN_VALUE else V3Theme.INSTANCE.grayAlpha(4, 120)
            )
            V3Border.INSTANCE.draw(ctx, c.x(), c.y(), c.width(), c.height(), V3Theme.INSTANCE.grayAlpha(6, 160))
         }
      )
      val var6: LabelComponent = LabelComponent(TextKt.getLiteral("✕") as Component)
      this.liveColor(var6, { 
         if (`$hovered`.element) V3Theme.INSTANCE.grayColor(12) else V3Theme.INSTANCE.grayColor(11)
      })
      var6.scale(0.8F)
      var2.child(var6 as UIComponent)
      var2.mouseDown().subscribe({ var2: MouseButtonEvent, var3: Boolean ->
         UISounds.playButtonSound()
         `this$0`.removeAccount(`$acc`)
         true
      })
      return var2
   }

   private fun buildReopenLink(challenge: Challenge): LabelComponent {
      val `args$iv`: Array<Any> = arrayOfNulls(0)
      val var10002: MutableComponent = Component.translatable("nrc.auth.select.devicecode.reopenLink", Arrays.copyOf(`args$iv`, `args$iv`.length))
      val label: LabelComponent = LabelComponent(var10002 as Component)
      label.scale(0.7F)
      label.cursorStyle(CursorStyle.HAND)
      label.margins(Insets.Companion.bottom(2))
      val var6: BooleanRef = BooleanRef()
      label.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      label.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      label.setAutoTextSupplier(
         { 
            var var10000: Style = TextKt.getEmptyStyle()
               .withColor(TextColor.fromRgb(if (`$hovered`.element) V3Theme.INSTANCE.accent(12) else V3Theme.INSTANCE.accent(11)))
               if (`$hovered`.element) {
               var10000 = var10000.withUnderlined(true)
            } else {
               var10000 = var10000
            }

            TextKt.getLiteral(`this$0`.t("nrc.auth.select.devicecode.reopenLink")).withStyle(var10000) as Component
         }
      )
      label.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
         UISounds.playButtonSound()
         LocalRedirectServer.INSTANCE.openBrowser(`$challenge`.getVerificationUri())
         true
      })
      return label
   }

   private fun buildAvatar(acc: Credentials, size: Int, cacheKey: String = acc.getId()): FlowLayout {
      val parsed: UUID = this.parseUuid(acc.getId())
      if (parsed != null) {
         val var12: java.util.Map = this.avatarCache
         val var14: Any = this.avatarCache.get(cacheKey)
         val var10000: Any
         if (var14 == null) {
            val var16: Any = PlayerHeadComponent(GameProfile(parsed, acc.getUsername()), size, null, null, 12, null)
            var12.put(cacheKey, var16)
            var10000 = var16
         } else {
            var10000 = var14
         }

         return var10000 as FlowLayout
      } else {
         var `$this$getOrPut$iv`: FlowLayout
         var var18: java.lang.String
         var var10001: LabelComponent
         run label28@{
            `$this$getOrPut$iv` = UIContainers.verticalFlow(Sizing.Companion.fixed(size), Sizing.Companion.fixed(size))
            `$this$getOrPut$iv`.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
            `$this$getOrPut$iv`.surface({ ctx: OwoUIGraphics, c: ParentUIComponent ->
               ctx.fill(c.x(), c.y(), c.x() + c.width(), c.y() + c.height(), V3Theme.INSTANCE.gray(4))
               V3Border.INSTANCE.draw(ctx, c.x(), c.y(), c.width(), c.height(), V3Theme.INSTANCE.gray(6))
            })
            var10001 = LabelComponent
            val var10003: Character = StringsKt.firstOrNull(acc.getUsername())
            if (var10003 != null) {
               val var17: java.lang.String = java.lang.String.valueOf(var10003.charValue())
               var18 = var17.toUpperCase(Locale.ROOT)
               if (var18 != null) {
                  return@label28
               }
            }

            var18 = "?"
         }

         var10001./* $VF: Unable to resugar constructor */<init>(TextKt.getLiteral(var18) as Component)
         var10001.color(V3Theme.INSTANCE.grayColor(11))
         `$this$getOrPut$iv`.child(var10001 as UIComponent)
         return `$this$getOrPut$iv`
      }
   }

   private fun onRefreshClicked() {
      MinecraftAuthStore.INSTANCE.invalidate()
      val currentId: java.lang.String = this.currentMcUserId()
      var var11: Credentials
      if (currentId != null) {
         val id: java.lang.String = currentId
         val var7: java.util.Iterator = this.loadAccounts().iterator()

         while (true) {
            if (!var7.hasNext()) {
               var11 = null
               break
            }

            val `element$iv`: Any = var7.next()
            if (StringsKt.equals((`element$iv` as Credentials).getId(), id, true)) {
               var11 = (Credentials)`element$iv`
               break
            }
         }

         var11 = var11
      } else {
         var11 = null
      }

      if (var11 == null) {
         this.rebuild()
      } else if (var11.getRefreshToken().length() > 0) {
         this.refreshActive(var11)
      } else {
         this.rebuild()
      }
   }

   private fun startAddFlow() {
      if (this.addState is AccountSelectContent.AddState.Idle) {
         this.addState = AccountSelectContent.AddState.Active(null, null, false, null, 15, null)
         this.deviceCancel.set(false)
         this.rebuild()
         CompletableFuture.runAsync({ 
            try {
               val e: Credentials = LoginOrchestrator.INSTANCE.loginDeviceCode({ ch: Challenge ->
                  ClipboardHelper.INSTANCE.setString(ch.getUserCode())
                  `this$0`.updateAddState({ it: AccountSelectContent.AddState.Active ->
                     AccountSelectContent.AddState.Active.copy$default(it, `$ch`, null, true, null, 10, null)
                  })
                  `this$0`.rebuildOnMain({ 
                     LocalRedirectServer.INSTANCE.openBrowser(`$ch`.getVerificationUri())
                     Unit.INSTANCE
                  })
                  Unit.INSTANCE
               }, { step: LoginStep ->
                  val var10001: java.lang.String = step.name().toLowerCase(Locale.ROOT)
                  `this$0`.updateAddState({ it: AccountSelectContent.AddState.Active ->
                     AccountSelectContent.AddState.Active.copy$default(it, null, `$phaseText`, false, null, 13, null)
                  })
                  rebuildOnMain$default(`this$0`, null, 1, null)
                  Unit.INSTANCE
               }, `this$0`.deviceCancel)
               `this$0`.applySession(e)
               `this$0`.logger.info("Add succeeded: ${e.getUsername()}")
               `this$0`.addState = AccountSelectContent.AddState.Idle.INSTANCE
               rebuildOnMain$default(`this$0`, null, 1, null)
            } catch (var2: Exception) {
               `this$0`.logger.error("Device code flow failed: ${var2.getMessage()}")
               `this$0`.updateAddState({ it: AccountSelectContent.AddState.Active ->
                  var var10000: java.lang.String = `$e`.getMessage()
                  if (var10000 == null) {
                     var10000 = "Unknown error"
                  }

                  AccountSelectContent.AddState.Active.copy$default(it, null, null, false, var10000, 5, null)
               })
               rebuildOnMain$default(`this$0`, null, 1, null)
            }
         })
      }
   }

   private fun cancelAdd() {
      this.deviceCancel.set(true)
      this.addState = AccountSelectContent.AddState.Idle.INSTANCE
      this.rebuild()
   }

   public open fun dismount(reason: DismountReason) {
      super.dismount(reason)
      if (reason === DismountReason.REMOVED) {
         this.deviceCancel.set(true)
         this.addState = AccountSelectContent.AddState.Idle.INSTANCE
         this.pickingRole = null
      }
   }

   private fun switchTo(acc: Credentials) {
      this.runWithToast(
         this.t("nrc.auth.select.toast.switching"), acc.getUsername(), acc.getId(), this.t("nrc.auth.select.toast.switched"), { it: Credentials ->
            it.getUsername()
         }, this.t("nrc.auth.select.toast.switchFailed"), { 
            val refreshed: Credentials = TokenRefresh.INSTANCE.updateMicrosoftTokenIfNeeded(`$acc`)
            MinecraftAuthStore.INSTANCE.setActiveAccount(refreshed.getId())
            `this$0`.applySession(refreshed)
            refreshed
         }
      )
   }

   private fun refreshActive(acc: Credentials) {
      this.runWithToast(
         this.t("nrc.auth.select.toast.refreshing"), acc.getUsername(), acc.getId(), this.t("nrc.auth.select.toast.refreshed"), { it: Credentials ->
            it.getUsername()
         }, this.t("nrc.auth.select.toast.refreshFailed"), { 
            val refreshed: Credentials = TokenRefresh.INSTANCE.refreshToken(`$acc`)
            MinecraftAuthStore.INSTANCE.setActiveAccount(refreshed.getId())
            `this$0`.applySession(refreshed)
            refreshed
         }
      )
   }

   private fun verifyActive(acc: Credentials) {
      if (this.refreshingUuid == null) {
         this.refreshingUuid = acc.getId()
         this.rebuild()
         CompletableFuture.runAsync({ 
            var e: SessionStatus
            try {
               e = SessionSwapper.INSTANCE.getSessionStatus()
            } catch (var7: Exception) {
               e = SessionStatus.OFFLINE
            }

            if (e === SessionStatus.VALID) {
               `this$0`.rebuildOnMain({ 
                  LoadingToastHandle.success$default(`$toast`, `this$0`.t("nrc.auth.select.toast.valid"), null, 2, null)
                  `this$0`.refreshingUuid = null
                  Unit.INSTANCE
               })
            } else if (`$acc`.getRefreshToken().length() == 0) {
               `this$0`.rebuildOnMain({ 
                  `$toast`.error(`this$0`.t("nrc.auth.select.toast.expired"), `this$0`.t("nrc.auth.select.toast.expiredDesc"))
                  `this$0`.refreshingUuid = null
                  Unit.INSTANCE
               })
            } else {
               try {
                  `this$0`.applySession(TokenRefresh.INSTANCE.refreshToken(`$acc`))
                  `this$0`.rebuildOnMain({ 
                     LoadingToastHandle.success$default(`$toast`, `this$0`.t("nrc.auth.select.toast.restored"), null, 2, null)
                     `this$0`.refreshingUuid = null
                     Unit.INSTANCE
                  })
               } catch (var6: Exception) {
                  `this$0`.rebuildOnMain({ 
                     `$toast`.error(`this$0`.t("nrc.auth.select.toast.refreshFailed"), `$e`.getMessage())
                     `this$0`.refreshingUuid = null
                     Unit.INSTANCE
                  })
               }
            }
         })
      }
   }

   private fun applySession(creds: Credentials) {
      SessionSwapper.INSTANCE.swap(SessionData(creds.getAccessToken(), creds.getId(), creds.getUsername()), creds.getId())
      AuthHandback.INSTANCE.write(creds)
   }

   private fun removeAccount(acc: Credentials) {
      this.avatarCache.remove(acc.getId())
      if (DevAuth.isEnabled) {
         DevPinStore.INSTANCE.clearUuid(acc.getId())
      }

      MinecraftAuthStore.INSTANCE.removeAccount(acc.getId())
      this.rebuild()
   }

   private fun rebuildOnMain(also: () -> Unit = { 
         Unit.INSTANCE
      }) {
      val `action$iv`: Runnable = { 
         `$also`()
         `this$0`.rebuild()
      }
      val var10000: Minecraft = Minecraft.getInstance()
      var10000.execute(`action$iv`)
   }

   private fun runWithToast(
      loadingTitle: String,
      loadingDesc: String?,
      uuid: String,
      successTitle: String,
      successDesc: (Credentials) -> String? = <unrepresentable>.INSTANCE as Function1,
      failureTitle: String,
      block: () -> Credentials
   ) {
      if (this.refreshingUuid == null) {
         this.refreshingUuid = uuid
         this.rebuild()
         CompletableFuture.runAsync({ 
            try {
               `this$0`.rebuildOnMain({ 
                  `$toast`.success(`$successTitle`, `$successDesc`(`$result`) as java.lang.String)
                  `this$0`.refreshingUuid = null
                  Unit.INSTANCE
               })
            } catch (var7: Exception) {
               `this$0`.logger.error("$`$failureTitle`: ${var7.getMessage()}")
               `this$0`.rebuildOnMain({ 
                  `$toast`.error(`$failureTitle`, `$e`.getMessage())
                  `this$0`.refreshingUuid = null
                  Unit.INSTANCE
               })
            }
         })
      }
   }

   private fun updateAddState(
      transform: (gg.norisk.client.v2.auth.AccountSelectContent.AddState.Active) -> gg.norisk.client.v2.auth.AccountSelectContent.AddState.Active
   ) {
      if (this.addState is AccountSelectContent.AddState.Active) {
         this.addState = transform(this.addState) as AccountSelectContent.AddState
      }
   }

   private fun parseUuid(raw: String): UUID? {
      var var2: UUID
      try {
         var2 = if (StringsKt.contains$default(raw, '-', false, 2, null))
            UUID.fromString(raw)
            else
            UUID.fromString(Regex("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})").replaceFirst(raw, "$1-$2-$3-$4-$5"))
         } catch (var5: Exception) {
         var2 = null
      }

      return var2
   }

   private fun LabelComponent.liveColor(supplier: () -> Color) {
      `$this$liveColor`.setAutoColorSupplier({ 
         `$supplier`() as Color
      })
   }

   private fun t(key: String): String {
      val `$this$asString$iv`: Array<Any> = arrayOfNulls(0)
      val var10000: MutableComponent = Component.translatable(key, Arrays.copyOf(`$this$asString$iv`, `$this$asString$iv`.length))
      val var6: java.lang.String = (var10000 as Component).getString()
      return var6
   }

   private sealed class AddState protected constructor() {
      public data class Active(challenge: Challenge? = null, phase: String? = null, codeCopied: Boolean = false, error: String? = null) : AccountSelectContent.AddState() {
         public final val challenge: Challenge?
         public final val phase: String?
         public final val codeCopied: Boolean
         public final val error: String?

         init {
            this.challenge = challenge
            this.phase = phase
            this.codeCopied = codeCopied
            this.error = error
         }

         public operator fun component1(): Challenge? {
            return this.challenge
         }

         public operator fun component2(): String? {
            return this.phase
         }

         public operator fun component3(): Boolean {
            return this.codeCopied
         }

         public operator fun component4(): String? {
            return this.error
         }

         public fun copy(
            challenge: Challenge? = this.challenge,
            phase: String? = this.phase,
            codeCopied: Boolean = this.codeCopied,
            error: String? = this.error
         ): gg.norisk.client.v2.auth.AccountSelectContent.AddState.Active {
            return AccountSelectContent.AddState.Active(challenge, phase, codeCopied, error)
         }

         public override fun toString(): String {
            return "Active(challenge=${this.challenge}, phase=${this.phase}, codeCopied=${this.codeCopied}, error=${this.error})"
         }

         public override fun hashCode(): Int {
            return (
                     ((if (this.challenge == null) 0 else this.challenge.hashCode()) * 31 + (if (this.phase == null) 0 else this.phase.hashCode())) * 31
                        + java.lang.Boolean.hashCode(this.codeCopied)
                  )
                  * 31
               + (if (this.error == null) 0 else this.error.hashCode())
            }

         public override operator fun equals(other: Any?): Boolean {
            label40@
            if (this === other) {
               return true
            } else {
               return other is AccountSelectContent.AddState.Active
                  && this.challenge == (other as AccountSelectContent.AddState.Active).challenge
                  && this.phase == (other as AccountSelectContent.AddState.Active).phase
                  && this.codeCopied == (other as AccountSelectContent.AddState.Active).codeCopied
                  && this.error == (other as AccountSelectContent.AddState.Active).error
               }
         }

         fun Active() {
            this(null, null, false, null, 15, null)
         }
      }

      public object Idle : AccountSelectContent.AddState()
   }
}
