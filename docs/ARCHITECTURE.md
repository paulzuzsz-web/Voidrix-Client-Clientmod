# Voidrix Client — Clientmodul: Wie es funktioniert

Analyse des entpackten JARs unter [`../client/`](../client).

| | |
|---|---|
| Mod-ID | `nrcclient` |
| Name | Voidrix Client |
| Version | `26.3.1663417-working+fabric.26.2` |
| Minecraft | 26.2 |
| Loader | Fabric ≥ 0.15.0 (gebaut mit Loader 0.19.3, Loom 1.15.12) |
| Umgebung | `client` (rein clientseitig, kein Server-Anteil) |
| Lizenz | ARR |
| Herkunft | `Source-Repository: https://github.com/NoRiskClient/clientside-mirror`, Branch `working`, Commit `5534191` |
| Sprache | Kotlin (399 Dateien) + Java (165 Dateien) |

Die `.kt`/`.java`-Dateien sind **Dekompilat**, kein Originalquellcode: sie enthalten
`@SourceDebugExtension`-Annotationen, `$$delegatedProperties`-Zugriffe und synthetische
Lambda-Klassen wie `PingHud$registerEvents$lambda$5$lambda$4$$inlined$text$default$1.kt`.
Sie sind lesbar, aber nicht ohne Weiteres kompilierbar.

---

## 1. Einordnung: nur ein Modul von mehreren

`fabric.mod.json` beschreibt das Modul als *"Client module implementations for Voidrix Client"*.
Der Paketbaum bestätigt das — alles unter `gg.norisk.client.*` liegt hier, aber der Code
importiert massiv aus Paketen, die **nicht** im JAR sind:

| Paket | Rolle | im JAR? |
|---|---|---|
| `gg.norisk.client.*` | Feature-Implementierungen | ✅ dieses Modul |
| `gg.norisk.compat.*` | Versionsabstraktion (`MCClient`, `ClientEvents`, `MCLogger`, `NoriskAuth`, `NrcWebSocketClient`, `NrcReflectionUtil`) | ❌ separat |
| `gg.norisk.ui.*` | Modul-/Wert-API, HUD-Framework, Keybinds, Themes, Toasts | ❌ separat |
| `gg.norisk.cosmetics.*` | Cosmetics, Emotes, NRCPlus | ❌ separat |
| `gg.norisk.owolib.owo.ui.*` | geshadetes owo-lib für UI-Layouts | ❌ separat |

Der Launcher lädt also mehrere JARs; dieses hier ist das Feature-Paket.

### Gebündelte Bibliotheken (`META-INF/jars/`, Jar-in-Jar)

| JAR | Zweck |
|---|---|
| `KDiscordIPC-0.2.2.jar` | Discord Rich Presence über IPC |
| `dbus-java-core` + `dbus-java-transport-native-unixsocket` | D-Bus unter Linux (Spotify/MPRIS) |
| `core` / `windows` / `linux` / `macos` `26.2.15-dev-java-8` | plattformspezifische Natives |
| `mixinsquared-fabric-0.3.3.jar` | erlaubt das Abschalten fremder Mixins |
| `nrc-analytics-client-26.2.13-master.jar` | Analytics-Client (`dev.jakub.nrc.analytics`) |

---

## 2. Startablauf

### 2.1 Einstiegspunkte

Es gibt **keinen** klassischen `client`-Entrypoint in `fabric.mod.json`. Der Start läuft über
drei Wege:

1. **ServiceLoader** — `META-INF/services/gg.norisk.compat.bootstrap.NrcBootstrap`
   registriert `gg.norisk.client.bootstrap.ClientBootstrap`.
   Die Compat-Schicht sammelt alle `NrcBootstrap`-Implementierungen ein und ruft sie nach
   `priority()` (hier `100`) in `onEarlyInit()` auf.
2. **Mixin-Plugin** — `nrcclient.mixins.json` nennt `gg.norisk.compat.mixin.NrcCompatMixinPlugin`
   als `plugin`; damit entscheidet Code zur Ladezeit, welche Mixins überhaupt angewendet werden.
