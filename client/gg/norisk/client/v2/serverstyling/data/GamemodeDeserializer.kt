package gg.norisk.client.v2.serverstyling.data

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nGamemodeDeserializer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GamemodeDeserializer.kt\ngg/norisk/client/v2/serverstyling/data/GamemodeDeserializer\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,23:1\n1563#2:24\n1634#2,3:25\n*S KotlinDebug\n*F\n+ 1 GamemodeDeserializer.kt\ngg/norisk/client/v2/serverstyling/data/GamemodeDeserializer\n*L\n16#1:24\n16#1:25,3\n*E\n"])
public class GamemodeDeserializer : JsonDeserializer<Gamemode> {
   public open fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Gamemode {
      if (json != null && !json.isJsonNull()) {
         val obj: JsonObject = json.getAsJsonObject()
         val name: java.lang.String = if (obj.has("name") && obj.get("name").isJsonPrimitive()) obj.get("name").getAsString() else ""
         val versionsElem: JsonElement = if (obj.has("versions")) obj.get("versions") else null
         val var18: java.util.List
         if (versionsElem == null || versionsElem.isJsonNull()) {
            var18 = CollectionsKt.emptyList()
         } else if (versionsElem.isJsonArray()) {
            val var10000: JsonArray = versionsElem.getAsJsonArray()
            val `$this$mapTo$iv$iv`: java.lang.Iterable = var10000 as java.lang.Iterable
            val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(var10000 as java.lang.Iterable, 10))

            for (`item$iv$iv` in `$this$mapTo$iv$iv`) {
               `destination$iv$iv`.add((`item$iv$iv` as JsonElement).getAsString())
            }

            var18 = `destination$iv$iv` as java.util.List
         } else {
            var18 = if (versionsElem.isJsonPrimitive()) CollectionsKt.listOf(versionsElem.getAsString()) else CollectionsKt.emptyList()
         }

         return Gamemode(name, var18)
      } else {
         return Gamemode("", CollectionsKt.emptyList())
      }
   }
}
