using System;
using System.IO;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace Puzzle13
{
    class Program
    {
        const int A_BUTTON_COST = 3;
        const int B_BUTTON_COST = 1;
        static long maxButtonPresses;

        public static void Main(string[] args)
        {
            string filename = args[0];
            
            List<ClawMachine> machines = new List<ClawMachine>();
            using (StreamReader sr = new StreamReader(filename))
            {
                int clawRightA = 0;
                int clawRightB = 0;
                int clawForwardA = 0;
                int clawForwardB = 0;
                long prizeX = 0;
                long prizeY = 0;
                string pattern = @"\d+";
                string line = "";
                while (line != null)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        line = sr.ReadLine();
                        MatchCollection matches = Regex.Matches(line, pattern);
                        switch (i)
                        {
                            case 0:
                                clawRightA = int.Parse(matches[0].Value);
                                clawForwardA = int.Parse(matches[1].Value);
                                break;
                            case 1:
                                clawRightB = int.Parse(matches[0].Value);
                                clawForwardB = int.Parse(matches[1].Value);
                                break;
                            case 2:
                                prizeX = long.Parse(matches[0].Value);
                                prizeY = long.Parse(matches[1].Value);
                                break;
                        }
                    }
                    machines.Add(new ClawMachine(clawRightA,
                                                 clawForwardA,
                                                 clawRightB,
                                                 clawForwardB,
                                                 prizeX,
                                                 prizeY));
                    line = sr.ReadLine();
                }
            }

            Part1(machines);
            Part2(machines);
        }

        static void Part1(List<ClawMachine> machines)
        {
            maxButtonPresses = 100;
            long total = 0;
            for (int i = 0; i < machines.Count; i++)
            {
                ClawMachine machine = machines[i];
                long cost = MinimizeCost(ref machine);
                if (cost > 0)
                {
                    total += cost;
                }
            }
            Console.WriteLine(total);
        }

        static void Part2(List<ClawMachine> machines)
        {
            maxButtonPresses = long.MaxValue;
            long total = 0;
            for (int i = 0; i < machines.Count; i++)
            {
                ClawMachine machine = machines[i];
                machine.prizeX += 10_000_000_000_000;
                machine.prizeY += 10_000_000_000_000;
                long cost = MinimizeCost(ref machine);
                if (cost > 0)
                {
                    total += cost;
                }
            }
            Console.WriteLine(total);
        }

        static long MinimizeCost(ref ClawMachine machine)
        {
            /*
             * Return the minimum number of tokens that will win the prize of the
             * given machine with a combination of A and B button presses, if a
             * combination exists; otherwise -1.
             * 
             * This method attempts to solve the following system of linear equations for A and B:
             * 
             * X = dx_a * A + dx_b * B
             * Y = dy_a * A + dy_b * B
             * 
             * where
             * 
             * - A is the number of A button presses
             * - B is the number of B button presses
             * - X is the prize's horizontal distance from 0
             * - Y is the prize's vertical distance from 0
             * - dx_a is how many units the claw moves right when the A button is pressed
             * - dx_b is how many units the claw moves right when the B button is pressed
             * - dy_a is how many units the claw moves forward when the A button is pressed
             * - dy_b is how many units the claw moves forward when the B button is pressed
             * 
             * The system is put into matrix form M * x = b to find the unique solution
             * x = M^-1 * b, where M^-1 is the inverse of matrix M. To avoid issues with
             * floating-point numbers, the inverse is not calculated directly. Instead, the
             * adjugate of M is calculated and multiplied by b, then divided by the determinant
             * of M to compute x. Thus:
             * 
             * x = adj(M) * b / det(M) = M^-1 * b
             * 
             * Each element of x is stored in separate quotient and remainder variables. Because
             * A and B must be integers in the interval [0, max_button_presses] for the solution
             * to be valid, the remainder of each division must necessarily be 0.
             * 
             * Note: The determinant is assumed to be nonzero, but just in case it is 0, this
             * method handles the DivideByZeroException by returning -1.
             */
            int[,] matrix = { { machine.clawDxForButtonA, machine.clawDxForButtonB },
                              { machine.clawDyForButtonA, machine.clawDyForButtonB } };
            int[,] adj = Adjugate(matrix);
            try
            {
                long buttonPressesA = Math.DivRem(adj[0, 0] * machine.prizeX + adj[0, 1] * machine.prizeY,
                                                  Determinant(matrix),
                                                  out long remainderA);
                long buttonPressesB = Math.DivRem(adj[1, 0] * machine.prizeX + adj[1, 1] * machine.prizeY,
                                                  Determinant(matrix),
                                                  out long remainderB);
                // if solutions are integers
                if (remainderA == 0 && remainderB == 0)
                {
                    if (InRange(buttonPressesA, 0, maxButtonPresses)
                        && InRange(buttonPressesB, 0, maxButtonPresses))
                    {
                        long fewestTokens = A_BUTTON_COST * buttonPressesA + B_BUTTON_COST * buttonPressesB;
                        return fewestTokens;
                    }
                }
                return -1;
            }
            catch (DivideByZeroException)
            {
                return -1;
            }
        }

        static long Determinant(int[,] matrix)
        {
            return matrix[0, 0] * matrix[1, 1] - matrix[0, 1] * matrix[1, 0];
        }

        static int[,] Adjugate(int[,] matrix)
        {
            return new int[,] { { matrix[1, 1], -matrix[0, 1] },
                                { -matrix[1, 0], matrix[0, 0] } };
        }

        static bool InRange(long number, long start, long end)
        {
            return number >= start && number <= end;
        }
    }

    internal struct ClawMachine
    {
        internal readonly int clawDxForButtonA;
        internal readonly int clawDxForButtonB;
        internal readonly int clawDyForButtonA;
        internal readonly int clawDyForButtonB;
        internal long prizeX;
        internal long prizeY;

        internal ClawMachine(int clawRightA,
                             int clawForwardA,
                             int clawRightB,
                             int clawForwardB,
                             long prizeX,
                             long prizeY)
        {
            clawDxForButtonA = clawRightA;
            clawDxForButtonB = clawRightB;
            clawDyForButtonA = clawForwardA;
            clawDyForButtonB = clawForwardB;
            this.prizeX = prizeX;
            this.prizeY = prizeY;
        }
    }
}
