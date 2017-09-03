@REM This is a one time execution to create multiple Eclipse projects from generated andromda output.
@REM Comment out eclipse goal under plugins\plugin\org.andromda.maven.plugins\andromdapp-maven-plugin.
@REM Reference http://forum.andromda.org/viewtopic.php?t=5066
@REM Maven eclipse plugin reference: http://maven.apache.org/plugins/maven-eclipse-plugin
@REM To run with the original AndroMDA eclipse functionality (generate .project and .classpath each MDA
@REM run, for the top level project only):

@REM Comment the following in the top level.pom.xml
@REM            <plugin>
@REM                <groupId>org.apache.maven.plugins</groupId>
@REM                <artifactId>maven-eclipse-plugin</artifactId>
@REM                <version>2.4</version>
@REM                <inherited>true</inherited>
@REM                <configuration>
@REM                    <wtpversion>1.5</wtpversion>
@REM                    <buildOutputDirectory>target/classes</buildOutputDirectory>
@REM                </configuration>
@REM            </plugin>

@REM Uncomment out the following in mda/pom.xml
@REM            <plugin>
@REM                <groupId>org.andromda.maven.plugins</groupId>
@REM                <artifactId>andromdapp-maven-plugin</artifactId>
@REM                <executions>
@REM                    <execution>
@REM                        <goals>
@REM                            <goal>eclipse</goal>
@REM                        </goals>
@REM                    </execution>
@REM                </executions>
@REM                <configuration>
@REM                    <excludes>
@REM                        <exclude>app/pom.xml</exclude>
@REM                        <exclude>mda/pom.xml</exclude>
@REM                        <exclude>webservice/pom.xml</exclude>
@REM                    </excludes>
@REM                </configuration>
@REM            </plugin>

@REM Import all projects into Eclipse when finished

@REM This manages all dependencies from within pom.xml files, re-run if dependencies change.
@REM If the Eclipse workspace dir is the same as the top level project dir, projects
@REM will not have appname- in front of project name.

@REM App generator does not copy empty directories into project, but they must exist for eclipse plugin to work
mkdir common\src\main\java
mkdir common\target\src\main\java

mkdir core\src\main\java
mkdir core\src\test\java
mkdir core\target\src\main\java

mkdir web\src\main\java
mkdir web\target\src\main\java

rename ..\.project old.project
rename ..\.classpath old.classpath

call mvn eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=true

pause
