using System;
using System.Collections.Generic;
using System.Threading;

using MyDotey.SCF.Facade;
using MyDotey.SCF.Type.String;
using MyDotey.SCF.Source.StringProperty.PropertiesFile;
using MyDotey.SCF.Source.StringProperty.MemoryDictionary;

namespace MyDotey.SCF.BP.LittleApp
{
    public class TwoSource
    {
        private static StringProperties _properties;
        private static MemoryDictionaryConfigurationSource _systemPropertiesSource;

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

            // crate a dynamic K/V (string/string) configuration source
            _systemPropertiesSource = StringPropertySources.NewMemoryDictionarySource("system-property");

            // create a configuration manager with 2 sources, system property has higher priority then app.properties
            ConfigurationManagerConfig managerConfig = ConfigurationManagers.NewConfigBuilder().SetName("little-app")
                    .AddSource(1, source).AddSource(2, _systemPropertiesSource).Build();
            IConfigurationManager manager = ConfigurationManagers.NewManager(managerConfig);

            // Add a log listener
            manager.OnChange += (o, e) =>
                Console.WriteLine("\nproperty {0} changed, old value: {1}, new value: {2}, changeTime: {3}",
                    e.Property, e.OldValue, e.NewValue, e.ChangeTime);

            // create a StringProperties facade tool
            _properties = new StringProperties(manager);

            // default to null
            _appId = _properties.GetStringProperty("app.id");

            // default to "unknown"
            _appName = _properties.GetStringProperty("app.name", "unknown");
            // Add change listener to app.name
            _appName.OnChange += (o, e) => Console.WriteLine("\napp.name changed, maybe we need do something");

            // default to empty list
            _userList = _properties.GetListProperty("user.list", new List<string>());

            // default to empty map
            _userData = _properties.GetDictionaryProperty("user.data", new Dictionary<string, string>());

            // default to 1000, if value in app._properties is invalid, ignore the invalid value
            _sleepTime = _properties.GetIntProperty("sleep.time", 1000, v => v < 0 ? null : v);

            // custom type property
            _myCustomData = _properties.GetProperty("my-custom-type-property", MyCustomType.CONVERTER);
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

            // Get some property value for non-stable property (the key is not stable, not sure it exists or not)
            string somePropertyValue = _properties.GetStringPropertyValue("some.data", "not-sure");
            Console.WriteLine();
            Console.WriteLine("some.data: " + somePropertyValue);

            Console.WriteLine();

            Console.WriteLine("Now sleep {0} ms ...", _sleepTime.Value);
            Thread.Sleep(_sleepTime.Value.Value);

            Console.WriteLine();
            Console.WriteLine("My custom property data: " + _myCustomData.Value);

            Console.WriteLine();
            for (int i = 0; i < _myCustomData.Value.Times; i++)
                Console.WriteLine("{0} {1}!", _myCustomData.Value.Say, _myCustomData.Value.Name);

            // system source has higher priority than app.properties,
            // change the system property, configuration manager will auto update it
            // the change listener will be auto-called as well
            _systemPropertiesSource.SetPropertyValue("app.name", "new-little-app");

            Console.WriteLine();
            Console.WriteLine("Bye!");
        }
    }
}