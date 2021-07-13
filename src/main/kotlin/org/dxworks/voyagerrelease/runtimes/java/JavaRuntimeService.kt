package org.dxworks.voyagerrelease.runtimes.java

import com.google.common.reflect.TypeToken
import com.vdurmont.semver4j.Semver
import org.dxworks.utils.java.rest.client.HttpClient
import org.dxworks.voyagerrelease.runtimes.RuntimeService
import java.io.File
import java.io.InputStream
import java.net.URL

class JavaRuntimeService : RuntimeService("java") {

    companion object {
        private const val version = "version"
        private const val platform = "platform"
        private const val arch = "arch"
        private const val implementation = "implementation"
    }

    private val httpClient = HttpClient()

    override fun download(configuration: Map<String, String>, location: File): InputStream {
        val semver = Semver(configuration[version]!!)

        val response = httpClient.get(
            AdoptRestUrl(
                semver.major.toString(),
                configuration[arch]!!,
                configuration[platform]!!,
                configuration[implementation]
            )
        )
        val adoptResponses =
            response.parseAs(object : TypeToken<List<AdoptRestResponse>>() {}.type) as List<AdoptRestResponse>

        val bestMatchedResponse = adoptResponses
            .filter { Semver(it.versionData!!.semver).satisfies(semver.toString()) }
            .sortedWith { a, b -> Semver(a.versionData!!.semver).compareTo(Semver(b.versionData!!.semver)) }
            .first()

        return URL(bestMatchedResponse.binaries!![0].packageData!!.link).openStream()
    }
}
