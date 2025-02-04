using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

namespace Puzzle23
{
    class Program
    {
        public static void Main(string[] args)
        {
            string filename = args[0];

            Graph graph = new Graph();
            string pattern = @"([a-z]{2})-([a-z]{2})";
            foreach (string line in File.ReadLines(filename)) 
            {
                Match match = Regex.Match(line, pattern);
                string computer1 = match.Groups[1].Value;
                string computer2 = match.Groups[2].Value;
                graph.AddNode(computer1);
                graph.AddNode(computer2);
                graph.InsertEdge(computer1, computer2);
            }

            Part1(graph);
        }

        static void Part1(Graph graph)
        {
            // problem of finding all 3-cliques
            List<HashSet<string>> sets = new List<HashSet<string>>();
            foreach (string computer in graph.GetNodes())
            {
                Find3Cliques(computer, graph, sets);
            }
            IEnumerable<HashSet<string>> uniqueSets = sets.Distinct(HashSet<string>.CreateSetComparer());
            Console.WriteLine(
                uniqueSets.Count(set => set.Any(computer => computer.StartsWith("t")))
            );
        }

        static void Find3Cliques(string computer, Graph graph, List<HashSet<string>> sets)
        {
            string[] neighbors = graph.GetNeighbors(computer);
            for (int i = 0; i < neighbors.Length; i++)
            {
                for (int j = 0; j < neighbors.Length; j++)
                {
                    // skip if same computer
                    if (i == j)
                    {
                        continue;
                    }
                    // if the other two computers are connected to each other,
                    // it's a set of three inter-connected computers
                    if (graph.IsEdge(neighbors[i], neighbors[j]))
                    {
                        HashSet<string> computers = new HashSet<string>(3);
                        computers.Add(computer);
                        computers.Add(neighbors[i]);
                        computers.Add(neighbors[j]);
                        sets.Add(computers);
                    }
                }
            }
        }
    }

    // unweighted, undirected graph
    class Graph
    {
        class Edge
        {
            internal string source;
            internal string dest;

            internal Edge(string source, string dest)
            {
                this.source = source;
                this.dest = dest;
            }

            internal bool Equals(Edge e)
            {
                return source.Equals(e.source) && dest.Equals(e.dest);
            }
        }

        Dictionary<string, LinkedList<Edge>> nodes;
        int size;

        internal Graph()
        {
            nodes = new Dictionary<string, LinkedList<Edge>>();
            size = 0;
        }

        internal IEnumerable<string> GetNodes()
        {
            return nodes.Keys.AsEnumerable();
        }

        internal int GetSize() { return size; }
        
        internal void AddNode(string node)
        {
            if (!nodes.TryGetValue(node, out LinkedList<Edge> neighbors))
            {
                neighbors = new LinkedList<Edge>();
                nodes[node] = neighbors;
                size++;
            }
        }

        internal void InsertEdge(string source, string dest)
        {
            if (nodes.TryGetValue(source, out LinkedList<Edge> sourceNeighbors)
                && nodes.TryGetValue(dest, out LinkedList<Edge> destNeighbors))
            {
                sourceNeighbors.AddLast(new Edge(source, dest));
                destNeighbors.AddLast(new Edge(dest, source));
            }
        }

        internal bool IsEdge(string source, string dest)
        {
            if (nodes.TryGetValue(source, out LinkedList<Edge> sourceNeighbors))
            {
                return sourceNeighbors.Any(edge => edge.Equals(new Edge(source, dest)));
            }
            else
            {
                return false;
            }
        }

        internal string[] GetNeighbors(string node)
        {
            if (nodes.TryGetValue(node, out LinkedList<Edge> neighbors))
            {
                return neighbors.Select(edge => edge.dest).ToArray();
            }
            else
            {
                return null;
            }
        }
    }
}
