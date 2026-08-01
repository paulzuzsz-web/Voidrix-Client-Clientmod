# Voidrix-Client-Clientmod

Inhalt der hochgeladenen `client.zip` — das **Client-Modul** des Voidrix Client
(Fabric-Mod-ID `nrcclient`, Version `26.3.1663417-working+fabric.26.2`, für Minecraft **26.2**).

Es handelt sich um den entpackten Mod-JAR: Ressourcen im Original plus die
**dekompilierten** Quellen (Vineflower-Ausgabe, erkennbar an `@SourceDebugExtension`
und `$$delegatedProperties` — kein Originalcode).

```
client/
├── fabric.mod.json              # Fabric-Metadaten, Entrypoints, gebündelte JARs
├── nrcclient.mixins.json        # 153 Client-Mixins, gruppiert nach Feature
├── owo-client.accesswidener     # Access-Widener für private MC-Felder
├── pack.mcmeta                  # Ressourcenpaket-Format 107
├── META-INF/
│   ├── MANIFEST.MF              # Build-Infos (Loom 1.15.12, Loader 0.19.3)
│   ├── services/                # ServiceLoader: NrcBootstrap, ImmediateWindowProvider
│   └── jars/                    # 9 gebündelte Bibliotheken (JiJ)
├── assets/                      # Shader, Texturen, Sprachdateien
└── gg/norisk/                   # 565 dekompilierte .kt/.java-Dateien
```

**Ausführliche Erklärung: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)**

## Kurzfassung

Der Voidrix Client ist ein **PvP-/Utility-Client** auf Fabric-Basis. Dieses Modul enthält
nicht den ganzen Client, sondern nur die **Feature-Implementierungen** ("Client module
implementations for Voidrix Client"). UI, Cosmetics, Auth, Events und die Compat-Schicht
liegen in separaten Modulen (`gg.norisk.ui.*`, `gg.norisk.compat.*`, `gg.norisk.cosmetics.*`),
die hier nur importiert werden.

Grob liefert das Modul:

* **~70 HUD- und Gameplay-Module** — FPS, Ping, CPS, Koordinaten, ArmorStatus, Keystrokes,
  ReachDisplay, Speedometer, ComboCounter, ScoreboardHUD, PotionStatus, Spotify, Discord RPC …
* **153 Mixins** in 43 Feature-Gruppen — Zoom, FreeLook, FullBright, MotionBlur, OldAnimations,
  ChatHeads, HitColor, ToggleSprint, NoHurtCam, Waypoints, Serverstyling …
* **Rebranding** von Vanilla (Fenstertitel, Splashscreen, Pause-Menü, Client-Brand `nrc`)
* **Backend-Anbindung** — Analytics, Token-Auth, WebSocket, serverabhängiges Sperren von Modulen,
  Moderations-Commands (`Punish`/`Report`)

## Hinweis zur Lizenz

`fabric.mod.json` deklariert `"license": "ARR"` (All Rights Reserved) und das Manifest verweist
auf `https://github.com/NoRiskClient/clientside-mirror` — der Code stammt also aus dem
NoRisk-Client-Projekt. Ein öffentliches Repo mit diesen Dateien wäre eine Weiterverbreitung
fremden, proprietären Codes. Wenn das Repo nicht privat ist, solltest du das prüfen.
