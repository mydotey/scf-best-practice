package org.mydotey.scf.bp.component.labeled;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.mydotey.scf.ConfigurationSourceConfig;

/**
 * @author koqizhao
 *
 * May 17, 2018
 */
public class DynamicDcConfigurationSource extends AbstractDcConfigurationSource {

    private Map<DcSetting, DcSetting> _dcSettings = new ConcurrentHashMap<>();

    public DynamicDcConfigurationSource(ConfigurationSourceConfig config) {
        super(config);
    }

    public void updateSetting(DcSetting setting) {
        Objects.requireNonNull(setting, "setting is null");
        Objects.requireNonNull(setting.getKey(), "setting.key is null");

        DcSetting oldValue = _dcSettings.get(setting);
        if (oldValue != null) {
            if (Objects.equals(oldValue.getValue(), setting.getValue()))
                return;
        }

        setting = setting.clone();
        _dcSettings.put(setting, setting);

        raiseChangeEvent();
    }

    public void removeSetting(DcSetting setting) {
        Objects.requireNonNull(setting, "setting is null");
        Objects.requireNonNull(setting.getKey(), "setting.key is null");

        DcSetting oldValue = _dcSettings.remove(setting);

        if (oldValue == null)
            return;

        raiseChangeEvent();
    }

    @Override
    protected DcSetting getPropertyValue(DcSetting setting) {
        return _dcSettings.get(setting);
    }

}
