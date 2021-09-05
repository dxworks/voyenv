# Voyenv

Voyenv is the official package manager for Voyager. 
Use the tool for packing [Voyager](https://github.com/dxworks/voyager) releases with the instruments and runtime dependencies you need.

## Installation & Usage
Install Voyenv either as a global npm package or download the latest release from Github.

### NPM Installation
Install Voyenv through the NPM package manager. 
We have created a wrapper around the voyenv jar (using [jdeploy](https://github.com/shannah/jdeploy)) to easily install and upgrade the tool.

```shell
npm install @dx-works/voyenv -g
```

Verify the installation by running:
```shell
voyenv -version
```

#### Usage
To generate a Voyager release, open a command line in the folder you want to generate the release in.
Before running the command you have to create a `yml` file described in the [Configuration](#Configuration) section, e.g. `my-voyenv.yml`.

Run the following command in a terminal window:
```yaml
voyenv [path/to/voyenv/file]
```

The path to the voyenv.yml file is optional. If none is specified voyenv will search for a file named `voyenv.yml` in the current working directory.
The voyager release will also be created in the current working directory.

### Github Release Download

Download the latest release from [Github](https://github.com/dxworks/voyenv/releases). Unpack the zip file to a folder and open a terminal window in the newly extracted folder.

Run the following command to verify the installation:

```shell
java -jar voyenv.jar -version
```

#### Usage
From the voyenv installation folder run:
```shell
java -jar  voyenv.jar [path/to/voyenv/file.yml]
```
The path to the voyenv.yml file is optional. If none is specified voyenv will search for a file named `voyenv.yml` in the current working directory.
The voyager release will also be created in the current working directory.

## Configuration
Voyenv declares the voyager release in a `yml` file.
A voyenv.yml example:

```yaml
# Name of the release (a folder with this name will be created in the working directory)
name: voyager-test-1
# The Voyager version to download. See at https://github.com/dxworks/voyager/releases 
voyager_version: v1.4.1
# A list of instruments to download (from Github only)
instruments:
    # Name in owner/repo format
  - name: dxworks/voyager-git-log-instrument
    # The tag to find the specific release / tag
    # If not specified voyenv pulls all tags matching v*-voyager 
    # and chooses the one with the highest semver
    tag: v1.0.0
    # The asset to download from the release 
    # If not specified, the source code zip for the tag will be downloaded
    asset: git-log.zip
  - name: dxworks/lizard
#    tag: v1.17.9-voyager
  - name: dxworks/metrixplusplus
#    tag: v1.7.1-voyager
  - name: dxworks/inspector-git
    asset: iglog.zip
  - name: dxworks/dude
    asset: dude-voyager.zip
  - name: dxworks/insider
    asset: insider-voyager.zip
  - name: dxworks/honeydew
    asset: honeydew.zip
  - name: dxworks/jafax
    asset: jafax.zip
  - name: dxworks/maven-miner
    asset: maven-miner-voyager.zip
  - name: dxworks/npm-miner
    asset: npm-miner-voyager.zip

# A list of Github tokens. To get the latest tagged release of a voyager instrument,
# voyenv uses the Github Rest API. Due to rate limits of the Github Rest API
# it might be the case that the voyenv installation will stop due to rate limits.
# To avoid this, generate a Github Personal Access token and add it here
tokens:

# A map of runtimes please check the runtimes configuration section for more details
runtimes:
  java:
    version: 11
    platform: mac
    arch: x64
  python:
    version: 3.9.4
    platform: mac
    arch: x86_64

```

### Instruments Configuration
TBA

### Runtimes Configuration
Currently, Voyenv only supports adding Java and Python runtime environments.

The options you have to configure the two runtimes are:
```yaml
    # get java versions from https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/tag/jdk-11.0.11%2B9_openj9-0.26.0 or api from https://api.adoptopenjdk.net/q/swagger-ui/#/Assets/searchReleases
    # getting java options by calling links like https://api.adoptopenjdk.net/v3/assets/feature_releases/11/ga?architecture=x64&jvm_impl=hotspot&os=macos
    java:
        version: 11.1
        platform: windows | linux | mac
        arch: x86 | x64

    # get python runtimes from https://github.com/indygreg/python-build-standalone/releases
    python:
        version: 3.9.1
        platform: windows | linux | mac
        arch: x86 | x64 | ...
    
```

## Official Instruments
This is a list of official Voyager instruments that the dxworks team recognizes.
### Software Metadata
#### [Git Log](https://github.com/dxworks/voyager-git-log-instrument)
```yaml
  - name: dxworks/voyager-git-log-instrument
    tag: v1.0.0
    asset: git-log.zip
```
#### [Iglog](https://github.com/dxworks/inspector-git)
```yaml
  - name: dxworks/inspector-git
    asset: iglog.zip
```

### Code Analyzers
#### [Insider](https://github.com/dxworks/insider)
```yaml
  - name: dxworks/insider
    asset: insider-voyager.zip
```

#### [DuDe](https://github.com/dxworks/dude)
```yaml
  - name: dxworks/dude
    asset: dude-voyager.zip
```

#### [Lizard](https://github.com/dxworks/lizard)
```yaml
  - name: dxworks/lizard
#    tag: v1.17.9-voyager
```

#### [Metrix++](https://github.com/dxworks/metrixplusplus)
```yaml
  - name: dxworks/metrixplusplus
#    tag: v1.7.1-voyager
```

### Fact Extractors
#### [Honeydew](https://github.com/dxworks/honeydew)
```yaml
  - name: dxworks/honeydew
    asset: honeydew.zip
```

#### [JaFaX](https://github.com/dxworks/jafax)
```yaml
  - name: dxworks/jafax
    asset: jafax.zip
```

### Library Extractors
#### [MaMi](https://github.com/dxworks/maven-miner)
```yaml
  - name: dxworks/maven-miner
    asset: maven-miner-voyager.zip
```

#### [NoMi](https://github.com/dxworks/npm-miner)
```yaml
  - name: dxworks/npm-miner
    asset: npm-miner-voyager.zip
```

## Acknowledgement
Thanks to the [jdeploy](https://github.com/shannah/jdeploy) for allowing us to easily deploy to npm.

## License
The current software is distributed under the Apache License 2.0
