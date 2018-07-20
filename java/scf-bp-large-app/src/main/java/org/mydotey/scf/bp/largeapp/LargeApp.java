package org.mydotey.scf.bp.largeapp;

import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.ConfigurationManagerConfig;
import org.mydotey.scf.ConfigurationSource;
import org.mydotey.scf.bp.component.OuterManagerComponent;
import org.mydotey.scf.bp.component.OwnManagerComponent;
import org.mydotey.scf.facade.ConfigurationManagers;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.facade.StringPropertySources;
import org.mydotey.scf.source.stringproperty.propertiesfile.PropertiesFileConfigurationSourceConfig;

/**
 * @author koqizhao
 *
 * Jul 20, 2018
 */
public class LargeApp {

    public static void main(String[] args) {
        LargeApp app = new LargeApp();
        app.doAppThing();
    }

    private OwnManagerComponent _ownManagerComponent;
    private OuterManagerComponent _outerManagerComponent;
    private StringProperties _appProperties;

    public LargeApp() {
        // OwnManagerComponent both users config and defines how to config properties itself
        _ownManagerComponent = new OwnManagerComponent();

        // OuterManagerComponent only uses config, let the component's user to define how to config
        // each user can define different config way
        PropertiesFileConfigurationSourceConfig sourceConfig = StringPropertySources
                .newPropertiesFileSourceConfigBuilder().setName("properties-file")
                .setFileName("outer-component.properties").build();
        ConfigurationSource source = StringPropertySources.newPropertiesFileSource(sourceConfig);
        ConfigurationManagerConfig managerConfig = ConfigurationManagers.newConfigBuilder().setName("outer-component")
                .addSource(1, source).build();
        ConfigurationManager manager = ConfigurationManagers.newManager(managerConfig);
        _outerManagerComponent = new OuterManagerComponent(manager);

        // large app's config
        PropertiesFileConfigurationSourceConfig sourceConfig2 = StringPropertySources
                .newPropertiesFileSourceConfigBuilder().setName("properties-file").setFileName("app.properties")
                .build();
        ConfigurationSource source2 = StringPropertySources.newPropertiesFileSource(sourceConfig2);
        ConfigurationManagerConfig managerConfig2 = ConfigurationManagers.newConfigBuilder().setName("outer-component")
                .addSource(1, source2).build();
        ConfigurationManager manager2 = ConfigurationManagers.newManager(managerConfig2);
        _appProperties = new StringProperties(manager2);
    }

    public void doAppThing() {
        // do component work
        _ownManagerComponent.yourComponentApi();
        _outerManagerComponent.yourComponentApi();

        // do app owner work
        String appName = _appProperties.getStringPropertyValue("app.name");
        System.out.printf("I'm %s. I use my app config to do work!\n", appName);
    }

}
