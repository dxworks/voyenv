package org.dxworks.voyagerrelease.runtimes.java

import com.google.api.client.http.GenericUrl
import com.google.api.client.util.Key

class AdoptBinariesResponse: GenericUrl(){
    @Key("package")
    var packageData: AdoptPackageData? = null
}
