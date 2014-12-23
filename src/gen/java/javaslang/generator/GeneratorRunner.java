/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.generator;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneratorRunner {

    private static final String ARG_TARGET = "target";
    private static final String ARG_GENERATOR = "generator";

    public static void main(String[] args) {
        final Config config = new Config(args);
        System.out.println("Running code generator with arguments:\n" + config);
        final String target = config.getString(ARG_TARGET);
        config.getStrings(ARG_GENERATOR).stream()
                .distinct()
                .map(GeneratorRunner::createGenerator)
                .forEach(generator -> generator.generate(target));
    }

    private static JavaGenerator createGenerator(String className) {
        try {
            return (JavaGenerator) Class.forName(className).newInstance();
        } catch(Exception x) {
            throw new IllegalStateException("Error instantiating generator " + className, x);
        }
    }

    static class Config {

        final Map<String, List<String>> argMap;

        Config(String[] args) {
            final Function<String[], String> keyMapper = xs -> xs[0];
            final Function<String[], List<String>> valueMapper =
                    xs -> new ArrayList<String>() {{ add((xs.length == 1) ? "true" : xs[1]); }};
            final BinaryOperator<List<String>> merger = (x,y) -> { x.addAll(y); return x; };
            argMap = Arrays.asList(args).stream()
                    .map(arg -> arg.startsWith("-") ? arg.substring(1).split("=", 2) : null)
                    .filter(entry -> entry != null && !entry[0].isEmpty())
                    .collect(Collectors.toMap(keyMapper, valueMapper, merger));
        }

        String getString(String key) {
            return getStrings(key).get(0);
        }

        List<String> getStrings(String key) {
            if (!argMap.containsKey(key)) {
                throw new NoSuchElementException(key);
            }
            return argMap.get(key);
        }

        @Override
        public String toString() {
            return argMap.entrySet().stream()
                    .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                    .sorted()
                    .collect(Collectors.joining("\n"));
        }
    }
}
