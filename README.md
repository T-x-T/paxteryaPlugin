# paxteryaPlugin
Custom plugin to integrate the Paxterya Minecraft server with our backend services and add simple QOL features

## Features

- Fetches roles and suffixes from [paxterya backend server](https://gitlab.com/paxterya/txt-bot-and-server), gives corresponding permissions and displays the role in scoreboard

- Adds private messages and group messages

- Adds an automatic afk feature

- Can replace specific words in chat messages with custom game information

## Gradle tasks

`gradlew build` to run tests and generate the plugin jar in `build/libs/`

`gradlew test` to run tests
