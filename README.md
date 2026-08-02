# Voidrix

Clientseitiger Minecraft-Mod für **Fabric**, mit Mod-Menü, freiem HUD-System,
PvP- und Optik-Modulen, Waypoints und Discord Rich Presence.

Voidrix ist ein **reiner Client-Mod**: Es wird kein eigenes Paket an den Server
geschickt und kein Spielverhalten verändert, das der Server nicht ohnehin
zulässt. Der Mod läuft damit auf jedem Server, ohne dort installiert zu sein.

---

## Bauen

```bash
./gradlew build
```

Die fertige JAR liegt danach unter:

```
build/libs/voidrix-1.0.0.jar
```

Diese Datei in den `mods/`-Ordner der Minecraft-Instanz kopieren. Zusätzlich
werden **Fabric Loader** und **Fabric API** benötigt.

Zum Testen im Entwickler-Client:

```bash
./gradlew runClient
```

### Voraussetzungen

| | Version |
|---|---|
| Minecraft | 26.2 |
| Fabric Loader | 0.19.3 |
| Fabric API | 0.156.0+26.2 |
| Fabric Loom | 1.17.17 |
| **JDK** | **25** |

> **Hinweis zur Java-Version:** Minecraft 26.2 verlangt laut Mojangs
> Version-Manifest Java 25 (`javaVersion.majorVersion: 25`). Mit einem JDK 21
> lassen sich die Klassen von 26.2 nicht einmal einlesen, deshalb ist die
> Toolchain in `build.gradle` auf 25 gesetzt.

> **Hinweis zu Mappings:** Ab Minecraft 26.x wird das Spiel **unobfuskiert**
> ausgeliefert. Es gibt daher keine Yarn-Mappings mehr und in `build.gradle`
> bewusst *keine* `mappings`-Zeile — der Code kompiliert direkt gegen Mojangs
> echte Klassen- und Methodennamen. Verwendet wird das Plugin
> `net.fabricmc.fabric-loom` (die `-remap`-Variante ist nur für die älteren,
> obfuskierten Versionen ≤ 1.21.11 gedacht).

---

## Bedienung

| Taste | Funktion |
|---|---|
| **Rechte Umschalttaste** | Voidrix-Menü öffnen |
| Rechte Strg-Taste | HUD-Editor öffnen |
| C | Zoom |
| B | Waypoint an aktueller Position setzen |

Alle Tasten sind in den Minecraft-Steuerungsoptionen unter der Kategorie
**Voidrix** frei belegbar.

---

## Konfiguration

Die gesamte Konfiguration liegt als JSON in:

```
config/voidrix.json
```

Sie wird beim Start geladen und bei jeder Änderung im Menü sowie beim Beenden
gespeichert. Die Datei ist bewusst gut lesbar formatiert und lässt sich auch
von Hand bearbeiten; unbekannte oder fehlerhafte Einträge werden ignoriert,
statt den Start zu verhindern.

```jsonc
{
  "configVersion": 1,
  "premium": { "unlocked": false, "redeemedAt": 0 },
  "modules": {
    "fps": {
      "enabled": true,
      "settings": { "scale": 1.0, "background": "BLUR", "text_color": "#FFEDE9F5" },
      "position": { "x": 0.02, "y": 0.02 }
    }
  },
  "waypoints": []
}
```

---

## Module

**HUD** — CPS-Zähler, FPS, Ping, Koordinaten, TPS, Health Indicator, Chat Heads

**Gameplay/PvP** — Item Highlighter, Custom Crosshair, Block Outlines,
NoHurtCam, Drop Stack, Toggle Sprint, Zoom

**Optik** — 3D Skin Vorschau, Shulker Preview, Shiny Pots, Wavey Capes,
Item Model, Particles Filter, Borderless Fullscreen

**Sonstiges** — Discord Rich Presence, Waypoints

Jedes Modul ist einzeln aktivierbar und hat eine eigene Detailseite mit
ON/OFF-Schalter, „Zurücksetzen“, Slidern, Dropdown, Farbwähler und Textfeldern.

---

## HUD-System

Jedes HUD-Modul erbt von `hud/HudModule` und liefert nur seinen **Text** —
Position, Skalierung, Hintergrund, Rahmen und Farben erledigt die Basisklasse
einheitlich. Positionen werden als **Bruchteil des Bildschirms** (0…1)
gespeichert und bleiben damit bei Auflösungs- oder GUI-Skalierungswechseln an
der gleichen relativen Stelle.

Das Format-Textfeld kennt die Platzhalter `{left}` (Beschriftung) und
`{right}` (Wert), z. B. `{left}: {right}` → `FPS: 144`.

Im HUD-Editor (rechte Strg-Taste) lassen sich alle aktiven Elemente per
Drag & Drop verschieben; sie rasten an Bildschirmkanten und -mitte ein.
`R` legt alle Elemente wieder untereinander an den linken Rand.

### Eigenes HUD-Modul

