## Android Studio Plugin 替换文件名插件

## 功能说明
> 对安卓工程选中目录下，文件以及文件夹，进行搜索并且替换名称

![20211230165902.png](https://raw.githubusercontent.com/cnoke/readmeimg/main/replacefilename-studio-plugin/20211230165902.png)

![20211230164607.png](https://raw.githubusercontent.com/cnoke/readmeimg/main/replacefilename-studio-plugin/20211230164607.png)

## 仓库地址
[GitHub](https://github.com/cnoke/replacefilename-studio-plugin.git)

# 开发流程
## 1. 初始化工程
1. 打开IntelliJ IDEA ，点击New Project
2. 左侧选择Gradle类型，右侧指定JDK版本为1.8，勾选Java，Intellij Platform Plugin和Kotlin/JVM，点击Next

![20211230165123.png](https://raw.githubusercontent.com/cnoke/readmeimg/main/replacefilename-studio-plugin/20211230165123.png)
3. 输入工程名称changeFileName，点击Next

![20211230165321.png](https://raw.githubusercontent.com/cnoke/readmeimg/main/replacefilename-studio-plugin/20211230165321.png)
## 2. 修改配置信息
### 1. 修改build.gradle文件
```gradle
plugins {
    id 'java'
    id 'org.jetbrains.intellij' version '0.4.15'
    id 'org.jetbrains.kotlin.jvm' version '1.3.72'
}

version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8" //kotlin引入
}

intellij {
    version '2020.3' //根据Android studio版本设置 参考 https://developer.android.google.cn/studio/releases?hl=zh-cn
    plugins = ['android']
}

compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
patchPluginXml {
    changeNotes "一键修改文件名"
    sinceBuild = '191' // 插件适用的IDEA版本范围
    untilBuild = '212.*'
}
```
intellij version根据Android studio配置
参考[android](https://developer.android.google.cn/studio/releases?hl=zh-cn)

![20211230171810.png](https://raw.githubusercontent.com/cnoke/readmeimg/main/replacefilename-studio-plugin/20211230171810.png)

### 2. 修改plugin.xml文件
- 修改\<id>唯一标识、\<name>插件名称、\<vendor>开发者信息、\<description>插件详情
- 在\<depends>标签下新增：
```xml
<depends>com.intellij.modules.platform</depends>
<depends>org.jetbrains.android</depends> // AS相关
```
### 3. 创建包结构
在/src/main/下面创建java文件夹。添加包com.cnoke.changefile
## 2. 同步工程
点击Gradle Sync同步工程, 同步由于要下载 build.gradle中配置的 intellij version '2020.3'。时间可能比较久


                                               
