using System;
using System.IO;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace Puzzle17
{
    class Program
    {
        static int regA;
        static int regB;
        static int regC;
        static int instPtr;

        public static void Main(string[] args)
        {
            string filename = args[0];

            int[] program = null;
            using (StreamReader sr = new StreamReader(filename))
            {
                string pattern = @"\d+";
                regA = int.Parse(Regex.Match(sr.ReadLine(), pattern).Value);
                regB = int.Parse(Regex.Match(sr.ReadLine(), pattern).Value);
                regC = int.Parse(Regex.Match(sr.ReadLine(), pattern).Value);
                sr.ReadLine();
                MatchCollection matches = Regex.Matches(sr.ReadLine(), pattern);
                program = new int[matches.Count];
                for (int i = 0; i < program.Length; i++)
                {
                    program[i] = int.Parse(matches[i].Value);
                }
            }

            RunProgram(program);
        }

        static void RunProgram(int[] program)
        {
            List<string> values = new List<string>();
            instPtr = 0;
            while (instPtr < program.Length - 1)
            {
                int opcode = program[instPtr];
                int operand = program[instPtr + 1];
                bool jumped = false;
                switch (opcode)
                {
                    case 0:
                        Adv(operand);
                        break;
                    case 1:
                        Bxl(operand);
                        break;
                    case 2:
                        Bst(operand);
                        break;
                    case 3:
                        jumped = Jnz(operand);
                        break;
                    case 4:
                        Bxc(operand);
                        break;
                    case 5:
                        Out(operand, values);
                        break;
                    case 6:
                        Bdv(operand);
                        break;
                    case 7:
                        Cdv(operand);
                        break;
                }
                if (jumped)
                {
                    continue;
                }
                instPtr += 2;
            }
            Console.WriteLine(string.Join(",", values));
        }

        static void Adv(int operand)
        {
            regA /= (int)Math.Pow(2, GetComboOperandValue(operand));
        }

        static void Bxl(int operand)
        {
            // bitwise XOR
            regB ^= operand;
        }

        static void Bst(int operand)
        {
            regB = GetComboOperandValue(operand) % 8;
        }

        static bool Jnz(int operand)
        {
            if (regA == 0)
            {
                return false;
            }
            instPtr = operand;
            return true;
        }

        static void Bxc(int operand)
        {
            // bitwise XOR
            regB ^= regC;
        }

        static void Out(int operand, List<string> values)
        {
           int value = GetComboOperandValue(operand) % 8;
           values.Add(value.ToString());
        }

        static void Bdv(int operand)
        {
            regB = regA / (int)Math.Pow(2, GetComboOperandValue(operand));
        }

        static void Cdv(int operand)
        {
            regC = regA / (int)Math.Pow(2, GetComboOperandValue(operand));
        }

        static int GetComboOperandValue(int operand)
        {
            switch (operand)
            {
                case 0:
                case 1:
                case 2:
                case 3:
                    return operand;
                case 4:
                    return regA;
                case 5:
                    return regB;
                case 6:
                    return regC;
                default:
                    return -1;
            }
        }
    }
}
