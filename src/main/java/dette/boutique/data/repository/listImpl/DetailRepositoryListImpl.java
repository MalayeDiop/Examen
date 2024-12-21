package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repo.impl.RepositoryListImpl;
import dette.boutique.data.entities.Details;
import dette.boutique.data.repository.interfaces.DetailRepository;

public class DetailRepositoryListImpl extends RepositoryListImpl<Details> implements DetailRepository {
    private List<Details> detailsList = new ArrayList<>();

    @Override
    public boolean insert(Details element) {
        detailsList.add(element);
        System.out.println("Détail ajouté : " + element);
        return false;
    }

    @Override
    public List<Details> selectAll() {
        return detailsList;
    }

    @Override
    public Details findById(int id) {
        for (Details detail : detailsList) {
            if (detail.getId() == id) {
                return detail;
            }
        }
        return null;
    }

    @Override
    public boolean update(Details element) {
        for (int i = 0; i < detailsList.size(); i++) {
            if (detailsList.get(i).getId() == element.getId()) {
                detailsList.set(i, element);
                return true;
            }
        }
        return false;
    }

    @Override
    public void find(Details object) {
        for (Details detail : detailsList) {
            if (detail.equals(object)) {
                System.out.println("Détail trouvé : " + detail);
                return;
            }
        }
        System.out.println("Aucun détail trouvé pour : " + object);
    }

    @Override
    public void remove(Details object) {
        if (detailsList.remove(object)) {
            System.out.println("Détail supprimé : " + object);
        } else {
            System.out.println("Détail non trouvé pour suppression.");
        }
    }
    
}