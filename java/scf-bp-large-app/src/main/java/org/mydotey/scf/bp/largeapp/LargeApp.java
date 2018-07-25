package org.mydotey.scf.bp.largeapp;

import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.ConfigurationManagerConfig;
import org.mydotey.scf.ConfigurationSource;
import org.mydotey.scf.bp.component.OuterManagerComponent;
import org.mydotey.scf.bp.component.OwnManagerComponent;
import org.mydotey.scf.bp.component.labeled.DynamicDcConfigurationSource;
import org.mydotey.scf.bp.component.labeled.LabeledPropertyComponent;
import org.mydotey.scf.bp.component.labeled.YamlDcConfigurationSource;
import org.mydotey.scf.facade.ConfigurationManagers;
import org.mydotey.scf.facade.ConfigurationSources;
import org.mydotey.scf.facade.LabeledConfigurationManagers;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.facade.StringPropertySources;
import org.mydotey.scf.labeled.LabeledConfigurationManager;
import org.mydotey.scf.source.stringproperty.propertiesfile.PropertiesFileConfigurationSourceConfig;
import org.mydotey.scf.yaml.YamlFileConfigurationSourceConfig;

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
    private LabeledPropertyComponent _labeledPropertyComponent;
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

        // LabeledPropertyComponent only uses config, let the component's user to define how to config
        // each user can define different config way
        YamlFileConfigurationSourceConfig labeledSourceConfig = new YamlFileConfigurationSourceConfig.Builder()
                .setName("yaml-file").setFileName("labeled-property-component.yaml").build();
        ConfigurationSource labeledSource = new YamlDcConfigurationSource(labeledSourceConfig);
        DynamicDcConfigurationSource dynamicLabeledSource = new DynamicDcConfigurationSource(
                ConfigurationSources.newConfig("dynamic-labeled-source"));
        ConfigurationManagerConfig managerConfig2 = ConfigurationManagers.newConfigBuilder()
                .setName("labeled-property-component").addSource(1, labeledSource).addSource(2, dynamicLabeledSource)
                .build();
        LabeledConfigurationManager manager2 = LabeledConfigurationManagers.newManager(managerConfig2);
        _labeledPropertyComponent = new LabeledPropertyComponent(manager2);

        // large app's config
        PropertiesFileConfigurationSourceConfig sourceConfig2 = StringPropertySources
                .newPropertiesFileSourceConfigBuilder().setName("properties-file").setFileName("app.properties")
                .build();
        ConfigurationSource source2 = StringPropertySources.newPropertiesFileSource(sourceConfig2);
        ConfigurationManagerConfig managerConfig3 = ConfigurationManagers.newConfigBuilder().setName("outer-component")
                .addSource(1, source2).build();
        ConfigurationManager manager3 = ConfigurationManagers.newManager(managerConfig3);
        _appProperties = new StringProperties(manager3);
    }

    public void doAppThing() {
        // do component work
        _ownManagerComponent.yourComponentApi();
        _outerManagerComponent.yourComponentApi();
        _labeledPropertyComponent.yourComponentApi();

        // do app owner work
        String appName = _appProperties.getStringPropertyValue("app.name");
        System.out.printf("I'm %s. I use my app config to do work!\n", appName);
    }

}
