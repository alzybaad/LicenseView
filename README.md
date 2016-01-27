LicenseView
===

View which shows OSS licenses in assets.

![SS1.png](https://raw.githubusercontent.com/wiki/alzybaad/LicenseView/ss1.png)

Usage
---
1. Create OSS license files
File name is OSS name, file contents is license text.

![SS1.png](https://raw.githubusercontent.com/wiki/alzybaad/LicenseView/ss2.png)

2. Save OSS license files in your `assets/license` directory

![SS1.png](https://raw.githubusercontent.com/wiki/alzybaad/LicenseView/ss3.png)

3. Create `LicenseView` and add in your `Activity` or `Fragment` etc.

XML:
```xml
<team.birdhead.widget.LicenseView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

Java:
```java
setContentView(new LicenseView(this));
```

XML Attributes
---
Name        | Description
----------- | -----------------
path        | Path of license files. If not set, read `license` directory.
list_layout | Custom layout. It must have 1 `ListView` whose id is `R.id.list`.
item_layout | Custom row layout in `ListView`. It must have 2 `TextView`s whose ids are `R.id.title` and `R.id.message`.

Download
---
Gradle:
```groovy
repositories {
    maven { url 'http://team-birdhead.github.io/maven' }
}

dependencies {
    compile 'team.birdhead.aspectratioimageview:aspectratioimageview:1.0.0'
}
```

License
---
    Copyright 2016 alzybaad

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