3. **`mixinsquared`-Entrypoint** — `gg.norisk.client.v2.plugin.NrcMixinCanceller` (siehe 5.2).

`gg.norisk.client.forge.ClientForgeEntrypoint` und `NoOpWindowProvider` sind leere Klassen —
Platzhalter für einen Forge-Port, der hier nicht implementiert ist. `NoOpWindowProvider` ist
trotzdem als `ImmediateWindowProvider`-Service eingetragen (unterdrückt Forges frühes Ladefenster).

### 2.2 Was `ClientBootstrap.initBootstrap()` macht

`client/gg/norisk/client/bootstrap/ClientBootstrap.kt` ist der zentrale Startpunkt. In Reihenfolge:

```kotlin
NoriskTokenManager.INSTANCE.ensureStarted()      // Auth-Token besorgen/erneuern
NrcWebSocketClient.INSTANCE.ensureAutoConnect()  // WebSocket zum Backend
NrcAnalytics.init()                              // Telemetrie
V3Preload.INSTANCE.register()                    // UI-Vorladen
```

Danach werden ~72 Modulklassen **per Reflection** über ihren voll qualifizierten Namen geladen
(`NrcReflectionUtil.tryLoadObject(...)`), gefiltert auf die, die tatsächlich existieren, und mit
`ModuleProvider.registerAll(...)` registriert. Das ist der Grund für die Reflection: fehlende
Module (z. B. weil ein Modul in dieser Version fehlt oder ein Drittanbieter-Mod nicht installiert
ist) führen nicht zum Absturz, sondern werden still übersprungen.

Anschließend:

* `initHooks()` auf den sechs Drittanbieter-Bridges (siehe 4.6)
* `init()`/`register()` auf Layern und Commands: Screenshot, ResourcePackOrganizer,
  LoadingTips, AccountSwitcher, ServerSwitcher, Keybinds, Cosmetics-Command, Waypoints
  (Store, Death, Destination, Command, Renderer), TestingLoop
* `ExternalResourcePackRegistry.register("VoidrixClient/designer")` — externes Ressourcenpaket
* `DirectoryLinkManager` verlinkt `resourcepacks/`, `screenshots/` und `shaderpacks/`
  aus dem Launcher-Verzeichnis in die Instanz
* `ServerLockedModuleManager` bekommt einen Provider, der aus `ServerStylingManager` die
  auf einem Server gesperrten Module liest, plus einen Toast-Handler für die Meldung
* Join-/Disconnect-Events werden auf `ServerLockedModuleManager` verdrahtet
* `PunishCommand.init()` und `ReportCommand.init()`
* **First-Launch-Defaults**: beim allerersten Start (erkannt an `MixinBridge.isFirstOptionsTxtCreate`)
  werden VSync aus, FPS-Limit 260 und Gamma 1.0 gesetzt und `options.txt` gespeichert

---

## 3. Das Modulsystem

Jedes Feature ist ein Kotlin-`object`, das von einer Basisklasse aus `gg.norisk.ui.api` erbt.
Beispiel `client/gg/norisk/client/v2/modules/ping/PingHud.kt`:

```kotlin
public object PingHud : SingleTextHud("Ping", "{ping} ms", ...) {
    @Category(name = "Display")
    public final var showHud: Boolean by ValueApiKt.boolean$default(true, ...)

    @Category(name = "Display")
    public final var usePingColor: Boolean by ValueApiKt.boolean$default(true, ...)
}
```

Drei Bausteine:

* **`Module` / `SingleTextHud`** — Basisklassen; `SingleTextHud` bringt Text-Template
  (`{ping} ms`), Positionierung (`AnchorPointPosition`), Hintergrund (`DynamicBackground`) mit.
* **`ValueApi`-Delegates** (`boolean`, `int`, `enum`, …) — jede Einstellung ist ein
  Kotlin-Property-Delegate auf einem `ValueHolder`. Dadurch werden Settings automatisch
  serialisiert, in Profilen gespeichert und in der Config-GUI erzeugt.
* **`@Category(name = ...)`** — gruppiert Settings in der GUI.

Gerendert wird mit **owo-lib** (`FlowLayout`, `LabelComponent`, `UIComponents`), geshaded als
`gg.norisk.owolib.owo.*`.

