/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import java.util.function.BooleanSupplier;

import javaslang.Lambda.λ1;
import javaslang.Lambda.λ10;
import javaslang.Lambda.λ11;
import javaslang.Lambda.λ12;
import javaslang.Lambda.λ13;
import javaslang.Lambda.λ2;
import javaslang.Lambda.λ3;
import javaslang.Lambda.λ4;
import javaslang.Lambda.λ5;
import javaslang.Lambda.λ6;
import javaslang.Lambda.λ7;
import javaslang.Lambda.λ8;
import javaslang.Lambda.λ9;

/**
 * Property-based testing.
 * 
 * <pre>
 * <code>
 * // VxE!y(x + y = 0)
 * final Iterable<Integer> ns = List.of(-3, -2, -1, 0, 1, 2, 3);
 * Props.forAll(ns).existsUnique(ns).suchThat((x,y) -> x + y == 0).test();
 * </code>
 * </pre>
 */
// TODO: Implement generators `Gen`
public class Prop {

	private final BooleanSupplier test;

	Prop(BooleanSupplier test) {
		this.test = test;
	}

	public boolean test() {
		return test.getAsBoolean();
	}

	// prop1 and ( prop2 or prop3 )
	public Prop and(Prop property) {
		return null;
	}

	// TODO: or, not

	public <T> ForAll1<T> forAll(Iterable<T> ts) {
		return new ForAll1<>(ts);
	}

	// TODO: exists, existsUnique

	public static class ForAll1<T1> {
		final Iterable<T1> t1s;

		ForAll1(Iterable<T1> t1s) {
			this.t1s = t1s;
		}

