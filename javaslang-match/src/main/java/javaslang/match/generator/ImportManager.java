/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match.generator;

import javaslang.match.model.ClassModel;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * A <em>stateful</em> ImportManager which generates an import section of a Java class file.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
// TODO: use Multimap (fqn without inner classes) -> (list/set of class names, incl. inner classes)
class ImportManager {

    static final int DEFAULT_WILDCARD_THRESHOLD = 5;

    // properties
    private final String packageNameOfClass;
    private final Set<String> knownSimpleClassNames;
    private final int wildcardThreshold;

    // mutable state
    private Map<FQN, String> imports = new HashMap<>();

    public ImportManager(String packageNameOfClass, Set<String> knownSimpleClassNames, int wildcardThreshold) {
        this.packageNameOfClass = packageNameOfClass;
        this.knownSimpleClassNames = knownSimpleClassNames;
        this.wildcardThreshold = wildcardThreshold;
    }

    public static ImportManager forClass(ClassModel classModel) {
        return new ImportManager(classModel.getPackageName(), Collections.emptySet(), DEFAULT_WILDCARD_THRESHOLD);
    }

    // used by generator to register non-static imports
    public String getType(ClassModel classModel) {
        final FQN fqn = new FQN(classModel.getPackageName(), classModel.getClassName());
        return simplify(fqn, imports, packageNameOfClass, knownSimpleClassNames);
    }

    public String getType(String packageName, String className) {
        final FQN fqn = new FQN(packageName, className);
        return simplify(fqn, imports, packageNameOfClass, knownSimpleClassNames);
    }

    // finally used by generator to get the import section
    public String getImports() {
        return optimizeImports(imports.keySet(), false, wildcardThreshold);
    }

    private static String optimizeImports(Set<FQN> imports, boolean isStatic, int wildcardThreshold) {

        final Map<String, Integer> counts = imports.stream()
                .map(fqn -> fqn.packageName)
                .collect(groupingBy(s -> s))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));

        final List<String> directImports = imports.stream()
                .filter(fqn -> counts.get(fqn.packageName) <= wildcardThreshold)
                .map(FQN::qualifiedName)
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

    private static String simplify(FQN fqn, Map<FQN, String> imports, String packageNameOfClass, Set<String> knownSimpleClassNames) {
        if (fqn.packageName.isEmpty() && !packageNameOfClass.isEmpty()) {
            throw new IllegalStateException("Can't import class '" + fqn.className + "' located in default package");
        } else if (fqn.packageName.equals(packageNameOfClass)) {
            return fqn.className;
        } else if (imports.containsKey(fqn)) {
            return imports.get(fqn);
        } else if (knownSimpleClassNames.contains(fqn.className) || imports.values().contains(fqn.className)) {
            return fqn.qualifiedName();
        } else {
            imports.put(fqn, fqn.className);
            return fqn.className;
        }
    }

    static class FQN {

        private final String packageName;
        private final String className;

        FQN(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        String qualifiedName() {
            return packageName.isEmpty() ? className : packageName + "." + className;
        }

        @Override
        public boolean equals(Object o) {
            return (o == this) || (o instanceof FQN && qualifiedName().equals(((FQN) o).qualifiedName()));
        }

        @Override
        public int hashCode() {
            return qualifiedName().hashCode();
        }

        @Override
        public String toString() {
            return qualifiedName();
        }
    }
}
