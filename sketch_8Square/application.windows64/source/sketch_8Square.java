import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Arrays; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_8Square extends PApplet {



int[] board = new int[9];
int[][] eBoard = new int[3][3];
PFont f;

public void setup() {
  
  f = createFont("Arial",16,true);
  
  for (int i = 0; i < board.length; i++) {
    board[i] = i;
  }
  
  genSolvable();
}

public void draw() {
  background(255);
  drawGrid();
  fillInEBoard();
  drawTiles();
  
  //takeRandomMove();
  
  if (isFinished()) {
    surface.setTitle("8Square Finished!");
  } else {
    surface.setTitle("8Square Unfinished");
  }
}

public void mousePressed() {
  if (!isFinished()) {
    if (mouseX <= width && mouseX >= 0 && mouseY <= height && mouseY >= 0) {
      int idx = getRealPos(getEPosWithCoords(mouseX, mouseY));
      if (isEmptyNeighbor(idx)) swap(idx, getEmptyIndex());
      fillInEBoard();
    }
  }
}

public void takeRandomMove() {
  swap((int) random(board.length), (int) random(board.length));
}

public void drawTiles() {
  for (int i = 0; i < board.length; i++) {
    int t = board[i];
    
    if (t != 0) {
      PVector ePos = getEPos(i);
      textFont(f,16);
      stroke(0);
      fill(0);
      text(t, (ePos.x*200)+100, (ePos.y*200)+100);
    }
  }
}

public int getYCoord(int pos) {
  if (pos <= 2) return 0;
  else if (pos <= 4) return 1;
  else return 2;
}

public void drawGrid() {
  line(200, 0, 200, height);
  line(400, 0, 400, height);
  
  line(0, 200, width, 200);
  line(0, 400, width, 400);
}

public void shuffle() {
  int lim = (int) random(10, 20);
  for (int i = 0; i < lim; i++) {
    swap((int) random(board.length), (int) random(board.length));
  }
  fillInEBoard();
}

public void swap(int idxA, int idxB) {
  int tmp = board[idxA];
  board[idxA] = board[idxB];
  board[idxB] = tmp;
}

public void fillInEBoard() {
  int index = 0;
  
  for (int x = 0; x < eBoard.length; x++) {
    for (int y = 0; y < eBoard[0].length; y++) {
      eBoard[x][y] = board[index];
      index++;
    }
  }
}

public PVector getEPos(int index) {
  int count = 0;
  PVector ePos = new PVector();
  for (int i = 0; i < index; i++) {
    count++;
    if (count >= 3) {
      count = 0;
      ePos.x = 0;
      ePos.y++;
    } else {
      ePos.x++;
    }
  }
  
  return ePos;
}

public int getRealPos(PVector ePos) {
  int count = 0;
  for (int x = 0; x < eBoard.length; x++) {
    for (int y = 0; y < eBoard[0].length; y++) {
      if (x == ePos.x && y == ePos.y) {
        return count;
      }
      count++;
    }
  }
  
  return -1;
}

public PVector getEPosWithCoords(int x, int y) {
  int yCoord = x / 200;
  int xCoord = y / 200;
  return new PVector(xCoord, yCoord);
}

public int getEmptyIndex() {
  for (int i = 0; i < board.length; i++) {
    if (board[i] == 0) {
      return i;
    }
  }
  
  return -1;
}

public boolean isEmptyNeighbor(int index) {
  int emptyIndex = getEmptyIndex();
  PVector e = getEPos(emptyIndex);
  
  PVector t = getEPos(index);
  
  if (e.x == t.x && abs(e.y - t.y) == 1) {
    return true;
  } else if (e.y == t.y && abs(e.x - t.x) == 1) {
    return true;
  } else {
    return false;
  }
}

public boolean isFinished() {
  boolean incorrect = false;
  int[] perfect = {0, 1, 2, 3, 4, 5, 6, 7, 8};
  for (int i = 0; i < board.length; i++) {
    if (board[i] != perfect[i]) incorrect = true;
  }
  
  return !incorrect;
}

public boolean isSolvable() {
  int inv_count = 0;
  for (int i = 0; i < 9 - 1; i++) {
    for (int j = i+1; j < 9; j++) {
       if (board[j] != 0 && board[i] != 0 &&  board[i] > board[j]) inv_count++;
    }
  }
                
  return (inv_count % 2 == 0);
}

public void genSolvable() {
  shuffle();
  while(!isSolvable()) {
    shuffle();
  }
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_8Square" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
