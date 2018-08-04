// Name: Michael Russo
// CS 455 PA3
// Fall 2016

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
Maze class

Stores information about a maze and can find a path through the maze
(if there is one).

Assumptions about the structure of a maze:
-- each maze is defined with a startLoc, endLoc, and mazeData that represents 
the walls and free spaces
-- no outer walls given in mazeData -- search assumes there is a virtual border
 around the maze (i.e., the maze path can't go outside of the maze boundaries)
-- start location for a path is a maze coordinate called startLoc
-- exit location is a maze coordinate called exitLoc
-- mazeData is input as a 2D array of booleans, where true means there is a wall
at that location, and false means there isn't (see public FREE / WALL
constants below)
-- in mazeData the first index indicates the row. e.g., mazeData[row][col]
-- only travel in 4 compass directions (no diagonal paths)
-- can't travel through walls
*/

public class Maze
{
  public static final boolean FREE = false;
  public static final boolean WALL = true;
  private static boolean[][] cellValues;
  private static MazeCoord startCoord;
  private static MazeCoord endCoord;
  private static ArrayList<MazeCoord> solutionPath;


  /**
  Constructs a maze.

  @param mazeData the maze to search.  See general Maze comments for what
  goes in this array.
  @param startLoc the location in maze to start the search (not necessarily on an edge)
  @param endLoc the "exit" location of the maze (not necessarily on an edge)
  
  PRE: 0 <= startLoc.getRow() < mazeData.length and 0 <= startLoc.getCol() < mazeData[0].length
    and 0 <= endLoc.getRow() < mazeData.length and 0 <= endLoc.getCol() < mazeData[0].length
  */
  public Maze(boolean[][] mazeData, MazeCoord startLoc, MazeCoord endLoc)
  {
    cellValues = mazeData;
    startCoord = startLoc;
    endCoord = endLoc;
    solutionPath = new ArrayList<MazeCoord>();
  }


  /**
  Returns the number of rows in the maze

  @return number of rows
  */
  public int numRows()
  {
    return cellValues.length;
  }


  /**
  Returns the number of columns in the maze

  @return number of columns
  */
  public int numCols()
  {
    return cellValues[0].length;
  }


  /**
  Returns true iff there is a wall at this location

  @param loc the location in maze coordinates
  @return whether there is a wall here

  PRE: 0 <= loc.getRow() < numRows() and 0 <= loc.getCol() < numCols()
  */
  public boolean hasWallAt(MazeCoord loc)
  {
    if ( cellValues[loc.getRow()][loc.getCol()] == WALL ) {
      return true;
    }
    return false;
  }


  /**
  Returns the entry location of this maze.

  @return maze coord defining start position
  */
  public MazeCoord getEntryLoc()
  {
    return startCoord;
  }


  /**
  Returns the exit location of this maze.
  */
  public MazeCoord getExitLoc()
  {
    return endCoord;
  }


  /**
  Returns the path through the maze. First element is starting location, and
  last element is exit location.  If there was not path, or if this is called
  before search, returns empty list.

  @return the maze path
  */
  public ArrayList<MazeCoord> getPath()
  {
    return solutionPath;
  }


  /**
  Find a path through the maze if there is one.  Client can access the
  path found via getPath method.

  @return whether path was found.
  */
  public boolean search()  {
    // initializes queue to store potential paths
    //  (a path is a list of coordinates through the maze)
    PriorityQueue<ArrayList<MazeCoord>> paths = new PriorityQueue<ArrayList<MazeCoord>>(11,new PathSorter());

    ArrayList<MazeCoord> potentialPath = new ArrayList<MazeCoord>();

    ArrayList<MazeCoord> exploredCoords = new ArrayList<MazeCoord>();

    // adds starting coordinate to first path in order to start search
    potentialPath.add(startCoord);
    paths.offer(potentialPath);

    return runSearch(paths, exploredCoords);  // returns the result of the method below
  }

