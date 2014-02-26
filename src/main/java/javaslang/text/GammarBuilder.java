package javaslang.text;

////GRAMMAR ::= TEST1 | TEST2 | TEST3
////TEST1 ::= 'test1'
////TEST2 ::= 'test2'
////TEST3 ::= 'test3'
//final Rule GRAMMAR = Rule("GRAMMAR")._(TEST1()).or(TEST2()).or(TEST3());
//
//final Rule TEST1() {
//return Rule("TEST1")._("test1");
//}
//
//final Rule TEST2() {
//return Rule("TEST2")._("test2");
//}
//
//final Rule TEST3() {
//return Rule("TEST3")._("test3");
//}
//
////JSON_OBJECT ::= '{' [ PAIR [',' PAIR]* ]? '}'
////PAIR ::= "'" KEY "'" ':' "'" VALUE "'"
////KEY ::= regex(.+)
////VALUE ::= regex(.+)
//final Rule JSON_OBJECT = Rule("JSON_OBJECT")._('{').n(PAIR(), ',')._('}');
//
//final Rule PAIR() {
//return Rule("PAIR")._("'")._(KEY())._("'")._(":")._("'")._(VALUE())._("'");
//}
//
//final Rule KEY() {
//return Rule("KEY").reg(".+");
//}
//
//final Rule VALUE() {
//return Rule("VALUE").reg(".+");
//}
public class GammarBuilder {
	
