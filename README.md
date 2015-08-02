Material Code input
-----------------
[![Download](https://api.bintray.com/packages/glomadrian/maven/MaterialCodeInput/images/download.svg) ](https://bintray.com/glomadrian/maven/MaterialCodeInput/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Material%20Code%20Input-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2221)

A material style input for put codes

![Demo Screenshot][1]

![Demo Screenshot][2]


Based on
----------

[Code input field concept](http://www.materialup.com/posts/code-input-field-concept) by [SAMUEL KANTALA](http://www.materialup.com/ontidop)


How to use
----------

Minimal SDK Version 11

Usage with default colors (the default codes is 6)

```xml
  <com.github.glomadrian.codeinputlib.CodeInput
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      app:hint_text="Pairing code"
      />
```

Usage with custom colors and attributes

```xml
<com.github.glomadrian.codeinputlib.CodeInput
    android:layout_marginTop="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:underline_color="#457ad1"
    app:underline_selected_color="#9e1ace"
    app:text_color="#b12eff"
    app:hint_color="#77ce9d"
    app:hint_text="Pin code"
    app:codes="4"
    />
```

Remember put this for custom attribute usage

```java

xmlns:app="http://schemas.android.com/apk/res-auto"

```

Get the input code (Returns a Character[])

```java
  codeInput.getCode()
```

For Gradle
---------------------

Add repository

```java
repositories {
  maven {
    url "http://dl.bintray.com/glomadrian/maven"
  }
}
```
Add dependency
```java
  compile 'com.github.glomadrian:CodeInput:1.1@aar'
```


Developed By
------------
Adrián García Lomas - <glomadrian@gmail.com>
* [Twitter](https://twitter.com/glomadrian)
* [LinkedIn](https://es.linkedin.com/in/glomadrian )

License
-------

    Copyright 2015 Adrián García Lomas

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: ./art/codeInput1.gif
[2]: ./art/codeInput2.gif
