# Box-AndroidLibrary

Sample
======
<a href="https://bintray.com/cbox/maven/download_file?file_path=me%2Fbox%2Flibrary%2Fboxlibrary%2F1.0.1%2Fboxlibrary-1.0.1.aar"> Sample & .aar file </a>

Using
======
First of all you have to upload animation submodule with `git submodule update --init` command <br>
<br>
Or you can add gradle dependency with command :<br>

#### Gradle
```groovy
dependencies {
	compile 'me.box.library:boxlibrary:1.0.1'
}
```

To add gradle dependency you need to open  build.gradle (in your app folder,not in a project folder) then copy and add the dependencies there in the dependencies block;

#### Maven
```xml
<dependency>
  <groupId>me.box.library</groupId>
  <artifactId>boxlibrary</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

#### lvy
```xml
<dependency org='me.box.library' name='boxlibrary' rev='1.0.1'>
  <artifact name='boxlibrary' ext='pom' ></artifact>
</dependency>
```

Example
========

#### Activity
```java
public abstract class BaseActivity extends IBaseActivity {

    @Override
    public void onUserCreateViews(Bundle savedInstanceState) {
    }

    public TestApplication getIApplication() {
        return (TestApplication) getApplication();
    }
}
```

#### Fragment
```java
public abstract class BaseFragment extends IBaseFragment {

    public TestApplication getIApplication() {
        return (TestApplication) getApplication();
    }

}
```

## License
    Copyright 2017, changlei

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
