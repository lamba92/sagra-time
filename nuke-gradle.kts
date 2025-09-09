@file:OptIn(ExperimentalPathApi::class)

import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.PathWalkOption
import kotlin.io.path.deleteRecursively
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.walk

val cwd = System.getProperty("user.dir")
val toDelete = listOf(".gradle", "build", ".kotlin")

println("Searching for dirs $toDelete in $cwd")

Path(cwd)
    .walk(PathWalkOption.INCLUDE_DIRECTORIES)
    .filter { it.isDirectory() }
    .filter { it.name in toDelete }
    .forEach {
        println("Deleting $it")
        it.deleteRecursively()
    }
