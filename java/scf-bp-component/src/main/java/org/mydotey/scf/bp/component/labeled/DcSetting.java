package org.mydotey.scf.bp.component.labeled;

/**
 * @author koqizhao
 *
 * Jun 19, 2018
 */
public class DcSetting implements Cloneable {

    public static final String APP_KEY = "app";
    public static final String DC_KEY = "dc";

    private String key;
    private String app;
    private String dc;
    private String value;

    public DcSetting() {

    }

    public DcSetting(String key, String app, String dc, String value) {
        super();
        this.key = key;
        this.app = app;
        this.dc = dc;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getApp() {
        return app;
    }

    public String getDc() {
        return dc;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((app == null) ? 0 : app.hashCode());
        result = prime * result + ((dc == null) ? 0 : dc.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DcSetting other = (DcSetting) obj;
        if (app == null) {
            if (other.app != null)
                return false;
        } else if (!app.equals(other.app))
            return false;
        if (dc == null) {
            if (other.dc != null)
                return false;
        } else if (!dc.equals(other.dc))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format("%s { key: %s, app: %s, dc: %s, value: %s }", getClass().getSimpleName(), key, app, dc,
                value);
    }

    @Override
    public DcSetting clone() {
        try {
            return (DcSetting) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
