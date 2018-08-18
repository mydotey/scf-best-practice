using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Linq;

using NLog;
using YamlDotNet.RepresentationModel;
using YamlDotNet.Serialization;
using YamlDotNet.Serialization.NamingConventions;

using MyDotey.SCF.Yaml;

namespace MyDotey.SCF.BP.Component.Labeled
{
    /**
     * @author koqizhao
     *
     * Jul 25, 2018
     * 
     * for sequence root YAML file
     */
    public class YamlDcConfigurationSource : AbstractDcConfigurationSource
    {
        private static Logger Logger = LogManager.GetCurrentClassLogger(typeof(YamlDcConfigurationSource));

        private Dictionary<DcSetting, DcSetting> _dcSettings;

        public YamlDcConfigurationSource(YamlFileConfigurationSourceConfig config)
            : base(config)
        {
            _dcSettings = new Dictionary<DcSetting, DcSetting>();
            try
            {
                using (Stream @is = new FileStream(config.FileName, FileMode.Open))
                {
                    if (@is == null)
                    {
                        Logger.Warn("file not found: {0}", config.FileName);
                        return;
                    }

                    StreamReader fileReader = new StreamReader(config.FileName, new UTF8Encoding(false));
                    YamlStream yamlStream = new YamlStream();
                    yamlStream.Load(fileReader);
                    if (!(yamlStream.Documents[0].RootNode is YamlSequenceNode))
                    {
                        Logger.Error(typeof(YamlFileConfigurationSource).Name
                                + " only accepts YAML file with Sequence root. Current is Mapping or Scala. YAML file: "
                            + config.FileName);
                        return;
                    }

                    YamlSequenceNode properties = (YamlSequenceNode)yamlStream.Documents[0].RootNode;
                    properties.Children.ToList().ForEach(m =>
                    {
                        DcSetting dcSetting = Convert<DcSetting>(m);
                        _dcSettings[dcSetting] = dcSetting;
                    });
                }
            }
            catch (Exception e)
            {
                Logger.Warn(e, "failed to load yaml file: " + config.FileName);
            }
        }

        protected override DcSetting GetPropertyValue(DcSetting setting)
        {
            _dcSettings.TryGetValue(setting, out DcSetting value);
            return value;
        }

        public virtual V Convert<V>(YamlNode node)
        {
            if (node.NodeType == YamlNodeType.Alias)
                return default(V);

            var deserializer = new DeserializerBuilder().WithNamingConvention(new CamelCaseNamingConvention()).Build();

            if (node.NodeType == YamlNodeType.Scalar)
                return deserializer.Deserialize<V>(((YamlScalarNode)node).Value);

            YamlDocument yamlDocument = new YamlDocument(node);
            YamlStream yamlStream = new YamlStream();
            yamlStream.Add(yamlDocument);
            using (MemoryStream stream = new MemoryStream())
            {
                StreamWriter writer = new StreamWriter(stream);
                yamlStream.Save(writer);
                writer.Flush();
                stream.Seek(0, SeekOrigin.Begin);
                StreamReader reader = new StreamReader(stream);
                return deserializer.Deserialize<V>(reader);
            }
        }
    }
}