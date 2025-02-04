using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Puzzle7
{
    class Program
    {
        const string ADD = "+";
        const string MUL = "*";
        const string CONCAT = "||";

        public static void Main(string[] args)
        {
            string filename = args[0];

            List<long> testValues = new List<long>();
            List<string[]> numbers = new List<string[]>();
            foreach (string line in File.ReadLines(filename))
            {
                string pattern = @"\d+";
                MatchCollection matches = Regex.Matches(line, pattern);
                long testValue = long.Parse(matches[0].Value);
                string[] terms = new string[matches.Count - 1];
                for (int i = 0; i < terms.Length; i++)
                {
                    terms[i] = matches[i + 1].Value;
                }
                testValues.Add(testValue);
                numbers.Add(terms);
            }

            Part1(testValues, numbers);
            Part2(testValues, numbers);
        }

        static void Part1(List<long> testValues, List<string[]> numbers)
        {
            string[] operators = new string[] { ADD, MUL };
            Console.WriteLine(CalcCalibrationResult(testValues, numbers, operators));
        }

        static void Part2(List<long> testValues, List<string[]> numbers)
        {
            string[] operators = new string[] { ADD, MUL, CONCAT };
            Console.WriteLine(CalcCalibrationResult(testValues, numbers, operators));
        }

        static long CalcCalibrationResult(List<long> testValues, List<string[]> numbers, string[] operators)
        {
            // save Cartesian products to avoid recalculations
            Dictionary<int, List<List<string>>> products = new Dictionary<int, List<List<string>>>();

            long totalCalibration = 0;
            for (int i = 0; i < testValues.Count; i++)
            {
                long testValue = testValues[i];
                string[] terms = numbers[i];
                int numOpPositions = terms.Length - 1;
                
                products.TryGetValue(numOpPositions, out List<List<string>> combinations);
                if (combinations == null)
                {
                    combinations = RepeatCartesianProduct(operators, numOpPositions);
                    products[numOpPositions] = combinations;
                }
                // brute-force method: check every combination of operators
                Parallel.ForEach(combinations, (combo, state) =>
                    {
                        string[] parts = ToRPN(terms, combo);
                        if (EvaluateExpression(parts) == testValue)
                        {
                            totalCalibration += testValue;
                            state.Stop();
                        }
                    }
                );
            }
            return totalCalibration;
        }

        static List<List<string>> RepeatCartesianProduct(string[] array, int repeats)
        {
            List<List<string>> result = new List<List<string>>();
            List<string[]> copies = new List<string[]>();
            for (int i = 0; i < repeats; i++)
            {
                copies.Add(array);
            }

            result.Add(new List<string>());
            foreach (string[] copy in copies)
            {
                List<List<string>> tempResult = new List<List<string>>();
                foreach (List<string> x in result)
                {
                    foreach (string y in copy)
                    {
                        List<string> tempList = new List<string>(x);
                        tempList.Add(y);
                        tempResult.Add(tempList);
                    }
                }
                result = tempResult;
            }
            return result;
        }

        static string[] ToRPN(string[] terms, List<string> operators)
        {
            /*
             * Return a mathematical expression written in Reverse-
             * Polish (postfix) notation. Since there are no precedence rules,
             * each RPN expression will have the form:
             * [number] [number] [operator] ([number] [operator])*
             */
            string[] parts = new string[terms.Length + operators.Count()];
            for (int i = 0; i < parts.Length; i++)
            {
                if (i < 2)
                {
                    parts[i] = terms[i];
                }
                else if (i % 2 == 0)
                {
                    parts[i] = operators[i / 2 - 1];
                }
                else
                {
                    parts[i] = terms[i / 2 + 1];
                }
            }
            return parts;
        }

        static long EvaluateExpression(string[] parts)
        {
            Stack<long> stack = new Stack<long>();
            for (int i = 0; i < parts.Length; i++)
            {
                if (parts[i].Equals(ADD))
                {
                    long b = stack.Pop();
                    long a = stack.Pop();
                    stack.Push(a + b);
                }
                else if (parts[i].Equals(MUL))
                {
                    long b = stack.Pop();
                    long a = stack.Pop();
                    stack.Push(a * b);
                }
                else if (parts[i].Equals(CONCAT))
                {
                    string b = stack.Pop().ToString();
                    string a = stack.Pop().ToString();
                    stack.Push(long.Parse(a + b));
                }
                else
                {
                    long value = long.Parse(parts[i]);
                    stack.Push(value);
                }
            }
            return stack.Pop();
        }
    }
}
