package pro.kuli4.repository.common.functional;

@FunctionalInterface
public interface HandlerWithException<V> {
    void handle(V v) throws Exception;
}
