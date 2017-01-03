/**
  * Created by Gellért on 2016. 12. 06..
  */
package com.madhukaraphatak.akka.local
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

object Globals {
  val p: Double = 0.5;
  var accu_load1: Double = 0.8;
  var accu_load2: Double = 0.7;
  var fd: Double = 0.2;
  var COUNTER: Int = 10;
  var risky2: Double = 0.4;
  var risky6: Double = 0.6;

  var visited: Array[Boolean] = Array(false, false, false, false, false, false, false)
  var totalMissionTime = 0;

}
