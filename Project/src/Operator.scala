/**
  * Created by Gellért on 2016. 12. 06..
  */

import akka.actor._

class Operator(drone: ActorRef) extends Actor {

  var k: Int = 0;
  // fatigue level measured by completed tasks
  var t: Int = 0;
  // workload level
  var s: Int = 0;
  // status of image processing, 0: init, 1: good, 2: bad
  var c: Int = 0;
  // choices at the check point

  var w: Int = 1

  var stop: Boolean = false;
  val rand = scala.util.Random

  drone ! "fly 0"
  println("OPERATOR: start")

  override def receive = {
    case input =>
      if (!terminateOnSucccess()) {
        val decomposition: Array[String] = input.toString.split(" ")

        if (decomposition(0) == "picture") {
          println("OPERATOR: got picture from drone at " + decomposition(1))
          t = 0;
          s = 0;
          w = decomposition(1).toInt
        }

        takeOneStep

        if (s == 1) {
          Globals.visited(w) = true
          println("OPERATOR: picture OK")
          drone ! "fly " + c
        }
        else if (s == 2) {
          println("OPERATOR: picture BAD")
          drone ! "picture"
        }
      }
      else {
        System.exit(0)
      }
  }

  def terminateOnSucccess(): Boolean = {
    if (Globals.visited(1) && Globals.visited(2) && Globals.visited(6)) {
      println("OPERATOR: Mission success!")
      println("OPERATOR: terminating Drone")
      context stop drone
      println("OPERATOR: terminating Operator")
      context stop self
      return true
    }
    return false
  }

  def takeOneStep = {

    if (!stop) {
      while (s == 0) {
        s match {
          case 0 =>
            t match {
              case 0 =>
                val p: Float = rand.nextFloat()
                if (p < Globals.p) {
                  t = 2;
                  s = 0;
                }
                else {
                  t = 1;
                  s = 0;
                }
              case 1 =>
                val p: Float = rand.nextFloat()
                if (k <= Globals.COUNTER) {
                  if (p < Globals.accu_load1) {
                    s = 1;
                    k = k + 1;
                  }
                  else {
                    s = 2;
                    k = k + 1;
                  }
                }
                else {
                  if (p < Globals.accu_load1 * Globals.fd) {
                    s = 1
                  }
                  else {
                    s = 2
                  }
                }
              case 2 =>
                val p: Float = rand.nextFloat()
                if (k <= Globals.COUNTER) {
                  if (p < Globals.accu_load2) {
                    s = 1;
                    k = k + 1
                  }
                  else {
                    s = 2;
                    k = k + 1
                  }
                }
                else {
                  if (p < Globals.accu_load2 * Globals.fd) {
                    s = 1
                  }
                  else {
                    s = 2
                  }
                }
            }
          case 1 =>
            w match {
              case 2 =>
                val p = rand.nextFloat()
                if (p < Globals.risky2) {
                  c = 2;
                  t = 0;
                  s = 0;
                }
                else if (p < (Globals.risky2 + (1 - Globals.risky2) / 3)) {
                  c = 3;
                  t = 0;
                  s = 0;
                }
                else if (p < (Globals.risky2 + 2 * (1 - Globals.risky2) / 3)) {
                  c = 1;
                  t = 0;
                  s = 0;
                }
                else {
                  c = 0;
                  t = 0;
                  s = 0;
                }
              case 5 =>
                val p = rand.nextFloat()
                if (p < 1 / 3) {
                  c = 2;
                  t = 0;
                  s = 0;
                }
                else if (p < 2 / 3) {
                  c = 1;
                  t = 0;
                  s = 0;
                }
                else {
                  c = 0;
                  t = 0;
                  s = 0;
                }
              case 6 =>
                val p = rand.nextFloat()
                if (p < Globals.risky6) {
                  c = 2;
                  t = 0;
                  s = 0;
                }
                else if (p < (Globals.risky2 + (1 - Globals.risky2) / 2)) {
                  c = 1;
                  t = 0;
                  s = 0;
                }
                else {
                  c = 0;
                  t = 0;
                  s = 0;
                }
              case _ =>
                t = 0; s = 0;
            }
          case 2 =>
            t = 0
            s = 0
        }
      }
    }
  }
}
