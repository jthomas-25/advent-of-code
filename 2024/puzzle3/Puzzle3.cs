using System;
using System.IO;
using System.Text.RegularExpressions;

namespace Puzzle3
{
    class Program
    {
        public static void Main(string[] args)
        {
            string filename = args[0];

            string memory = File.ReadAllText(filename);
            Part1(memory);
            Part2(memory);
        }

        static void Part1(string memory)
        {
            // regex for a valid mul instruction
            string mulPattern = @"mul\((\d{1,3}),(\d{1,3})\)";
            long sum = 0;
            foreach (Match match in Regex.Matches(memory, mulPattern))
            {
                int x = int.Parse(match.Groups[1].Value);
                int y = int.Parse(match.Groups[2].Value);
                sum += x * y;
            }
            Console.WriteLine(sum);
        }

        static void Part2(string memory)
        {
            bool multiply = true;
            long sum = 0;
            // regex for a do(), don't(), or a valid mul instruction
            string instPattern = @"do\(\)|don't\(\)|mul\((\d{1,3}),(\d{1,3})\)";
            foreach (Match match in Regex.Matches(memory, instPattern))
            {
                if (match.Value.Equals("do()"))
                {
                    multiply = true;
                }
                else if (match.Value.Equals("don't()"))
                {
                    multiply = false;
                }
                else if (multiply)
                {
                    int x = int.Parse(match.Groups[1].Value);
                    int y = int.Parse(match.Groups[2].Value);
                    sum += x * y;
                }
            }
            Console.WriteLine(sum);
        }
    }
}
