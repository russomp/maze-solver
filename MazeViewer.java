// Name: Michael Russo
// CS 455 PA3
// Fall 2016

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import java.util.Scanner;


/**
* MazeViewer class
*
* Program to read in and display a maze and a path through the maze. At user
* command displays a path through the maze if there is one.
*
* How to call it from the command line:
*
*      java MazeViewer mazeFile
*
* where mazeFile is a text file of the maze. The format is the number of rows
* and number of columns, followed by one line per row, followed by the start location,
* and ending with the exit location. Each maze location is
* either a wall (1) or free (0). Here is an example of contents of a file for
* a 3x4 maze, with start location as the top left, and exit location as the bottom right
* (we count locations from 0, similar to Java arrays):
*
* 3 4
* 0111
* 0000
* 1110
* 0 0
* 2 3
*
*/

public class MazeViewer {

  private static final char WALL_CHAR = '1';
  private static final char FREE_CHAR = '0';

  public static void main(String[] args)  {

    String fileName = "";

    try {

      if (args.length < 1) {
        System.out.println("ERROR: missing file name command line argument");
      }
      else {
        fileName = args[0];

        JFrame frame = readMazeFile(fileName);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
      }

    }
    catch (FileNotFoundException exc) {
      System.out.println("File not found: " + fileName);
    }
    catch (IOException exc) {
      exc.printStackTrace();
    }
  }

  /**
  readMazeFile reads in maze from the file whose name is given and
  returns a MazeFrame created from it.

  @param fileName
  the name of a file to read from (file format shown in class comments, above)
  @returns a MazeFrame containing the data from the file.

  @throws FileNotFoundException
  if there's no such file (subclass of IOException)
  @throws IOException
  (hook given in case you want to do more error-checking.
  that would also involve changing main to catch other exceptions)
  */
  private static MazeFrame readMazeFile(String fileName) throws IOException
  {
    // creates a scanner object to read file lines
    File inputFile = new File(fileName);
    Scanner fileReader = new Scanner(inputFile);

    // reads the first file line to get maze size
    String[] sizeData = fileReader.nextLine().split(" ");
    int numMazeRows = Integer.parseInt(sizeData[0]);
    int numMazeCols = Integer.parseInt(sizeData[1]);

    // initializes an empty array to store maze data
    boolean[][] mazeCellValues = new boolean[numMazeRows][numMazeCols];

    // sets maze values for each array element based on input file data
    for (int i = 0; i < numMazeRows; i++) {
      // reads file lines containing row values
      String fileLine = fileReader.nextLine();
      // parses each file line to unpack column values
      for (int j = 0; j < numMazeCols; j++) {
        // assigns a boolean to maze value based on whether a wall is present
        //  (i.e. a '1' given in the input maze data)
        if (fileLine.charAt(j) == WALL_CHAR) {
          mazeCellValues[i][j] = true;  // true if wall is present
        } else {
          mazeCellValues[i][j] = false; // false if open maze space
        }
      }
    }

    // reads second to last file line to get maze start location
    String[] entryLocData = fileReader.nextLine().split(" ");
    int entryLocXCoord = Integer.parseInt(entryLocData[0]);
    int entryLocYCoord = Integer.parseInt(entryLocData[1]);

    // sets start location to an open space
    mazeCellValues[entryLocXCoord][entryLocYCoord] = false;

    // creates a maze coord for start location based on (row, col)
    MazeCoord entryLoc = new MazeCoord(entryLocXCoord, entryLocYCoord);

    // reads last file line to get maze exit location
    String[] exitLocData = fileReader.nextLine().split(" ");
    int exitLocXCoord = Integer.parseInt(exitLocData[0]);
    int exitLocYCoord = Integer.parseInt(exitLocData[1]);

    // sets exit location to an open space
    mazeCellValues[exitLocXCoord][exitLocYCoord] = false;

    // creates a maze coord for start location based on (row, col)
    MazeCoord exitLoc = new MazeCoord(exitLocXCoord, exitLocYCoord);

    return new MazeFrame(mazeCellValues, entryLoc, exitLoc);
  }
}
