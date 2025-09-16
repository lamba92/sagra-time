package it.sagratime.app.web.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName(
    value = "urlset",
    namespace = "http://www.sitemaps.org/schemas/sitemap/0.9",
)
data class Sitemap(
    @XmlElement(true)
    val url: List<UrlEntry>,
)

@Serializable
@XmlSerialName("url")
data class UrlEntry(
    @SerialName("loc")
    @XmlElement(true)
    val location: String,
    @SerialName("lastmod")
    @XmlElement(true)
    val lastModified: String? = null,
    @SerialName("changefreq")
    @XmlElement(true)
    val changeFrequency: String? = null,
    @XmlElement(true)
    val priority: Double? = null,
)
