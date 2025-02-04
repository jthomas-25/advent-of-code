using System;
using System.Collections.Generic;
using System.Linq;

namespace Puzzle15
{
    class Robot
    {
        int x;
        int y;
        Dictionary<Tuple<char, char>, CallbackMove> actionTable;
        const char UP = '^';
        const char DOWN = 'v';
        const char LEFT = '<';
        const char RIGHT = '>';

        internal Robot(int x, int y)
        {
            this.x = x;
            this.y = y;
            SetUpActionTable();
        }

        delegate bool CallbackMove(char[,] map);

        void SetUpActionTable()
        {
            char[] directions =
            { 
                UP,
                DOWN,
                LEFT,
                RIGHT
            };
            char[] tileTypes =
            {
                Program.EMPTY,
                Program.BOX,
                Program.WIDE_BOX_LEFT_HALF,
                Program.WIDE_BOX_RIGHT_HALF
            };
            CallbackMove[] actions =
            {
                new CallbackMove(MoveUp),
                new CallbackMove(TryPushBoxesUp),
                new CallbackMove(TryPushWideBoxesUp),
                new CallbackMove(MoveDown),
                new CallbackMove(TryPushBoxesDown),
                new CallbackMove(TryPushWideBoxesDown),
                new CallbackMove(MoveLeft),
                new CallbackMove(TryPushBoxesLeft),
                new CallbackMove(TryPushWideBoxesLeft),
                new CallbackMove(MoveRight),
                new CallbackMove(TryPushBoxesRight),
                new CallbackMove(TryPushWideBoxesRight)
            };
            
            actionTable = new Dictionary<Tuple<char, char>, CallbackMove>();
            for (int i = 0; i < directions.Length; i++)
            {
                char dir = directions[i];
                for (int j = 0; j < tileTypes.Length; j++)
                {
                    char tile = tileTypes[j];
                    int actionIndex = 3 * i + j;
                    // repeat previous action for wide box halves
                    CallbackMove action = (j == 3) ? actions[actionIndex - 1] : actions[actionIndex];
                    actionTable.Add(Tuple.Create(dir, tile), action);
                }
            }
        }

        internal void SetX(int value) { x = value; }

        internal void SetY(int value) { y = value; }

        internal void Move(char dir, char[,] map)
        {
            char tile;
            switch (dir)
            {
                case UP:
                    tile = map[y - 1, x];
                    break;
                case DOWN:
                    tile = map[y + 1, x];
                    break;
                case LEFT:
                    tile = map[y, x - 1];
                    break;
                case RIGHT:
                    tile = map[y, x + 1];
                    break;
                default:
                    tile = Program.WALL;
                    break;
            }
            if (tile != Program.WALL)
            {
                CallbackMove actionHandler = actionTable[Tuple.Create(dir, tile)];
                actionHandler(map);
            }
        }

        bool MoveUp(char[,] map)
        {
            map[y, x] = Program.EMPTY;
            y--;
            map[y, x] = Program.ROBOT;
            return true;
        }

        bool MoveDown(char[,] map)
        {
            map[y, x] = Program.EMPTY;
            y++;
            map[y, x] = Program.ROBOT;
            return true;
        }

        bool MoveLeft(char[,] map)
        {
            map[y, x] = Program.EMPTY;
            x--;
            map[y, x] = Program.ROBOT;
            return true;
        }

        bool MoveRight(char[,] map)
        {
            map[y, x] = Program.EMPTY;
            x++;
            map[y, x] = Program.ROBOT;
            return true;
        }

        bool TryPushBoxesUp(char[,] map)
        {
            int start;
            int end = y - 1;
            for (start = end; map[start - 1, x] == Program.BOX; start--) ;
            if (map[start - 1, x] == Program.WALL)
            {
                return false;
            }
            PushBoxesUp(start, end, map);
            return true;
        }

        bool TryPushBoxesDown(char[,] map)
        {
            int start = y + 1;
            int end;
            for (end = start; map[end + 1, x] == Program.BOX; end++) ;
            if (map[end + 1, x] == Program.WALL)
            {
                return false;
            }
            PushBoxesDown(start, end, map);
            return true;
        }

