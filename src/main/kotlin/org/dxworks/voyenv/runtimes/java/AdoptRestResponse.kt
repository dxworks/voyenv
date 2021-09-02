package org.dxworks.voyenv.runtimes.java

import com.google.api.client.json.GenericJson
import com.google.api.client.util.Key

class AdoptRestResponse : GenericJson() {
    @Key
    var binaries: List<AdoptBinariesResponse>? = null

    @Key("version_data")
    var versionData: AdoptVersionData? = null
}
