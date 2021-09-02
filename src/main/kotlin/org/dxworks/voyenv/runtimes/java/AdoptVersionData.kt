package org.dxworks.voyenv.runtimes.java

import com.google.api.client.json.GenericJson
import com.google.api.client.util.Key

class AdoptVersionData : GenericJson() {
    @Key
    var semver: String? = null
}
