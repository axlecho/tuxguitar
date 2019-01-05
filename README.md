# Tuxguitar
由tuxguitar-android项目修改而来的android控件（Activity）
用于播放GP4，GP5，GPX文件

[![](https://jitpack.io/v/axlecho/tuxguitar.svg)](https://jitpack.io/#axlecho/tuxguitar)

Usage
---

With Gradle:

*   Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
allprojects {

    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
*   Step 2. Add the dependency
```
    api 'com.github.axlecho.tuxguitar:tuxguitar-ptb:v0.1.1'
    api 'com.github.axlecho.tuxguitar:tuxguitar-gtp:v0.1.1'
    api 'com.github.axlecho.tuxguitar:tuxguitar-lib:v0.1.1'
    api 'com.github.axlecho.tuxguitar:tuxguitar-gm-utils:v0.1.1'
    api 'com.github.axlecho.tuxguitar:tuxguitar-editor-utils:v0.1.1'
    api 'com.github.axlecho.tuxguitar:tuxguitar-gpx:v0.1.1'
    api 'com.github.axlecho.tuxguitar:tuxguitar-widget:v0.1.1'
    api 'com.github.axlecho.tuxguitar:tuxguitar-android-gervill:v0.1.1'
```

*	Step 3. Start activity with file path

```java	
    Uri gtpUri = Uri.parse("file://" + filePath);
    Intent intent = new Intent();
    intent.setData(gtpUri);

    Bundle bundle = new Bundle();
    bundle.putSerializable("title", JtsTextUnitls.getFileNameFromPath(filePath));
    intent.putExtras(bundle);

    intent.setAction(Intent.ACTION_VIEW);
    intent.setClass(context, TGActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
```
