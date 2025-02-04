using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;

namespace Puzzle10
{
    class Program
    {
        const char MAX_HEIGHT = '9';

        public static void Main(string[] args)
        {
            string filename = args[0];

            string[] mapString = File.ReadLines(filename).ToArray();
            char[,] map = new char[mapString.Length, mapString[0].Length];
            List<Tuple<int, int>> trailheads = new List<Tuple<int, int>>();
            for (int y = 0; y < mapString.Length; y++)
            {
                for (int x = 0; x < mapString[0].Length; x++)
                {
                    map[y, x] = mapString[y][x];
                    if (mapString[y][x] == '0')
                    {
                        trailheads.Add(Tuple.Create(x, y));
                    }
                }
            }

            Part1(trailheads, map);
            Part2(trailheads, map);
        }

        static void Part1(List<Tuple<int, int>> trailheads, char[,] map)
        {
            Console.WriteLine(trailheads.Sum(start => GetTrailheadScore(start, map)));
        }

        static void Part2(List<Tuple<int, int>> trailheads, char[,] map)
        {
            Console.WriteLine(trailheads.Sum(start => GetTrailheadRating(start, map)));
        }

        static int GetTrailheadScore(Tuple<int, int> trailhead, char[,] map)
        {
            /*
             * Return the number of 9-height positions any hiking trail can
             * reach from the given position, using breadth-first search (BFS)
             */
            int score = 0;
            bool[,] visited = new bool[map.GetLength(0), map.GetLength(1)];
            Queue<Tuple<int, int>> queue = new Queue<Tuple<int, int>>();
            queue.Enqueue(trailhead);
            while (queue.Count > 0)
            {
                Tuple<int, int> current = queue.Dequeue();
                current.Deconstruct(out int x, out int y);
                if (!visited[y, x])
                {
                    visited[y, x] = true;
                    if (map[y, x] == MAX_HEIGHT)
                    {
                        score++;
                    }
                }

                foreach (Tuple<int, int> neighbor in GetNeighbors(x, y))
                {
                    neighbor.Deconstruct(out int neighborX, out int neighborY);
                    // visit neighbor only if you haven't been there yet and can ascend by 1
                    if (InBounds(neighborX, neighborY, map)
                        && !visited[neighborY, neighborX]
                        && GetHeightDiff(map[neighborY, neighborX], map[y, x]) == 1)
                    {
                        queue.Enqueue(neighbor);
                    }
                }
            }
            return score;
        }

        static int GetTrailheadRating(Tuple<int, int> trailhead, char[,] map)
        {
            /*
             * Return the number of hiking trails that begin at the
             * given position, using depth-first search (DFS)
             */
            int rating = 0;
            Stack<Tuple<int, int>> stack = new Stack<Tuple<int, int>>();
            stack.Push(trailhead);
            while (stack.Count > 0)
            {
                Tuple<int, int> current = stack.Pop();
                current.Deconstruct(out int x, out int y);
                if (map[y, x] == MAX_HEIGHT)
                {
                    rating++;
                }

                foreach (Tuple<int, int> neighbor in GetNeighbors(x, y))
                {
                    neighbor.Deconstruct(out int neighborX, out int neighborY);
                    // visit neighbor only if you can ascend by 1
                    if (InBounds(neighborX, neighborY, map)
                        && GetHeightDiff(map[neighborY, neighborX], map[y, x]) == 1)
                    {
                        stack.Push(neighbor);
                    }
                }
            }
            return rating;
        }

        static Tuple<int, int>[] GetNeighbors(int x, int y)
        {
            // Von Neumann neighborhood of 1
            return new Tuple<int, int>[] { Tuple.Create(x, y - 1),
                                           Tuple.Create(x, y + 1),
                                           Tuple.Create(x - 1, y),
                                           Tuple.Create(x + 1, y) };
        }

        static bool InBounds(int x, int y, char[,] map)
        {
            return (x >= 0 && x <= map.GetLength(1) - 1) && (y >= 0 && y <= map.GetLength(0) - 1);
        }

        static int GetHeightDiff(char neighbor, char current)
        {
            return neighbor - current;
        }
    }
}
