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
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecOperations;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@CacheableTask
public abstract class AmmoniteCodeGenerator extends DefaultTask {
    @Inject
    public abstract ExecOperations getExecOperations();

    @Classpath
    abstract ConfigurableFileCollection getClasspath();

    @InputFile
    abstract RegularFileProperty getScriptFile();

    @OutputDirectory
    abstract DirectoryProperty getOutputDirectory();

    @TaskAction
    public void generateSources() throws IOException {
        File inputFile = getScriptFile().getAsFile().get();
        File outputDir = getOutputDirectory().getAsFile().get();
        if (outputDir.exists()) {
            recurseDelete(outputDir.toPath());
        }
        getExecOperations().javaexec(spec -> {
            spec.classpath(getClasspath().getAsFileTree().getSingleFile());
            spec.args(inputFile.getAbsolutePath());
        });
    }

    private static void recurseDelete(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
