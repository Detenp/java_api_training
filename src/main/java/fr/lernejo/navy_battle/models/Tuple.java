package fr.lernejo.navy_battle.models;

import java.util.Objects;

public class Tuple <K,V> {
    private final K k;
    private final V v;

    public Tuple(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public Object getK() {
        return k;
    }

    public Object getV() {
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return k.equals(tuple.k) && v.equals(tuple.v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(k, v);
    }
}
