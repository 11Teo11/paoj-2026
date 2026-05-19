package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CustomCollectors {

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {

        // Container mutabil folosit în timpul colectării
        class Agg {
            Map<String, Long> byCountry = new HashMap<>();
            Map<String, Long> byChannel = new HashMap<>();
            BigDecimal total = BigDecimal.ZERO;
            List<Transaction> all = new ArrayList<>();

            void add(Transaction tx) {
                byCountry.merge(tx.getCountry(), 1L, Long::sum);
                byChannel.merge(tx.getChannel(), 1L, Long::sum);
                total = total.add(tx.getAmount());
                all.add(tx);
            }

            // combine pentru stream paralel
            Agg combine(Agg other) {
                other.byCountry.forEach((k, v) -> byCountry.merge(k, v, Long::sum));
                other.byChannel.forEach((k, v) -> byChannel.merge(k, v, Long::sum));
                total = total.add(other.total);
                all.addAll(other.all);
                return this;
            }

            // finisher — construiește snapshot-ul imutabil
            Snapshot finish() {
                List<Transaction> top = all.stream()
                        .sorted(Comparator.comparing(Transaction::getAmount).reversed()
                                .thenComparingInt(Transaction::getId))
                        .limit(topN)
                        .collect(Collectors.toList());
                return new Snapshot(byCountry, byChannel, total, top);
            }
        }

        return Collector.of(
                Agg::new,
                Agg::add,
                Agg::combine,
                Agg::finish,
                Collector.Characteristics.UNORDERED
        );
    }
}