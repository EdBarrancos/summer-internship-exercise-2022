package com.premiumminds.internship.screenlocking;

import com.premiumminds.internship.screenlocking.exceptions.ErrorMessage;
import com.premiumminds.internship.screenlocking.exceptions.ScreenLockinException;

import java.util.ArrayList;
import java.util.TreeMap;


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
    return adjacentPoints(point, matrixSize, visited, getSubMap(new String[]{"Right", "Up", "Left", "Down"}));
  }

  public static ArrayList<Integer> adjacentPoints(int point, int matrixSize, ArrayList<Integer> visited, TreeMap<String, Getter> getters){
    if (point < 1 || point > 9){
      throw new ScreenLockinException(ErrorMessage.SCREEN_POINT_OUT_OF_RANGE);
    }
    if (matrixSize < 0){
      throw new ScreenLockinException(ErrorMessage.INVALID_MATRIX_SIZE);
    }
    ArrayList<Integer> adjacentPoints = new ArrayList<Integer>();

    ArrayList<Integer> to_be_add;
  
    for(Getter getter: getters.values()){
      to_be_add = getter.getAdjacentPoint(point, matrixSize, new ArrayList<Integer>(visited));
      if (to_be_add != null){
        for(Integer to_add : to_be_add){
          if(!adjacentPoints.contains(to_add)){
            adjacentPoints.add(to_add);
          }
        }
      }
    }

    System.out.println("FOR: " + point + " ADJACENT: " + adjacentPoints);
    return adjacentPoints;
  }

  private static TreeMap<String, Getter> getSubMap(String prefix, boolean exact){
    TreeMap<String, Getter> subMap = new TreeMap<String, Getter>();
    getterList.forEach((String key, Getter getter) -> {
      if (exact){
        if (key.equals(prefix))
          subMap.put(key, getter);
      }
      else {
        if (key.contains(prefix))
          subMap.put(key, getter);
      }
    });
    return subMap;
  }

  private static TreeMap<String, Getter> getSubMap(String prefix){
    return getSubMap(prefix, true);
  }

  private static TreeMap<String, Getter> getSubMap(String[] prefixes, boolean exact){
    TreeMap<String, Getter> subMap = new TreeMap<String, Getter>();
    for (int i = 0; i < prefixes.length; ++ i){
      subMap.putAll(getSubMap(prefixes[i], exact));
    }
    return subMap;
  }

  private static TreeMap<String, Getter> getSubMap(String[] prefixes){
    return getSubMap(prefixes, false);
  }

  private interface Getter {
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited);
  }

  private static class GetRight implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point >= matrixSize * matrixSize || point % matrixSize == 0)
        return null;
      
      if (!visited.contains(point + 1)){
        ArrayList<Integer> right = new ArrayList<Integer>();
        right.add(point + 1);
        return right;
      }

      visited.add(point);
      return adjacentPoints(point + 1, matrixSize, visited, getSubMap("Right"));
    }
  }

  private static class GetLeft implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point <= 1 || point % matrixSize == 1)
        return null;
      
      if (!visited.contains(point - 1)){
        ArrayList<Integer> left = new ArrayList<Integer>();
        left.add(point - 1);
        return left;
      }
      
      visited.add(point);
      return adjacentPoints(point - 1, matrixSize, visited, getSubMap("Left"));
    }
  }

  private static class GetDown implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point > matrixSize * (matrixSize - 1))
        return null;
      
      if (!visited.contains(point + matrixSize)){
        ArrayList<Integer> down = new ArrayList<Integer>();
        down.add(point + matrixSize);
        return down;
      }

      visited.add(point);
      return adjacentPoints(point + matrixSize, matrixSize, visited, getSubMap("Down"));
    }
  }

  private static class GetUp implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point <= matrixSize)
        return null;
      
      if (!visited.contains(point - matrixSize)){
        ArrayList<Integer> up = new ArrayList<Integer>();
        up.add(point - matrixSize);
        return up;
      }

      visited.add(point);
      return adjacentPoints(point - matrixSize, matrixSize, visited, getSubMap("Up"));
    }
  }

  private static class GetUp_Left implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point <= 1 || point <= matrixSize || point % matrixSize == 1)
        return null;
      
      ArrayList<Integer> up_left = new ArrayList<Integer>();
      visited.add(point);
      point = point - matrixSize - 1;
      up_left.addAll(adjacentPoints(point, matrixSize, visited, getSubMap(new String[]{"Above", "West"})));

      if (!visited.contains(point )){
        up_left.add(point);
        return up_left;
      }

      up_left.addAll(adjacentPoints(point, matrixSize, visited, getSubMap(new String[]{"Up", "Left", "Up_Left"}, true)));
      return up_left;
    }
  }

  private static class GetUp_Right implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point <= 1 || point <= matrixSize || point % matrixSize == 0)
        return null;
      
      ArrayList<Integer> up_right = new ArrayList<Integer>();
      visited.add(point);
      point = point - matrixSize + 1;
      up_right.addAll(adjacentPoints(point, matrixSize, visited, getSubMap(new String[]{"Above", "East"})));
      if (!visited.contains(point)){
        up_right.add(point);
        return up_right;
      }

      up_right.addAll(adjacentPoints(point, matrixSize, visited, getSubMap(new String[]{"Up", "Right", "Up_Right"}, true)));
      return up_right;
    }
  }
  
  private static class GetDown_Left implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point > matrixSize * (matrixSize - 1) || point % matrixSize == 1)
        return null;
      
      ArrayList<Integer> down_left = new ArrayList<Integer>();
      visited.add(point);
      point = point + matrixSize - 1;
      down_left.addAll(adjacentPoints(point, matrixSize, visited, getSubMap(new String[]{"Below", "West"})));
      if (!visited.contains(point)){
        down_left.add(point);
        return down_left;
      }
      down_left.addAll(adjacentPoints(point, matrixSize, visited, getSubMap(new String[]{"Down", "Left", "Down_Left"}, true)));
      return down_left;
    }
  }

  private static class GetDown_Right implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point > matrixSize * (matrixSize - 1) || point % matrixSize == 0)
        return null;

      ArrayList<Integer> down_right = new ArrayList<Integer>();
      visited.add(point);
      point = point + matrixSize + 1;
      down_right.addAll(adjacentPoints(point, matrixSize, visited, getSubMap(new String[]{"Below", "East"})));
      if (!visited.contains(point)){
        down_right.add(point);
        return down_right;
      }

      down_right.addAll(adjacentPoints(point, matrixSize, visited, getSubMap(new String[]{"Down", "Right", "Down_Right"}, true)));
      return down_right;
    }
  }

  private static class GetAbove implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point <= matrixSize)
        return null;
      
      ArrayList<Integer> above = new ArrayList<Integer>();
      while (point > matrixSize){
        point = point - matrixSize;
        if (!visited.contains(point))
          above.add(point);
      }

      return above;
    }
  }

  private static class GetBelow implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point > matrixSize * (matrixSize - 1))
        return null;
      
      ArrayList<Integer> below = new ArrayList<Integer>();
      while (point <= matrixSize * (matrixSize - 1)){
        point = point + matrixSize;
        if (!visited.contains(point))
          below.add(point);

      }

      return below;
    }
  }

  private static class GetWest implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point % matrixSize == 1)
        return null;
      
      ArrayList<Integer> west = new ArrayList<Integer>();
      while (point % matrixSize != 1){
        point = point - 1;
        if (!visited.contains(point))
          west.add(point);
      }

      return west;
    }
  }
  
  private static class GetEast implements Getter{
    public ArrayList<Integer> getAdjacentPoint(int point, int matrixSize, ArrayList<Integer> visited){
      if (point % matrixSize == 0)
        return null;
      
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
