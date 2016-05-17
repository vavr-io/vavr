/**
 * Purely functional collections based on {@linkplain javaslang.collection.Traversable}.
 *
 * <h2>Performance Characteristics of Javaslang Collections</h2>
 * <table cellpadding="5" cellspacing="0" border="1" style="border-collapse: collapse">
 * <caption>Time Complexity of Sequential Operations</caption>
 * <thead>
 * <tr>
 * <th>&nbsp;</th>
 * <th>head()</th>
 * <th>tail()</th>
 * <th>get(int)</th>
 * <th>update(int, T)</th>
 * <th>prepend(T)</th>
 * <th>append(T)</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr><td>{@linkplain javaslang.collection.Array}</td><td><small>const</small></td><td><small>linear</small></td><td><small>const</small></td><td><small>const</small></td><td><small>linear</small></td><td><small>linear</small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.CharSeq}</td><td><small>const</small></td><td><small>linear</small></td><td><small>const</small></td><td><small>linear</small></td><td><small>linear</small></td><td><small>linear</small></td></tr>
 * <tr><td><em>{@linkplain javaslang.collection.Iterator}</em></td><td><small>const</small></td><td><small>const</small></td><td>&mdash;</td><td>&mdash;</td><td>&mdash;</td><td>&mdash;</td></tr>
 * <tr><td>{@linkplain javaslang.collection.List}</td><td><small>const</small></td><td><small>const</small></td><td><small>linear</small></td><td><small>linear</small></td><td><small>const</small></td><td><small>linear</small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.Queue}</td><td><small>const</small></td><td><small>const<sup>a</sup></small></td><td><small>linear</small></td><td><small>linear</small></td><td><small>const</small></td><td><small>const</small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.PriorityQueue}</td><td><small>log</small></td><td><small>log</small></td><td><small>&mdash;</small></td><td><small>&mdash;</small></td><td><small>log</small></td><td><small>log</small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.Stream}</td><td><small>const</small></td><td><small>const</small></td><td><small>linear</small></td><td><small>linear</small></td><td><small>const<sup>lazy</sup></small></td><td><small>const<sup>lazy</sup></small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.Vector}</td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td></tr>
 * </tbody>
 * </table>
 * <br>
 * <table cellpadding="5" cellspacing="0" border="1" style="border-collapse: collapse">
 * <caption>Time Complexity of Map/Set Operations</caption>
 * <thead>
 * <tr>
 * <th>&nbsp;</th>
 * <th>contains/Key</th>
 * <th>add/put</th>
 * <th>remove</th>
 * <th>min</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr><td>{@linkplain javaslang.collection.HashMap}</td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>linear</small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.HashSet}</td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>linear</small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.LinkedHashMap}</td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>linear</small></td><td><small>linear</small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.LinkedHashSet}</td><td><small>const<sup>eff</sup></small></td><td><small>const<sup>eff</sup></small></td><td><small>linear</small></td><td><small>linear</small></td></tr>
 * <tr><td><em>{@linkplain javaslang.collection.Tree}</em></td><td><small>log</small></td><td><small>log</small></td><td><small>log</small></td><td><small>log</small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.TreeMap}</td><td><small>log</small></td><td><small>log</small></td><td><small>log</small></td><td><small>log</small></td></tr>
 * <tr><td>{@linkplain javaslang.collection.TreeSet}</td><td><small>log</small></td><td><small>log</small></td><td><small>log</small></td><td><small>log</small></td></tr>
 * </tbody>
 * </table>
 * <br>
 * <ul>
 * <li><small>const</small>&nbsp;&middot;&nbsp;constant time</li>
 * <li><small>const<sup>a</sup></small>&nbsp;&middot;&nbsp;amotized constant time, few operations may take longer</li>
 * <li><small>const<sup>eff</sup></small>&nbsp;&middot;&nbsp;effectively constant time, depending on assumptions like distribution of hash keys</li>
 * <li><small>const<sup>lazy</sup></small>&nbsp;&middot;&nbsp;lazy constant time, the operation is deferred</li>
 * <li><small>log</small>&nbsp;&middot;&nbsp;logarithmic time</li>
 * <li><small>linear</small>&nbsp;&middot;&nbsp;linear time</li>
 * </ul>
 *
 * @since 1.1.0
 */
package javaslang.collection;