### Registrierte Module (aus `ClientBootstrap`)

**HUD/Anzeige:** FPS, PingHud, CPS, Clock, Coordinates, Speedometer, ReachDisplay, Uptime,
DayCounter, TPS, ItemCounter, ArmorStatus, ComboCounter, JumpReset, Keystrokes, MouseTracker,
ScoreboardModule, PotionStatus, Titles, ActionBar, ResourcePackDisplay, ServerHud, SpotifyHud

**Rendering/Optik:** FullBrightModule, NoFogModule, ColorSaturationModule, MotionBlurModule,
FovChanger, NameTagsModule, HitColorModule, HitBox, BlockOutline, ArrowTrail, ParticleModule,
GlintColorizerModule, ItemModel, ItemHighlighter, ShinyPots, LowFireModule, SideShieldModule,
CustomCrosshair, ClearBackgroundModule, OldAnimationsModule, NoHurtCam, IconModule

**Gameplay/QoL:** ZoomModule, FreeLookModule, ToggleSprintModule, DropStackModule, AutoText,
AutoReconnect, WeatherChanger, TimeChanger, TntTimer, NoAdvancementModule, ChatHeads,
BorderlessFullscreenModule, StreamerMode, PackTweaks, LoadingScreenTipsModule,
ResourcePackOrganizerModule, BadOptimizationsModule, ScreenshotModule, WaypointModule,
DiscordIntegrationModule, ProfilesModule, NRCPlusModule

**Drittanbieter-Bridges:** SaturationModule, ShulkerPreviewModule, ThreeDSkinModule,
TiersModule, HealthIndicatorsModule, WaveyCapesModule

Zusätzlich `DummyModule`, aber nur wenn `DevAuth.isEnabled`.

---

## 4. Die Mixins

`nrcclient.mixins.json` listet **153 Client-Mixins** in 43 Feature-Paketen unter
`gg.norisk.client.v2.mixin`. `"required": false` bedeutet: schlägt ein Mixin fehl, stürzt das
Spiel nicht ab. `compatibilityLevel: JAVA_21`.

Die größten Gruppen:

| Paket | # | Was es tut |
|---|---:|---|
| `compat` | 13 | Kompatibilität mit Iris, ImmediatelyFast, Xaero, 3D-Skin-Layers, Tiers, HealthIndicators |
| `resourcepackorganizer` | 11 | Ordner/Kategorien im Ressourcenpaket-Bildschirm |
| `chatheads` | 10 | Spielerköpfe vor Chatnachrichten (hakt sich tief in Font-/GuiRenderer ein) |
| `oldanimations` | 10 | 1.7-Item-/Schlag-/Sneak-/Herzanimationen |
| `itemmodel` | 8 | Item-Modell-Anpassungen in der Hand |
| `particle` | 7 | Partikelfarben/-verhalten (mit `ParticleColorAccessor`) |
| `zoom` | 6 | OptiFine-artiger Zoom inkl. Scroll und Tastenkonflikt-Handling |
| `glintcolorizer` | 6 | Verzauberungs-Glint einfärben |
| `bugfixes`, `hitbox`, `itemhighlighter` | je 5 | Bugfixes, Hitbox-Anzeige, Item-Hervorhebung |
| `serverstyling` | 5 | Server-Einträge in der Multiplayer-Liste stylen, beworbene Server |
| `freelook`, `clearbackground`, `togglesprint` | je 4 | Perspektiv-Freilauf, transparente Hintergründe, Sprint-Toggle |
| `branding` | 3 | Rebranding |

Kleinere Einzelmixins: `fullbright`, `nofog`, `nohurtcam`, `lowfire`, `sideshield`, `clearchat`,
`dropstack`, `nametags`, `windowtitle`, `splashscreen`, `weatherchanger`, `timechanger`,
`packtweaks`, `perf`.

### 4.1 Access Widener

`owo-client.accesswidener` (Format v2, Namespace `official`) öffnet private Vanilla-Felder.
Die Kommentare darin sind faktisch ein Änderungslog der MC-Versionen:

