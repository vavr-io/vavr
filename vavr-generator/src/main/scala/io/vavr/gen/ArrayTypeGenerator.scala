/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.gen

import Generator._
import JavaGenerator._
import Config._
import scala.collection.immutable.ListMap

/**
 * Generator of io.vavr.collection.*ArrayType
 */
def genArrayTypes(targetMain: String): Unit = {

  val types = ListMap(
    "boolean" -> "Boolean",
    "byte" -> "Byte",
    "char" -> "Character",
    "double" -> "Double",
    "float" -> "Float",
    "int" -> "Integer",
    "long" -> "Long",
    "short" -> "Short",
    "Object" -> "Object" // fallback
  ) // note: there is no void[] in Java

  genVavrFile("io.vavr.collection", "ArrayType", targetMain)((im: ImportManager, packageName: String, className: String) => xs"""
    import java.util.Collection;

    /**
     * Helper to replace reflective array access.
     *
     * @author Pap Lőrinc
     */
    interface ArrayType<T> extends Serializable {

        long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        static <T> ArrayType<T> obj() { return (ArrayType<T>) ObjectArrayType.INSTANCE; }

        Class<T> type();
        int lengthOf(Object array);
        T getAt(Object array, int index);

        Object empty();
        void setAt(Object array, int index, T value) throws ClassCastException;
        Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size);

        @SuppressWarnings("unchecked")
        static <T> ArrayType<T> of(Object array)  { return of((Class<T>) array.getClass().getComponentType()); }
        static <T> ArrayType<T> of(Class<T> type) { return !type.isPrimitive() ? obj() : ofPrimitive(type); }
        @SuppressWarnings("unchecked")
        static <T> ArrayType<T> ofPrimitive(Class<T> type) {
            if (boolean.class == type) {
                return (ArrayType<T>) BooleanArrayType.INSTANCE;
            } else if (byte.class == type) {
                return (ArrayType<T>) ByteArrayType.INSTANCE;
            } else if (char.class == type) {
                return (ArrayType<T>) CharArrayType.INSTANCE;
            } else if (double.class == type) {
                return (ArrayType<T>) DoubleArrayType.INSTANCE;
            } else if (float.class == type) {
                return (ArrayType<T>) FloatArrayType.INSTANCE;
            } else if (int.class == type) {
                return (ArrayType<T>) IntArrayType.INSTANCE;
            } else if (long.class == type) {
                return (ArrayType<T>) LongArrayType.INSTANCE;
            } else if (short.class == type) {
                return (ArrayType<T>) ShortArrayType.INSTANCE;
            } else {
                throw new IllegalArgumentException(String.valueOf(type));
            }
        }

        default Object newInstance(int length) { return copy(empty(), length); }

        /** System.arrayCopy with same source and destination */
        default Object copyRange(Object array, int from, int to) {
            final int length = to - from;
            return copy(array, length, from, 0, length);
        }

        /** Repeatedly group an array into equal sized sub-trees */
        default Object grouped(Object array, int groupSize) {
            final int arrayLength = lengthOf(array);
            final Object results = obj().newInstance(1 + ((arrayLength - 1) / groupSize));
            obj().setAt(results, 0, copyRange(array, 0, groupSize));

            for (int start = groupSize, i = 1; start < arrayLength; i++) {
                final int nextLength = Math.min(groupSize, arrayLength - (i * groupSize));
                obj().setAt(results, i, copyRange(array, start, start + nextLength));
                start += nextLength;
            }

            return results;
        }

        /** clone the source and set the value at the given position */
        default Object copyUpdate(Object array, int index, T element) {
            final Object copy = copy(array, index + 1);
            setAt(copy, index, element);
            return copy;
        }

        default Object copy(Object array, int minLength) {
            final int arrayLength = lengthOf(array);
            final int length = Math.max(arrayLength, minLength);
            return copy(array, length, 0, 0, arrayLength);
        }

        /** clone the source and keep everything after the index (pre-padding the values with null) */
        default Object copyDrop(Object array, int index) {
            final int length = lengthOf(array);
            return copy(array, length, index, index, length - index);
        }

        /** clone the source and keep everything before and including the index */
        default Object copyTake(Object array, int lastIndex) {
            return copyRange(array, 0, lastIndex + 1);
        }

        /** Create a single element array */
        default Object asArray(T element) {
            final Object result = newInstance(1);
            setAt(result, 0, element);
            return result;
        }

        /** Store the content of an iterable in an array */
        static Object[] asArray(java.util.Iterator<?> it, int length) {
            final Object[] array = new Object[length];
            for (int i = 0; i < length; i++) {
                array[i] = it.next();
            }
            return array;
        }

        @SuppressWarnings("unchecked")
        static <T> T asPrimitives(Class<?> primitiveClass, Iterable<?> values) {
            final Object[] array = Array.ofAll(values).toJavaArray();
            final ArrayType<T> type = of((Class<T>) primitiveClass);
            final Object results = type.newInstance(array.length);
            for (int i = 0; i < array.length; i++) {
                type.setAt(results, i, (T) array[i]);
            }
            return (T) results;
        }

        ${types.keys.toSeq.gen(arrayType =>
          genArrayType(arrayType)(im, packageName, arrayType.capitalize + className)
        )(using "\n\n")}
    }
  """)

  def genArrayType(arrayType: String)(im: ImportManager, packageName: String, className: String): String = {
    val wrapperType = types(arrayType)
    val isPrimitive = arrayType != "Object"

    xs"""
      final class $className implements ArrayType<$wrapperType>, ${im.getType("java.io.Serializable")} {
          private static final long serialVersionUID = 1L;
          static final $className INSTANCE = new $className();
          static final $arrayType[] EMPTY = new $arrayType[0];

          private static $arrayType[] cast(Object array) { return ($arrayType[]) array; }

          @Override
          public Class<$wrapperType> type() { return $arrayType.class; }

          @Override
          public $arrayType[] empty() { return EMPTY; }

          @Override
          public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

          @Override
          public $wrapperType getAt(Object array, int index) { return cast(array)[index]; }

          @Override
          public void setAt(Object array, int index, $wrapperType value) ${if (isPrimitive) "throws ClassCastException " else ""}{
              ${if (isPrimitive)
              """if (value != null) {
                |    cast(array)[index] = value;
                |} else {
                |    throw new ClassCastException();
                |}""".stripMargin
            else "cast(array)[index] = value;" }
          }

          @Override
          public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
              return (size > 0)
                      ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                      : new $arrayType[arraySize];
          }
          private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
              final $arrayType[] result = new $arrayType[arraySize];
              System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
              return result;
          }
      }
    """
  }
}
