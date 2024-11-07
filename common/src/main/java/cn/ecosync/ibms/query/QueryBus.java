package cn.ecosync.ibms.query;

public interface QueryBus {
    <T extends Query<R>, R> R execute(T query);
}