        bool TryPushBoxesLeft(char[,] map)
        {
            int start;
            int end = x - 1;
            for (start = end; map[y, start - 1] == Program.BOX; start--) ;
            if (map[y, start - 1] == Program.WALL)
            {
                return false;
            }
            PushBoxesLeft(start, end, map);
            return true;
        }

        bool TryPushBoxesRight(char[,] map)
        {
            int start = x + 1;
            int end;
            for (end = start; map[y, end + 1] == Program.BOX; end++) ;
            if (map[y, end + 1] == Program.WALL)
            {
                return false;
            }
            PushBoxesRight(start, end, map);
            return true;
        }

        void PushBoxesUp(int start, int end, char[,] map)
        {
            // move boxes starting with the topmost box so
            // we don't overwrite the boxes too early
            for (int y = start; y <= end; y++)
            {
                map[y, x] = Program.EMPTY;
                map[y - 1, x] = Program.BOX;
            }
            MoveUp(map);
        }

        void PushBoxesDown(int start, int end, char[,] map)
        {
            // move boxes starting with the bottommost box so
            // we don't overwrite the boxes too early
            for (int y = end; y >= start; y--)
            {
                map[y, x] = Program.EMPTY;
                map[y + 1, x] = Program.BOX;
            }
            MoveDown(map);
        }

        void PushBoxesLeft(int start, int end, char[,] map)
        {
            // move boxes starting with the leftmost box so
            // we don't overwrite the boxes too early
            for (int x = start; x <= end; x++)
            {
                map[y, x] = Program.EMPTY;
                map[y, x - 1] = Program.BOX;
            }
            MoveLeft(map);
        }

        void PushBoxesRight(int start, int end, char[,] map)
        {
            // move boxes starting with the rightmost box so
            // we don't overwrite the boxes too early
            for (int x = end; x >= start; x--)
            {
                map[y, x] = Program.EMPTY;
                map[y, x + 1] = Program.BOX;
            }
            MoveRight(map);
        }

        bool TryPushWideBoxesUp(char[,] map)
        {
            List<Tuple<int, int>> boxHalves = UpwardBFSWideBoxes(Tuple.Create(x, y - 1), map);
            // don't push if any box would move into a wall
            if (boxHalves.Any(half => map[half.Item2 - 1, half.Item1] == Program.WALL))
            {
                return false;
            }
            PushWideBoxesUp(boxHalves, map);
            return true;
        }

        bool TryPushWideBoxesDown(char[,] map)
        {
            List<Tuple<int, int>> boxHalves = DownwardBFSWideBoxes(Tuple.Create(x, y + 1), map);
            // don't push if any box would move into a wall
            if (boxHalves.Any(half => map[half.Item2 + 1, half.Item1] == Program.WALL))
            {
                return false;
            }
            PushWideBoxesDown(boxHalves, map);
            return true;
        }

        bool TryPushWideBoxesLeft(char[,] map)
        {
            List<Tuple<int, int>> boxHalves = new List<Tuple<int, int>>();
            for (int x = this.x - 1; IsWideBox(map[y, x]); x--)
            {
                boxHalves.Add(Tuple.Create(x, y));
            }
            // don't push if any box would move into a wall
            if (boxHalves.Any(half => map[half.Item2, half.Item1 - 1] == Program.WALL))
            {
                return false;
            }
            PushWideBoxesLeft(boxHalves, map);
            return true;
        }

        bool TryPushWideBoxesRight(char[,] map)
        {
            List<Tuple<int, int>> boxHalves = new List<Tuple<int, int>>();
            for (int x = this.x + 1; IsWideBox(map[y, x]); x++)
            {
                boxHalves.Add(Tuple.Create(x, y));
            }
            // don't push if any box would move into a wall
            if (boxHalves.Any(half => map[half.Item2, half.Item1 + 1] == Program.WALL))
            {
                return false;
            }
            PushWideBoxesRight(boxHalves, map);
            return true;
        }

        bool IsWideBox(char tile)
        {
            return tile == Program.WIDE_BOX_LEFT_HALF || tile == Program.WIDE_BOX_RIGHT_HALF;
        }

