package dette.boutique.core.service;

import java.util.List;

public interface Service<T> {
    void save(T element);
    boolean update(T element);
    void selectById(int id);
    List<T> selectAll();
}