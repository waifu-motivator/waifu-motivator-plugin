# Changelog

## v1.3
## ??/09/2020

Features:

* [WMP-190](https://github.com/zd-zero/waifu-motivator-plugin/issues/190) Organized settings menu.
* [WMP-155](https://github.com/zd-zero/waifu-motivator-plugin/issues/155) When any executed process through the IDE fails with an un-allowed exit code then you get a motivation event.
* [WMP-194](https://github.com/zd-zero/waifu-motivator-plugin/issues/194) Can trigger a manual update of the list of motivation assets from the remote source.
* [WMP-182](https://github.com/zd-zero/waifu-motivator-plugin/issues/182) Added configurable personality to your virtual companion.
* [WMP-159](https://github.com/zd-zero/waifu-motivator-plugin/issues/159) Plugin can be used offline now.
* [WMP-157](https://github.com/zd-zero/waifu-motivator-plugin/issues/157) `Motivate Me` now uses visual notifications.
* [WMP-133](https://github.com/zd-zero/waifu-motivator-plugin/issues/133) When your code fails to build you'll get a `Surprised` or `Dissapointed` event. The next time that your build succeeds, then you'll get a `Celebration` or `Smug` event.
* [WMP-154](https://github.com/zd-zero/waifu-motivator-plugin/issues/154) When the user's IDE remains idle for a configured amount of time (defaults to 5 minutes) then the plugin will display a bored/waiting anime girl.

Bug Fixes:

* Fixed [WMP-153](https://github.com/zd-zero/waifu-motivator-plugin/issues/153) Successful test runs with ignored tests now emit success motivation events.
* Fixed [WMP-178](https://github.com/zd-zero/waifu-motivator-plugin/issues/178) Plugin actions can now be used while IDE is indexing.
* Fixed [WMP-186](https://github.com/zd-zero/waifu-motivator-plugin/issues/186) Can close projects before they are initialized.
* Fixed [WMP-198](https://github.com/zd-zero/waifu-motivator-plugin/issues/198) Enabled plugin to work without full IDE restart.


## v1.2
## 21/08/2020
Features:

* [WMP-097](https://github.com/zd-zero/waifu-motivator-plugin/issues/97) Ability to disable 'Motivate Me' notification
* [WMP-080](https://github.com/zd-zero/waifu-motivator-plugin/issues/80) Improvements on toolbar menu
* [WMP-103](https://github.com/zd-zero/waifu-motivator-plugin/issues/103) Ability to enable 'Do Not Disturb' mode on different viewing modes
* [WMP-102](https://github.com/zd-zero/waifu-motivator-plugin/issues/102) Welcome dialog for newly installed plugin or new version
* [WMP-100](https://github.com/zd-zero/waifu-motivator-plugin/issues/100) Add images associated to the alert notification

Bug fix:

* Fix [WMP-110](https://github.com/zd-zero/waifu-motivator-plugin/issues/110) Waifu unit tester executes multiple times
* Fix [WMP-112](https://github.com/zd-zero/waifu-motivator-plugin/issues/112) Multiple different startup sounds
* Fix [WMP-124](https://github.com/zd-zero/waifu-motivator-plugin/issues/124) Multiple test notifications
* Fix [WMP-144](https://github.com/zd-zero/waifu-motivator-plugin/issues/144) Cancelled tests registers as passed event

## [v1.1.2](https://github.com/zd-zero/waifu-motivator-plugin/releases/tag/v1.1.2)
#### 24/04/20
* Fix: WMP-083 Plugin state AssertionError
* Fix: WMP-084 Webstorm 2020.1 NoClassDefFoundError

## [v1.1.1](https://github.com/zd-zero/waifu-motivator-plugin/releases/tag/v1.1.1)
#### 18/04/20
* Fix: WMP-075 No image for Nejire Hado for Waifu of the Day

## [v1.1](https://github.com/zd-zero/waifu-motivator-plugin/releases/tag/v1.1)
#### 12/04/20
* WMP-073 Properly style Waifu of the Day based on theme
* WMP-069 Add settings navigation menu on Plugin toolbar menu
* WMP-066 Migration to dynamic plugin
* WMP-060 Support IDEA Version to 2020.1
* WMP-015 Separate settings for alert
* WMP-056 Support for playing different sound formats
* WMP-053 Global project closing sayonara
* WMP-026 Create a message bundle for WMP
* WMP-037 Create Waifu of the Day provider
* WMP-050 Update text 'source' to 'sauce
* WMP-051 Include more assets for waifu of the day and events
  * **Waifu of The Day**
    * Update Aqua image
    * Shinobu Kocho
    * Mitsuri Kanroji
    * Kanae Kocho
    * Kanao Tsuyuri
    * Toru Hagakure
    * Ochaco Uraraka
    * Kyoka Jiro
    * Nejire Hado
    * Eri
    * Himiko Toga
    * Ishtar
    * Lucy Heartfilia
    * Erza Scarlet
    * Wendy Marvell
    * Nao Tomori
    * Kotoko Iwanaga
    * Inori Yuzuriha
    * Miku Nakano
    * Ichika Nakano
    * Nino Nakano
    * Itsuki Nakano
    * Yotsuba Nakano

  * **Alert Assets**
    * Nyaaan
    * Wwwwwwaaaaaaaaaaaaaaaaaaaaah
    * Wwwwaaaaaooowww
    * OH MY GAH


## [v1.0.1](https://github.com/zd-zero/waifu-motivator-plugin/releases/tag/v1.0.1)
#### 01/01/20
* Add more assets for Waifu of the Day
* WMP-039 asset provider not properly categorizing on the next invocation
* WMP-041 update images for dark theme
* WMP-042 resolve sound stopping when the alert is expired

## [v1.0](https://github.com/zd-zero/waifu-motivator-plugin/releases/tag/v1.0)
#### 30/12/19
* WMP-008 waifu of the day
* WMP-022 random motivations on all alerts
* WMP-027 sayonara
* WMP-028 persist the consumed assets to play until all of it are consumed
* WMP-029 stop playing the audio if the alert was closed

## v0.1.1
#### 03/12/19
* Bumped IDEA for compatibility to >= 2019.3

## v0.1
#### 03/12/19
* Initial EAP Release for IDEA >= 2019.2.3
