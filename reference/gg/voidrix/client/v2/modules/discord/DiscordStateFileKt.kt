package gg.voidrix.client.v2.modules.discord

import gg.voidrix.compat.client.MCLogger
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.json.JsonKt
import org.slf4j.Logger

private final val logger: Logger = MCLogger.getLogger("DiscordStateFile")

private final val json: Json = JsonKt.Json$default(null, { $this$Json: JsonBuilder ->
   `$this$Json`.setIgnoreUnknownKeys(true)
   `$this$Json`.setPrettyPrint(false)
   Unit.INSTANCE
}, 1, null)
