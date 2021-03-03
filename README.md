# Directed Hamiltonian Path

Instance: A directed graph G = (V, E) of n vertices, such that each vertex is labeled with a unique integer from 1 to n. A Hamiltonian path is a simple path (with no repeated nodes) such that this one contains all the vertices in V.

Question: Is there a Hamiltonian path from 1 to n in the graph G? 
 
**Note: This problem is NP-complete.** 
 
Example
----- 

Instance: A directed graph G = (V, E), where V = {1,2,3} and E = {(1, 2), (2, 3)}

Answer: A Hamiltonian path from 1 to 3 is **1 -> 2 -> 3**

Input of this project
-----

We convert the set of edges E into a MONOTONE-2CNF formula such that for each edge (u, v) with the two vertices u and v, we make a clause (u OR v) and join all the clauses by a disjunction with the AND operator.

Conversion format (In this project, we assume the conversion is already done by the user, that's why we receive as input only the formulas)
-----

The conversion is to a [DIMACS](http://www.satcompetition.org/2009/format-benchmarks2009.html) formula with the extension .cnf. 
  
Let's take as the **file.cnf** from our previous example: The directed graph G = (V, E), where V = {1,2,3} and E = {(1, 2), (2, 3)}
```  
p cnf 3 2
1 2 0
2 3 0  
```  

- The first line **p cnf 3 2** means the directed graph G has 3 vertices and 2 edges.

- The second line **1 2 0** means the edge (1, 2) (Note also that the number *0* means the end of the edge).

- The third line **2 3 0** means the edge (2, 3) (Note also that the number *0* means the end of the edge).

Compiling in Linux and Windows
-----

Install: Install JDK 8 and SBT 0.13

To build and run from the command prompt:

```
git clone https://github.com/frankvegadelgado/sat.git
cd sat
sbt assembly
cd bin
```

After that, copy the equivalent Dimacs **file.cnf** into the folder sat\bin\dimacs and next:

On Windows within sat\bin directory run

```
run.bat
```

On Linux within sat/bin directory run

```
chmod +x run.sh
./run.sh
```

Finally, it would obtain in the console output (the answer *true* for **file.cnf** which means this graph contains a Hamiltonian path from 1 to 3):

```
Starting....
RUNNING TASK
* EXECUTION TIME IN MILLISECONDS: 477
file.cnf: true
Finished....
```

*Important: It is possible to copy several DIMACS files with the extension .cnf inside of the directory sat\bin\dimacs and obtain something like*:

```
Starting....
RUNNING TASK
* EXECUTION TIME IN MILLISECONDS: 513
formula1.cnf: true
formula2.cnf: false
formula3.cnf: true
formula4.cnf: false
Finished....
```

# Code

- Scala code by Frank Vega

# License
- MIT.
