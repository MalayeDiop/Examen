package dette.boutique.core.repo.impl;

import java.util.ArrayList;
import java.util.List;
import dette.boutique.core.repo.Repository;
import dette.boutique.data.entities.Identifiable;

public abstract class RepositoryListImpl<T> implements Repository<T> {
    protected List<T> data = new ArrayList<>();

    public void find(T object) {
        int index = data.indexOf(object);
        if (index == -1) {
            throw new IllegalArgumentException("Objet non trouvé !");
        }
        data.get(index);
    }

    @Override
    public boolean insert(T element) {
        data.add(element);
        return true; 
    }

    @Override
    public void remove(T object) {
        if (!data.remove(object)) {
            throw new IllegalArgumentException("Impossible de supprimer : objet non trouvé !");
        }
    }

    @Override
    public List<T> selectAll() {
        return new ArrayList<>(data);
    }

    @Override
    public T findById(int id) {
        return data.stream()
               .filter(element -> element instanceof Identifiable && ((Identifiable) element).getId() == id)
               .findFirst()
               .orElseThrow(() -> new IllegalArgumentException("Objet avec l'id " + id + " non trouvé !"));
    }

    @Override
    public boolean update(T element) {
        if (element instanceof Identifiable identifiableElement) {
            int id = identifiableElement.getId();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i) instanceof Identifiable existingElement && existingElement.getId() == id) {
                    data.set(i, element);
                    return true;
                }
            }
        }
        throw new IllegalArgumentException("Objet à mettre à jour non trouvé !");
    }

}