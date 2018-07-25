package org.mydotey.scf.bp.component.labeled;

import java.util.ArrayList;

import org.mydotey.scf.Property;
import org.mydotey.scf.facade.LabeledConfigurationProperties;
import org.mydotey.scf.facade.LabeledStringProperties;
import org.mydotey.scf.labeled.LabeledConfigurationManager;
import org.mydotey.scf.labeled.LabeledKey;
import org.mydotey.scf.labeled.PropertyLabel;
import org.mydotey.scf.labeled.PropertyLabels;

/**
 * @author koqizhao
 *
 * Jul 25, 2018
 */
public class LabeledPropertyComponent {

    private PropertyLabels _propertyLabels;

    private LabeledStringProperties _properties;

    private Property<LabeledKey<String>, Integer> _requestTimeout;

    public LabeledPropertyComponent(LabeledConfigurationManager manager) {
        _properties = new LabeledStringProperties(manager);
        initLabels();

        _requestTimeout = _properties.getIntProperty(toLabeledKey("request.timeout"), 1000);
    }

    // priority: (key, app, dc) > (key, app) > (key, dc) > key
    private void initLabels() {
        PropertyLabel dcLabel = LabeledConfigurationProperties.newLabel("dc", "shanghai");
        PropertyLabel appLabel = LabeledConfigurationProperties.newLabel("app", "10000");
        ArrayList<PropertyLabel> dcLabels = new ArrayList<>();
        dcLabels.add(dcLabel);
        ArrayList<PropertyLabel> appLabels = new ArrayList<>();
        appLabels.add(appLabel);
        ArrayList<PropertyLabel> allLabels = new ArrayList<>();
        allLabels.add(dcLabel);
        allLabels.add(appLabel);

        PropertyLabels alternative1 = LabeledConfigurationProperties.newLabels(dcLabels, PropertyLabels.EMPTY);
        PropertyLabels alternative2 = LabeledConfigurationProperties.newLabels(appLabels, alternative1);
        _propertyLabels = LabeledConfigurationProperties.newLabels(allLabels, alternative2);
    }

    protected LabeledKey<String> toLabeledKey(String key) {
        return LabeledConfigurationProperties.<String> newKeyBuilder().setKey(key).setPropertyLabels(_propertyLabels)
                .build();
    }

    /**
     * use the following methods to get your properties / property values
     *      LabeledKey&lt;String&gt; labeledKey = toLabeledKey(key);
     *      getProperties().getSomeProperty
     *      getProperties().getSomePropertyValue
     *      getProperties().getManager()
     */
    protected LabeledStringProperties getProperties() {
        return _properties;
    }

    public void yourComponentApi() {
        System.out.println("OuterManagerComponent is doing something");
        System.out.println("request.timeout is: " + _requestTimeout.getValue());
    }

}
