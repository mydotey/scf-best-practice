using System;
using System.Collections.Generic;

using MyDotey.SCF.Facade;
using MyDotey.SCF.Labeled;

namespace MyDotey.SCF.BP.Component.Labeled
{
    /**
     * @author koqizhao
     *
     * Jul 25, 2018
     */
    public class LabeledPropertyComponent
    {
        private PropertyLabels _propertyLabels;

        private LabeledStringProperties _properties;

        private IProperty<LabeledKey<string>, int?> _requestTimeout;

        public LabeledPropertyComponent(ILabeledConfigurationManager manager)
        {
            _properties = new LabeledStringProperties(manager);
            InitLabels();

            _requestTimeout = _properties.GetIntProperty(ToLabeledKey("request.timeout"), 1000);
        }

        // priority: (key, app, dc) > (key, app) > (key, dc) > key
        private void InitLabels()
        {
            IPropertyLabel dcLabel = LabeledConfigurationProperties.NewLabel("dc", "shanghai");
            IPropertyLabel appLabel = LabeledConfigurationProperties.NewLabel("app", "10000");
            List<IPropertyLabel> dcLabels = new List<IPropertyLabel>();
            dcLabels.Add(dcLabel);
            List<IPropertyLabel> appLabels = new List<IPropertyLabel>();
            appLabels.Add(appLabel);
            List<IPropertyLabel> allLabels = new List<IPropertyLabel>();
            allLabels.Add(dcLabel);
            allLabels.Add(appLabel);

            PropertyLabels alternative1 = LabeledConfigurationProperties.NewLabels(dcLabels, PropertyLabels.EMPTY);
            PropertyLabels alternative2 = LabeledConfigurationProperties.NewLabels(appLabels, alternative1);
            _propertyLabels = LabeledConfigurationProperties.NewLabels(allLabels, alternative2);
        }

        protected LabeledKey<string> ToLabeledKey(string key)
        {
            return LabeledConfigurationProperties.NewKeyBuilder<string>().SetKey(key).SetPropertyLabels(_propertyLabels)
                    .Build();
        }

        /**
         * use the following methods to get your properties / property values
         *      LabeledKey&lt;string&gt; labeledKey = ToLabeledKey(key);
         *      Properties.GetSomeProperty
         *      Properties.GetSomePropertyValue
         *      Properties.Manager
         */
        protected LabeledStringProperties Properties { get { return _properties; } }

        public void YourComponentApi()
        {
            Console.WriteLine("OuterManagerComponent is doing something");
            Console.WriteLine("request.timeout is: " + _requestTimeout.Value);
        }
    }
}