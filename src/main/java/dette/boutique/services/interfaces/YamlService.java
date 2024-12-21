package dette.boutique.services.interfaces;

import java.util.Map;

public interface YamlService{
    Map<String, Object> loadYaml();
    Map<String, Object> loadYaml(String path);
}