	// TODO
	
//	public static Rule Rule(String name) {
//	return new Identifier(name);
//}
//
//public static final class Identifier extends Rule {
//
//	private final String name;
//
//	private Identifier(String name) {
//		require(name != null, "name is null");
//		// TODO: require(isUnique(name), name + " is ambiguous.");
//		this.name = name;
//	}
//
//	@Override
//	public Token apply(String text, int index) {
//		// TODO Auto-generated method stub
//		// TODO: concat all rules with logical AND (in some way) => Tree
//		return null;
//	}
//	
//}
//
//public static final class ZeroOrOne extends Rule {
//
//	private final Rule rule;
//
//	ZeroOrOne(Rule rule) {
//		this.rule = rule;
//	}
//
//	@Override
//	public Token apply(String text, int index) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
//
//public static final class OneOrMore extends Rule {
//
//	private final Rule rule;
//	private final String separator;
//
//	OneOrMore(Rule rule) {
//		this(rule, null);
//	}
//
//	OneOrMore(Rule rule, char separator) {
//		this(rule, String.valueOf(separator));
//	}
//
//	OneOrMore(Rule rule, String separator) {
//		this.rule = rule;
//		this.separator = separator;
//	}
//
//	@Override
//	public Token apply(String text, int index) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//}
//
//public static final class N extends Rule {
//
//	private final Rule rule;
//	private final String separator;
//
//	N(Rule rule) {
//		this(rule, null);
//	}
//
//	N(Rule rule, char separator) {
//		this(rule, String.valueOf(separator));
//	}
//
//	N(Rule rule, String separator) {
//		this.rule = rule;
//		this.separator = separator;
//	}
//
//	// TODO: test
//	@Override
//	public Token apply(String text, int index) {
//		int end = -1;
//		for (int i = index; i != -1; i = rule.search(text, i), result = i);
//		return (end == -1) ? new Token(text, index, index) : new Token(text, index, end);
//	}
//	
//}
//
//public static final class Literal extends Rule {
//
//	private final String s;
//
//	Literal(char c) {
//		this.s = String.valueOf(c);
//	}
//
//	Literal(String s) {
//		require(s != null, "string is null");
//		this.s = s;
//	}
//
//	// TODO: test
//	@Override
//	public Token apply(String text, int index) {
//		final int end = text.startsWith(s, index) ? index + s.length() : -1;
//		return (end == -1) ? null : new Token(s, index, end);
//	}
//
//}
//
//public static final class RegEx extends Rule {
//
//	private final Pattern pattern;
//
//	RegEx(String regex) {
//		require(regex != null, "regular expression is null");
//		this.pattern = Pattern.compile(regex);
//	}
//
//	@Override
//	public Token apply(String text, int index) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
//
//public static final class Or extends Rule {
//	
//	// TODO: find a better name for Or-Nodes than "(anonymous)". Identifier+x?
//	private static final String ANONYMOUS = "(anonymous)";
//
//	private final List<Rule> rules;
//
//	Or(Rule rule) {
//		this(rule, new Identifier(ANONYMOUS));
//	}
//
//	Or(Rule either, Rule or) {
//		this.rules = new ArrayList<>();
//		rules.add(either);
//		rules.add(or);
//	}
//
//	private Rule currentRule() {
//		return rules.get(rules.size() - 1);
//	}
//
//	@Override
//	protected Rule add(Rule rule) {
//		currentRule().add(rule);
//		return this;
//	}
//
//	@Override
//	public Rule or() {
//		rules.add(new Identifier(ANONYMOUS));
//		return this;
//	}
//
//	@Override
//	public Rule or(Rule rule) {
//		rules.add(rule);
//		return this;
//	}
//
//	// TODO: test
//	@Override
//	public Token apply(String text, int index) {
//		final int i = rules
//				.parallelStream()
//				.map(rule -> rule.search(text, index))
//				.reduce((i1, i2) -> {
//					require(i1 != -1 && i2 != -1,
//							"ambiguity found at " + Types.toString(lineAndColumn(text, index)) + ":\n" + text);
//					return (i1 != -1) ? i1 : i2; // may be -1
//				}).orElse(-1);
//		return (i == -1) ? null : new Token(text.substring(index, i), index, i);
//	}
//	
//}
//
///**
// * 
// * Rules: literals: char, String, regEx composition: and (default), or
// * 
// * Multiplicity: _ 1 opt 0..1 n 0..n has 1..n
// * 
// * TODO: whitespace handling -- TODO: - Rule.parse(String): BNF/EBNF => Rule - Rule.toString(): Rule => BNF/EBNF
// * 
// */
//public static abstract class Rule {
//
//	public abstract Token apply(String text, int index);
//
//	protected Rule add(Rule rule) {
//		return this;
//	}
//
//	// logical or
//
//	public Rule or() {
//		return new Or(this);
//	}
//
//	public Rule or(Rule rule) {
//		return new Or(this, rule);
//	}
//
//	// regular expression
//
//	public final Rule reg(String regexp) {
//		return add(new RegEx(regexp));
//	}
//
//	// multiplicity: 1
//
//	public final Rule _(char c) {
//		return add(new Literal(c));
//	}
//
//	public final Rule _(String s) {
//		return add(new Literal(s));
//	}
//
//	public final Rule _(Rule rule) {
//		return add(rule);
//	}
//
//	// multiplicity: 0..1 / optional
//
//	public final Rule opt(char c) {
//		return add(new ZeroOrOne(new Literal(c)));
//	}
//
//	public final Rule opt(String s) {
//		return add(new ZeroOrOne(new Literal(s)));
//	}
//
//	public final Rule opt(Rule rule) {
//		return add(new ZeroOrOne(rule));
//	}
//
//	// multiplicity: 1..n / has one or more
//
//	public final Rule has(char c) {
//		return add(new OneOrMore(new Literal(c)));
//	}
//
//	public final Rule has(char c, char separator) {
//		return add(new OneOrMore(new Literal(c), separator));
//	}
//
//	public final Rule has(char c, String separator) {
//		return add(new OneOrMore(new Literal(c), separator));
//	}
//
//	public final Rule has(String s) {
//		return add(new OneOrMore(new Literal(s)));
//	}
//
//	public final Rule has(String s, char separator) {
//		return add(new OneOrMore(new Literal(s), separator));
//	}
//
//	public final Rule has(String s, String separator) {
//		return add(new OneOrMore(new Literal(s), separator));
//	}
//
//	public final Rule has(Rule rule) {
//		return add(new OneOrMore(rule));
//	}
//
//	public final Rule has(Rule rule, char separator) {
//		return add(new OneOrMore(rule, separator));
//	}
//
//	public final Rule has(Rule rule, String separator) {
//		return add(new OneOrMore(rule, separator));
//	}
//
//	// multiplicity: 0..n / zero or more
//
//	public final Rule n(char c) {
//		return add(new N(new Literal(c)));
//	}
//
//	public final Rule n(char c, char separator) {
//		return add(new N(new Literal(c), separator));
//	}
//
//	public final Rule n(char c, String separator) {
//		return add(new N(new Literal(c), separator));
//	}
//
//	public final Rule n(String s) {
//		return add(new N(new Literal(s)));
//	}
//
//	public final Rule n(String s, char separator) {
//		return add(new N(new Literal(s), separator));
//	}
//
//	public final Rule n(String s, String separator) {
//		return add(new N(new Literal(s), separator));
//	}
//
//	public final Rule n(Rule rule) {
//		return add(new N(rule));
//	}
//
//	public final Rule n(Rule rule, char separator) {
//		return add(new N(rule, separator));
//	}
//
//	public final Rule n(Rule rule, String separator) {
//		return add(new N(rule, separator));
//	}
//
//}

}
