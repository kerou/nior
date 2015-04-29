# 简介 #

这是一个用于android自动化测试方向的eclipse插件.欢迎反馈及提出您的想法。请加入[论坛](https://groups.google.com/forum/#!forum/nior)

# 特性 #

  * 跨应用录制和回放
  * 支持几乎所有操作，比如：摇一摇


# 用途 #

我们目前暂时不支持断言，因此不能用来做传统的自动化测试。不过，如果你需要做不需要断言的跨应用评测，这将是一个不错的选择。比如：你需要打开你的应用，在进行某个操作的时候需要利用已装的app进行截图或者抓包操作。然后你需要多次重复这个过程已拿到多次的数据。这个时候就可以考虑使用nior-eclipse

# 开始 #

## 安装 ##

从eclipse中安装这个插件，插件的安装地址是：
http://nior.googlecode.com/svn/trunk/eclipse/sites/internal

## 前提 ##

  * Android Eclipse plugin—Android Development Tools(ADT) 已经安装
  * 设备与pc通过usb连接完毕


## 录制 ##

安装完插件后，进行一下操作即可录制。
  1. 在eclipse中新建录制文件.操作路径“File”->"new"->"other"->"Nior Record&Replay File"->"next"
  1. 输入文件名比如 "newRecord"
  1. 选择要进行录制的设备，然后点击“录制”按钮
  1. 开始在你选择的设备上进行你的操作
  1. 在设备上结束操作后在eclipse中点击“停止”录制按钮
  1. 点击“finish”按钮即完成所有操作。这时你会看到刚刚操作的位置多了一个叫“newRecord.nior”的文件

## 回放 ##

  1. 右键你要回放的录制文件（文件名后缀为nior）
  1. 菜单中点击"Nior"->"replay"
  1. 在弹出框中输入需要回放的次数
  1. 点击“OK”完成操作。你会发现设备已经开始进行回放