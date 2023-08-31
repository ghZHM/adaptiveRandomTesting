# Adaptive Random Testing
MSc dissertation project at the University of Sheffield: **Testing and Diversity**

# Overview
1. Implement *Adaptive random testing by **static partitioning*** proposed by Sabor K K. and Thiel S.

2. Implement *Adaptive Random Testing incorporating a **local search technique*** by Schneckenburger C and Schweiggert F.

3. Implement Select Test From Candidate. Distribution and Select Strategy from Candidate Set can be modified [here](https://github.com/ghZHM/adaptiveRandomTesting/blob/master/src/com/company/Main.java#L113).
> Uniform distribution & Non-uniform distribution; Average distance & Maximum distance selection were implemented.

And **Random Testing** as the baseline.

The failure Region is the block(strip) pattern.

The result printed in .csv contains F-measure and multiple metrics to evaluate test case distribution.

# Parameter adjustment

The failure rate and the size of the input domain can be adjusted [here](https://github.com/ghZHM/adaptiveRandomTesting/blob/master/src/com/company/Main.java#L18).

# Usage
1. Clone the project.
2. Open it with IntelliJ IDEA.
3. Run the main function.
