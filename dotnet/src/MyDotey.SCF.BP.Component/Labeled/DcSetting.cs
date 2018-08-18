using System;

namespace MyDotey.SCF.BP.Component.Labeled
{
    /**
     * @author koqizhao
     *
     * Jun 19, 2018
     */
    public class DcSetting : ICloneable
    {
        public static string APP_KEY = "App";
        public static string DC_KEY = "DC";

        public string Key { get; set; }
        public string App { get; set; }
        public string Dc { get; set; }
        public string Value { get; set; }

        public DcSetting()
        {

        }

        public DcSetting(string key, string app, string dc, string value)
        {
            Key = key;
            App = app;
            Dc = dc;
            Value = value;
        }

        public override int GetHashCode()
        {
            int prime = 31;
            int result = 1;
            result = prime * result + ((App == null) ? 0 : App.GetHashCode());
            result = prime * result + ((Dc == null) ? 0 : Dc.GetHashCode());
            result = prime * result + ((Key == null) ? 0 : Key.GetHashCode());
            return result;
        }

        public override bool Equals(object obj)
        {
            if (object.ReferenceEquals(this, obj))
                return true;
            if (obj == null)
                return false;
            if (GetType() != obj.GetType())
                return false;
            DcSetting other = (DcSetting)obj;
            if (App == null)
            {
                if (other.App != null)
                    return false;
            }
            else if (!App.Equals(other.App))
                return false;
            if (Dc == null)
            {
                if (other.Dc != null)
                    return false;
            }
            else if (!Dc.Equals(other.Dc))
                return false;
            if (Key == null)
            {
                if (other.Key != null)
                    return false;
            }
            else if (!Key.Equals(other.Key))
            {
                return false;
            }

            return true;
        }

        public override string ToString()
        {
            return string.Format("{0} {{ Key: {1}, App: {2}, Dc: {3}, Value: {4} }}", GetType().Name, Key, App, Dc,
                    Value);
        }

        public virtual object Clone()
        {
            return MemberwiseClone();
        }
    }
}