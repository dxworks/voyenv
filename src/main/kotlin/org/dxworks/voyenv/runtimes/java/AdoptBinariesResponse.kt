package org.dxworks.voyenv.runtimes.java

import com.google.api.client.http.GenericUrl
import com.google.api.client.util.Key

class AdoptBinariesResponse: GenericUrl(){
    @Key("package")
    var packageData: AdoptPackageData? = null
    @Key
    var project: String? = null
    @Key("image_type")
    var imageType: String? = null
}
