
<p align="center"><img src="images/wmp_logo.png" height="424px" alt="Waifu Motivator Plugin Logo"></p>
<h2 align="center">Waifu Motivator Plugin</h2>

<!--suppress HtmlDeprecatedAttribute, HtmlRequiredAltAttribute -->
<p align="center">
    <a href="https://github.com/zd-zero/waifu-motivator-plugin/actions"><img src="https://github.com/zd-zero/waifu-motivator-plugin/workflows/Java%20CI/badge.svg"></a>
  <a href="https://plugins.jetbrains.com/plugin/13381-waifu-motivator"><img alt="JetBrains IntelliJ Plugins" src="https://img.shields.io/jetbrains/plugin/v/13381-waifu-motivator"></a>
  <a href="./LICENSE"><img src="https://img.shields.io/github/license/zd-zero/waifu-motivator-plugin"></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/BUILT%20WITH-COFFEE-blue?style=for-the-badge">
</p>

<p align="center">An open-sourced Jetbrains IDE plugin that brings in your <i>Waifu</i> to help motivate you to complete your coding challenges.</p>

**What is a Waifu?** 🤷

> *Waifu* is a term for a fictional character, usually in anime or related media, that someone has great, and sometimes romantic, affection for. [source](https://www.dictionary.com/e/fictional-characters/waifu/)

## Usage
To install the plugin, go to `Settings | Plugins | Marketplace Tab | Search for 'Waifu Motivator'`. Or you can install it from [here](https://plugins.jetbrains.com/plugin/13381-waifu-motivator).

![Waifu Motivation On-demand](screenshots/plugin_installation.png)

## Waifu of the Day
Why do you need the _Tip of the Day_, when instead you can have the _Waifu of the Day_?
This feature replaces the out of the box Tip of the Day of IntelliJ with a top-tier *Waifu*!

<p align="center">
  <img src="screenshots/waifu_of_the_day.png" alt="Waifu of the Day">
</p>

*It only shows up once a day at the project startup, and you can also view it at the 'Waifu Motivator' tool menu.*

## Motivation Events

Your virtual companion has the ability to react to various events trigger by your IDE.
As you build your code, they will present various visual notifications of their reactions.

### Starting Up
Your *Waifu* loves to welcome you, make sure to keep coming back!
![Waifu Startup Motivation](screenshots/waifu_welcome_demo.gif)

### Test Runs

Your *Waifu* will motivate you whenever your tests pass or fail.

**Test Pass**
![Test Pass](screenshots/waifu_unit_test_pass.gif)

*When a test passes your companion will rejoice with you together with their voice/sound.*

**Test Fails**
![Test Fails](screenshots/waifu_unit_test_fail.gif)

*When a test fails they will cheer you up, so you can overcome your challenges/frustrations.*
_Also you don't want to disappoint you waifu, now do you?_

## Motivation On-demand
Whenever you feel demotivated on your task, you can request a motivation.
Your companion will randomly play a sound and show a *Waifu* to cheer you up.
You can invoke it on `Help | Waifu Motivator | Motivate Me` or the shortcut key with (`alt + M` for Windows/Linux and `option + M` for MacOs).

<p align="center">
  <img src="screenshots/motivate_me.png" alt="Waifu Motivation On-demand">
</p>

## Plugin Settings
Of course, not all the time you're okay with your *Waifu*, there are bad times after all and sometimes you'll get annoyed by them and not wanting to hear anything from them. You can configure it through the settings (`Settings | Other Settings | Waifu Motivator`).

![Waifu Settings](screenshots/waifu_motivator_settings.png)

# Development

## Getting Started

If want to run it to your local machine for development, please see the following sections.

## Contributing
### Prerequisites
* JDK 8+
* IntelliJ IDEA
* Plugin DevKit
* Lombok Plugin

### Running
Execute the `intellij/runIde` task from Gradle.
```
./gradlew runIde
```
This will fire up a new IntelliJ IDE instance with the plugin already installed.

### Plugin Image
<img src="images/wmp_logo.png" height="144px" alt="Waifu Motivator Plugin Logo">

Art by [@gweninja](https://www.instagram.com/gweninja/)

## License
The license of this project is under MIT License - see [LICENSE](./LICENSE) file for details.

## Disclaimer
There are resources used in the [sound](./src/main/resources/sound) directory that is pulled randomly from the internet, I do *not* own it as they are clipped/trimmed, if you own it please contact me so that I could remove it from here.
