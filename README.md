# hytale-plugin-template-commands

## Overview

Command registration patterns, argument handling, and status/reporting commands. This repository is a practical starting point for a Hytale plugin.

## Main entrypoint

- Main class from manifest.json: net.hytaledepot.templates.plugin.commands.CommandsPluginTemplate
- Includes asset pack: false

## Source layout

- Java sources: src/main/java
- Manifest: src/main/resources/manifest.json
- Runtime jar output: build/libs/hytale-plugin-template-commands-1.0.0.jar

## Key classes

- CommandsDemoCommand
- CommandsDemoService
- CommandsPluginLifecycle
- CommandsPluginState
- CommandsPluginTemplate
- CommandsStatusCommand

## Commands

- /hdcommandsdemo
- /hdcommandsstatus

## Build

1. Ensure the server jar is available in one of these locations:
   - HYTALE_SERVER_JAR
   - HYTALE_HOME/install/$patchline/package/game/latest/Server/HytaleServer.jar
   - workspace root HytaleServer.jar
   - libs/HytaleServer.jar
2. Run: ./gradlew clean build
3. Copy build/libs/hytale-plugin-template-commands-1.0.0.jar into your server mods/ folder.

## License

MIT