        void PushWideBoxesUp(List<Tuple<int, int>> boxHalves, char[,] map)
        {
            // sort y coordinates in ascending order, then x coordinates
            // in ascending order for convenience
            IOrderedEnumerable<Tuple<int, int>> query = 
                boxHalves.OrderBy(pos => pos.Item2).ThenBy(pos => pos.Item1);
            // move the topmost boxes first
            foreach (Tuple<int, int> half in query)
            {
                half.Deconstruct(out int x, out int y);
                if (map[y, x] == Program.WIDE_BOX_LEFT_HALF)
                {
                    map[y, x] = Program.EMPTY;
                    map[y - 1, x] = Program.WIDE_BOX_LEFT_HALF;
                }
                else
                {
                    map[y, x] = Program.EMPTY;
                    map[y - 1, x] = Program.WIDE_BOX_RIGHT_HALF;
                }
            }
            MoveUp(map);
        }

        void PushWideBoxesDown(List<Tuple<int, int>> boxHalves, char[,] map)
        {
            // sort y coordinates in descending order, then x coordinates
            // in ascending order for convenience
            IOrderedEnumerable<Tuple<int, int>> query = 
                boxHalves.OrderByDescending(pos => pos.Item2).ThenBy(pos => pos.Item1);
            // move the bottommost boxes first
            foreach (Tuple<int, int> half in query)
            {
                half.Deconstruct(out int x, out int y);
                if (map[y, x] == Program.WIDE_BOX_LEFT_HALF)
                {
                    map[y, x] = Program.EMPTY;
                    map[y + 1, x] = Program.WIDE_BOX_LEFT_HALF;
                }
                else
                {
                    map[y, x] = Program.EMPTY;
                    map[y + 1, x] = Program.WIDE_BOX_RIGHT_HALF;
                }
            }
            MoveDown(map);
        }

        void PushWideBoxesLeft(List<Tuple<int, int>> boxHalves, char[,] map)
        {
            // sort x coordinates in ascending order
            IOrderedEnumerable<Tuple<int, int>> query = 
                boxHalves.OrderBy(pos => pos.Item1);
            // move the leftmost box first
            foreach (Tuple<int, int> half in query)
            {
                half.Deconstruct(out int x, out int y);
                if (map[y, x] == Program.WIDE_BOX_LEFT_HALF)
                {
                    map[y, x] = Program.EMPTY;
                    map[y, x - 1] = Program.WIDE_BOX_LEFT_HALF;
                }
                else
                {
                    map[y, x] = Program.EMPTY;
                    map[y, x - 1] = Program.WIDE_BOX_RIGHT_HALF;
                }
            }
            MoveLeft(map);
        }

        void PushWideBoxesRight(List<Tuple<int, int>> boxHalves, char[,] map)
        {
            // sort x coordinates in descending order
            IOrderedEnumerable<Tuple<int, int>> query = 
                boxHalves.OrderByDescending(pos => pos.Item1);
            // move the rightmost box first
            foreach (Tuple<int, int> half in query)
            {
                half.Deconstruct(out int x, out int y);
                if (map[y, x] == Program.WIDE_BOX_LEFT_HALF)
                {
                    map[y, x] = Program.EMPTY;
                    map[y, x + 1] = Program.WIDE_BOX_LEFT_HALF;
                }
                else
                {
                    map[y, x] = Program.EMPTY;
                    map[y, x + 1] = Program.WIDE_BOX_RIGHT_HALF;
                }
            }
            MoveRight(map);
        }

