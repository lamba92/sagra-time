package it.sagratime.app.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlin.jvm.JvmName

data class Combine2<A, B>(
    val a: A,
    val b: B,
)

@Suppress("UNCHECKED_CAST")
@JvmName("flowCombine")
public fun <A, B> Flow<A>.combine(flow: Flow<B>): Flow<Combine2<A, B>> =
    combineTransform(this, flow) {
        emit(
            Combine2(
                a = it[0] as A,
                b = it[1] as B,
            ),
        )
    }