  /**
  Runs a recursive search to determine whether a path through the maze exists.  If such
  a path exists, the solution path is stored.
  
  @return whether path was found.
  */
  private static boolean runSearch( PriorityQueue<ArrayList<MazeCoord>> paths, ArrayList<MazeCoord> exploredCoords)
  {
    // returns false if all paths have been explored and no goal was found
    if (paths.size() == 0) {
      return false;
    }

    // gets and removes first path in queue
    ArrayList<MazeCoord> potentialPath = paths.poll();

    // gets the coordinate for the current position along the maze path
    MazeCoord currentCoord = potentialPath.get(potentialPath.size()-1);

    // goal test
    if ( currentCoord.equals(endCoord) ) {
      solutionPath = potentialPath;
      return true;
    }

    exploredCoords.add(currentCoord); // adds current coord to set of explored coords

    ArrayList<MazeCoord> freeNeighbors = getFreeNeighbors(currentCoord);  // function below

    // creates a new potential path for each unexplored neighbor returned
    //  and adds each new path to the queue
    for ( MazeCoord coord : freeNeighbors ) {
      if ( !exploredCoords.contains(coord) ) {

        // creates a copy of potential path
        ArrayList<MazeCoord> updatedPath = new ArrayList<MazeCoord>();
        for ( MazeCoord oldCoords : potentialPath) {
          updatedPath.add(oldCoords);
        }

        // updates path and adds new path to queue
        updatedPath.add(coord);
        paths.offer(updatedPath);
      }
    }

    return runSearch(paths, exploredCoords);  // recursive call with updated params
  }

  /**
  Checks for free spaces surrounding the given coordinate.
  @return a list of neighboring coordinates.
  */
  private static ArrayList<MazeCoord> getFreeNeighbors(MazeCoord currentCoord)
  {
    // creates empty arraylist to store all potential neighbors
    ArrayList<MazeCoord> neighbors = new ArrayList<MazeCoord>();

    // gets (row, col) values for current coordinate
    int currentRow = currentCoord.getRow();
    int currentCol = currentCoord.getCol();

    // checks if coordinate is on a boundary or is neighbored by a wall
    // appends neighboring free spaces to neighbors list
    if (currentRow < cellValues.length - 1 && cellValues[currentRow+1][currentCol] == FREE) {
      neighbors.add(new MazeCoord(currentRow+1,currentCol));
    }
    if (currentRow > 0 && cellValues[currentRow-1][currentCol] == FREE ) {
      neighbors.add(new MazeCoord(currentRow-1,currentCol));
    }
    if (currentCol < cellValues[0].length - 1 && cellValues[currentRow][currentCol+1] == FREE) {
      neighbors.add(new MazeCoord(currentRow,currentCol+1));
    }
    if (currentCol > 0 && cellValues[currentRow][currentCol-1] == FREE)
    {
      neighbors.add(new MazeCoord(currentRow,currentCol-1));
    }
    return neighbors;
  }

  /**
  Class for comparing elements in the priority queue.  Uses a heuristic value
  (manhatten distance to goal) as well as actual path cost to sort each path.

  Note: originally ordered path by shortest value, according to A* search, but test
  case 'upperLeft.txt' resulted in StackOverflowError. Since, shortest path was not
  a priortiy for this assignment, the comparator was reversed.  As a result, the
  priortiy queue is maintained in reverse order, and optimal solution is not guranteed.
  */
  static class PathSorter implements Comparator< ArrayList<MazeCoord> >
  {
    public int compare( ArrayList<MazeCoord> path1, ArrayList<MazeCoord> path2 )
    {
      // gets current location in path
      MazeCoord lastCoord1 = path1.get(path1.size()-1);
      MazeCoord lastCoord2 = path2.get(path2.size()-1);

      // computes Manahattan distance to goal
      int heuristic1 = Math.abs(lastCoord1.getRow() - endCoord.getRow()) +
      Math.abs(lastCoord1.getCol() - endCoord.getCol());
      int heuristic2 = Math.abs(lastCoord2.getRow() - endCoord.getRow()) +
      Math.abs(lastCoord2.getCol() - endCoord.getCol());

      // computes path cost using actual path length and heurstic
      int pathCost1 = path1.size() + heuristic1;
      int pathCost2 = path2.size() + heuristic2;

      // returns comparison value based on path cost (longest path given priority)
      if ( pathCost1 < pathCost2 ) {
        return 1;
      }
      else if ( pathCost1 > pathCost2 ) {
        return -1;
      } else {
        return 0;
      }
    }
  }

}
