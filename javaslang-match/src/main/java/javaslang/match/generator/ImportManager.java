/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match.generator;

import javaslang.match.model.ClassModel;

import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * A <em>stateful</em> ImportManager which generates an import section of a Java class file.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
class ImportManager {

    // properties
    private final String packageNameOfClass;
    private List<String> directImports;

    // mutable state
    private Map<FQN, Import> imports = new HashMap<>();

    private ImportManager(String packageNameOfClass, List<String> directImports) {
        this.packageNameOfClass = packageNameOfClass;
        this.directImports = directImports;
    }

    // directImport FQN("javaslang", "Match.API") will import "javaslang.Match.API.Pattern0"
    // otherwise "javaslang.Match" is imported and "Match.API.Pattern0" is qualified
    public static ImportManager forClass(ClassModel classModel, String... directImports) {
        return new ImportManager(classModel.getPackageName(), reverseSort(directImports));
    }

    // used by generator to register non-static imports
    public String getType(ClassModel classModel) {
        final FQN fqn = new FQN(classModel.getPackageName(), classModel.getClassName());
        return getType(fqn, imports, packageNameOfClass, directImports);
    }

    public String getType(String packageName, String className) {
        final FQN fqn = new FQN(packageName, className);
        return getType(fqn, imports, packageNameOfClass, directImports);
    }

    // finally used by generator to get the import section
    public String getImports() {
        return optimizeImports(imports.values());
    }

    private static String getType(FQN fqn, Map<FQN, Import> imports, String packageNameOfClass, List<String> directImports) {
        if (fqn.packageName.isEmpty() && !packageNameOfClass.isEmpty()) {
            throw new IllegalStateException("Can't import class '" + fqn.className + "' located in default package");
        } else if (fqn.packageName.equals(packageNameOfClass)) {
            final Import _import = createImport(fqn, directImports);
            if (_import.type.equals(fqn.className)) {
                return fqn.className;
            } else {
                imports.put(fqn, _import);
                return _import.type;
            }
        } else if (imports.containsKey(fqn)) {
            return imports.get(fqn).type;
        } else {
            final Import _import = createImport(fqn, directImports);
            imports.put(fqn, _import);
            return _import.type;
        }
    }

    private static Import createImport(FQN fqn, List<String> directImports) {
        final String qualifiedName = fqn.qualifiedName();
        final Optional<String> directImportOption = directImports.stream()
                .filter(directImport -> qualifiedName.equals(directImport) || qualifiedName.startsWith(directImport + "."))
                .findFirst();
        if (directImportOption.isPresent()) {
            final String directImport = directImportOption.get();
            if (qualifiedName.equals(directImport)) {
                final String type = directImport.substring(directImport.lastIndexOf('.') + 1);
                return new Import(directImport, type);
            } else {
                final String type = qualifiedName.substring(directImport.length() + 1);
                final int index = type.indexOf(".");
                final String firstSegment = (index < 0) ? type : type.substring(0, index);
                return new Import(directImport + "." + firstSegment, type);
            }
        } else {
            final int index = fqn.className.indexOf(".");
            final String firstSegment = (index < 0) ? fqn.className : fqn.className.substring(0, index);
            return new Import(fqn.packageName + "." + firstSegment, fqn.className);
        }
    }

    private static String optimizeImports(Collection<Import> imports) {
        return imports.stream()
                .filter(currentImport -> !currentImport.name.startsWith("java.lang.") ||
                        imports.stream()
                                .filter(otherImport -> !otherImport.equals(currentImport))
                                .map(otherImport -> otherImport.type)
                                .filter(otherType -> otherType.equals(currentImport.type))
                                .findFirst().isPresent())
                .map(_import -> _import.name)
                .distinct()
                .sorted()
                .map(s -> "import " + s + ";")
                .collect(joining("\n"));
    }

    private static List<String> reverseSort(String[] strings) {
        final String[] copy = new String[strings.length];
        System.arraycopy(strings, 0, copy, 0, strings.length);
        Arrays.sort(copy, (s1, s2) -> s2.compareTo(s1));
        return Arrays.asList(copy);
    }

    private static class FQN {

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
            return "FQN(" + packageName + ", " + className + ")";
        }
    }

    private static class Import {

        final String name;
        final String type;

        Import(String name, String type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            return (o == this) || (o instanceof Import && toString().equals(o.toString()));
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public String toString() {
            return "Import(" + name + ", " + type + ")";
        }
    }
}
