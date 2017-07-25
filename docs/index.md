Box-AndroidLibrary

Sample

Sample & .aar file Note

depends on Google's appcompat-v7 for this for 25.3.0 version

Using

First of all you have to upload animation submodule with git submodule update --init command 

Or you can add gradle dependency with command :

Gradle

dependencies {
	compile 'me.box.library:boxlibrary:1.1.1'
}
To add gradle dependency you need to open build.gradle (in your app folder,not in a project folder) then copy and add the dependencies there in the dependencies block;

Maven

<dependency>
  <groupId>me.box.library</groupId>
  <artifactId>boxlibrary</artifactId>
  <version>1.1.1</version>
  <type>pom</type>
</dependency>
lvy

<dependency org='me.box.library' name='boxlibrary' rev='1.1.1'>
  <artifact name='boxlibrary' ext='pom' ></artifact>
</dependency>
Example

Activity

public abstract class BaseActivity extends IBaseActivity {

    @Override
    public void onUserCreateViews(Bundle savedInstanceState) {
    }

    public TestApplication getIApplication() {
        return (TestApplication) getApplication();
    }
}
Fragment

public abstract class BaseFragment extends IBaseFragment {

    public TestApplication getIApplication() {
        return (TestApplication) getApplication();
    }

}
