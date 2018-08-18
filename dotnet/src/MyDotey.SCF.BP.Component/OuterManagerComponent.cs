using System;

using MyDotey.SCF.Facade;

namespace MyDotey.SCF.BP.Component
{
    /**
     * @author koqizhao
     *
     * Jul 19, 2018
     * 
     * The component only uses config, don't care how the properties are configured!
     * The component's user knows that!
     */
    public class OuterManagerComponent
    {
        private StringProperties _properties;

        public OuterManagerComponent(StringProperties properties)
        {
            _properties = properties;
        }

        public OuterManagerComponent(IConfigurationManager manager)
        {
            _properties = new StringProperties(manager);
        }

        /**
         * use the following methods to get your properties / property values
         *      getProperties().getSomeProperty
         *      getProperties().getSomePropertyValue
         *      getProperties().getManager()
         */
        protected StringProperties GetProperties()
        {
            return _properties;
        }

        public void YourComponentApi()
        {
            Console.WriteLine("OuterManagerComponent is doing something");
        }
    }
}