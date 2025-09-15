@file:OptIn(ExperimentalSerializationApi::class)

package it.sagratime.core

import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import io.ktor.util.appendAll
import io.ktor.util.toMap
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromStringMap
import kotlinx.serialization.serializer

sealed class UrlParameters(
    val properties: Properties,
    override val serializersModule: SerializersModule,
) : SerialFormat {
    companion object Default : UrlParameters(
        properties = Properties.Default,
        serializersModule = EmptySerializersModule(),
    )

    fun <T> decodeFromParameters(
        deserializer: DeserializationStrategy<T>,
        parameters: Parameters,
    ): T =
        properties.decodeFromStringMap(
            deserializer = deserializer,
            map =
                properties.decodeFromStringMap(
                    parameters
                        .toMap()
                        .mapValues { it.value.joinToString(",") },
                ),
        )

    fun <T> encodeToParameters(
        serializer: SerializationStrategy<T>,
        value: T,
    ): Parameters =
        buildQueryParams {
            appendAll(properties.encodeToStringMap(serializer, value))
        }
}

inline fun <reified T> UrlParameters.decodeFromParameters(parameters: Parameters): T =
    decodeFromParameters(
        deserializer = properties.serializersModule.serializer(),
        parameters = parameters,
    )

inline fun <reified T> UrlParameters.encodeToParameters(value: T): Parameters =
    encodeToParameters(
        serializer = properties.serializersModule.serializer(),
        value = value,
    )

fun buildQueryParams(block: ParametersBuilder.() -> Unit) = ParametersBuilder().apply(block).build()
