package com.premiumminds.internship.screenlocking;

import com.premiumminds.internship.screenlocking.exceptions.ErrorMessage;
import com.premiumminds.internship.screenlocking.exceptions.ScreenLockinException;

import java.util.ArrayList;
import java.util.TreeMap;

import java.util.Arrays;


class ScreenLockinMatrix {

  private static TreeMap<String, Getter> getterList = new TreeMap<String, Getter>() {
    {
      put("Right", new GetRight());
      put("Left", new GetLeft());
      put("Up", new GetUp());
      put("Down", new GetDown());
      put("Up_Right", new GetUp_Right());
      put("Up_Left", new GetUp_Left());
      put("Down_Right", new GetDown_Right());
      put("Down_Left", new GetDown_Left());
      put("Above", new GetAbove());
      put("Below", new GetBelow());
      put("West", new GetWest());
      put("East", new GetEast());
    }
  };

  public static ArrayList<Integer> adjacentPoints(int point, int matrixSize, ArrayList<Integer> visited){
    return adjacentPoints(point, matrixSize, visited, getGettersFromPrefix(new String[]{"Right", "Up", "Left", "Down", "Up_Right", "Up_Left", "Down_Right", "Down_Left"}));
  }

  public static ArrayList<Integer> adjacentPoints(int point, int matrixSize, ArrayList<Integer> visited, TreeMap<String, Getter> getters){
    /* 
      Returns the points adjacents to the point passed as argument depending on the selected getters
     */
    if (point < 1 || point > 9){
      throw new ScreenLockinException(ErrorMessage.SCREEN_POINT_OUT_OF_RANGE);
    }
    if (matrixSize < 0){
      throw new ScreenLockinException(ErrorMessage.INVALID_MATRIX_SIZE);
    }
    ArrayList<Integer> adjacentPoints = new ArrayList<Integer>();

    ArrayList<Integer> to_be_add;
  
    for(Getter getter: getters.values()){
      for(Integer to_add : getter.getAdjacentPoint(point, matrixSize, new ArrayList<Integer>(visited))){
        if(!adjacentPoints.contains(to_add)){
          adjacentPoints.add(to_add);
        }
      }
    }

    return adjacentPoints;
  }

  private static TreeMap<String, Getter> getGettersFromPrefix(String prefix){
    TreeMap<String, Getter> subMap = new TreeMap<String, Getter>();
    getterList.forEach((String key, Getter getter) -> {
      if (key.equals(prefix))
        subMap.put(key, getter);
    });
    return subMap;
  }

  private static TreeMap<String, Getter> getGettersFromPrefix(String[] prefixes){
    TreeMap<String, Getter> subMap = new TreeMap<String, Getter>();
    for (int i = 0; i < prefixes.length; ++ i){
      subMap.putAll(getGettersFromPrefix(prefixes[i]));
    }
    return subMap;
  }

