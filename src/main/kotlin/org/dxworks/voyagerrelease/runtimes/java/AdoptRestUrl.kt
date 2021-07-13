package org.dxworks.voyagerrelease.runtimes.java

import com.google.api.client.http.GenericUrl
import com.google.api.client.util.Key

class AdoptRestUrl(
    version: String,
    @Key
    val architecture: String,
    @Key
    val os: String,
    @Key
    val jvm_impl: String? = "hotspot"
) : GenericUrl("https://api.adoptopenjdk.net/v3/assets/feature_releases/${version}/ga")