		public Prop suchThat(λ1<T1, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					if (!predicate.apply(t1)) {
						return false;
					}
				}
				return true;
			});
		}

		public <T2> ForAll2<T1, T2> forAll(Iterable<T2> t2s) {
			return new ForAll2<>(t1s, t2s);
		}
	}

	public static class ForAll2<T1, T2> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;

		ForAll2(Iterable<T1> t1s, Iterable<T2> t2s) {
			this.t1s = t1s;
			this.t2s = t2s;
		}

		public Prop suchThat(λ2<T1, T2, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						if (!predicate.apply(t1, t2)) {
							return false;
						}
					}
				}
				return true;
			});
		}

		public <T3> ForAll3<T1, T2, T3> forAll(Iterable<T3> t3s) {
			return new ForAll3<>(t1s, t2s, t3s);
		}
	}

	public static class ForAll3<T1, T2, T3> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;

		ForAll3(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
		}

		public Prop suchThat(λ3<T1, T2, T3, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							if (!predicate.apply(t1, t2, t3)) {
								return false;
							}
						}
					}
				}
				return true;
			});
		}

		public <T4> ForAll4<T1, T2, T3, T4> forAll(Iterable<T4> t4s) {
			return new ForAll4<>(t1s, t2s, t3s, t4s);
		}
	}

	public static class ForAll4<T1, T2, T3, T4> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;

		ForAll4(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
		}

		public Prop suchThat(λ4<T1, T2, T3, T4, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								if (!predicate.apply(t1, t2, t3, t4)) {
									return false;
								}
							}
						}
					}
				}
				return true;
			});
		}

		public <T5> ForAll5<T1, T2, T3, T4, T5> forAll(Iterable<T5> t5s) {
			return new ForAll5<>(t1s, t2s, t3s, t4s, t5s);
		}
	}

	public static class ForAll5<T1, T2, T3, T4, T5> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;
		final Iterable<T5> t5s;

		ForAll5(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s, Iterable<T5> t5s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
			this.t5s = t5s;
		}

		public Prop suchThat(λ5<T1, T2, T3, T4, T5, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								for (T5 t5 : t5s) {
									if (!predicate.apply(t1, t2, t3, t4, t5)) {
										return false;
									}
								}
							}
						}
					}
				}
				return true;
			});
		}

		public <T6> ForAll6<T1, T2, T3, T4, T5, T6> forAll(Iterable<T6> t6s) {
			return new ForAll6<>(t1s, t2s, t3s, t4s, t5s, t6s);
		}
	}

	public static class ForAll6<T1, T2, T3, T4, T5, T6> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;
		final Iterable<T5> t5s;
		final Iterable<T6> t6s;

		ForAll6(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s, Iterable<T5> t5s,
				Iterable<T6> t6s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
			this.t5s = t5s;
			this.t6s = t6s;
		}

		public Prop suchThat(λ6<T1, T2, T3, T4, T5, T6, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								for (T5 t5 : t5s) {
									for (T6 t6 : t6s) {
										if (!predicate.apply(t1, t2, t3, t4, t5, t6)) {
											return false;
										}
									}
								}
							}
						}
					}
				}
				return true;
			});
		}

		public <T7> ForAll7<T1, T2, T3, T4, T5, T6, T7> forAll(Iterable<T7> t7s) {
			return new ForAll7<>(t1s, t2s, t3s, t4s, t5s, t6s, t7s);
		}
	}

	public static class ForAll7<T1, T2, T3, T4, T5, T6, T7> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;
		final Iterable<T5> t5s;
		final Iterable<T6> t6s;
		final Iterable<T7> t7s;

		ForAll7(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s, Iterable<T5> t5s,
				Iterable<T6> t6s, Iterable<T7> t7s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
			this.t5s = t5s;
			this.t6s = t6s;
			this.t7s = t7s;
		}

		public Prop suchThat(λ7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								for (T5 t5 : t5s) {
									for (T6 t6 : t6s) {
										for (T7 t7 : t7s) {
											if (!predicate.apply(t1, t2, t3, t4, t5, t6, t7)) {
												return false;
											}
										}
									}
								}
							}
						}
					}
				}
				return true;
			});
		}

		public <T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Iterable<T8> t8s) {
			return new ForAll8<>(t1s, t2s, t3s, t4s, t5s, t6s, t7s, t8s);
		}
	}

	public static class ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;
		final Iterable<T5> t5s;
		final Iterable<T6> t6s;
		final Iterable<T7> t7s;
		final Iterable<T8> t8s;

		ForAll8(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s, Iterable<T5> t5s,
				Iterable<T6> t6s, Iterable<T7> t7s, Iterable<T8> t8s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
			this.t5s = t5s;
			this.t6s = t6s;
			this.t7s = t7s;
			this.t8s = t8s;
		}

		public Prop suchThat(λ8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								for (T5 t5 : t5s) {
									for (T6 t6 : t6s) {
										for (T7 t7 : t7s) {
											for (T8 t8 : t8s) {
												if (!predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8)) {
													return false;
												}
											}
										}
									}
								}
							}
						}
					}
				}
				return true;
			});
		}

		public <T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Iterable<T9> t9s) {
			return new ForAll9<>(t1s, t2s, t3s, t4s, t5s, t6s, t7s, t8s, t9s);
		}
	}

	public static class ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;
		final Iterable<T5> t5s;
		final Iterable<T6> t6s;
		final Iterable<T7> t7s;
		final Iterable<T8> t8s;
		final Iterable<T9> t9s;

		ForAll9(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s, Iterable<T5> t5s,
				Iterable<T6> t6s, Iterable<T7> t7s, Iterable<T8> t8s, Iterable<T9> t9s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
			this.t5s = t5s;
			this.t6s = t6s;
			this.t7s = t7s;
			this.t8s = t8s;
			this.t9s = t9s;
		}

		public Prop suchThat(λ9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								for (T5 t5 : t5s) {
									for (T6 t6 : t6s) {
										for (T7 t7 : t7s) {
											for (T8 t8 : t8s) {
												for (T9 t9 : t9s) {
													if (!predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9)) {
														return false;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				return true;
			});
		}

		public <T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Iterable<T10> t10s) {
			return new ForAll10<>(t1s, t2s, t3s, t4s, t5s, t6s, t7s, t8s, t9s, t10s);
		}
	}

	public static class ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;
		final Iterable<T5> t5s;
		final Iterable<T6> t6s;
		final Iterable<T7> t7s;
		final Iterable<T8> t8s;
		final Iterable<T9> t9s;
		final Iterable<T10> t10s;

		ForAll10(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s, Iterable<T5> t5s,
				Iterable<T6> t6s, Iterable<T7> t7s, Iterable<T8> t8s, Iterable<T9> t9s, Iterable<T10> t10s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
			this.t5s = t5s;
			this.t6s = t6s;
			this.t7s = t7s;
			this.t8s = t8s;
			this.t9s = t9s;
			this.t10s = t10s;
		}

		public Prop suchThat(λ10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								for (T5 t5 : t5s) {
									for (T6 t6 : t6s) {
										for (T7 t7 : t7s) {
											for (T8 t8 : t8s) {
												for (T9 t9 : t9s) {
													for (T10 t10 : t10s) {
														if (!predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10)) {
															return false;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				return true;
			});
		}

		public <T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Iterable<T11> t11s) {
			return new ForAll11<>(t1s, t2s, t3s, t4s, t5s, t6s, t7s, t8s, t9s, t10s, t11s);
		}
	}

	public static class ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;
		final Iterable<T5> t5s;
		final Iterable<T6> t6s;
		final Iterable<T7> t7s;
		final Iterable<T8> t8s;
		final Iterable<T9> t9s;
		final Iterable<T10> t10s;
		final Iterable<T11> t11s;

		ForAll11(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s, Iterable<T5> t5s,
				Iterable<T6> t6s, Iterable<T7> t7s, Iterable<T8> t8s, Iterable<T9> t9s, Iterable<T10> t10s,
				Iterable<T11> t11s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
			this.t5s = t5s;
			this.t6s = t6s;
			this.t7s = t7s;
			this.t8s = t8s;
			this.t9s = t9s;
			this.t10s = t10s;
			this.t11s = t11s;
		}

		public Prop suchThat(λ11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								for (T5 t5 : t5s) {
									for (T6 t6 : t6s) {
										for (T7 t7 : t7s) {
											for (T8 t8 : t8s) {
												for (T9 t9 : t9s) {
													for (T10 t10 : t10s) {
														for (T11 t11 : t11s) {
															if (!predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9,
																	t10, t11)) {
																return false;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				return true;
			});
		}

		public <T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Iterable<T12> t12s) {
			return new ForAll12<>(t1s, t2s, t3s, t4s, t5s, t6s, t7s, t8s, t9s, t10s, t11s, t12s);
		}
	}

	public static class ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;
		final Iterable<T5> t5s;
		final Iterable<T6> t6s;
		final Iterable<T7> t7s;
		final Iterable<T8> t8s;
		final Iterable<T9> t9s;
		final Iterable<T10> t10s;
		final Iterable<T11> t11s;
		final Iterable<T12> t12s;

		ForAll12(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s, Iterable<T5> t5s,
				Iterable<T6> t6s, Iterable<T7> t7s, Iterable<T8> t8s, Iterable<T9> t9s, Iterable<T10> t10s,
				Iterable<T11> t11s, Iterable<T12> t12s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
			this.t5s = t5s;
			this.t6s = t6s;
			this.t7s = t7s;
			this.t8s = t8s;
			this.t9s = t9s;
			this.t10s = t10s;
			this.t11s = t11s;
			this.t12s = t12s;
		}

		public Prop suchThat(λ12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								for (T5 t5 : t5s) {
									for (T6 t6 : t6s) {
										for (T7 t7 : t7s) {
											for (T8 t8 : t8s) {
												for (T9 t9 : t9s) {
													for (T10 t10 : t10s) {
														for (T11 t11 : t11s) {
															for (T12 t12 : t12s) {
																if (!predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8,
																		t9, t10, t11, t12)) {
																	return false;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				return true;
			});
		}

		public <T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Iterable<T13> t13s) {
			return new ForAll13<>(t1s, t2s, t3s, t4s, t5s, t6s, t7s, t8s, t9s, t10s, t11s, t12s, t13s);
		}
	}

	public static class ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
		final Iterable<T1> t1s;
		final Iterable<T2> t2s;
		final Iterable<T3> t3s;
		final Iterable<T4> t4s;
		final Iterable<T5> t5s;
		final Iterable<T6> t6s;
		final Iterable<T7> t7s;
		final Iterable<T8> t8s;
		final Iterable<T9> t9s;
		final Iterable<T10> t10s;
		final Iterable<T11> t11s;
		final Iterable<T12> t12s;
		final Iterable<T13> t13s;

		ForAll13(Iterable<T1> t1s, Iterable<T2> t2s, Iterable<T3> t3s, Iterable<T4> t4s, Iterable<T5> t5s,
				Iterable<T6> t6s, Iterable<T7> t7s, Iterable<T8> t8s, Iterable<T9> t9s, Iterable<T10> t10s,
				Iterable<T11> t11s, Iterable<T12> t12s, Iterable<T13> t13s) {
			this.t1s = t1s;
			this.t2s = t2s;
			this.t3s = t3s;
			this.t4s = t4s;
			this.t5s = t5s;
			this.t6s = t6s;
			this.t7s = t7s;
			this.t8s = t8s;
			this.t9s = t9s;
			this.t10s = t10s;
			this.t11s = t11s;
			this.t12s = t12s;
			this.t13s = t13s;
		}

		public Prop suchThat(λ13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean> predicate) {
			return new Prop(() -> {
				for (T1 t1 : t1s) {
					for (T2 t2 : t2s) {
						for (T3 t3 : t3s) {
							for (T4 t4 : t4s) {
								for (T5 t5 : t5s) {
									for (T6 t6 : t6s) {
										for (T7 t7 : t7s) {
											for (T8 t8 : t8s) {
												for (T9 t9 : t9s) {
													for (T10 t10 : t10s) {
														for (T11 t11 : t11s) {
															for (T12 t12 : t12s) {
																for (T13 t13 : t13s) {
																	if (!predicate.apply(t1, t2, t3, t4, t5, t6, t7,
																			t8, t9, t10, t11, t12, t13)) {
																		return false;
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				return true;
			});
		}
	}
}

class Props {

	public static <T> Prop.ForAll1<T> forAll(Iterable<T> ts) {
		return new Prop.ForAll1<>(ts);
	}

	//	public static <T> Prop.Exists1<T> exists(Iterable<T> ts) {
	//		return new Prop.Exists1<>(ts);
	//	}
	//
	//	public static <T> Prop.ExistsUnique1<T> existsUnique(Iterable<T> ts) {
	//		return new Prop.ExistsUnique1<>(ts);
	//	}
}
