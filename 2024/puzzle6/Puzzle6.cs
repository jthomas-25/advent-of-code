using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;

namespace Puzzle6
{
    class Program
    {
        internal const char OBSTACLE = '#';

        public static void Main(string[] args)
        {
            string filename = args[0];

            string[] mapString = File.ReadLines(filename).ToArray();
            Guard guard = null;
            char[,] map = new char[mapString.Length, mapString[0].Length];
            for (int y = 0; y < mapString.Length; y++)
            {
                for (int x = 0; x < mapString[0].Length; x++)
                {
                    if (mapString[y][x] == '^')
                    {
                        guard = new Guard(x, y);
                    }
                    map[y, x] = mapString[y][x];
                }
            }
            
            Part1(guard, map);
            guard.Reset();
            Part2(guard, map);
        }

        static void Part1(Guard guard, char[,] map)
        {
            List<Tuple<int, int>> path = new List<Tuple<int, int>>();
            path.Add(Tuple.Create(guard.GetX(), guard.GetY()));
            while (guard.InBounds(map))
            {
                bool moved = guard.Patrol(map);
                if (moved)
                {
                    path.Add(Tuple.Create(guard.GetX(), guard.GetY()));
                }
            }
            Console.WriteLine(path.Distinct().Count() - 1);
        }

        static void Part2(Guard guard, char[,] map)
        {
            int count = 0;
            for (int y = 0; y < map.GetLength(0); y++)
            {
                for (int x = 0; x < map.GetLength(1); x++)
                {
                    // don't drop an obstacle on the guard
                    if (x == guard.GetX() && y == guard.GetY())
                    {
                        continue;
                    }
                    char oldValue = map[y, x];
                    map[y, x] = OBSTACLE;
                    if (GuardStuckInLoop(guard, map))
                    {
                        count++;
                    }
                    map[y, x] = oldValue;
                    guard.Reset();
                }
            }
            Console.WriteLine(count);
        }

        static bool GuardStuckInLoop(Guard guard, char[,] map)
        {
            // all directions the guard has faced at a given position
            Dictionary<Tuple<int, int>, List<char>> guardStates = new Dictionary<Tuple<int, int>, List<char>>();
            Tuple<int, int> pos = Tuple.Create(guard.GetX(), guard.GetY());
            while (guard.InBounds(map))
            {
                // Return true if the guard has faced this direction before
                if (guardStates.TryGetValue(pos, out List<char> directions) && directions.Contains(guard.GetDir()))
                {
                    return true;
                }
                else if (directions == null)
                {
                    directions = new List<char>();
                    guardStates[pos] = directions;
                }
                directions.Add(guard.GetDir());
                bool moved = guard.Patrol(map);
                if (moved)
                {
                    pos = Tuple.Create(guard.GetX(), guard.GetY());
                }
            }
            return false;
        }
    }

    class Guard
    {
        int startX;
        int startY;
        int x;
        int y;
        char dir;
        const char UP = 'U';
        const char RIGHT = 'R';
        const char LEFT = 'L';
        const char DOWN = 'D';
        
        internal Guard(int x, int y)
        {
            startX = x;
            startY = y;
            this.x = x;
            this.y = y;
            dir = UP;
        }

        internal int GetX() { return x; }

        internal int GetY() { return y; }

        internal char GetDir() { return dir; }

        internal void Reset()
        {
            x = startX;
            y = startY;
            dir = UP;
        }

        internal bool InBounds(char[,] map)
        {
            return (y >= 0 && y <= map.GetLength(0) - 1) && (x >= 0 && x <= map.GetLength(1) - 1);
        }

        internal bool Patrol(char[,] map)
        {
            /*
             * Return true if the guard's position changed; false otherwise
             * Patrol protocol is as follows:
             * - If obstacle, turn right 90 degrees
             * - Otherwise, move forward one step
             */
            bool moved = false;
            switch (dir)
            {
                case UP:
                    if ((y - 1 >= 0) && (map[y - 1, x] == Program.OBSTACLE))
                    {
                        dir = RIGHT;
                    }
                    else
                    {
                        y--;
                        moved = true;
                    }
                    break;
                case RIGHT:
                    if ((x + 1 <= map.GetLength(1) - 1) && (map[y, x + 1] == Program.OBSTACLE))
                    {
                        dir = DOWN;
                    }
                    else
                    {
                        x++;
                        moved = true;
                    }
                    break;
                case DOWN:
                    if ((y + 1 <= map.GetLength(0) - 1) && (map[y + 1, x] == Program.OBSTACLE))
                    {
                        dir = LEFT;
                    }
                    else
                    {
                        y++;
                        moved = true;
                    }
                    break;
                case LEFT:
                    if ((x - 1 >= 0) && (map[y, x - 1] == Program.OBSTACLE))
                    {
                        dir = UP;
                    }
                    else
                    {
                        x--;
                        moved = true;
                    }
                    break;
            }
            return moved;
        }
    }
}
