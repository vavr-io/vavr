/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.generator;

class PropGenerator extends JavaGenerator {

	static final int N = 13;

	protected PropGenerator() {
		super("javaslang.test.Prop");
	}

	@Override
	protected void gen() {
		// TODO: generateForAllMethods();
	}

	void generateForAllMethods() {
		for (int i = 1; i <= N; i++) {
			generateForAllMethod(i);
		}
	}

	void generateForAllMethod(int n) {

		final String generics = range(1, n).join(i -> "T" + i, ", ");
		final String vars = range(1, n).join(i -> format("    final Iterable<T%s> t%ss;", i, i), "\n", "", "\n");
		final String params = range(1, n).join(i -> format("Iterable<T%s> t%ss", i, i), ", ");
		final String inits = range(1, n).join(i -> format("        this.t%ss = t%ss;", i, i), "\n", "", "\n");
		final String forStarts = range(1, n).join(i -> format("        %sfor (T%s t%s : t%ss) {", indent(i), i, i, i),
				"\n", "", "\n");
		final String forEnds = range(1, n).join(i -> format("        %s}", indent(n - i + 1)), "\n", "", "\n");
		final String forVars = range(1, n).join(i -> "t" + i, ", ");
		final String forAllArgs = range(1, n).join(i -> "t" + i + "s", ", ");

		gen("public static class ForAll%s<%s> {\n", n, generics);
		gen(vars);
		gen("    ForAll%s(%s) {\n", n, params);
		gen(inits);
		gen("    }\n");
		gen("    public Prop suchThat(Functions.Function%s<%s, Boolean> predicate) {\n", n, generics);
		gen("        return new Prop(() -> {\n");
		gen(forStarts);
		gen("            %sif (!predicate.apply(%s)) {\n", indent(n), forVars);
		gen("                %sreturn false;\n", indent(n));
		gen("            %s}\n", indent(n));
		gen(forEnds);
		gen("            return true;\n");
		gen("        });\n");
		gen("    }\n");
		if (n < N) {
			final int m = n + 1;
			gen("    public <T%s> ForAll%s<%s, T%s> forAll(Iterable<T%s> t%ss) {\n", m, m, generics, m, m, m);
			gen("        return new ForAll%s<>(%s, t%ss);\n", m, forAllArgs, m);
			gen("    }\n");
		}
		gen("}\n");
	}
}
