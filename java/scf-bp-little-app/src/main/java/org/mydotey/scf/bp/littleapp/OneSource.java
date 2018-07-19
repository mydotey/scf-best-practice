package org.mydotey.scf.bp.littleapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.ConfigurationManagerConfig;
import org.mydotey.scf.ConfigurationSource;
import org.mydotey.scf.Property;
import org.mydotey.scf.facade.ConfigurationManagers;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.facade.StringPropertySources;
import org.mydotey.scf.source.stringproperty.propertiesfile.PropertiesFileConfigurationSourceConfig;

public class OneSource {

    private static StringProperties _properties;

    private static Property<String, String> _appId;
    private static Property<String, String> _appName;
    private static Property<String, List<String>> _userList;
    private static Property<String, Map<String, String>> _userData;
    private static Property<String, Integer> _sleepTime;
    private static Property<String, MyCustomType> _myCustomData;

    private static void initConfig() {
        // create a non-dynamic K/V (String/String) configuration source
        PropertiesFileConfigurationSourceConfig sourceConfig = StringPropertySources
                .newPropertiesFileSourceConfigBuilder().setName("app").setFileName("app.properties").build();
        ConfigurationSource source = StringPropertySources.newPropertiesFileSource(sourceConfig);

        // create a configuration manager with single source
        ConfigurationManagerConfig managerConfig = ConfigurationManagers.newConfigBuilder().setName("little-app")
                .addSource(1, source).build();
        ConfigurationManager manager = ConfigurationManagers.newManager(managerConfig);

        // create a StringProperties facade tool
        _properties = new StringProperties(manager);

        // default to null
        _appId = _properties.getStringProperty("app.id");

        // default to "unknown"
        _appName = _properties.getStringProperty("app.name", "unknown");

        // default to empty list
        _userList = _properties.getListProperty("user.list", new ArrayList<>());

        // default to empty map
        _userData = _properties.getMapProperty("user.data", new HashMap<>());

        // default to 1000, if value in app._properties is invalid, ignore the invalid value
        _sleepTime = _properties.getIntProperty("sleep.time", 1000, v -> v < 0 ? null : v);

        // custom type property
        _myCustomData = _properties.getProperty("my-custom-type-property", MyCustomType.CONVERTER);
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
        String somePropertyValue = _properties.getStringPropertyValue("some.data", "not-sure");
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
