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
import org.mydotey.scf.type.string.StringConverter;
import org.mydotey.scf.type.string.StringToIntConverter;
import org.mydotey.scf.type.string.StringToMapConverter;

public class OneSourceApp {

    private static StringProperties _properties;

    private static Property<String, String> _appId;
    private static Property<String, String> _appName;
    private static Property<String, List<String>> _userList;
    private static Property<String, Map<String, String>> _userData;
    private static Property<String, Integer> _sleepTime;
    private static Property<String, MyCustomType> _myCustomData;

    private static void initConfig() {
        PropertiesFileConfigurationSourceConfig sourceConfig = StringPropertySources
                .newPropertiesFileSourceConfigBuilder().setName("app").setFileName("app.properties").build();
        ConfigurationSource source = StringPropertySources.newPropertiesFileSource(sourceConfig);
        ConfigurationManagerConfig managerConfig = ConfigurationManagers.newConfigBuilder().setName("little-app")
                .addSource(1, source).build();
        ConfigurationManager manager = ConfigurationManagers.newManager(managerConfig);
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

        System.out.println("AppId: " + _appId.getValue());
        System.out.println("AppName: " + _appName.getValue());
        System.out.println("UserList: " + _userList.getValue());
        System.out.println("UserData: " + _userData.getValue());
        System.out.println("SleepTime: " + _sleepTime.getValue());

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

    private static class MyCustomType {

        public static final StringConverter<MyCustomType> CONVERTER = new StringConverter<MyCustomType>(
                MyCustomType.class) {
            @Override
            public MyCustomType convert(String source) {
                Map<String, String> fieldValueMap = StringToMapConverter.DEFAULT.convert(source);
                return new MyCustomType(fieldValueMap.get("name"), fieldValueMap.get("say"),
                        StringToIntConverter.DEFAULT.convert(fieldValueMap.get("times")));
            }
        };

        private String name;
        private String say;
        private int times;

        public MyCustomType(String name, String say, int times) {
            super();
            this.name = name;
            this.say = say;
            this.times = times;
        }

        public String getName() {
            return name;
        }

        public String getSay() {
            return say;
        }

        public int getTimes() {
            return times;
        }

        // for custom type, must override the equals method, so as to know whether a value changed
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MyCustomType other = (MyCustomType) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (say == null) {
                if (other.say != null)
                    return false;
            } else if (!say.equals(other.say))
                return false;
            if (times != other.times)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return String.format("%s { name: %s, say: %s, times: %d }", getClass().getSimpleName(), name, say, times);
        }

    }

}
