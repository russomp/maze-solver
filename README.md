# Maze Solver

A Java program for displaying and finding paths through mazes built in FL2016 for CSCI455: Programming Systems Design at USC.  

The maze solver builds and displays the given maze in a GUI using Java Swing.  Mazes are passed as textfiles detailing the number of rows and columns in a maze, a matrix of walls (1s) and free spaces (0s), and a start and end location.  The solver then uses a greedy algorithm minimizing manhatten distance to the end location to compute a path through the maze if one exists and draws the path in the GUI.

Example maze representation:
```
9 10              # rows, cols
0111111111        # 1=wall, 0=free space
0110000000
0000111110
1011101010
1000001000
1011011101
1010001111
0000100000
1111111110
5 8              # start location
8 9              # end location
```

Demo:
![Demo](https://github.com/russomp/maze-solver/blob/master/maze_solver_demo.gif)

### Getting started

```
git clone https://github.com/russomp/maze-solver
make clean
make
java MazeViewer ./mazes/MAZE_FILE_TO_RUN
```

#### TODO
Implemennt A* search to find optimal path through maze.
