using System;
using System.Collections.Generic;

using MyDotey.SCF.Type.String;

namespace MyDotey.SCF.BP.LittleApp
{
    /**
     * @author koqizhao
     *
     * Jul 19, 2018
     * 
     * for custom type, equals must be overridden so as to decide whether a value changed
     */
    public class MyCustomType
    {
        private class MyConverter : StringConverter<MyCustomType>
        {
            public override MyCustomType Convert(string source)
            {
                Dictionary<string, string> fieldValueDictionary = StringToDictionaryConverter.Default.Convert(source);
                return new MyCustomType(fieldValueDictionary["name"], fieldValueDictionary["say"],
                        StringToIntConverter.Default.Convert(fieldValueDictionary["times"]));
            }
        }

        public static readonly StringConverter<MyCustomType> CONVERTER = new MyConverter();

        public string Name { get; private set; }
        public string Say { get; private set; }
        public int? Times { get; private set; }

        public MyCustomType(string name, string say, int? times)
        {
            Name = name;
            Say = say;
            Times = times;
        }

        public override int GetHashCode()
        {
            int prime = 31;
            int result = 1;
            result = prime * result + ((Name == null) ? 0 : Name.GetHashCode());
            result = prime * result + ((Say == null) ? 0 : Say.GetHashCode());
            result = prime * result + (Times ?? 0);
            return result;
        }

        // for custom type, must override the equals method, so as to know whether a value changed
        public override bool Equals(Object obj)
        {
            if (object.ReferenceEquals(this, obj))
                return true;
            if (obj == null)
                return false;
            if (GetType() != obj.GetType())
                return false;
            MyCustomType other = (MyCustomType)obj;
            if (Name == null)
            {
                if (other.Name != null)
                    return false;
            }
            else if (!Name.Equals(other.Name))
                return false;
            if (Say == null)
            {
                if (other.Say != null)
                    return false;
            }
            else if (!Say.Equals(other.Say))
                return false;
            if (Times != other.Times)
                return false;
            return true;
        }

        public override string ToString()
        {
            return string.Format("{0} {{ name: {1}, say: {2}, times: {3} }}", GetType().Name, Name, Say, Times);
        }
    }
}