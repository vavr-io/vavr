package io.vavr;

import io.vavr.collection.LinkedChampSet;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Scratch {
    public static void main(String[] args) {
        LinkedChampSet<Integer> s = LinkedChampSet.<Integer>of();
        s = s.addAll(IntStream.range(0, 1000000).boxed().collect(Collectors.toList()));
        s.remove(5).add(5);
    }
}
