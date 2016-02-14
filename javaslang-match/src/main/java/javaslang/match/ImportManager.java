/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * A <em>stateful</em> ImportManager which generates an import section of a Java class file.
 */
class ImportManager {

    static final int DEFAULT_WILDCARD_THRESHOLD = 5;

    // properties
    private final String packageNameOfClass;
    private final Set<String> knownSimpleClassNames;
    private final int wildcardThreshold;

    // mutable state
    private Map<String, String> nonStaticImports = new HashMap<>();
    private Map<String, String> staticImports = new HashMap<>();

    public ImportManager(String packageNameOfClass, Set<String> knownSimpleClassNames, int wildcardThreshold) {
        this.packageNameOfClass = packageNameOfClass;
        this.knownSimpleClassNames = knownSimpleClassNames;
        this.wildcardThreshold = wildcardThreshold;
    }

    public static ImportManager of(String packageNameOfClass) {
        return new ImportManager(packageNameOfClass, Collections.emptySet(), DEFAULT_WILDCARD_THRESHOLD);
    }

    // used by generator to register non-static imports
    public String getType(String fullQualifiedName) {
        return simplify(fullQualifiedName, nonStaticImports, packageNameOfClass, knownSimpleClassNames);
    }

    // used by generator to register static imports
    public String getStatic(String fullQualifiedName) {
        return simplify(fullQualifiedName, staticImports, packageNameOfClass, knownSimpleClassNames);
    }

    // finally used by generator to get the import section
    public String getImports() {
        final String staticImportSection = optimizeImports(staticImports.keySet(), true, wildcardThreshold);
        final String nonStaticImportSection = optimizeImports(nonStaticImports.keySet(), false, wildcardThreshold);
        return staticImportSection + "\n\n" + nonStaticImportSection;
    }

    private static String optimizeImports(Set<String> imports, boolean isStatic, int wildcardThreshold) {
        final Map<String, Integer> counts = imports.stream()
                .map(ImportManager::getPackageName)
                .collect(groupingBy(s -> s))
                .entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue().size()));

        final List<String> directImports = imports.stream()
                .filter(s -> counts.get(getPackageName(s)) <= wildcardThreshold)
                .collect(toList());

        final List<String> wildcardImports = counts.entrySet().stream()
                .filter(entry -> entry.getValue() > wildcardThreshold)
                .map(entry -> entry.getKey() + ".*")
                .collect(toList());

        final List<String> result = new ArrayList<>(directImports);
        result.addAll(wildcardImports);

        final String prefix = "import " + (isStatic ? "static " : "");
        return result.stream().sorted().map(s -> prefix + s + ";").collect(joining("\n"));
    }

    private static String simplify(String fullQualifiedName, Map<String, String> imports, String packageNameOfClass, Set<String> knownSimpleClassNames) {
        final String simpleName = getSimpleName(fullQualifiedName);
        final String packageName = getPackageName(fullQualifiedName);
        if (packageName.isEmpty() && !packageNameOfClass.isEmpty()) {
            throw new IllegalStateException("Can't import class '" + simpleName + "' located in default package");
        } else if (packageName.equals(packageNameOfClass)) {
            return simpleName;
        } else if (imports.containsKey(fullQualifiedName)) {
            return imports.get(fullQualifiedName);
        } else if (knownSimpleClassNames.contains(simpleName) || imports.values().contains(simpleName)) {
            return fullQualifiedName;
        } else {
            imports.put(fullQualifiedName, simpleName);
            return simpleName;
        }
    }

    private static String getPackageName(String fqn) {
        return fqn.substring(0, Math.max(fqn.lastIndexOf('.'), 0));
    }

    private static String getSimpleName(String fqn) {
        return fqn.substring(fqn.lastIndexOf(".") + 1);
    }
}
