# 介绍 #

本页面介绍工程的总体结构，并且告诉你如何搭建开发环境

# 工程目录结构 #

下载完主干代码后，应该能看到一下三个子目录:
  * eclipse： Noir的eclipse插件工程，用来实现在eclipse上进行录制和回放操作
  * Noirdroid: Noir的android app工程，开发中
  * web：Noir的web系统，包括远程控制等功能

# 配置开发环境 #
  * eclipse：
    * 下载 [Eclipse for RCP/Plug-in Developers](http://www.eclipse.org/downloads/packages/eclipse-rcpplug-developers/galileosr2)
    * 导入工程
  * Noirdroid： 正常在eclipse导入工程
  * web：
    * 下载 [maven3](http://www.apache.org/dyn/closer.cgi?path=/maven/binaries/apache-maven-3.0.4-bin.zip)。我们用maven来进行构建
    * 建议在eclipse中安装 m2eclipse。 插件地址：http://download.eclipse.org/technology/m2e/releases
    * 执行bin目录中的eclipse.bat
    * eclipse 导入工程