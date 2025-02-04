using System;
using System.IO;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace Puzzle18
{
    class Program
    {
        const int GRID_WIDTH = 71;
        const int GRID_HEIGHT = GRID_WIDTH;
        const char CORRUPTED = '#';
        const char EMPTY = '.';

        public static void Main(string[] args)
        {
            string filename = args[0];

            List<Tuple<int, int>> bytePositions = new List<Tuple<int, int>>();
            string pattern = @"\d+";
            foreach (string line in File.ReadLines(filename))
            {
                MatchCollection matches = Regex.Matches(line, pattern);
                int x = int.Parse(matches[0].Value);
                int y = int.Parse(matches[1].Value);
                bytePositions.Add(Tuple.Create(x, y));
            }

            char[,] grid = new char[GRID_HEIGHT, GRID_WIDTH];
            for (int y = 0; y < GRID_HEIGHT; y++)
            {
                for (int x = 0; x < GRID_WIDTH; x++)
                {
                    grid[y, x] = EMPTY;
                }
            }

            Tuple<int, int> start = Tuple.Create(0, 0);
            Tuple<int, int> end = Tuple.Create(GRID_WIDTH - 1, GRID_HEIGHT - 1);
            Part1(start, end, bytePositions, grid);
            Part2(start, end, bytePositions, grid);
        }

        static void Part1(Tuple<int, int> start, Tuple<int, int> end, List<Tuple<int, int>> bytePositions, char[,] grid)
        {
            for (int i = 0; i < 1024; i++)
            {
                bytePositions[i].Deconstruct(out int x, out int y);
                grid[y, x] = CORRUPTED;
            }
            Console.WriteLine(ShortestPath(start, end, grid));
        }

        static void Part2(Tuple<int, int> start, Tuple<int, int> end, List<Tuple<int, int>> bytePositions, char[,] grid)
        {
            // first 1024 bytes didn't block the exit
            for (int i = 1024; i < bytePositions.Count; i++)
            {
                bytePositions[i].Deconstruct(out int x, out int y);
                grid[y, x] = CORRUPTED;
                // if there's no path to the exit
                if (ShortestPath(start, end, grid) == int.MaxValue)
                {
                    Console.WriteLine("{0},{1}", x, y);
                    return;
                }
            }
        }

        static int ShortestPath(Tuple<int, int> start, Tuple<int, int> end, char[,] grid)
        {
            // Dijkstra's algorithm
            Dictionary<Tuple<int, int>, int> tentative = new Dictionary<Tuple<int, int>, int>();
            for (int y = 0; y < GRID_HEIGHT; y++)
            {
                for (int x = 0; x < GRID_WIDTH; x++)
                {
                    tentative[Tuple.Create(x, y)] = int.MaxValue;
                }
            }
            tentative[start] = 0;

            Queue<Tuple<int, int>> queue = new Queue<Tuple<int, int>>();
            queue.Enqueue(start);
            while (queue.Count > 0)
            {
                Tuple<int, int> current = queue.Dequeue();
                current.Deconstruct(out int x, out int y);
                foreach (Tuple<int, int> neighbor in GetNeighbors(x, y))
                {
                    neighbor.Deconstruct(out int neighborX, out int neighborY);
                    if (InBounds(neighborX, neighborY) && grid[neighborY, neighborX] == EMPTY)
                    {
                        // number of steps from start to neighbor through current
                        int distance = tentative[current] + 1;
                        if (distance < tentative[neighbor])
                        {
                            tentative[neighbor] = distance;
                            queue.Enqueue(neighbor);
                        }
                    }
                }
            }
            return tentative[end];
        }

        static Tuple<int, int>[] GetNeighbors(int x, int y)
        {
            // Von Neumann neighborhood of 1
            return new Tuple<int, int>[] { Tuple.Create(x, y - 1),
                                           Tuple.Create(x, y + 1),
                                           Tuple.Create(x - 1, y),
                                           Tuple.Create(x + 1, y) };
        }

        static bool InBounds(int x, int y)
        {
            return (x >= 0 && x <= GRID_WIDTH - 1) && (y >= 0 && y <= GRID_HEIGHT - 1); 
        }
    }
}
