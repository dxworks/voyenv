package org.dxworks.voyenv.runtimes.java

import com.google.api.client.json.GenericJson
import com.google.api.client.util.Key
import java.math.BigDecimal

class AdoptPackageData : GenericJson() {
    @Key
    var link: String? = null
    @Key
    var name: String? = null
    @Key("size")
    var packageSize: BigDecimal = BigDecimal.ZERO
}
