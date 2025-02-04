using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;

namespace Puzzle2
{
    class Program
    {
        public static void Main(string[] args)
        {
            string filename = args[0];

            List<int[]> reports = new List<int[]>();
            foreach (string line in File.ReadLines(filename)) {
                string[] levels = line.Split();
                int[] report = new int[levels.Length];
                for (int i = 0; i < levels.Length; i++)
                {
                    report[i] = int.Parse(levels[i]);
                }
                reports.Add(report);
            }

            Part1(reports);
            Part2(reports);
        }

        static void Part1(List<int[]> reports)
        {
            Console.WriteLine(reports.Count(report => IsSafe(report)));
        }

        static void Part2(List<int[]> reports)
        {
            int count = 0;
            foreach (int[] report in reports)
            {
                if (IsSafe(report))
                {
                    count++;
                }
                else
                {
                    int[] subReport = new int[report.Length-1];
                    for (int skipIndex = 0; skipIndex < report.Length; skipIndex++)
                    {
                        int pos = 0;
                        for (int i = 0; i < report.Length; i++)
                        {
                            if (i == skipIndex)
                            {
                                continue;
                            }
                            subReport[pos] = report[i];
                            pos++;
                        }
                        if (IsSafe(subReport))
                        {
                            count++;
                            break;
                        }
                    }
                }
            }
            Console.WriteLine(count);
        }

        static bool IsSafe(int[] report)
        {
            int? prevDelta = null;
            for (int i = 0; i < report.Length-1; i++)
            {
                int delta = report[i+1] - report[i];
                // Second condition: 1 <= delta <= 3
                // Return false if no change or levels differ too much
                if (Math.Abs(delta) < 1 || Math.Abs(delta) > 3)
                {
                    return false;
                }
                // First condition: levels are all increasing or all decreasing
                // Return false otherwise
                // increasing
                if (delta > 0)
                {
                    // was decreasing
                    if (prevDelta != null && prevDelta < 0)
                    {
                        return false;
                    }
                }
                // decreasing
                else if (delta < 0) {
                    // was increasing
                    if (prevDelta != null && prevDelta > 0)
                    {
                        return false;
                    }
                }
                prevDelta = delta;
            }
            return true;
        }
    }
}
