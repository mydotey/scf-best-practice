using System;
using System.Collections.Generic;
using System.Threading;

using MyDotey.SCF.Facade;
using MyDotey.SCF.Type.String;
using MyDotey.SCF.Source.StringProperty.PropertiesFile;

namespace MyDotey.SCF.BP.LittleApp
{
    /**
     * @author koqizhao
     *
     * Jul 19, 2018
     */
    public class RawManager
    {
        private static IConfigurationManager _manager;

        private static IProperty<string, string> _appId;
        private static IProperty<string, string> _appName;
        private static IProperty<string, List<string>> _userList;
        private static IProperty<string, Dictionary<string, string>> _userData;
        private static IProperty<string, int?> _sleepTime;
        private static IProperty<string, MyCustomType> _myCustomData;

        private static void InitConfig()
        {
            // create a non-dynamic K/V (string/string) configuration source
            PropertiesFileConfigurationSourceConfig sourceConfig = StringPropertySources
                    .NewPropertiesFileSourceConfigBuilder().SetName("app-properties-file").SetFileName("app.properties")
                    .Build();
            IConfigurationSource source = StringPropertySources.NewPropertiesFileSource(sourceConfig);

            // create a configuration manager with single source
            ConfigurationManagerConfig managerConfig = ConfigurationManagers.NewConfigBuilder().SetName("little-app")
                    .AddSource(1, source).Build();
            _manager = ConfigurationManagers.NewManager(managerConfig);

            // default to null
            PropertyConfig<string, string> propertyConfig = ConfigurationProperties.NewConfigBuilder<string, string>()
                     .SetKey("app.id").Build();
            _appId = _manager.GetProperty(propertyConfig);

            // default to "unknown"
            PropertyConfig<string, string> propertyConfig2 = ConfigurationProperties.NewConfigBuilder<string, string>()
                        .SetKey("app.name").SetDefaultValue("unknown").Build();
            _appName = _manager.GetProperty(propertyConfig2);

            // default to empty list
            PropertyConfig<string, List<string>> propertyConfig3 = ConfigurationProperties
                    .NewConfigBuilder<string, List<string>>().SetKey("user.list")
                    .SetDefaultValue(new List<string>()).AddValueConverter(StringToListConverter.Default).Build();
            _userList = _manager.GetProperty(propertyConfig3);

            // default to empty map
            PropertyConfig<string, Dictionary<string, string>> propertyConfig4 = ConfigurationProperties
                    .NewConfigBuilder<string, Dictionary<string, string>>().SetKey("user.data")
                    .SetDefaultValue(new Dictionary<string, string>()).AddValueConverter(StringToDictionaryConverter.Default).Build();
            _userData = _manager.GetProperty(propertyConfig4);

            // default to 1000, if value in app._properties is invalid, ignore the invalid value
            PropertyConfig<string, int?> propertyConfig5 = ConfigurationProperties.NewConfigBuilder<string, int?>()
                        .SetKey("sleep.time").SetDefaultValue(1000)
                        .AddValueConverter(StringToIntConverter.Default).SetValueFilter(v => v < 0 ? null : v).Build();
            _sleepTime = _manager.GetProperty(propertyConfig5);

            // custom type property
            PropertyConfig<string, MyCustomType> propertyConfig6 = ConfigurationProperties
                    .NewConfigBuilder<string, MyCustomType>().SetKey("my-custom-type-property")
                    .AddValueConverter(MyCustomType.Converter).Build();
            _myCustomData = _manager.GetProperty(propertyConfig6);
        }

        public static void DoMain(string[] args)
        {
            InitConfig();

            // show properties
            Console.WriteLine("app.id: " + _appId.Value);
            Console.WriteLine("app.name: " + _appName.Value);
            Console.WriteLine("user.list: " + _userList.Value);
            Console.WriteLine("user.data: " + _userData.Value);
            Console.WriteLine("sleep.time: " + _sleepTime.Value);

            // get some property value for non-stable property (the key is not stable, not sure it exists or not)
            PropertyConfig<string, string> propertyConfig = ConfigurationProperties.NewConfigBuilder<string, string>()
                    .SetKey("some.data").SetDefaultValue("not-sure").Build();
            string someIPropertyValue = _manager.GetPropertyValue(propertyConfig);
            Console.WriteLine();
            Console.WriteLine("some.data: " + someIPropertyValue);

            Console.WriteLine();

            Console.WriteLine("Now sleep {0} ms ...", _sleepTime.Value);
            Thread.Sleep(_sleepTime.Value.Value);

            Console.WriteLine();
            Console.WriteLine("My custom property data: " + _myCustomData.Value);

            Console.WriteLine();
            for (int i = 0; i < _myCustomData.Value.Times; i++)
                Console.WriteLine("{0} {1}!", _myCustomData.Value.Say, _myCustomData.Value.Name);

            Console.WriteLine();
            Console.WriteLine("Bye!");
        }
    }
}