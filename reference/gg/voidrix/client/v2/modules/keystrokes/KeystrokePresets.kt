package gg.voidrix.client.v2.modules.keystrokes

public object KeystrokePresets {
   public final val WASD: KeystrokePreset =
      KeystrokePreset(
         "wasd",
         "WASD",
         CollectionsKt.listOf(
            arrayOf(
               KeystrokePreset.PresetEntry(2, 0, 2, 2, "key.keyboard.w"),
               KeystrokePreset.PresetEntry(0, 2, 2, 2, "key.keyboard.a"),
               KeystrokePreset.PresetEntry(2, 2, 2, 2, "key.keyboard.s"),
               KeystrokePreset.PresetEntry(4, 2, 2, 2, "key.keyboard.d")
            )
         )
      )
      public final val WASD_SPACE: KeystrokePreset =
      KeystrokePreset(
         "wasd_space",
         "WASD + Space",
         CollectionsKt.listOf(
            arrayOf(
               KeystrokePreset.PresetEntry(2, 0, 2, 2, "key.keyboard.w"),
               KeystrokePreset.PresetEntry(0, 2, 2, 2, "key.keyboard.a"),
               KeystrokePreset.PresetEntry(2, 2, 2, 2, "key.keyboard.s"),
               KeystrokePreset.PresetEntry(4, 2, 2, 2, "key.keyboard.d"),
               KeystrokePreset.PresetEntry(0, 4, 6, 1, "key.keyboard.space")
            )
         )
      )
      public final val PVP_CLICKS: KeystrokePreset =
      KeystrokePreset(
         "pvp_clicks",
         "PvP + Clicks",
         CollectionsKt.listOf(
            arrayOf(
               KeystrokePreset.PresetEntry(2, 0, 2, 2, "key.keyboard.w"),
               KeystrokePreset.PresetEntry(0, 2, 2, 2, "key.keyboard.a"),
               KeystrokePreset.PresetEntry(2, 2, 2, 2, "key.keyboard.s"),
               KeystrokePreset.PresetEntry(4, 2, 2, 2, "key.keyboard.d"),
               KeystrokePreset.PresetEntry(0, 4, 3, 2, "key.mouse.left"),
               KeystrokePreset.PresetEntry(3, 4, 3, 2, "key.mouse.right"),
               KeystrokePreset.PresetEntry(0, 6, 6, 1, "key.keyboard.space")
            )
         )
      )
      public final val all: List<KeystrokePreset> = CollectionsKt.listOf(arrayOf(WASD, WASD_SPACE, PVP_CLICKS))
   public final val cpsPresets: Set<String> = SetsKt.setOf(PVP_CLICKS.id)
}
