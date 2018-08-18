using System;

using MyDotey.SCF.Facade;
using MyDotey.SCF.Source.StringProperty.PropertiesFile;

namespace MyDotey.SCF.BP.Component
{
    /**
     * @author koqizhao
     *
     * Jul 19, 2018
     * 
     * The component defines how the properties are configured!
     */
    public class OwnManagerComponent
    {
        private StringProperties _properties;

        public OwnManagerComponent()
        {
            InitConfig();
        }

        /**
         * use the following methods to get your properties / property values
         *      getProperties().getSomeProperty
         *      getProperties().getSomePropertyValue
         *      getProperties().getManager()
         */
        protected StringProperties GetProperties()
        {
            return _properties;
        }

        private void InitConfig()
        {
            // create a non-dynamic K/V (String/String) configuration source
            PropertiesFileConfigurationSourceConfig sourceConfig = StringPropertySources
                    .NewPropertiesFileSourceConfigBuilder().SetName("properties-file")
                    .SetFileName("own-component.properties").Build();
            IConfigurationSource source1 = StringPropertySources.NewPropertiesFileSource(sourceConfig);

            // create other dynamic or non-dynamic sources
            // ...

            // create a configuration manager with single source
            ConfigurationManagerConfig managerConfig = ConfigurationManagers.NewConfigBuilder().SetName("own-component")
                    .AddSource(1, source1)
                    // Add other sources
                    .Build();
            IConfigurationManager manager = ConfigurationManagers.NewManager(managerConfig);

            // create a StringProperties facade tool
            _properties = new StringProperties(manager);
        }

        public void YourComponentApi()
        {
            Console.WriteLine("OwnManagerComponent is doing something");
        }
    }
}