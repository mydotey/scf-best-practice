package org.mydotey.scf.bp.component.labeled;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mydotey.scf.yaml.YamlFileConfigurationSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author koqizhao
 *
 * Jul 25, 2018
 * 
 * for sequence root YAML file
 */
public class YamlDcConfigurationSource extends AbstractDcConfigurationSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlDcConfigurationSource.class);

    private Map<DcSetting, DcSetting> _dcSettings;

    @SuppressWarnings("unchecked")
    public YamlDcConfigurationSource(YamlFileConfigurationSourceConfig config) {
        super(config);

        _dcSettings = new HashMap<>();
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(config.getFileName())) {
            if (is == null) {
                LOGGER.warn("file not found: {}", config.getFileName());
                return;
            }

            Object properties = new Yaml().load(is);
            if (properties == null || !(properties instanceof List)) {
                LOGGER.error(YamlDcConfigurationSource.class.getSimpleName()
                        + " only accepts YAML file with Sequence root. Current is Mapping or Scala. YAML file: "
                        + config.getFileName());
                return;
            }

            List<Map<Object, Object>> listProperties = (List<Map<Object, Object>>) properties;
            ObjectMapper objectMapper = new ObjectMapper();
            listProperties.forEach(m -> {
                DcSetting dcSetting = objectMapper.convertValue(m, DcSetting.class);
                _dcSettings.put(dcSetting, dcSetting);
            });

        } catch (Exception e) {
            LOGGER.warn("failed to load yaml file: " + config.getFileName(), e);
        }
    }

    @Override
    protected DcSetting getPropertyValue(DcSetting setting) {
        return _dcSettings.get(setting);
    }

}
