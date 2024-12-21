package dette.boutique.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import dette.boutique.services.interfaces.YamlService;

public class YamlServiceImpl implements YamlService {

    private String path="META-INF/conf.yaml";

    @Override
    public Map<String, Object> loadYaml() {
        return this.loadYaml(path);
    }

    @Override
    public Map<String, Object> loadYaml(String path) {
        Yaml yaml = new Yaml();
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("Fichier non trouvé : " + path);
                }
                return yaml.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException("Échec du chargement du fichier YAML : " + path, e);
        }
    }
  
}