using System;
using System.IO;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace Puzzle15
{
    class Program
    {
        internal const char ROBOT = '@';
        internal const char WALL = '#';
        internal const char BOX = 'O';
        internal const char EMPTY = '.';
        internal const char WIDE_BOX_LEFT_HALF = '[';
        internal const char WIDE_BOX_RIGHT_HALF = ']';

        public static void Main(string[] args)
        {
            string filename = args[0];

            List<string> mapString = new List<string>();
            string moves = null;
            using (StreamReader sr = new StreamReader(filename))
            {
                string line = sr.ReadLine();
                while (!line.Equals(""))
                {
                    mapString.Add(line);
                    line = sr.ReadLine();
                }
                moves = Regex.Replace(sr.ReadToEnd(), "\n", "");
            }

            Robot robot = null;
            char[,] originalMap = new char[mapString.Count, mapString[0].Length];
            char[,] map = new char[mapString.Count, mapString[0].Length];
            for (int y = 0; y < mapString.Count; y++)
            {
                for (int x = 0; x < mapString[0].Length; x++)
                {
                    if (mapString[y][x] == ROBOT)
                    {
                        robot = new Robot(x, y);
                    }
                    originalMap[y, x] = mapString[y][x];
                    map[y, x] = mapString[y][x];
                }
            }

            Part1(robot, moves, map);
            map = originalMap;
            Part2(robot, moves, map);
        }

        static void Part1(Robot robot, string moves, char[,] map)
        {
            foreach (char dir in moves)
            {
                robot.Move(dir, map);
            }
            Console.WriteLine(SumBoxesGPSCoords(map));
        }

        static void Part2(Robot robot, string moves, char[,] map)
        {
            char[,] wideMap = GetWideMap(map, robot);
            foreach (char dir in moves)
            {
                robot.Move(dir, wideMap);
            }
            Console.WriteLine(SumBoxesGPSCoords(wideMap));
        }

        static int SumBoxesGPSCoords(char[,] map)
        {
            int total = 0;
            for (int y = 0; y < map.GetLength(0); y++)
            {
                for (int x = 0; x < map.GetLength(1); x++)
                {
                    if (map[y, x] == BOX || map[y, x] == WIDE_BOX_LEFT_HALF)
                    {
                        int gpsCoord = 100 * y + x;
                        total += gpsCoord;
                    }
                }
            }
            return total;
        }

        static char[,] GetWideMap(char[,] map, Robot robot)
        {
            char[,] wideMap = new char[map.GetLength(0), map.GetLength(1) * 2];
            for (int y = 0; y < map.GetLength(0); y++)
            {
                for (int x = 0; x < map.GetLength(1); x++)
                {
                    switch (map[y, x])
                    {
                        case WALL:
                            // ##
                            wideMap[y, 2 * x] = WALL;
                            wideMap[y, 2 * x + 1] = WALL;
                            break;
                        case BOX:
                            // []
                            wideMap[y, 2 * x] = WIDE_BOX_LEFT_HALF;
                            wideMap[y, 2 * x + 1] = WIDE_BOX_RIGHT_HALF;
                            break;
                        case EMPTY:
                            // ..
                            wideMap[y, 2 * x] = EMPTY;
                            wideMap[y, 2 * x + 1] = EMPTY;
                            break;
                        case ROBOT:
                            // @.
                            wideMap[y, 2 * x] = ROBOT;
                            wideMap[y, 2 * x + 1] = EMPTY;
                            // reposition robot
                            robot.SetX(2 * x);
                            robot.SetY(y);
                            break;
                    }
                }
            }
            return wideMap;
        }

        static void ShowMap(char[,] map)
        {
            for (int y = 0; y < map.GetLength(0); y++)
            {
                char[] row = new char[map.GetLength(1)];
                for (int x = 0; x < row.Length; x++)
                {
                    row[x] = map[y, x];
                }
                Console.WriteLine(row);
            }
            Console.WriteLine();
        }
    }
}
