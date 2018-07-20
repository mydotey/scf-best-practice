package org.mydotey.scf.bp.littleapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.ConfigurationManagerConfig;
import org.mydotey.scf.ConfigurationSource;
import org.mydotey.scf.Property;
import org.mydotey.scf.PropertyConfig;
import org.mydotey.scf.facade.ConfigurationManagers;
import org.mydotey.scf.facade.ConfigurationProperties;
import org.mydotey.scf.facade.StringPropertySources;
import org.mydotey.scf.source.stringproperty.propertiesfile.PropertiesFileConfigurationSourceConfig;
import org.mydotey.scf.type.string.StringToIntConverter;
import org.mydotey.scf.type.string.StringToListConverter;
import org.mydotey.scf.type.string.StringToMapConverter;

/**
 * @author koqizhao
 *
 * Jul 19, 2018
 */
public class RawManager {
    private static ConfigurationManager _manager;

    private static Property<String, String> _appId;
    private static Property<String, String> _appName;
    private static Property<String, List<String>> _userList;
    private static Property<String, Map<String, String>> _userData;
    private static Property<String, Integer> _sleepTime;
    private static Property<String, MyCustomType> _myCustomData;

    private static void initConfig() {
        // create a non-dynamic K/V (String/String) configuration source
        PropertiesFileConfigurationSourceConfig sourceConfig = StringPropertySources
                .newPropertiesFileSourceConfigBuilder().setName("app-properties-file").setFileName("app.properties")
                .build();
        ConfigurationSource source = StringPropertySources.newPropertiesFileSource(sourceConfig);

        // create a configuration manager with single source
        ConfigurationManagerConfig managerConfig = ConfigurationManagers.newConfigBuilder().setName("little-app")
                .addSource(1, source).build();
        _manager = ConfigurationManagers.newManager(managerConfig);

        // default to null
        PropertyConfig<String, String> propertyConfig = ConfigurationProperties.<String, String> newConfigBuilder()
                .setKey("app.id").setValueType(String.class).build();
        _appId = _manager.getProperty(propertyConfig);

        // default to "unknown"
        PropertyConfig<String, String> propertyConfig2 = ConfigurationProperties.<String, String> newConfigBuilder()
                .setKey("app.name").setValueType(String.class).setDefaultValue("unknown").build();
        _appName = _manager.getProperty(propertyConfig2);

        // default to empty list
        @SuppressWarnings({ "rawtypes", "unchecked" })
        PropertyConfig<String, List<String>> propertyConfig3 = ConfigurationProperties
                .<String, List<String>> newConfigBuilder().setKey("user.list").setValueType((Class) List.class)
                .setDefaultValue(new ArrayList<>()).addValueConverter(StringToListConverter.DEFAULT).build();
        _userList = _manager.getProperty(propertyConfig3);

        // default to empty map
        @SuppressWarnings({ "rawtypes", "unchecked" })
        PropertyConfig<String, Map<String, String>> propertyConfig4 = ConfigurationProperties
                .<String, Map<String, String>> newConfigBuilder().setKey("user.data").setValueType((Class) Map.class)
                .setDefaultValue(new HashMap<>()).addValueConverter(StringToMapConverter.DEFAULT).build();
        _userData = _manager.getProperty(propertyConfig4);

        // default to 1000, if value in app._properties is invalid, ignore the invalid value
        PropertyConfig<String, Integer> propertyConfig5 = ConfigurationProperties.<String, Integer> newConfigBuilder()
                .setKey("sleep.time").setValueType(Integer.class).setDefaultValue(1000)
                .addValueConverter(StringToIntConverter.DEFAULT).setValueFilter(v -> v < 0 ? null : v).build();
        _sleepTime = _manager.getProperty(propertyConfig5);

        // custom type property
        PropertyConfig<String, MyCustomType> propertyConfig6 = ConfigurationProperties
                .<String, MyCustomType> newConfigBuilder().setKey("my-custom-type-property")
                .setValueType(MyCustomType.class).addValueConverter(MyCustomType.CONVERTER).build();
        _myCustomData = _manager.getProperty(propertyConfig6);
    }

    public static void main(String[] args) throws InterruptedException {
        initConfig();

        // show properties
        System.out.println("app.id: " + _appId.getValue());
        System.out.println("app.name: " + _appName.getValue());
        System.out.println("user.list: " + _userList.getValue());
        System.out.println("user.data: " + _userData.getValue());
        System.out.println("sleep.time: " + _sleepTime.getValue());

        // get some property value for non-stable property (the key is not stable, not sure it exists or not)
        PropertyConfig<String, String> propertyConfig = ConfigurationProperties.<String, String> newConfigBuilder()
                .setKey("some.data").setValueType(String.class).setDefaultValue("not-sure").build();
        String somePropertyValue = _manager.getPropertyValue(propertyConfig);
        System.out.println();
        System.out.println("some.data: " + somePropertyValue);

        System.out.println();

        System.out.printf("Now sleep %d ms ...\n", _sleepTime.getValue());
        Thread.sleep(_sleepTime.getValue());

        System.out.println();
        System.out.println("My custom property data: " + _myCustomData.getValue());

        System.out.println();
        for (int i = 0; i < _myCustomData.getValue().getTimes(); i++)
            System.out.printf("%s %s!\n", _myCustomData.getValue().getSay(), _myCustomData.getValue().getName());

        System.out.println();
        System.out.println("Bye!");
    }

}
