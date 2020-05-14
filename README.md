
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

<p align="center">Simple and open-sourced Jetbrains IDE plugin that your waifu motivates you and helps you work on your coding challenges.</p>

> *Waifu* is a term for a fictional character, usually in anime or related media, that someone has great, and sometimes romantic, affection for. [source](https://www.dictionary.com/e/fictional-characters/waifu/)


## Waifu of the Day
Why need a Tip of the day when you can have Waifu of the Day. This feature replaces the out of the box Tip of the Day of Intellij and shows some waifu!

<p align="center">
  <img src="screenshot/waifu_of_the_day.png" alt="Waifu of the Day">
</p>

*It only shows up once a day at the project startup and you can view it at the 'Waifu Motivator' tool menu.*

## Waifu Unit Tester
Your waifu motivates you whenever a test passes or fails. 

**Test Pass**
![Test Pass](screenshot/test_pass.png)

*When a test passes it'll rejoice with you together with their voice/sound.*

**Test Fails**
![Test Fails](screenshot/test_fail.png)

*When a test fails it'll cheer you up for you to not get demotivated*

## Waifu Motivation On-demand
Whenever you feel demotivated on your task, you can request a motivation that randomly plays a sound and a notification to cheer you up.

<p align="center">
  <img src="screenshot/motivate_me.png" alt="Waifu Motivation On-demandy">
</p>

## Waifu Settings
Of course, not all the time you're okay with your waifu, there are bad times after all and sometimes you'll get annoyed by them and not wanting to hear anything from them. You can configure it through the settings (`Settings > Other Settings > Waifu Motivator`).

![Waifu Settings](screenshot/waifu_motivator_settings.png)

# Getting Started
If you'd like to use it to your Intellij IDE or run it to your local machine for development, please see the following sections.

## Usage
To install the plugin, go to `Settings > Plugins > Marketplace Tab > Search for 'Waifu Motivator'`.
![Waifu Motivation On-demand](screenshot/plugin_installation.png)

## Contributing
### Prerequisites
* JDK 8+
* Intellij IDEA
* Plugin DevKit 
* Lombok Plugin

### Running
Execute the `intellij/runIde` task from gradle. 
```
./gradlew runIde
```
This will open up a new Intellij IDE that the plugin is installed.

### Plugin Image
<img src="images/wmp_logo.png" height="144px" alt="Waifu Motivator Plugin Logo">

Art by [@gweninja](https://www.instagram.com/gweninja/)

## License
This project is licensed under MIT License - see [LICENSE](./LICENSE) file for details.

## Disclaimer
There are resources used in the [sound](./src/main/resources/sound) directory that is pulled from the internet randomly and most of it is clipped, if you own it please email me so that I could remove it here.

