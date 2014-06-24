
/**
 * <h1>Syntax</h1>
 * 
 * Note: No distinction between parser and lexer.
 * 
 * <h2>Comments</h2>
 * 
 * <u>Multi line comment</u>
 * <pre><code>/*
 * * Comment: '/*' .*? '*&#47;' -&gt; skip ;
 * *&#47;</code></pre>
 * 
 * <u>Single line comment</u>
 * <pre><code>// Comment: '//' ~[\r\n]* ('\r'? '\n' | EOF) -&gt; skip ;</code></pre>
 * 
 * <h2>Rules</h2>
 * 
 * <u>Invocation (of rule T)</u>
 * <pre><code>Rule : T ;</code></pre>
 * 
 * <u>Helper rule (not producing tokens 'Rule')</u>
 * <pre><code><strong>fragment</strong> Rule : T ; <em>// TODO: fragment vs. -&gt; skip</em></code></pre>
 * 
 * <u>Skip rule</u>
 * <pre><code>Rule : T <strong>-&gt; skip</strong> ; <em>// TODO: fragment vs. -&gt; skip</em></code></pre>
 * 
 * <h2>Syntax</h2>
 * 
 * <u>Sequence (of two or more rules)</u>
 * <pre><code>T1 T2</code></pre>
 * 
 * <u>Branch (of two or more rules)</u>
 * <pre><code>T1 | T2</code></pre>
 * 
 * <u>Braces</u>
 * <pre><code>(T) <em>// e.g. ( T1 | T2 ) T3 </em></code></pre>
 * 
 * <u>Multiplicity (greedy)</u>
 * <pre><code>T? <em>// zero or one occurrence, consuming all</em>
 *T* <em>// zero or more occurrences, consuming all</em>
 *T+ <em>// one or more occurrences, consuming all</em></code></pre>
 * 
 * <u>Multiplicity (non-greedy)</u>
 * <pre><code>T?? <em>// zero or one occurrence, consuming until next rule matches</em>
 *T*? <em>// zero or more occurrences, consuming until next rule matches</em>
 *T+? <em>// one or more occurrences, consuming unitl next rule matches</em></code></pre>
 * 
 * <h2>Syntax</h2>
 * 
 * <u>Literal</u>
 * <pre><code>'literal'</code></pre>
 * TODO: escape chars + unicode<p>
 * 
 * <u>Charset</u>
 * <pre><code>[charset] <em>// e.g. [a-zA-Z]</em></code></pre>
 * TODO: escape chars + unicode<p>
 * 
 * <u>Range</u>
 * <pre><code>'a'..'z' <em>// same as [a-z]</em></code></pre>
 * TODO: escape chars + unicode<p>
 * 
 * <u>Wildcard</u>
 * <pre><code>.</code></pre>
 * 
 * <u>Negation</u>
 * <pre><code>~T <em>// ~[\r\n]* = (~[\r\n])*</em></code></pre>
 * 
 * <u>Whitespace (WS)</u>
 * <pre><code>WS : [ \f\n\r\t]+ -&gt; skip <em>// rule name used to define whitespace</em></code></pre>
 *
 * <u>End of file (EOF)</u>
 * <pre><code>PartialMatch : rule*; <em>// parser stops when rule does not match any more</em>
 *TotalMatch : rule* EOF; <em>// input is fully parsed</em></code></pre>
 *
 * <u>Conventions:</u>
 * <pre><code>RuleName : OtherRule; <em>// camel case for rules which reference other rules</em>
 *RULE_NAME : 'literal'; <em>// upper 'snake' case for rules which contain no rule references</em></code></pre>
 *
 * <h2>Recursive rules</h2>
 * <pre><code>e : e '*' e
 *  | e '+' e
 *  | [0-9]+
 *  ;</code></pre>
 * 
 * <h2>Syntactic sugar</h2>
 * <pre><code>e : e '*' e # Mul
 *  | e '+' e # Add
 *  | INT # Int
 *  ;</code></pre>
 *
 * is the same as
 *
 * <pre><code>e : Mul
 *  | Add
 *  | Int
 *  ;
 *
 *Mul : e '*' e;
 *Add : e '+' e;
 *Int : INT;
 *
 *INT : [0-9]+</code></pre>
 */
package javaslang.text;
