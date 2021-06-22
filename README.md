# voyager-compose

### voyenv.yml

```yml
# The name of the Environment / Release you want to create
name: voyager-kafka

# The voyager version you want to use
voyager_version: 1.1.0

# The instruments you want in your environment 
instruments:
    # For Github repos
    - name: owmer/repo
      tag: v1.2.5-voyager # also possible to write 1.2.5 but the tag will automatically be converted to v{semver}-voyager?
      asset: asset-name.zip # optional, if not specified, the source code of the specified tag is downloaded
      token: ghp_***** # optional for private repositories
    - dxworks/jafax@v1.2.5-voyager:jafax.zip # format: <owner>/<repo>@<full_tag>:<asset_name> # asset name and colon are optional as stated before
    - url: https://mynexus.example.org/folder/subfolder/resource.zip
      authentication:
          type: basic | bearer | cookie
          username: user
          password: pass
          token: <bearer_token>
          cookie: <cookie content>
          

# The Runtimes you want available in your environment
runtimes:
    # get java versions from https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/tag/jdk-11.0.11%2B9_openj9-0.26.0 or api from https://api.adoptopenjdk.net/q/swagger-ui/#/Assets/searchReleases
    java:
        version: 11.1
        platform: windows | linux | mac
        arch: x86 | x64

    # get node versions from https://github.com/actions/node-versions/blob/main/versions-manifest.json
    node:
        version: 14.1.1
        platform: windows | linux | mac
        arch: x86 | x64 | ...
    
    # get python versions from https://github.com/actions/python-versions/blob/main/versions-manifest.json
    # See this Github Issue for more details on Node an Python distributions: https://github.com/actions/virtual-environments/issues/3630#issuecomment-865638642
    python:
        version: 3.9.1
        platform: windows | linux | mac
        arch: x86 | x64 | ...
    
    # get ruby versions from https://github.com/ruby/ruby-builder/releases    
    ruby:
        version: 12
        distribution: jruby | ruby | truffleruby
        platform: windows | linux | mac
    
    # get maven versions from https://downloads.apache.org/maven/maven-3/    
    maven:
        version: 3.0.5
    
    # get gradle versions from https://gradle.org/next-steps/?version=7.1&format=bin
    gradle: 
        version: 7.1

```

### commands:

* voyenv create file.voyenv.yml -> creates a zip file with the specified environment and with the current version of voyenv
* voyenv update list -> lists all instruments that have new versions
* voyenv update -> updates all instruments to their newest versions
