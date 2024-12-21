package dette.boutique.services.list;

import java.util.List;
import java.util.stream.Collectors;

import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.repository.interfaces.DetailRepository;
import dette.boutique.services.interfaces.DetailServiceImpl;

public class DetailService implements DetailServiceImpl {
    private DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository){
        this.detailRepository = detailRepository;
    }

    @Override
    public void save(Details element) {
        detailRepository.insert(element);
    }

    @Override
    public boolean update(Details element) {
        return detailRepository.update(element);
    }

    @Override
    public void selectById(int id) {
        detailRepository.findById(id);
    }

    @Override
    public List<Details> selectAll() {
        return detailRepository.selectAll();
    }

    public List<Details> findByClient(Client client) {
        return detailRepository.selectAll().stream()
                .filter(detail -> detail.getDette().getClient().equals(client))
                .collect(Collectors.toList());
    }
    
}