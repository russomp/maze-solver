// Name: Michael Russo
// CS 455 PA3
// Fall 2016

import java.awt.Graphics;
import javax.swing.JComponent;
import java.awt.Color;
import java.util.ArrayList;

/**
   MazeComponent class:
    A component that displays the maze and path through it if one has been found.
*/
public class MazeComponent extends JComponent
{
   private Maze maze;

   private static final int START_X = 10; // where to start drawing maze in frame
   private static final int START_Y = 10;
   private static final int BOX_WIDTH = 20;  // width and height of one maze unit
   private static final int BOX_HEIGHT = 20;
   private static final int INSET = 2;  // how much smaller on each side to make entry/exit inner box
   /**
      Constructs the component.
      @param maze   the maze to display
   */
   public MazeComponent(Maze maze)
   {
     this.maze = maze;
   }


   /**
     Draws the current state of maze including the path through it if one has
     been found.
     @param g the graphics context
   */
   public void paintComponent(Graphics g)
   {
     // initializes the starting (x,y) posistion for drawing graphics object
     int x_loc = START_X;
     int y_loc = START_Y;

     MazeCoord currentCoord;  // stores the current (row, col) position of graphics object

     Graphics component = g;

     // loops through each element of the maze draws a maze cell based on value
     for (int i = 0; i < maze.numRows(); i++ ) {

       for (int j = 0; j < maze.numCols(); j++) {

         currentCoord = new MazeCoord(i,j); // resets coordinate based on cell

         // checks if cell is the start location
         if ( currentCoord.equals(maze.getEntryLoc()) ) {
           paintEntryLoc(component, x_loc, y_loc);
         }
         // checks if cell is the end location
         else if ( currentCoord.equals(maze.getExitLoc()) ) {
           paintExitLoc(component, x_loc, y_loc);
         }
         else {
           // checks for wall or free space and assigns proper color
           if ( maze.hasWallAt(currentCoord) ) {
             component.setColor(Color.BLACK);
             component.fillRect(x_loc, y_loc, BOX_WIDTH, BOX_HEIGHT);
           }
           else {
             component.setColor(Color.WHITE);
             component.fillRect(x_loc, y_loc, BOX_WIDTH, BOX_HEIGHT);
           }
         }
         x_loc += BOX_WIDTH;
       }
       x_loc = START_X;   // resets x coord to draw the next row
       y_loc += BOX_HEIGHT;
     }

     // draws a maze border
     component.setColor(Color.BLACK);
     component.drawRect(START_X, START_Y, maze.numCols() * BOX_WIDTH, maze.numRows() * BOX_HEIGHT);

     // checks if a search has finished and a path has been returned
     if ( !maze.getPath().isEmpty() )
     {
       paintPath(component, maze.getPath());  // repaints maze with returned path
     }
   }


   /**
     Draws the current starting point of the maze.
   */
   private static void paintEntryLoc(Graphics component, int x_loc, int y_loc)
   {
     // draws yellow cell with inset for start location of maze
     component.setColor(Color.YELLOW);
     component.fillRect(x_loc+INSET, y_loc+INSET, BOX_WIDTH-2*INSET, BOX_HEIGHT-2*INSET);
     // fills inset white
     component.setColor(Color.WHITE);
     component.drawRect(x_loc, y_loc, BOX_WIDTH, BOX_HEIGHT);
     component.drawRect(x_loc+1, y_loc+1, BOX_WIDTH-1, BOX_HEIGHT-1);
   }


   /**
     Draws the current starting point of the maze.
   */
   private static void paintExitLoc(Graphics component, int x_loc, int y_loc)
   {
     // draws green cell with inset for end location of maze
     component.setColor(Color.GREEN);
     component.fillRect(x_loc+INSET, y_loc+INSET, BOX_WIDTH-2*INSET, BOX_HEIGHT-2*INSET);
     // fills inset white
     component.setColor(Color.WHITE);
     component.drawRect(x_loc, y_loc, BOX_WIDTH, BOX_HEIGHT);
     component.drawRect(x_loc+1, y_loc+1, BOX_WIDTH-1, BOX_HEIGHT-1);
   }


   /**
     Repaints the maze with the path returned by search if a path was found.
   */
   private static void paintPath(Graphics component, ArrayList<MazeCoord> path)
   {
     // initializes the pathline coordinates and color
     component.setColor(Color.BLUE);
     MazeCoord loc1 = path.get(0);
     MazeCoord loc2 = path.get(0);

     for (int i = 1; i < path.size(); i++) {
       // gets (x,y) location for start of pathline
       int loc1_y = (loc1.getRow()+1) * BOX_WIDTH;
       int loc1_x = (loc1.getCol()+1) * BOX_HEIGHT;

       // gets (x,y) location for pathline endpoint
       loc2 = path.get(i);
       int loc2_y = (loc2.getRow()+1) * BOX_WIDTH;
       int loc2_x = (loc2.getCol()+1) * BOX_HEIGHT;

       // draws pathline
       component.drawLine(loc1_x, loc1_y, loc2_x, loc2_y);

       // sets the pathline start location to end of previous pathline
       loc1 = new MazeCoord(loc2.getRow(), loc2.getCol());
     }
   }
}
