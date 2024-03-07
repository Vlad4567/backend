package com.example.demo;

import java.util.BitSet;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PrimeStream {
    private static List<Integer> sieveOfEratosthenes(int limit) {
        BitSet isComposite = new BitSet(limit + 1);

        return IntStream.rangeClosed(2, limit)
                .filter(i -> !isComposite.get(i))
                .peek(prime -> IntStream.iterate(prime * prime, j -> j <= limit, j -> j + prime)
                        .forEach(isComposite::set))
                .boxed()
                .toList();
    }

    public static Stream<Integer> primeStream() {
        return Stream.iterate(sieveOfEratosthenes(150), primes -> sieveOfEratosthenes(primes.get(primes.size() - 1) * 2))
                .flatMap(List::stream)
                .distinct();
    }
}
