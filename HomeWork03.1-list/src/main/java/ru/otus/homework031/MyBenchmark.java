package ru.otus.homework031;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)

public class MyBenchmark {
    @State(Scope.Thread)
    public static class ListsState {
        private List<Integer> arrayList = new ArrayList<>();
        private List<Integer> myArrayList = new MyArrayList<>();

        @Setup(Level.Iteration)
        public void setup() {
            for (int i = 0; i < 1000; i++) {
                arrayList.add(i);
                myArrayList.add(i);
            }
        }
    }

    @Benchmark
    public void arrayListInsert(ListsState state, Blackhole blackhole) {
        insert(state.arrayList, 7777);
        blackhole.consume(state.arrayList);
    }

    @Benchmark
    public void myArrayListInsert(ListsState state, Blackhole blackhole) {
        insert(state.myArrayList, 7777);
        blackhole.consume(state.myArrayList);
    }

    private static <T> void insert(List<T> list, T value) {
        for (int i = 0; i < 100; i++) {
            list.add(350, value);
        }
    }
    
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(50)
                .measurementIterations(50)
                .build();

        new Runner(options).run();
    }
}
