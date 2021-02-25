package RocioBorderProtection;

public class Pair<K, V> {

  public final K k;
  public final V v;

  public Pair(K k, V v) {
    this.k = k;
    this.v = v;
  }

  public static <K, V> Pair<K, V> getInstance(K k, V v) {
    return new Pair<>(k, v);
  }

  public K getKey() {
    return k;
  }

  public V getValue() {
    return v;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pair)) {
      return false;
    }

    Pair<?, ?> pair = (Pair<?, ?>) o;

    if (!k.equals(pair.k)) {
      return false;
    }
    return v != null ? v.equals(pair.v) : pair.v == null;
  }

  @Override
  public int hashCode() {
    int result = k.hashCode();
    result = 31 * result + (v != null ? v.hashCode() : 0);
    return result;
  }
}