  private static abstract class Getter {
    public abstract ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited);
  }

  private static class GetRight extends Getter{
    /* 
      Finds the Point to the Right. If already Visited it tries to find the one two spaces to the right
     */
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point % matrixSize == 0)
        return new ArrayList<Integer>();
      
      point = point + 1;
      if (!visited.contains(point))
        return new ArrayList<Integer>(Arrays.asList(point));

      visited.add(point);
      return adjacentPoints(point, matrixSize, visited, getGettersFromPrefix("Right"));
    }
  }

  private static class GetLeft extends Getter{
    /* 
      Finds the Point to the Left. If already Visited it tries to find the one two spaces to the Left
     */
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point % matrixSize == 1)
        return new ArrayList<Integer>();
      
      point = point - 1;
      if (!visited.contains(point))
        return new ArrayList<Integer>(Arrays.asList(point));;

      
      visited.add(point);
      return adjacentPoints(point, matrixSize, visited, getGettersFromPrefix("Left"));
    }
  }

  private static class GetDown extends Getter{
    /* 
      Finds the Point to the Down. If already Visited it tries to find the one two spaces to the Down
     */
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point > matrixSize * (matrixSize - 1))
        return new ArrayList<Integer>();
      
      point = point + matrixSize;
      if (!visited.contains(point))
        return new ArrayList<Integer>(Arrays.asList(point));;

      visited.add(point);
      return adjacentPoints(point, matrixSize, visited, getGettersFromPrefix("Down"));
    }
  }

  private static class GetUp extends Getter{
    /* 
      Finds the Point to the Up. If already Visited it tries to find the one two spaces to the Up
     */
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point <= matrixSize)
        return new ArrayList<Integer>();

      point = point - matrixSize;
      if (!visited.contains(point))
        return new ArrayList<Integer>(Arrays.asList(point));;

      visited.add(point);
      return adjacentPoints(point, matrixSize, visited, getGettersFromPrefix("Up"));
    }
  }

  private static class GetUp_Left extends Getter{
    /* 
      Finds the Point to the Up-Left Diagonal. If already Visited it tries to find the one two spaces to the Up-Left Diagonal
      It also stores, if not visited, the points above and to the left of itself
     */
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point <= matrixSize || point % matrixSize == 1)
        return new ArrayList<Integer>();
      
      ArrayList<Integer> up_left = new ArrayList<Integer>();
      visited.add(point);
      point = point - matrixSize - 1;

      up_left.addAll(adjacentPoints(point, matrixSize, visited, getGettersFromPrefix(new String[]{"Above", "West"})));

      if (!visited.contains(point)){
        up_left.add(point);
        return up_left;
      }

      up_left.addAll(adjacentPoints(point, matrixSize, visited, getGettersFromPrefix(new String[]{"Up", "Left", "Up_Left"})));
      return up_left;
    }
  }

  private static class GetUp_Right extends Getter{
    /* 
      Finds the Point to the Up-Right Diagonal. If already Visited it tries to find the one two spaces to the Up-Right Diagonal
      It also stores, if not visited, the points above and to the right of itself
     */
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point <= matrixSize || point % matrixSize == 0)
        return new ArrayList<Integer>();
      
      ArrayList<Integer> up_right = new ArrayList<Integer>();
      visited.add(point);
      point = point - matrixSize + 1;

      up_right.addAll(adjacentPoints(point, matrixSize, visited, getGettersFromPrefix(new String[]{"Above", "East"})));

      if (!visited.contains(point)){
        up_right.add(point);
        return up_right;
      }

      up_right.addAll(adjacentPoints(point, matrixSize, visited, getGettersFromPrefix(new String[]{"Up", "Right", "Up_Right"})));
      return up_right;
    }
  }
  
  private static class GetDown_Left extends Getter{
    /* 
      Finds the Point to the Down-Left Diagonal. If already Visited it tries to find the one two spaces to the Down-Left Diagonal
      It also stores, if not visited, the points below and to the left of itself
     */
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point > matrixSize * (matrixSize - 1) || point % matrixSize == 1)
        return new ArrayList<Integer>();
      
      ArrayList<Integer> down_left = new ArrayList<Integer>();
      visited.add(point);
      point = point + matrixSize - 1;

      down_left.addAll(adjacentPoints(point, matrixSize, visited, getGettersFromPrefix(new String[]{"Below", "West"})));

      if (!visited.contains(point)){
        down_left.add(point);
        return down_left;
      }
      down_left.addAll(adjacentPoints(point, matrixSize, visited, getGettersFromPrefix(new String[]{"Down", "Left", "Down_Left"})));
      return down_left;
    }
  }

  private static class GetDown_Right extends Getter{
    /* 
      Finds the Point to the Down-Right Diagonal. If already Visited it tries to find the one two spaces to the Down-Right Diagonal
      It also stores, if not visited, the points below and to the right of itself
     */
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point > matrixSize * (matrixSize - 1) || point % matrixSize == 0)
        return new ArrayList<Integer>();

      ArrayList<Integer> down_right = new ArrayList<Integer>();
      visited.add(point);
      point = point + matrixSize + 1;

      down_right.addAll(adjacentPoints(point, matrixSize, visited, getGettersFromPrefix(new String[]{"Below", "East"})));

      if (!visited.contains(point)){
        down_right.add(point);
        return down_right;
      }

      down_right.addAll(adjacentPoints(point, matrixSize, visited, getGettersFromPrefix(new String[]{"Down", "Right", "Down_Right"})));
      return down_right;
    }
  }

  private static class GetAbove extends Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point <= matrixSize)
        return new ArrayList<Integer>();
      
      ArrayList<Integer> above = new ArrayList<Integer>();
      while (point > matrixSize){
        point = point - matrixSize;
        if (!visited.contains(point))
          above.add(point);
      }

      return above;
    }
  }

  private static class GetBelow extends Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point > matrixSize * (matrixSize - 1))
        return new ArrayList<Integer>();
      
      ArrayList<Integer> below = new ArrayList<Integer>();
      while (point <= matrixSize * (matrixSize - 1)){
        point = point + matrixSize;
        if (!visited.contains(point))
          below.add(point);

      }

      return below;
    }
  }

  private static class GetWest extends Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point % matrixSize == 1)
        return new ArrayList<Integer>();
      
      ArrayList<Integer> west = new ArrayList<Integer>();
      while (point % matrixSize != 1){
        point = point - 1;
        if (!visited.contains(point))
          west.add(point);
      }

      return west;
    }
  }
  
  private static class GetEast extends Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point % matrixSize == 0)
        return new ArrayList<Integer>();
      
      ArrayList<Integer> east = new ArrayList<Integer>();
      while (point % matrixSize != 0){
        point = point + 1;
        if (!visited.contains(point))
          east.add(point);
      }

      return east;
    }
  }
}