        List<Tuple<int, int>> UpwardBFSWideBoxes(Tuple<int, int> start, char[,] map)
        {
            /*
             * Using breadth-first search (BFS), return a list of all boxes
             * (represented as box halves) that could move up if the robot
             * pushed the box at the given position.
             * 
             * The search primarily moves up; however, if one half of a box
             * is found, it will also branch out left or right to include the
             * other half. This handles cases like the two shown below:
             * 
             * ##############
             * ##..........##
             * ##...[][]...##
             * ##....[]....##
             * ##.....@....##
             * ##..........##
             * ##############
             * 
             * ##############
             * ##..........##
             * ##....[]....##
             * ##....[]....##
             * ##....@.....##
             * ##..........##
             * ##############
             */
            List<Tuple<int, int>> boxHalves = new List<Tuple<int, int>>();
            Queue<Tuple<int, int>> queue = new Queue<Tuple<int, int>>();
            queue.Enqueue(start);
            while (queue.Count > 0)
            {
                Tuple<int, int> current = queue.Dequeue();
                if (!boxHalves.Contains(current))
                {
                    boxHalves.Add(current);
                }

                current.Deconstruct(out int x, out int y);
                Tuple<int, int> leftHalf;
                Tuple<int, int> rightHalf;
                if (current.Equals(start))
                {
                    if (map[y, x] == Program.WIDE_BOX_LEFT_HALF)
                    {
                        rightHalf = Tuple.Create(x + 1, y);
                        queue.Enqueue(rightHalf);
                    }
                    else
                    {
                        leftHalf = Tuple.Create(x - 1, y);
                        queue.Enqueue(leftHalf);
                    }
                }

                char tile = map[y - 1, x];
                if (tile == Program.WIDE_BOX_LEFT_HALF)
                {
                    leftHalf = Tuple.Create(x, y - 1);
                    rightHalf = Tuple.Create(x + 1, y - 1);
                    queue.Enqueue(leftHalf);
                    queue.Enqueue(rightHalf);
                }
                else if (tile == Program.WIDE_BOX_RIGHT_HALF)
                {
                    rightHalf = Tuple.Create(x, y - 1);
                    leftHalf = Tuple.Create(x - 1, y - 1);
                    queue.Enqueue(rightHalf);
                    queue.Enqueue(leftHalf);
                }
            }
            return boxHalves;
        }

        List<Tuple<int, int>> DownwardBFSWideBoxes(Tuple<int, int> start, char[,] map)
        {
            /*
             * Using breadth-first search (BFS), return a list of all boxes
             * (represented as box halves) that could move down if the robot
             * pushed the box at the given position.
             * 
             * The search primarily moves down; however, if one half of a box
             * is found, it will also branch out left or right to include the
             * other half. This handles cases like the two shown below:
             * 
             * ##############
             * ##..........##
             * ##.....@....##
             * ##....[]....##
             * ##...[][]...##
             * ##..........##
             * ##############
             * 
             * ##############
             * ##..........##
             * ##....@.....##
             * ##....[]....##
             * ##....[]....##
             * ##..........##
             * ##############
             */
            List<Tuple<int, int>> boxHalves = new List<Tuple<int, int>>();
            Queue<Tuple<int, int>> queue = new Queue<Tuple<int, int>>();
            queue.Enqueue(start);
            while (queue.Count > 0)
            {
                Tuple<int, int> current = queue.Dequeue();
                if (!boxHalves.Contains(current))
                {
                    boxHalves.Add(current);
                }

                current.Deconstruct(out int x, out int y);
                Tuple<int, int> leftHalf;
                Tuple<int, int> rightHalf;
                if (current.Equals(start))
                {
                    if (map[y, x] == Program.WIDE_BOX_LEFT_HALF)
                    {
                        rightHalf = Tuple.Create(x + 1, y);
                        queue.Enqueue(rightHalf);
                    }
                    else
                    {
                        leftHalf = Tuple.Create(x - 1, y);
                        queue.Enqueue(leftHalf);
                    }
                }

                char tile = map[y + 1, x];
                if (tile == Program.WIDE_BOX_LEFT_HALF)
                {
                    leftHalf = Tuple.Create(x, y + 1);
                    rightHalf = Tuple.Create(x + 1, y + 1);
                    queue.Enqueue(leftHalf);
                    queue.Enqueue(rightHalf);
                }
                else if (tile == Program.WIDE_BOX_RIGHT_HALF)
                {
                    rightHalf = Tuple.Create(x, y + 1);
                    leftHalf = Tuple.Create(x - 1, y + 1);
                    queue.Enqueue(rightHalf);
                    queue.Enqueue(leftHalf);
                }
            }
            return boxHalves;
        }
    }
}
