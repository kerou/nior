本插件支持：
  1. 通过对操作录像来进行操作时间的分析。
  1. 通过tcpdump对网络包数据的抓取。
  1. 内存监控的数据抓取。

# 操作录像分析 #
## 前提步骤： ##

  1. 安装Screencast Video Recorder，建议2.4版本。[点击下载](http://nior.googlecode.com/files/Media%20Solutions%20Screencast%20v2.4%20Android%20XDA-CN.apk)

## 执行要点： ##

  1. 在浏览器打开网页之前，切换到screencast软件，启动屏幕录制
  1. 浏览器访问完网页之后，停止屏幕录制。

## 数据分析： ##
设定Flag图片。
规则：
如果当前屏幕第一次包含start图片, 则表示页面开始加载
如果当前页面不包含blank图片, 并且已经不包含开始标志图片, 则说明开始有文字出现了
——blank图片的规则，在start之后一直存在，但是出现首文字以后就没有了的图片
出现文字后，如果当前屏幕变得不包含end标志图片, 则说明首屏加载完毕
一般情况：
start  开始加载时的上半部分截图
blank  右上角空白截图
end    右下角或者左下角空白截图

# TCPDUMP #

## 前提步骤： ##
安装Android Nior [点击下载](http://nior.googlecode.com/files/AndroidNoir.apk)

## 执行要点： ##

  1. 在浏览器打开网页之前，切换到Android Nior软件，启动数据包抓取
  1. 浏览器访问完网页之后，停止数据包抓取。
  1. 由插件完成数据包拉取和分析