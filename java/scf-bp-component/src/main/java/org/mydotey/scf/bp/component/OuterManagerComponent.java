package org.mydotey.scf.bp.component;

import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.facade.StringProperties;

/**
 * @author koqizhao
 *
 * Jul 19, 2018
 * 
 * The component only uses config, don't care how the properties are configured!
 * The component's user knows that!
 */
public class OuterManagerComponent {

    private StringProperties _properties;

    public OuterManagerComponent(StringProperties properties) {
        _properties = properties;
    }

    public OuterManagerComponent(ConfigurationManager manager) {
        _properties = new StringProperties(manager);
    }

    /**
     * use the following methods to get your properties / property values
     *      getProperties().getSomeProperty
     *      getProperties().getSomePropertyValue
     *      getProperties().getManager()
     */
    protected StringProperties getProperties() {
        return _properties;
    }

    public void yourComponentApi() {

    }

}
