# Introduction #

This is an eclipse plugin for android automation.

# Features #

  * cross app recording and replaying
  * support nearly all kinds of input.eg:shake


# Usage #

we currently do not support assertion,so you can't do traditional automation tests using nior. However, if you want to take screenshot or grab packets when you test some feature of your app, this process can all be done on your device,and you want to repeat this for a bunch of times to get an average result. This will be your choice~

# Getting Started #

## Installation ##

Install this plugin from your eclipse. the update site is:
https://nior.googlecode.com/svn/trunk/eclipse/sites/internal

## Prerequisites ##

  * Android Eclipse plugin—Android Development Tools(ADT) is installed
  * your android device is connected to your pc using USB


## Record ##

After the plugin is installed。
  1. right click at wherever you want to add a record in your project. choose "new"->"other"->"Nior Record&Replay File"->"next"
  1. type a file name like "newRecord"
  1. choose the device you want to use,then click "record"
  1. do the operations on the device you selected just now
  1. click "stop" button on eclipse when you finished your operations
  1. click "finish" and a file named "newRecord.nior" will be created at the position you were earlier

## Replay ##

After you have a least one record file
  1. right click a record file(file extension is nior)
  1. click "Nior"->"replay"
  1. enter a number how many times you want to replay your record
  1. click "OK" and you will see your device replays as you orderd