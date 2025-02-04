using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;

namespace Puzzle11
{
    class Program
    {
        const int NUM_BLINKS = 25;

        public static void Main(string[] args)
        {
            string filename = args[0];

            string[] line = File.ReadAllText(filename).TrimEnd().Split();
            Dictionary<long, long> counts = new Dictionary<long, long>();
            Dictionary<long, long> primes = new Dictionary<long, long>();
            foreach (string stone in line)
            {
                long number = long.Parse(stone);
                counts[number] = 1;
                primes[number] = 0;
            }

            for (int i = 0; i < NUM_BLINKS; i++)
            {
                Blink(counts, primes);
            }
            Console.WriteLine(counts.Values.Sum());
        }

        static void Blink(Dictionary<long, long> counts, Dictionary<long, long> primes)
        {
            CalcGrowthRates(counts, primes);
            long[] keys = new long[primes.Keys.Count];
            primes.Keys.CopyTo(keys, 0);
            foreach (long key in keys)
            {
                long change = primes[key];
                counts[key] = (counts.ContainsKey(key)) ? counts[key] + change : change;
                primes[key] = 0;
            }
        }

        static void CalcGrowthRates(Dictionary<long, long> counts, Dictionary<long, long> primes)
        {
            foreach (long number in counts.Keys)
            {
                long count = counts[number];
                if (count == 0)
                {
                    continue;
                }
                string keyString = number.ToString();
                // even number of digits
                if (keyString.Length % 2 == 0)
                {
                    string leftHalf = keyString.Substring(0, keyString.Length / 2);
                    string rightHalf = keyString.Substring(keyString.Length / 2);
                    long leftNum = long.Parse(leftHalf);
                    long rightNum = long.Parse(rightHalf);
                    primes[leftNum] = primes.ContainsKey(leftNum) ? primes[leftNum] + count : count;
                    primes[rightNum] = primes.ContainsKey(rightNum) ? primes[rightNum] + count : count;
                }
                else if (number == 0)
                {
                    primes[1] = primes.ContainsKey(1) ? primes[1] + count : count;
                }
                else
                {
                    primes[number * 2024] = primes.ContainsKey(number * 2024) ? primes[number * 2024] + count : count;
                }
                primes[number] -= count;
            }
        }
    }
}
