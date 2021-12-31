## Android Studio Plugin 替换文件名插件

## 功能说明
> 对安卓工程选中目录下，文件以及文件夹，进行搜索并且替换名称

![20211230165902.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230165902.png)

![20211230164607.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230164607.png)

## 仓库地址
[GitHub](https://github.com/cnoke/replacefilename-studio-plugin.git)

# 开发流程
## 1. 初始化工程
1. 打开IntelliJ IDEA ，点击New Project
2. 左侧选择Gradle类型，右侧指定JDK版本为1.8，勾选Java，Intellij Platform Plugin和Kotlin/JVM(不需要Kotlin开发就无需勾选)，点击Next

![20211230165123.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230165123.png)

3. 输入工程名称changeFileName，点击Next

![20211230165321.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230165321.png)
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

![20211230171810.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230171810.png)

### 2. 修改plugin.xml文件
- 修改\<id>唯一标识、\<name>插件名称、\<vendor>开发者信息、\<description>插件详情
- 在\<depends>标签下新增：
```xml
<depends>com.intellij.modules.platform</depends>
<depends>org.jetbrains.android</depends> // AS相关
```
### 3. 创建包结构
在/src/main/下面创建java文件夹。添加包com.cnoke.changefile
## 3. 同步工程
点击Gradle Sync同步工程, 同步由于要下载 build.gradle中配置的 intellij version '2020.3'。时间可能比较久

## 4. 创建菜单项（Action）

1. 在com.cnoke.changefile文件夹上右键，选择New > Plugin Devkit > Action  

   ![20211230214451.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230214451.png)

2. 填入id calss Name等信息，Class Name是自动生成的类名。Name插件名。Group是插件入口，本插件需求是选择目录后修改文件名。因此这边选择ProjectViewPopuoMenu放在Replace File后面

   ![20211230220442.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230220442.png)                                       

生成如下Action代码

![20211230221040.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230221040.png)

在project目录上鼠标右键，点击（替换文件名）会调用上述ChangeFileNameAction中的actionPerformed。

因此我们在此方法中弹出搜索文件替换文件的Dialog

## 5. 创建Dialog

1. 创建view文件夹，在view文件夹上右键，选择New > Swing UI Designer > Create Dialog Class

   ![20211230221717.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230221717.png)

2. 输入类名，取消勾选项目，点击OK

   ![20211230222440.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230222440.png)

3. 生成from文件，和Dialog.java文件。（java改为kt文件，可不改）

4. 将生成的DIalog改为DialogWrapper其他代码删除

   ![20211230222937.png](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230222937.png)

5. 点击from，修改布局。点击palette中元素放入布局中

   ![](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230223619.png)

6. 修改Dialog.kt中代码获取布局中对于控件。布局中的field name和kt中字段要一致。并且kt中字段必须public可修改。swing会自动把from中控件实例化给kt中字段。

   ![](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230223955.png)

   ![](https://gitee.com/cnoke_301/readmeimg/raw/master/replacefilename-studio-plugin/20211230224154.png)

7. 将from中父布局给kt中createCenterPanel() return

```kotlin
   override fun createCenterPanel(): JComponent? {
           title = "工具"
           setOKButtonText("替换")
           setCancelButtonText("取消")
           scrollPane!!.viewport.view = mapperList
           return contentPane
       }
   ```

## 6. 搜索目录下文件

 ```kotlin
 VfsUtilCore.iterateChildrenRecursively(it, { filter->
   !filterFolder(filter)
}, { content->
   if(content.name.contains(searchText)){
      externalFiles.add(content)
      showTexts.add( "${content.path} → ${content.name}")
   }
   true
})  
```

## 7.修改文件名

```kotlin
 VirtualFile.rename()
```

   
