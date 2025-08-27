package it.sagratime.app.desktop

import coil3.ImageLoader
import coil3.Uri
import coil3.decode.DataSource
import coil3.decode.ImageSource
import coil3.fetch.Fetcher
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import okio.FileSystem
import okio.buffer
import okio.source
import java.net.URI
import java.net.URLConnection


object JarUriFetcherFactory : Fetcher.Factory<Uri> {

    override fun create(
        data: Uri,
        options: Options,
        imageLoader: ImageLoader
    ): Fetcher? {
        // Accept real jar: URIs or "file:" that point inside a JAR (have "!/").
        val s = data.toString()
        val isJarLike = data.scheme?.startsWith("jar") == true
        if (!isJarLike) return null

        return Fetcher {
            // Open the entry inside the JAR and expose it as an ImageSource
            val input = URI(s).toURL().openStream()
            val source = input.source().buffer()
            val imageSource = ImageSource(source, FileSystem.Companion.SYSTEM)

            val mime = URLConnection.guessContentTypeFromName(s) // e.g. "image/png"
            SourceFetchResult(
                source = imageSource,
                mimeType = mime,
                dataSource = DataSource.DISK // local artifact, not NETWORK/MEMORY
            )
        }
    }
}
