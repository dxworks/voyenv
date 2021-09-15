## Install

To install `voyenv` run the following command using `npm`:

```shell
npm install @dx-works/voyenv -g
```

To verify that the installation has completed successfully, run:
```shell
voyenv -v
```

To see how you can use the Voyenv CLI run:
```shell
voyenv -h
```

## Configure

=== "CLI"
    Open a terminal window in a folder of your choosing and initialize a new `voyenv` project:

    ```shell
    voyenv init -cir voyenv-tutorial
    cd voyenv-tutorial
    ```

=== "Manual"
    Create a new folder where you want `voyenv` to download your voyager release. In that folder create a file
    called `voyenv.yml`
    
    Depending on your operating system, add the following contents in the `voyenv.yml` file:
    
    === "Windows"
    
        ```yaml
        name: voyager-test-1
        
        voyager_version: v1.5.0
        
        instruments:
            - name: dxworks/voyager-git-log-instrument
              asset: git-log.zip
            - name: dxworks/inspector-git
              asset: iglog.zip
            - name: dxworks/insider
              asset: insider-voyager.zip
            - name: dxworks/dude
              asset: dude-voyager.zip
            - name: dxworks/lizard
            - name: dxworks/metrixplusplus
            - name: dxworks/honeydew
              asset: honeydew.zip
            - name: dxworks/jafax
              asset: jafax.zip
            - name: dxworks/maven-miner
              asset: maven-miner-voyager.zip
            - name: dxworks/npm-miner
              asset: npm-miner-voyager.zip
        
        tokens:
        
        runtimes:
            java:
                version: 11
                platform: windows
                arch: x64
            python:
                version: 3.9.4
                platform: windows
                arch: x86_64
        ```
    
    === "Mac Os"
    
        ```yaml
        name: voyager-test-1
        
        voyager_version: v1.5.0
        
        instruments:
            - name: dxworks/voyager-git-log-instrument
              asset: git-log.zip
            - name: dxworks/inspector-git
              asset: iglog.zip
            - name: dxworks/insider
              asset: insider-voyager.zip
            - name: dxworks/dude
              asset: dude-voyager.zip
            - name: dxworks/lizard
            - name: dxworks/metrixplusplus
            - name: dxworks/honeydew
              asset: honeydew.zip
            - name: dxworks/jafax
              asset: jafax.zip
            - name: dxworks/maven-miner
              asset: maven-miner-voyager.zip
            - name: dxworks/npm-miner
              asset: npm-miner-voyager.zip
        
        tokens:
        
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
    
    === "Linux"
    
        ```yaml
        name: voyager-test-1
        
        voyager_version: v1.5.0
        
        instruments:
            - name: dxworks/voyager-git-log-instrument
              asset: git-log.zip
            - name: dxworks/inspector-git
              asset: iglog.zip
            - name: dxworks/insider
              asset: insider-voyager.zip
            - name: dxworks/dude
              asset: dude-voyager.zip
            - name: dxworks/lizard
            - name: dxworks/metrixplusplus
            - name: dxworks/honeydew
              asset: honeydew.zip
            - name: dxworks/jafax
              asset: jafax.zip
            - name: dxworks/maven-miner
              asset: maven-miner-voyager.zip
            - name: dxworks/npm-miner
              asset: npm-miner-voyager.zip
        
        tokens:
    
        runtimes:
            java:
                version: 11
                platform: linux
                arch: x64
            python:
                version: 3.9.4
                platform: linux
                arch: x86_64
        ```

## Run
Open a terminal window in the parent folder of the previously created `voyenv.yml` file and run the following command:
```shell
voyenv install
```

This should download and configure a Voyager instance with all the official Voyager instruments and with their required runtime dependencies.

## Check
To check `Voyager` was installed successfully, run the following commands:

=== "Mac/Linux"
    ```shell
    cd voyager-test-1
    ./voyager.sh --version
    ./voyager.sh doctor
    ```
=== "Windows"
    ```shell
    cd voyager-test-1
    voyager.bat --version
    voyager.bat doctor
    ```
