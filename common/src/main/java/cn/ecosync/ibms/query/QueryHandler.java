package cn.ecosync.ibms.query;

public interface QueryHandler<T extends Query<R>, R> {
    R handle(T query);
}
