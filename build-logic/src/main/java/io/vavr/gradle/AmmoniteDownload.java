/*
 * Copyright 2003-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@CacheableTask
public abstract class AmmoniteDownload extends DefaultTask {
    @Nested
    abstract Property<AmmoniteExtension> getExtension();

    @OutputDirectory
    abstract DirectoryProperty getOutputDirectory();

    @TaskAction
    void downloadFrom() {
        AmmoniteExtension ammoniteExtension = getExtension().get();
        File outputDir = getOutputDirectory().getAsFile().get();
        File finalDestination = new File(outputDir, jarFileNameFrom(ammoniteExtension));
        if (finalDestination.exists()) {
            // Downloaded already, task is not up-to-date because of Gradle version change
            return;
        }
        URL url = generateDownloadUrl(ammoniteExtension).get();
        try {
            // We use a temporary file in order to avoid broken downloads
            // for example if the user interrupts the build during download
            File tmpFile = downloadFrom(url);
            if (outputDir.exists() || outputDir.mkdirs()) {
                Files.copy(tmpFile.toPath(), finalDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String jarFileNameFrom(AmmoniteExtension ammoniteExtension) {
        return "ammonite-" + ammoniteExtension.getScalaVersion().get() + "-" + ammoniteExtension.getAmmoniteVersion().get() + ".jar";
    }

    private File downloadFrom(URL url) throws IOException {
        getLogger().info("Downloading " + url);
        UUID uuid = UUID.randomUUID();
        File tmpFile = new File(getTemporaryDir(), "ammonite-" + uuid + ".jar");
        ReadableByteChannel readChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOS = new FileOutputStream(tmpFile);
        FileChannel writeChannel = fileOS.getChannel();
        writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
        return tmpFile;
    }

    private Provider<URL> generateDownloadUrl(AmmoniteExtension ammoniteExtension) {
        return ammoniteExtension.getAmmoniteVersion()
                .zip(ammoniteExtension.getScalaVersion(), (version, scalaVersion) -> {
                    try {
                        return new URL("https://github.com/com-lihaoyi/Ammonite/releases/download/" + version + "/" + scalaVersion + "-" + version);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
