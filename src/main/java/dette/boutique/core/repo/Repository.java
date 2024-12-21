package dette.boutique.core.repo;

import java.util.List;

public interface Repository<T> {
    boolean insert(T element);
    List<T> selectAll();
    T findById(int id);
    boolean update(T element);
    void remove(T object);
}