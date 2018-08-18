using System;
using System.Collections.Concurrent;

namespace MyDotey.SCF.BP.Component.Labeled
{
    /**
     * @author koqizhao
     *
     * May 17, 2018
     */
    public class DynamicDcConfigurationSource : AbstractDcConfigurationSource
    {
        private ConcurrentDictionary<DcSetting, DcSetting> _dcSettings = new ConcurrentDictionary<DcSetting, DcSetting>();

        public DynamicDcConfigurationSource(ConfigurationSourceConfig config)
            : base(config)
        {

        }

        public virtual void UpdateSetting(DcSetting setting)
        {
            if (setting == null)
                throw new ArgumentNullException("setting is null");

            if (string.IsNullOrWhiteSpace(setting.Key))
                throw new ArgumentNullException("setting.key is null");

            _dcSettings.TryGetValue(setting, out DcSetting oldValue);
            if (oldValue != null)
            {
                if (object.Equals(oldValue.Value, setting.Value))
                    return;
            }

            setting = (DcSetting)setting.Clone();
            _dcSettings[setting] = setting;

            RaiseChangeEvent();
        }

        public virtual void RemoveSetting(DcSetting setting)
        {
            if (setting == null)
                throw new ArgumentNullException("setting is null");

            if (string.IsNullOrWhiteSpace(setting.Key))
                throw new ArgumentNullException("setting.key is null");

            _dcSettings.TryRemove(setting, out DcSetting oldValue);

            if (oldValue == null)
                return;

            RaiseChangeEvent();
        }

        protected override DcSetting GetPropertyValue(DcSetting setting)
        {
            _dcSettings.TryGetValue(setting, out DcSetting value);
            return value;
        }
    }
}