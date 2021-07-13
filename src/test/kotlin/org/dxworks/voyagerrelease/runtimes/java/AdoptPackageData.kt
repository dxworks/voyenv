package org.dxworks.voyagerrelease.runtimes.java

import com.google.api.client.json.GenericJson
import com.google.api.client.util.Key

class AdoptPackageData : GenericJson() {
    @Key
    var link: String? = null
}
