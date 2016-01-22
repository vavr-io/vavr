package javaslang.control;

import java.util.function.Consumer;

class IntegerConsumer implements Consumer<Integer> {

    public int value;

    @Override
    public void accept(Integer i) {
        this.value = i;
    }
}
