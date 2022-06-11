package com.premiumminds.internship.screenlocking;

import com.premiumminds.internship.screenlocking.exceptions.ErrorMessage;
import com.premiumminds.internship.screenlocking.exceptions.ScreenLockinException;

import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.ArrayList;

/**
 * Created by aamado on 05-05-2022.
 */
class ScreenLockinPattern implements IScreenLockinPattern {

 /**
  * Method to count patterns from firstPoint with the length
  * @param firstPoint initial matrix position
  * @param length the number of points used in pattern
  * @return number of patterns
  */
  public Future<Integer> countPatternsFrom(int firstPoint,int length) throws ScreenLockinException {
    if (firstPoint < 1 || firstPoint > 9){
      throw new ScreenLockinException(ErrorMessage.SCREEN_POINT_OUT_OF_RANGE);
    }
    if (length < 1 || length > 9){
      throw new ScreenLockinException(ErrorMessage.INVALID_PATTERN_LENGTH);
    }

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    return executorService.submit( ()-> {
      return recursiveCountPattern(new ArrayList<Integer>(), new ArrayList<Integer>(), new Integer(firstPoint), length);
    });
  };

  private Integer recursiveCountPattern(ArrayList<Integer> visited, Integer visiting, int remainLength) throws ScreenLockinException{
    /* 
      It Recursivly looks for all possible combinations
     */
    if (visited.contains(visiting)){
      return new Integer(0);
    }
    if (remainLength == 1){
      return new Integer(1);
    }

    Integer count = new Integer(0);
    visited.add(visiting);
    for (Integer adjacent: ScreenLockinMatrix.adjacentPoints(visiting, 3, visited)){
      count += recursiveCountPattern(new ArrayList<Integer>(visited), adjacent, remainLength - 1);
    }

    return count;
  }
}
