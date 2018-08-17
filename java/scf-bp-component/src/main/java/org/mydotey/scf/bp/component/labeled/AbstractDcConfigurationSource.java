package org.mydotey.scf.bp.component.labeled;

import java.util.Collection;
import java.util.Objects;

import org.mydotey.scf.ConfigurationSourceConfig;
import org.mydotey.scf.labeled.AbstractLabeledConfigurationSource;
import org.mydotey.scf.labeled.PropertyLabel;

/**
 * @author koqizhao
 *
 * May 17, 2018
 */
public abstract class AbstractDcConfigurationSource
        extends AbstractLabeledConfigurationSource<ConfigurationSourceConfig> {

    public AbstractDcConfigurationSource(ConfigurationSourceConfig config) {
        super(config);
    }

    @Override
    protected Object getPropertyValue(Object key, Collection<PropertyLabel> labels) {
        if (key.getClass() != String.class)
            return null;

        String app = null;
        String dc = null;
        if (labels != null) {
            for (PropertyLabel l : labels) {
                if (Objects.equals(l.getKey(), DcSetting.APP_KEY))
                    app = (String) l.getValue();

                if (Objects.equals(l.getKey(), DcSetting.DC_KEY))
                    dc = (String) l.getValue();
            }
        }

        return getPropertyValue((String) key, app, dc);
    }

    protected String getPropertyValue(String key, String app, String dc) {
        DcSetting dcSetting = getPropertyValue(new DcSetting(key, app, dc, null));
        return dcSetting == null ? null : dcSetting.getValue();
    }

    protected abstract DcSetting getPropertyValue(DcSetting setting);

}
