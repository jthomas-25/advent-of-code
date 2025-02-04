using System;
using System.IO;
using System.Linq;

namespace Puzzle9
{
    class Program
    {
        const char FREE_SPACE = '.';

        public static void Main(string[] args)
        {
            string filename = args[0];

            string diskMap = File.ReadAllText(filename).TrimEnd();
            int totalBlocks = diskMap.Sum(digit => GetNumericValue(digit));
            char[] initialDiskLayout = new char[totalBlocks];
            char maxId = CreateDisk(diskMap, initialDiskLayout);
            char[] blocks = new char[initialDiskLayout.Length];
            initialDiskLayout.CopyTo(blocks, 0);
            
            Part1(blocks);
            blocks = initialDiskLayout;
            Part2(blocks, maxId);
        }

        static void Part1(char[] blocks)
        {
            CompactFilesAtBlockLevel(blocks);
            Console.WriteLine(Checksum(blocks));
        }

        static void Part2(char[] blocks, char maxId)
        {
            CompactFilesAtFileLevel(blocks, maxId);
            Console.WriteLine(Checksum(blocks));
        }

        static int GetNumericValue(char value)
        {
            return value - '0';
        }

        static void CompactFilesAtBlockLevel(char[] blocks)
        {
            /*
             * Move file blocks one by one from the end of the disk
             * to the leftmost free space block, until the file
             * blocks are contiguous
             */
            int left = 0;
            int right = blocks.Length - 1;
            while (left != right)
            {
                if (blocks[left] == FREE_SPACE && blocks[right] != FREE_SPACE)
                {
                    blocks[left] = blocks[right];
                    blocks[right] = FREE_SPACE;
                }
                if (blocks[left] != FREE_SPACE)
                {
                    left++;
                }
                if (blocks[right] == FREE_SPACE)
                {
                    right--;
                }
            }
        }

        static void CompactFilesAtFileLevel(char[] blocks, char maxId)
        {
            /*
             * Move entire files one by one from the end of the disk
             * to the leftmost span of free space blocks that is big
             * enough to fit the file, starting with the file with
             * the maximum ID number.
             * 
             * - If no such span of free space exists, a file will not be moved.
             * - Each file will try to move once.
             */
            // starting position to search for rightmost span of file blocks
            int right = blocks.Length - 1;
            char zero = Convert.ToChar("0");
            for (char id = maxId; id >= zero; id--)
            {
                FindFileSpan(blocks, id, right).Deconstruct(out int fileStart, out int fileEnd);
                int fileSpan = (fileEnd - fileStart) + 1;
                bool attemptComplete = false;
                // starting position to search for leftmost span of free space
                int left = 0;
                while (!attemptComplete)
                {
                    FindFreeSpan(blocks, left, fileStart).Deconstruct(out int freeStart, out int freeEnd);
                    if (freeStart == -1)
                    {
                        attemptComplete = true;
                        continue;
                    }
                    int freeSpan = (freeEnd - freeStart) + 1;
                    if (freeStart < fileStart && freeSpan >= fileSpan)
                    {
                        for (int i = 0; i < fileSpan; i++)
                        {
                            blocks[freeStart + i] = blocks[fileStart + i];
                            blocks[fileStart + i] = FREE_SPACE;
                        }
                        attemptComplete = true;
                    }
                    else
                    {
                        left = freeEnd + 1;
                    }
                }
                right = fileStart - 1;
            }
        }

        static Tuple<int, int> FindFileSpan(char[] blocks, char id, int startIndex)
        {
            int fileEnd = -1;
            for (int i = startIndex; i >= 0; i--)
            {
                if (blocks[i] == id)
                {
                    fileEnd = i;
                    break;
                }
            }
            int fileStart = fileEnd;
            for (int i = fileEnd; i > 0; i--)
            {
                if (blocks[i - 1] != id)
                {
                    break;
                }
                fileStart--;
            }
            return Tuple.Create(fileStart, fileEnd);
        }

        static Tuple<int, int> FindFreeSpan(char[] blocks, int startIndex, int endIndex)
        {
            int freeStart = -1;
            for (int i = startIndex; i < endIndex; i++)
            {
                if (blocks[i] == FREE_SPACE)
                {
                    freeStart = i;
                    break;
                }
            }
            if (freeStart == -1)
            {
                return Tuple.Create(-1, -1);
            }
            int freeEnd = freeStart;
            for (int i = freeStart; i < endIndex; i++)
            {
                if (blocks[i + 1] != FREE_SPACE)
                {
                    break;
                }
                freeEnd++;
            }
            return Tuple.Create(freeStart, freeEnd);
        }

        static long Checksum(char[] blocks)
        {
            long checksum = 0;
            for (int i = 0; i < blocks.Length; i++)
            {
                if (blocks[i] == FREE_SPACE)
                {
                    continue;
                }
                checksum += i * GetNumericValue(blocks[i]);
            }
            return checksum;
        }

        static char CreateDisk(string diskMap, char[] blocks)
        {
            int blockPos = 0;
            // ID number of a file
            char id = Convert.ToChar("0");
            for (int i = 0; i < diskMap.Length; i++)
            {
                if (i % 2 == 0)
                {
                    // copy file blocks
                    int numFileBlocks = GetNumericValue(diskMap[i]);
                    for (int j = 0; j < numFileBlocks; j++)
                    {
                        blocks[blockPos] = id;
                        blockPos++;
                    }
                    id++;
                }
                else
                {
                    // copy empty blocks
                    int numFreeSpaceBlocks = GetNumericValue(diskMap[i]);
                    for (int j = 0; j < numFreeSpaceBlocks; j++)
                    {
                        blocks[blockPos] = FREE_SPACE;
                        blockPos++;
                    }
                }
            }
            // Return the highest file ID number
            return (id > 0) ? Convert.ToChar(id - 1) : id;
        }

        static void DisplayDisk(char[] blocks)
        {
            // debug method
            string[] array = new string[blocks.Length];
            for (int i = 0; i < blocks.Length; i++)
            {
                char block = blocks[i];
                if (block == FREE_SPACE)
                {
                    array[i] = block.ToString();
                }
                else
                {
                    array[i] = GetNumericValue(block).ToString();
                }
            }
            Console.WriteLine(string.Join(" ", array));
        }
    }
}
