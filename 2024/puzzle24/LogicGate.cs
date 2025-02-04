namespace Puzzle24
{
    abstract class LogicGate
    {
        protected Wire wire1;
        protected Wire wire2;
        protected Wire outputWire;

        protected LogicGate(Wire wire1, Wire wire2, Wire outputWire)
        {
            this.wire1 = wire1;
            this.wire2 = wire2;
            this.outputWire = outputWire;
        }

        internal abstract void Operate();
    }

    class AndGate : LogicGate
    {
        internal AndGate(Wire wire1, Wire wire2, Wire outputWire)
            : base(wire1, wire2, outputWire)
        {
        }

        internal override void Operate()
        {
            if (outputWire.HasOutput())
            {
                // don't change output
                return;
            }
            else if (wire1.HasOutput() && wire2.HasOutput())
            {
                int? value1 = wire1.GetValue();
                int? value2 = wire2.GetValue();
                // bitwise AND because values are binary
                outputWire.SetValue(value1 & value2);
            }
        }
    }

    class OrGate : LogicGate
    {
        internal OrGate(Wire wire1, Wire wire2, Wire outputWire)
            : base(wire1, wire2, outputWire)
        {
        }

        internal override void Operate()
        {
            if (outputWire.HasOutput())
            {
                // don't change output
                return;
            }
            else if (wire1.HasOutput() && wire2.HasOutput())
            {
                int? value1 = wire1.GetValue();
                int? value2 = wire2.GetValue();
                // bitwise OR because values are binary
                outputWire.SetValue(value1 | value2);
            }
        }
    }

    class XorGate : LogicGate
    {
        internal XorGate(Wire wire1, Wire wire2, Wire outputWire)
            : base(wire1, wire2, outputWire)
        {
        }

        internal override void Operate()
        {
            if (outputWire.HasOutput())
            {
                // don't change output
                return;
            }
            else if (wire1.HasOutput() && wire2.HasOutput())
            {
                int? value1 = wire1.GetValue();
                int? value2 = wire2.GetValue();
                // bitwise XOR because values are binary
                outputWire.SetValue(value1 ^ value2);
            }
        }
    }

    class LogicGateFactory
    {
        static LogicGateFactory theInstance;

        LogicGateFactory()
        {
        }

        internal static LogicGateFactory Instance()
        {
            if (theInstance == null)
            {
                theInstance = new LogicGateFactory();
            }
            return theInstance;
        }

        internal LogicGate Create(string type, Wire wire1, Wire wire2, Wire outputWire)
        {
            LogicGate g = null;
            switch (type)
            {
                case "AND":
                    g = new AndGate(wire1, wire2, outputWire);
                    break;
                case "OR":
                    g = new OrGate(wire1, wire2, outputWire);
                    break;
                case "XOR":
                    g = new XorGate(wire1, wire2, outputWire);
                    break;
            }
            return g;
        }
    }
}