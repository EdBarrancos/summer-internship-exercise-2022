package com.premiumminds.internship.screenlocking;

import com.premiumminds.internship.screenlocking.exceptions.ErrorMessage;
import com.premiumminds.internship.screenlocking.exceptions.ScreenLockinException;

import java.util.HashSet;


class ScreenLockinMatrix {

  public static HashSet<Integer> adjacentPoints(int point, int matrixSize){
    /* 
      Returns position reachable with length one to "point"
     */

    if (point < 1 || point > 9){
      throw new ScreenLockinException(ErrorMessage.SCREEN_POINT_OUT_OF_RANGE);
    }
    if (matrixSize < 0){
      throw new ScreenLockinException(ErrorMessage.INVALID_MATRIX_SIZE);
    }

    HashSet<Integer> adjacentPoints = new HashSet<Integer>();

    adjacentPoints.addAll(getLineUp(point, matrixSize));
    adjacentPoints.addAll(getLineDown(point, matrixSize));
    adjacentPoints.addAll(getLineLeft(point, matrixSize));
    adjacentPoints.addAll(getLineRight(point, matrixSize));

    return adjacentPoints;
  }

  private static HashSet<Integer> getLineUp(int point, int matrixSize){
    if (point <= matrixSize)
      return new HashSet<Integer>();
    
    HashSet<Integer> above = new HashSet<Integer>();

    int offset = -1;
    do{
      //All above and to the right of "point"
      offset += 1;
      above.add(point -  matrixSize - (1 * offset));
    } while ((point - matrixSize - (1 * offset)) % matrixSize != 1);

    offset = -1;
    do{
      //All above and to the left of "point"
      offset += 1;
      above.add(point - matrixSize + (1 * offset));
    } while ((point - matrixSize + (1 * offset)) % matrixSize != 0);

    return above;
  }

  private static HashSet<Integer> getLineDown(int point, int matrixSize){
    if (point > matrixSize * (matrixSize - 1))
      return new HashSet<Integer>();
    
    HashSet<Integer> below = new HashSet<Integer>();

    int offset = -1;
    do{
      //All below and to the right of "point"
      offset += 1;
      below.add(point + matrixSize - (1 * offset));
    } while ((point + matrixSize - (1 * offset)) % matrixSize != 1);

    offset = -1;
    do{
      //All below and to the left of "point"
      offset += 1;
      below.add(point + matrixSize + (1 * offset));
    } while ((point + matrixSize + (1 * offset)) % matrixSize != 0);

    return below;
  }

  private static HashSet<Integer> getLineLeft(int point, int matrixSize){
    if (point % matrixSize == 1)
      return new HashSet<Integer>();
    
    HashSet<Integer> west = new HashSet<Integer>();
    
    int offset = -1;
    do{
      //All to the Left and up of "point"
      offset += 1;
      west.add(point - 1 - (matrixSize * offset));
    } while (point - 1 - (matrixSize * offset) > matrixSize);

    offset = -1;
    do{
      //All to the Left and down of "point"
      offset += 1;
      west.add(point - 1 + (matrixSize * offset));
    } while (point - 1 + (matrixSize * offset) <= matrixSize * (matrixSize - 1));

    return west;
  }
  
  private static HashSet<Integer> getLineRight(int point, int matrixSize){
    if (point % matrixSize == 0)
      return new HashSet<Integer>();
    
    HashSet<Integer> east = new HashSet<Integer>();
    
    int offset = -1;
    do{
      //All to the Right and up of "point"
      offset += 1;
      east.add(point + 1 - (matrixSize * offset));
    } while (point + 1 - (matrixSize * offset) > matrixSize);

    offset = -1;
    do{
      //All to the Right and dpwn of "point"
      offset += 1;
      east.add(point + 1 + (matrixSize * offset));
    } while (point + 1 + (matrixSize * offset) <= matrixSize * (matrixSize - 1));

    return east;
  }
}