package org.mydotey.scf.bp.littleapp;

import java.util.Map;

import org.mydotey.scf.type.string.StringConverter;
import org.mydotey.scf.type.string.StringToIntConverter;
import org.mydotey.scf.type.string.StringToMapConverter;

/**
 * @author koqizhao
 *
 * Jul 19, 2018
 * 
 * for custom type, equals must be overridden so as to decide whether a value changed
 */
public class MyCustomType {
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