```
# 26.2: GameRenderer.mainCamera became private (MCCamera.getCameraEntity)
# 26.2: FOG_SNIPPET removed (use MATRICES_FOG_SNIPPET)
# 26.2: GuiRenderer$MeshToDraw removed
# ChatHeads - Font.PreparedTextBuilder for text rendering interception
```

Weil `fabric.loom.disableObfuscation=true` gesetzt ist, laufen Mojang-Namen bis zur Laufzeit durch.

### 4.2 Rebranding

Drei Stellen ersetzen Vanilla-Branding:

* `BrandingClientBrandRetrieverMixin` — `getClientModName()` liefert `nrc:<version>`;
  auf Experimental-Servern mit `§c`-Präfix (rot). Server sehen also den Client-Brand `nrc`.
* `WindowTitleMixin` — bricht `Window.setTitle()` ab und setzt stattdessen
  `WindowTitleModule.getTitle()`.
* `MojangSplashOverlayMixin` — ersetzt den weißen Mojang-Splash-Hintergrund durch
  `ARGB.color(255, 27, 30, 43)` (dunkelblau).
* Dazu `BrandingPauseScreenMixin` und `BrandingContainerScreenMixin`.

### 4.3 Post-Processing-Shader

`assets/noriskclient/`:

* `shaders/post/nrc_saturation.fsh` + `post_effect/color_saturation.json` — Sättigungsregler
* `shaders/post/motion_blur_simple.fsh` + `post_effect/motion_blur_simple.json` — Motion Blur
* `shaders/post/blit.fsh` — Hilfs-Blit

Angesteuert von `colorsaturation.*` und `motionblur.*`-Mixins über `PostPass`/`PostChain`.

### 4.4 Waypoints

`gg.norisk.client.v2.waypoints` ist ein eigenes Subsystem:
`PersistentWaypointStore` (Speicherung unter `VoidrixClient/waypoints`), `PersistentWaypointRenderer`,
`DeathWaypointHandler` (setzt automatisch einen Deathpoint), `DestinationWaypointHandler`
(Ankunftserkennung), `WaypointCommand`, `WaypointColors` — plus eine **Xaero-Bridge**
(`waypoints/xaero`, Logger `Voidrix-NrcToXaeroBridge`, `Voidrix-XaeroSource`), die Waypoints
mit Xaero's Minimap synchronisiert.

### 4.5 Server-Styling

`gg.norisk.client.v2.serverstyling` lädt ein `ServerStyleManifest` (mit `StyledServer`, `Assets`,
`Gamemode`, `Socials`) und stylt damit die Multiplayer-Liste: Banner, Farben, Gamemodes,
beworbene Server (`PromotedServerMixin`, `PromotedServerMotdMixin`). `PromotedServerVisibility`
referenziert `https://norisk.host`.

Wichtig: derselbe `ServerStylingManager` liefert `ServerLockedModuleManager` die Liste der
**auf einem Server gesperrten Module** — beim Join wird `onServerJoin(ip)` aufgerufen und
verbotene Module werden deaktiviert (mit Toast-Hinweis). Das ist der Anticheat-Kompromiss:
Server können Features des Clients abschalten.

### 4.6 Drittanbieter-Kompatibilität

`gg.norisk.client.v2.mixin.compat` behandelt: **Iris** (Shader), **ImmediatelyFast**
(Renderoptimierung), **Xaero** (Minimap), **3D Skin Layers**, **Tiers**, **HealthIndicators**.
Die passenden `modules/thirdparty/*Module`-Klassen bekommen in `ClientBootstrap` ein
`initHooks()` per Reflection — vorhanden oder nicht, beides ist ok.

---

## 5. Backend-Anbindung und Konfiguration

### 5.1 Analytics

`gg/norisk/client/v2/analytics/NrcAnalytics.kt` + `NrcAnalyticsConfig.kt`:

* Endpoint: `https://analytics-api-staging.norisk.gg/api/track`, lokal `http://127.0.0.1:8080`
* Nutzer-ID: `AnalyticsUtils.generatePersistentUserId()` + `systemProperties()`
* Getrackt werden Server-Join/Disconnect, Modul-Toggles, Theme-Wechsel, Emote-Auswahl und
  Cosmetic-Equip — jeweils mit 1000 ms Debounce pro Schlüssel
