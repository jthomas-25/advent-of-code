using System;
using System.IO;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Linq;

namespace Puzzle1
{
	class Program
	{
		public static void Main(string[] args)
		{
			string filename = args[0];
			
			List<int> leftList = new List<int>();
			List<int> rightList = new List<int>();
			String pattern = @"(\d+)   (\d+)";
			foreach (string line in File.ReadLines(filename))
            {
				Match match = Regex.Match(line, pattern);
				leftList.Add(int.Parse(match.Groups[1].Value));
				rightList.Add(int.Parse(match.Groups[2].Value));
            }

			Part1(leftList, rightList);
			Part2(leftList, rightList);
		}

		static void Part1(List<int> leftList, List<int> rightList)
        {
			leftList.Sort();
			rightList.Sort();

			int totalDistance = 0;
			for (int i = 0; i < leftList.Count; i++)
			{
				int pairDistance = Math.Abs(leftList[i] - rightList[i]);
				totalDistance += pairDistance;
			}
			Console.WriteLine(totalDistance);
		}

		static void Part2(List<int> leftList, List<int> rightList)
        {
			long similarityScore = 0;
			foreach (int l in leftList)
			{
				int count = rightList.Count(r => (r == l));
				similarityScore += l * count;
			}
			Console.WriteLine(similarityScore);
		}
	}
}
