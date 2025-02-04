using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;

namespace Puzzle5
{
    class Program
    {
        static Dictionary<int, List<int>> pageRules;

        public static void Main(string[] args)
        {
            string filename = args[0];

            pageRules = new Dictionary<int, List<int>>();
            List<List<int>> updates = new List<List<int>>();
            using (StreamReader sr = new StreamReader(filename))
            {
                string line = sr.ReadLine();
                while (!line.Equals(""))
                {
                    string[] nums = line.Split('|');
                    int current = int.Parse(nums[0]);
                    int next = int.Parse(nums[1]);
                    if (!pageRules.ContainsKey(current))
                    {
                        pageRules[current] = new List<int>();
                    }
                    pageRules[current].Add(next);
                    line = sr.ReadLine();
                }

                line = sr.ReadLine();
                while (line != null)
                {
                    List<int> update = new List<int>();
                    foreach (string num in line.Split(','))
                    {
                        update.Add(int.Parse(num));
                    }
                    updates.Add(update);
                    line = sr.ReadLine();
                }
            }

            Part1(updates);
            Part2(updates);
        }

        static void Part1(List<List<int>> updates)
        {
            int sum = 0;
            foreach (List<int> update in updates)
            {
                if (InOrder(update))
                {
                    int middle = update.Count() / 2;
                    sum += update[middle];
                }
            }
            Console.WriteLine(sum);
        }

        static void Part2(List<List<int>> updates)
        {
            int sum = 0;
            foreach (List<int> update in updates)
            {
                if (!InOrder(update))
                {
                    update.Sort(ComparePagesUsingRules);
                    int middle = update.Count() / 2;
                    sum += update[middle];
                }
            }
            Console.WriteLine(sum);
        }

        static bool InOrder(List<int> update)
        {
            for (int i = 0; i < update.Count - 1; i++)
            {
                int page = update[i];
                int nextPage = update[i + 1];
                // Return false if no rule exists or pages are in the wrong order
                if (ComparePagesUsingRules(page, nextPage) > 0)
                {
                    return false;
                }
            }
            return true;
        }

        static int ComparePagesUsingRules(int x, int y)
        {
            if (x == y)
            {
                // x and y are the same page
                return 0;
            }
            else if (pageRules.TryGetValue(x, out List<int> nextPages)
                     && nextPages.Contains(y))
            {
                // according to the rules, x must be before y at some point
                return -1;
            }
            else
            {
                // no rule applies: x is after y at some point
                return 1;
            }
        }
    }
}
