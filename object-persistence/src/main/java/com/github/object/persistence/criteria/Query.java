package com.github.object.persistence.criteria;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Query<T> {

    CompletableFuture<List<T>> selectWhere(Predicate predicate);

    CompletableFuture<Long> updateWhere(Map<String, Object> fieldValueMap, Predicate predicate);

    CompletableFuture<Void> deleteWhere(Predicate predicate);

}
