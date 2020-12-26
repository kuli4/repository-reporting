package pro.kuli4.repository.maprocessor.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface OuterRequestMapper<T, K> {

    K map(T entity);

    default Collection<K> mapAll(Iterable<T> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(this::map).collect(Collectors.toList());
    }

}
