package cn.ecosync.aiot.query;

/**
 * @author yan
 * @since 2024
 */
public interface QueryHandler<T extends Query<R>, R> {
    R handle(T query);
}