```java
public class MyHud extends HudModule {
    public MyHud() {
        super("my_hud", "Mein HUD", "Zeigt etwas", 0.02f, 0.5f);
    }

    @Override public String getLeftText()  { return "Label"; }
    @Override public String getRightText() { return "42"; }
}
```

Danach nur noch in `ModuleManager.registerAll()` eintragen — Menü, HUD-Editor
und Config greifen automatisch darauf zu. Neue Einstellungen erscheinen
ebenfalls automatisch auf der Detailseite, weil diese generisch aus den
`Setting`-Typen aufgebaut wird.

---

## Namens-Präfix

Vor dem Namen jedes Voidrix-Nutzers steht ein kleines „V“ — im Nametag über
dem Kopf, in der Tab-Liste und im Chat. Voidrix+-Nutzer bekommen die
hervorgehobene Variante (im Nametag zusätzlich zwischen Violett und Cyan
pulsierend), normale Nutzer ein schlichtes graues V.

Umgesetzt per Mixin auf `EntityRenderer#getNameTag` (Nametag),
`PlayerTabOverlay#getNameForDisplay` (Tab-Liste) und
`ChatComponent#addMessage` (Chat).

Da der Mod rein clientseitig ist, kennt der Client zunächst nur **sich selbst**
sicher als Voidrix-Nutzer. Wie sich das über ein Backend (REST-Lookup der
Tab-Listen-UUIDs) oder eine Custom Payload (`voidrix:users` via
`ClientPlayNetworking`) auf andere Spieler ausweiten lässt, ist ausführlich in
`user/VoidrixUsers.java` beschrieben — beides dockt an derselben Methode
`markVoidrixUser(UUID, boolean)` an, ohne dass Rendering oder Mixins angefasst
werden müssen.

---

## Voidrix+

Im Voidrix+-Bereich des Menüs lässt sich ein Code einlösen. Bei Erfolg wird
Voidrix+ dauerhaft in der Config gespeichert, das Premium-Badge aktiv, das
Namens-V wechselt zur Premium-Variante und gesperrte Module (Schloss-Icon)
werden freigeschaltet. Bei falscher Eingabe erscheint
„Code ungültig – prüfe deine Eingabe“.

Die gültigen Codes stehen als Liste in `premium/PremiumCodes.java` und lassen
sich dort einfach erweitern:

```java
public static final List<String> VALID_CODES = List.of(
        "kwhfiejyguso+"
        // , "voidrix-beta-2026"
);
```

> Da der Mod clientseitig ist, stehen die Codes im Klartext in der JAR. Für
> eine fälschungssichere Lösung müsste die Einlösung gegen ein Backend geprüft
> werden — der Ansatz ist in `premium/PremiumManager.java` kommentiert.

---

## Design

| | Farbe |
|---|---|
| Basis (dunkles Violett-Grau) | `#12101A` |
| Akzent (Violett) | `#7C3AED` |
| Zweitakzent (Cyan) | `#22D3EE` |

Karten tragen eine **gekappte Ecke oben rechts** als Wiedererkennungsmerkmal,
Konturen sind feine glühende Linien statt harter Schatten. Alle Farben und
Layout-Konstanten stehen zentral in `ui/Theme.java` — wer umfärben möchte,
ändert nur diese Datei.

---

## Projektstruktur

```
src/main/java/gg/voidrix/client/
├── VoidrixClient.java          Einstiegspunkt (Client-Entrypoint)
├── VoidrixKeys.java            Tastenbelegungen
├── config/ConfigManager.java   Laden/Speichern von config/voidrix.json
├── premium/                    Voidrix+ Codes und Status
├── module/
│   ├── Module.java             Basisklasse aller Module
│   ├── ModuleManager.java      Registrierung (hier neue Module eintragen)
│   ├── setting/                Bool, Int, Double, Enum, Color, String
│   ├── hud/  pvp/  visual/  misc/
├── hud/                        Generisches HUD-System + HudManager
├── ui/                         Menü, Detailseite, HUD-Editor, Theme, Draw
├── mixin/                      7 Mixins (Zoom, NoHurtCam, V-Präfix, …)
├── user/VoidrixUsers.java      Wer bekommt das V
├── waypoint/                   Waypoints
└── integration/DiscordRpc.java Discord IPC ohne externe Bibliothek
```

---

## Hinweise

- **Discord Rich Presence** ist ohne externe Bibliothek umgesetzt: Voidrix
  spricht direkt den lokalen Discord-IPC-Socket an (Java 21+ bringt mit
  `UnixDomainSocketAddress` alles Nötige mit). Läuft kein Discord, passiert
  schlicht nichts. Die eigene Application-ID trägt man im Modul ein.
- **Cloth Config** wird bewusst nicht verwendet — Voidrix bringt seine eigene
  Config-GUI mit, damit das Menü zum Design passt. Wer lieber Cloth Config
  nutzt, findet den nötigen Eintrag auskommentiert in `build.gradle`.

## Lizenz

MIT — siehe [LICENSE](LICENSE).
