
<p align="center"><img src="images/wmp_logo.png" height="424px" alt="Waifu Motivator Plugin Logo"></p>
<h2 align="center">Waifu Motivator Plugin</h2>

<!--suppress HtmlDeprecatedAttribute, HtmlRequiredAltAttribute -->
<p align="center">
    <a href="https://github.com/waifu-motivator/waifu-motivator-plugin/actions"><img src="https://github.com/waifu-motivator/waifu-motivator-plugin/workflows/Java%20CI/badge.svg"></a>
  <a href="https://plugins.jetbrains.com/plugin/13381-waifu-motivator"><img alt="JetBrains IntelliJ Plugins" src="https://img.shields.io/jetbrains/plugin/v/13381-waifu-motivator"></a>
  <a href="./LICENSE"><img src="https://img.shields.io/github/license/waifu-motivator/waifu-motivator-plugin"></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/BUILT%20WITH-COFFEE-blue?style=for-the-badge">
</p>

<p align="center">A collection of open-sourced Jetbrains IDE plugins that bring <i>Waifus</i> in to help keep your motivation to complete during your coding challenges.</p>

**What is a Waifu?** 🤷

> *Waifu* is a term for a fictional character, usually in anime or related media, that someone has great, and sometimes romantic, affection for. [source](https://www.dictionary.com/e/fictional-characters/waifu/)

## Installation
To install the plugin, go to `Settings | Plugins | Marketplace Tab | Search for 'Waifu Motivator'`.
You can install it from [the plugin marketplace.](https://plugins.jetbrains.com/plugin/13381-waifu-motivator)

![Waifu plugin installation](screenshots/plugin_installation.png)

## Configuration

To configure the plugin, go to `Settings | Other Settings | Waifu Motivator`

## Waifu of the Day
Why would you want the _Tip of the Day_, when instead you can have a _Waifu of the Day_?
This feature replaces the out-of-the-box Tip of the Day feature of IntelliJ with a top-tier *Waifu*!

<p align="center">
  <img src="screenshots/waifu_of_the_day.png" alt="Waifu of the Day">
</p>

*This only shows up once a day at the project startup, and you can also view it at the 'Waifu Motivator' tool menu.*

## Anime Memes (AMII)

<p align="center">
  <img src="https://raw.githubusercontent.com/Unthrottled/AMII/main/readmeAssets/exit_code.gif" alt="Waifu of the Day">
</p>

Give your IDE more personality and have <emphasis>more</emphasis> fun programming with the **A**nime **M**eme **I**DE **I**ntegration! (AMII)<br/><br/>
Upon installation, our Meme Inference Knowledge Unit (or MIKU for short)
will begin interact with you as you build code.
MIKU knows when your programs fail to run or tests pass/fail.
Your new companion has the ability to react to these events.
Which will most likely take the form of an anime meme of your: waifu, husbando, and/or favorite character(s)!<br/><br/>

Please see [AMII's feature documentation](https://github.com/Unthrottled/AMII#documentation) for more information.

## Doki-Theme Integration

You can also install [Doki Theme](https://github.com/doki-theme/doki-theme-jetbrains) to have a better *Waifu* development experience!

![Doki Theme](screenshots/doki-theme.gif)

# Development

## Getting Started

If want to run it on your local machine for development, please see the following sections.

## Contributing
### Prerequisites
* JDK 11+
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

# Attributions

Project uses icons from [Twemoji](https://github.com/twitter/twemoji).
Graphics licensed under CC-BY 4.0: https://creativecommons.org/licenses/by/4.0/

## Disclaimer
There are resources used in the [sound](./src/main/resources/sound) directory that is pulled randomly from the internet, I do *not* own it as they are clipped/trimmed, if you own it please contact me so that I could remove it from here.