* **Standardmäßig aus.** `isEnabled()` liefert nur `true`, wenn `-Dnorisk.analytics.enabled=true`
  gesetzt ist oder `DevAuth.isEnabled`. URL überschreibbar via `-Dnorisk.analytics.url`.

### 5.2 Mixin-Canceller

`gg/norisk/client/v2/plugin/NrcMixinCanceller.kt` implementiert `MixinCanceller` aus
mixinsquared. Er lädt zur Laufzeit `noriskclient:mixin_cancellor.json` (über `NrcAssetReader`,
also aus dem Cache/Backend nachladbar) mit einer Liste von Mixin-Klassennamen und
Regex-Paketmustern, die deaktiviert werden sollen. Fehlt die Datei, gelten Defaults.
Fest verdrahtet abgeschaltet sind zwei ImmediatelyFast-Mixins zum Font-Atlas-Resizing.

Praktisch heißt das: **fremde Mixins lassen sich per Serverkonfiguration abschalten**, ohne den
Client neu auszuliefern — der Weg, um Konflikte mit anderen Mods zu entschärfen.

### 5.3 Auth und Accounts

* `NoriskTokenManager` (Compat-Modul) hält den Backend-Token
* `gg.norisk.client.v2.auth` — `AccountSelectScreen`, `AccountSwitcherComponent/Button/Layer`,
  `SessionAccountMemory`: Account-Wechsel im laufenden Spiel
* `modules/reauth/SessionReauth.kt` — erkennt an Disconnect-Texten (`AUTH_FAIL_MARKERS`)
  abgelaufene Sessions und bietet einen Reauth-Button samt `SessionSwapper` an

### 5.4 Moderation

`gg.norisk.client.v2.moderation` enthält `PunishCommand` und `ReportCommand` — clientseitig
registrierte Brigadier-Commands, die Requests ans Backend schicken und das Ergebnis als Toast
zeigen (inkl. `vcPardon`). Das sind Staff-Werkzeuge; sichtbar/nutzbar sind sie nur mit
entsprechender Backend-Berechtigung.

### 5.5 Screenshot-Upload

`gg/norisk/client/v2/screenshot/LitterboxUploader.kt` lädt Screenshots zu
`https://litterbox.catbox.moe/resources/internals/api.php` hoch (temporärer Filehost) und legt
den Link in die Zwischenablage. `ScreenshotModule` hängt sich an das Screenshot-Event und
registriert eigene Commands.

---

## 6. Auffälligkeiten

* **Die Sprachdateien sind identisch.** `assets/minecraft/lang/en_us.json` und `de_de.json`
  haben beide 8258 Schlüssel mit **exakt denselben Werten — auf Deutsch**. Der Mod überschreibt
  also die englische Vanilla-Lokalisierung mit deutschem Text; auf `en_us` steht das Spiel
  trotzdem auf Deutsch. Ob gewollt oder ein Build-Fehler, lässt sich von außen nicht sagen —
  es ist aber der wahrscheinlichste Kandidat, falls jemand "englisch geht nicht" meldet.
* **Zwei Asset-Namespaces mit gleichem Logo.** `assets/nrc-client/textures/noriskclient-logo-text.png`
  und `assets/noriskclient/textures/noriskclient-logo-text.png` sind Dubletten unter
  verschiedenen Namespaces — Altlast aus dem Namespace-Wechsel.
* **Branding nur halb durchgezogen.** Nach außen "Voidrix" (Logger `Voidrix-*`, Verzeichnis
  `VoidrixClient/`, `pack.mcmeta`-Beschreibung), intern weiterhin `gg.norisk`, Mod-ID `nrcclient`,
  Asset-Namespace `noriskclient`, Backend `*.norisk.gg`. Ein Rebrand der Oberfläche, nicht des Codes.
* **Kein Server-Anteil.** `"environment": "client"` — alles läuft lokal; der einzige
  „Server"-Einfluss ist `ServerLockedModuleManager`, der Module auf Wunsch des Servers sperrt.
