using System;
using System.Collections.Generic;

using MyDotey.SCF.Labeled;

namespace MyDotey.SCF.BP.Component.Labeled
{
    /**
     * @author koqizhao
     *
     * May 17, 2018
     */
    public abstract class AbstractDcConfigurationSource
        : AbstractLabeledConfigurationSource<ConfigurationSourceConfig>
    {

        public AbstractDcConfigurationSource(ConfigurationSourceConfig config)
            : base(config)
        {

        }

        protected override object GetPropertyValue(object key, ICollection<IPropertyLabel> labels)
        {
            if (!(key is string))
                return null;

            String app = null;
            String dc = null;
            if (labels != null)
            {
                foreach (IPropertyLabel l in labels)
                {
                    if (object.Equals(l.Key, DcSetting.APP_KEY))
                        app = (String)l.Value;

                    if (object.Equals(l.Key, DcSetting.DC_KEY))
                        dc = (String)l.Value;
                }
            }

            return GetPropertyValue((String)key, app, dc);
        }

        protected virtual String GetPropertyValue(String key, String app, String dc)
        {
            DcSetting dcSetting = GetPropertyValue(new DcSetting(key, app, dc, null));
            return dcSetting == null ? null : dcSetting.Value;
        }

        protected abstract DcSetting GetPropertyValue(DcSetting setting);
    }
}