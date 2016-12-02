IntelliJ IDEA
-------------

The following IntelliJ IDEA settings contained:

![Export Settings](./img/idea-settings.png)

We reformat only .java files.

![Reformat Code](./img/idea-reformat-code.png)

After the code is reformatted we have to generate sources:

```bash
mvn clean package -DskipTests
```
