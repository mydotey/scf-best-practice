using System;
using System.Collections.Generic;

using MyDotey.SCF.Facade;
using MyDotey.SCF.Labeled;
using MyDotey.SCF.Source.StringProperty.PropertiesFile;
using MyDotey.SCF.Yaml;

using MyDotey.SCF.BP.Component;
using MyDotey.SCF.BP.Component.Labeled;

namespace MyDotey.SCF.BP.LargeApp
{
    /**
     * @author koqizhao
     *
     * Jul 20, 2018
     */
    public class LargeApp
    {
        public static void DoMain(String[] args)
        {
            LargeApp app = new LargeApp();
            app.DoAppThing();
        }

        private OwnManagerComponent _ownManagerComponent;
        private OuterManagerComponent _outerManagerComponent;
        private LabeledPropertyComponent _labeledPropertyComponent;
        private StringProperties _appProperties;

        public LargeApp()
        {
            // OwnManagerComponent both users config and defines how to config properties itself
            _ownManagerComponent = new OwnManagerComponent();

            // OuterManagerComponent only uses config, let the component's user to define how to config
            // each user can define different config way
            PropertiesFileConfigurationSourceConfig sourceConfig = StringPropertySources
                    .NewPropertiesFileSourceConfigBuilder().SetName("properties-file")
                    .SetFileName("outer-component.properties").Build();
            IConfigurationSource source = StringPropertySources.NewPropertiesFileSource(sourceConfig);
            ConfigurationManagerConfig managerConfig = ConfigurationManagers.NewConfigBuilder().SetName("outer-component")
                    .AddSource(1, source).Build();
            IConfigurationManager manager = ConfigurationManagers.NewManager(managerConfig);
            _outerManagerComponent = new OuterManagerComponent(manager);

            // LabeledPropertyComponent only uses config, let the component's user to define how to config
            // each user can define different config way
            YamlFileConfigurationSourceConfig labeledSourceConfig = new YamlFileConfigurationSourceConfig.Builder()
                    .SetName("yaml-file").SetFileName("labeled-property-component.yaml").Build();
            IConfigurationSource labeledSource = new YamlDcConfigurationSource(labeledSourceConfig);
            DynamicDcConfigurationSource dynamicLabeledSource = new DynamicDcConfigurationSource(
                    ConfigurationSources.NewConfig("dynamic-labeled-source"));
            ConfigurationManagerConfig managerConfig2 = ConfigurationManagers.NewConfigBuilder()
                    .SetName("labeled-property-component").AddSource(1, labeledSource).AddSource(2, dynamicLabeledSource)
                    .Build();
            ILabeledConfigurationManager manager2 = LabeledConfigurationManagers.NewManager(managerConfig2);
            _labeledPropertyComponent = new LabeledPropertyComponent(manager2);

            // large app's config
            PropertiesFileConfigurationSourceConfig sourceConfig2 = StringPropertySources
                    .NewPropertiesFileSourceConfigBuilder().SetName("properties-file").SetFileName("app.properties")
                    .Build();
            IConfigurationSource source2 = StringPropertySources.NewPropertiesFileSource(sourceConfig2);
            ConfigurationManagerConfig managerConfig3 = ConfigurationManagers.NewConfigBuilder().SetName("outer-component")
                    .AddSource(1, source2).Build();
            IConfigurationManager manager3 = ConfigurationManagers.NewManager(managerConfig3);
            _appProperties = new StringProperties(manager3);
        }

        public void DoAppThing()
        {
            // do component work
            _ownManagerComponent.YourComponentApi();
            _outerManagerComponent.YourComponentApi();
            _labeledPropertyComponent.YourComponentApi();

            // do app owner work
            String appName = _appProperties.GetStringPropertyValue("app.name");
            Console.WriteLine("I'm {0}. I use my app config to do work!", appName);
        }
    }
}