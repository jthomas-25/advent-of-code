using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

namespace Puzzle24
{
    class Program
    {
        public static void Main(string[] args)
        {
            string filename = args[0];

            Dictionary<string, Wire> wires = new Dictionary<string, Wire>();
            List<LogicGate> gates = new List<LogicGate>();
            using (StreamReader sr = new StreamReader(filename))
            {
                string wirePattern = @"([xy]\d\d): (1|0)";
                string line = sr.ReadLine();
                do
                {
                    Match match = Regex.Match(line, wirePattern);
                    string name = match.Groups[1].Value;
                    int? value = int.Parse(match.Groups[2].Value);
                    wires.Add(name, new Wire(name, value));
                    line = sr.ReadLine();
                } while (!line.Equals(""));

                string wireName = @"[a-z]\d\d|[a-z]{3}";
                string gatePattern = $"({wireName}) (AND|OR|XOR) ({wireName}) -> ({wireName})";
                line = sr.ReadLine();
                do
                {
                    Match match = Regex.Match(line, gatePattern);
                    Wire wire1 = GetWire(match.Groups[1].Value, wires);
                    string type = match.Groups[2].Value;
                    Wire wire2 = GetWire(match.Groups[3].Value, wires);
                    Wire outputWire = GetWire(match.Groups[4].Value, wires);
                    LogicGate g =
                        LogicGateFactory.Instance().Create(type, wire1, wire2, outputWire);
                    gates.Add(g);
                    line = sr.ReadLine();
                } while (line != null);
            }
            Part1(gates, wires);
        }

        static void Part1(List<LogicGate> gates, Dictionary<string, Wire> wires)
        {
            // simulate the system until all wires
            // starting with z are outputting values
            IEnumerable<Wire> zWires = 
                wires.Values.Where(wire => wire.GetName().StartsWith("z"));
            while (!zWires.All(wire => wire.HasOutput()))
            {
                gates.ForEach(gate => gate.Operate());
            }
            // z00 is the least signficant bit
            IEnumerable<int?> bits = 
                zWires.OrderByDescending(wire => wire.GetName()).Select(wire => wire.GetValue());
            string binaryString = string.Join("", bits);
            long number = Convert.ToInt64(binaryString, 2);
            Console.WriteLine(number);
        }

        static Wire GetWire(string wireName, Dictionary<string, Wire> wires)
        {
            if (!wires.TryGetValue(wireName, out Wire wire))
            {
                wire = new Wire(wireName, null);
                wires.Add(wireName, wire);
            }
            return wire;
        }
    }

    class Wire
    {
        string name;
        int? value;

        internal Wire(string name, int? value)
        {
            this.name = name;
            this.value = value;
        }

        internal string GetName() { return name; }

        internal int? GetValue() { return value; }

        internal void SetValue(int? value) { this.value = value; }

        internal bool HasOutput() { return value != null; }
    }
}
