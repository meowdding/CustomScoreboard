# Contributing to CustomScoreboard

If you have questions, join our [Discord](https://meowdd.ing/discord).

## Prerequisites

Basic Java/Kotlin knowledge is required.

- **IDE**: Use [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
- **JDK**: Install [JDK 25](https://jdk.java.net/archive/)
- **Optional Intellij Plugins:**
    - [Minecraft Development](https://plugins.jetbrains.com/plugin/8327-minecraft-development)
    - [Stonecutter Dev](https://plugins.jetbrains.com/plugin/25044-stonecutter-dev)

## Setting Up

1. Clone the repository
2. Open the project in IntelliJ
3. Set up JDK 25 in Project Settings

## Writing Code & Booting up the Game

To support multiple Minecraft versions at once, we use [StoneCutter](https://stonecutter.kikugie.dev/), a lib for Minecraft Multiversion.
Code shared between the Minecraft versions is in src/main,
version specific code in src/\<version> but can also be in main with preprocessed comments.

To learn how to use Stonecutter, please reference the [Docs](https://stonecutter.kikugie.dev/wiki/v2/reference/) or the existing code.

## Dependencies

Meowdding mainly uses these custom libraries:

### [SkyBlockAPI](https://github.com/SkyBlockAPI/SkyblockAPI)

Handles SkyBlock features:

- API's like BazaarAPI, CurrencyAPI, SlayerAPI, etc.
- Other SkyBlock utilities

### [MeowddingLib](https://github.com/meowdding/meowdding-lib)

Provides:

- Rendering systems (Displays, Layouts)
- Common utilities

## Adding Features

If an API feature is missing:

1. Implement it temporarily in your feature mod
2. Test if it works
3. Open a PR to the appropriate library

## Custom Scoreboard Structure

```
├── src/
│   ├── lang/                                   # Language Files
│   └── main/                                   # Contains Checkstyle configurations
│       ├── java                                # Java Code Like Mixins
│       ├── kotlin                              # The main mod
│       │   └── me.owdding.customscoreboard
│       │       ├── compat                      # For Compatability with other Mods
│       │       ├── config                      # The config of the Mod
│       │       ├── core                        # Everything related to rendering etc.
│       │       ├── elements                    # The main appearance of the scoreboard
│       │       ├── events                      # The event specific lines
│       │       ├── utils                       # Utilities needed for rendering etc.
│       │       └── CustomScoreboardMod.kt      # The initializer of the mod
│       └── resources                           # Assets etc.
│
│                                               # Version Specific Code
└── versions/\[version]/src/main/kotlin/me/owdding/customscoreboard
```
