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

**F-measure of methods in different failure rate**

<table class="tg">
<thead>
  <tr>
    <th class="tg-9wq8" rowspan="2">Method</th>
    <th class="tg-9wq8" colspan="4">Failure Rate</th>
  </tr>
  <tr>
    <th class="tg-c3ow">0.01</th>
    <th class="tg-c3ow">0.005</th>
    <th class="tg-c3ow">0.002</th>
    <th class="tg-c3ow">0.001</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td class="tg-c3ow">Random Testing</td>
    <td class="tg-c3ow">97</td>
    <td class="tg-c3ow">198</td>
    <td class="tg-c3ow">501</td>
    <td class="tg-c3ow">1014</td>
  </tr>
  <tr>
    <td class="tg-c3ow">Select Test from Candidate</td>
    <td class="tg-c3ow">37</td>
    <td class="tg-c3ow">105</td>
    <td class="tg-c3ow">218</td>
    <td class="tg-c3ow">423</td>
  </tr>
  <tr>
    <td class="tg-c3ow">Static Partitioning</td>
    <td class="tg-c3ow">84</td>
    <td class="tg-c3ow">143</td>
    <td class="tg-c3ow">383</td>
    <td class="tg-c3ow">770</td>
  </tr>
  <tr>
    <td class="tg-c3ow">Hill Climbing</td>
    <td class="tg-c3ow">92</td>
    <td class="tg-c3ow">189</td>
    <td class="tg-c3ow">496</td>
    <td class="tg-c3ow">1374</td>
  </tr>
</tbody>
</table>

# Parameter adjustment

The failure rate and the size of the input domain can be adjusted [here](https://github.com/ghZHM/adaptiveRandomTesting/blob/master/src/com/company/Main.java#L18).

# Usage
1. Clone the project.
2. Open it with IntelliJ IDEA.
3. Run the main function.
