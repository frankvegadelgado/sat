# Algebraic Polynomial Sum Solver Over {0, 1}

Instance: A polynomial P(k, x<sub>1</sub>, x<sub>2</sub>, ..., x<sub>n</sub>) which is the sum of terms, where each term is a product of k distinct variables x<sub>j</sub>.

Answer: Calculate the total sum value of &sum;<sub>U<sub>i</sub></sub> P(k, u<sub>1</sub>, u<sub>2</sub>, ... u<sub>n</sub>), for all the possible assignments U<sub>i</sub> = {u<sub>1</sub>, u<sub>2</sub>, ... u<sub>n</sub>} such that u<sub>j</sub> is in {0, 1}.
 
Example
----- 

Instance: P(2, x<sub>1</sub>, x<sub>2</sub>, x<sub>3</sub>) = x<sub>1</sub> * x<sub>2</sub> + x<sub>2</sub> * x<sub>3</sub>

Answer: The total sum value is **4** for all the possible assignments:

|  x<sub>1</sub>    |  x<sub>2</sub>  |  x<sub>3</sub>   |      P(x<sub>1</sub>, x<sub>2</sub>, x<sub>3</sub>)        |
| ----------------- | ----------------| ---------------- | ---------------------------------------------------------- |
|         1         |         1       |          1       |        2                                                   |
|         1         |         1       |          0       |        1                                                   |
|         0         |         1       |          1       |        1                                                   |
|         0         |         0       |          0       |        0                                                   |
|         1         |         0       |          1       |        0                                                   |
|         0         |         0       |          1       |        0                                                   |
|         1         |         0       |          0       |        0                                                   |
|         0         |         1       |          0       |        0                                                   |

**Total**: 2 + 1 + 1 + 0 + 0 + 0 + 0 + 0  = **4**.

Input of this project
-----

Convert the polynomial P(k, x<sub>1</sub>, x<sub>2</sub>, ..., x<sub>n</sub>) into a MONOTONE-2SAT formula such that for each term x<sub>i_1</sub>* x<sub>i_2</sub> * ... * x<sub>i_k</sub> with k variables, we make a clause (x<sub>i_1</sub> OR x<sub>i_2</sub> OR ... OR x<sub>i_k</sub>) and join all the summands by a disjunction with the AND operator.

Conversion format (In this project, we assume the conversion is already done by the user, that's why we receive as input only the formulas))
-----

The conversion is to a [DIMACS](http://www.satcompetition.org/2009/format-benchmarks2009.html) formula with the extension .cnf. 
  
Let's take as the **file.cnf** from our previous example: P(2, x<sub>1</sub>, x<sub>2</sub>, x<sub>3</sub>) = x<sub>1</sub> * x<sub>2</sub> + x<sub>2</sub> * x<sub>3</sub>
```  
p cnf 3 2
1 2 0
2 3 0  
```  

- The first line **p cnf 3 2** means the polynomial P(x<sub>1</sub>, x<sub>2</sub>, x<sub>3</sub>) has 3 variables and 2 terms.

- The second line **1 2 0** means the term x<sub>1</sub> * x<sub>2</sub> (Note also that the number *0* means the end of the term).

- The third line **2 3 0** means the term x<sub>2</sub> * x<sub>3</sub> (Note also that the number *0* means the end of the term).

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

Finally, it would obtain in the console output (the answer 4 for **file.cnf**):

```
Starting....
RUNNING TASK
* EXECUTION TIME IN MILLISECONDS: 435
file.cnf: 4.0
Finished....
```

*Important: It is possible to copy several DIMACS files with the extension .cnf inside of the directory sat\bin\dimacs and obtain something like*:

```
Starting....
RUNNING TASK
* EXECUTION TIME IN MILLISECONDS: 565
formula1.cnf: 1.0
formula2.cnf: 8.0
formula3.cnf: 4.0
formula4.cnf: 6.0
Finished....
```

Changing the execution
-----

The run.bat has this code:

```
java -jar "..\target\scala-2.12\sat.jar" --reduce dimacs 2
```

We can change the directory *dimacs* and the value of **k** (in this example has the value of *2*).




# Code

- Scala code by Frank Vega

# License
- MIT.
