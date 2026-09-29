@file:OptIn(ExperimentalStdlibApi::class)

package org.example

import com.squareup.moshi.*
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer

@JsonQualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TestJsonQualifier

@JsonClass(generateAdapter = true)
data class SerializableDto(
    @param:TestJsonQualifier
    val param: String,
    @field:TestJsonQualifier
    val field: String,
    @get:TestJsonQualifier
    val get: String,
    @TestJsonQualifier // no target means the same as param in this kotlin version
    val default: String,
    @set:TestJsonQualifier
    var set: String,
)

object TestAdapters {
    @ToJson
    fun toJson(@TestJsonQualifier string: String): String {
        return "to qualified"
    }

    @FromJson
    @TestJsonQualifier
    fun fromJson(input: String): String {
        return "from qualified"
    }
}

fun main() {
    val moshi = Moshi.Builder().add(TestAdapters)
        .build()

    val adapter = moshi.adapter<SerializableDto>()

    val dto = SerializableDto(
        param = "param",
        field = "field",
        get = "get",
        default = "default",
        set = "set",
    )

    System.out.println("To json:")
    System.out.println(adapter.toJson(dto))


    val fileSource = FileSystem.RESOURCES.source("dto.json".toPath())

    System.out.println("From json:")
    System.out.println(adapter.fromJson(fileSource.buffer()))
}