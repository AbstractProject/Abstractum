import akka.actor.Actor

/**
  * Created by gohonel on 08.11.2016.
  */

class Drone extends Actor {

  //drone state variable

  var waypoint = 1
  // marks the Goals of the drone

  var angle = 0
  // marks the direction the drone is looking

  var roadpoint = 0
  // marks diffrent points on the paths of the drone

  var expectedTime = 0;

  var in = true

  //to be used when taking random decisions
  val random = scala.util.Random

  def takePicture() = {
    println("DRONE: taking picture at waypoint " + waypoint)
    Thread.sleep(100)
    //println(expectedTime)
    sender ! "picture " + waypoint + " " + expectedTime
    expectedTime = 10
  }

  def receive = {
    case input =>
      println("DRONE got cmd: " + input)
      val decomposition: Array[String] = input.toString.split(" ")

      if (decomposition(0) == "picture") {
        takePicture()
      }

      else if (decomposition(0) == "fly") {
        // receive a fly command
        println("DRONE: flying")
        expectedTime = 0

        val newDestination = decomposition(1).toInt

        //PRISM go command, fly out of the waypoint via any angle point
        if (waypoint != 0 && angle == 0 && roadpoint == 0) {
          angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
          in = false
        }

        while (!in) {
          println("DRONE1: waypoint " + waypoint, "roadpoint " + roadpoint, "angle " + angle, "in " + in)
          expectedTime += 60
          // movement by orders
          if (angle != 0 && roadpoint == 0 && !in) {

            waypoint match {
              case 2 =>
                // w2 -> r5 |r6 |r7 |r9
                newDestination match {
                  case 0 => roadpoint = 5
                  case 1 => roadpoint = 6
                  case 2 => roadpoint = 7
                  case 3 => roadpoint = 9
                }


              case 5 =>
                // w5 -> r3 | r4 | w4 (at any angle point)
                newDestination match {
                  case 0 => roadpoint = 3
                  case 1 => roadpoint = 4
                  case 2 =>
                    waypoint = 4
                    angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                    roadpoint = 0
                    in = true
                }


              case 6 =>
                // w6 -> r2 | r3 |r8
                newDestination match {
                  case 0 => roadpoint = 2
                  case 1 => roadpoint = 3
                  case 2 => roadpoint = 8
                }

              case _ =>
            }
          }

          //autonomous movement starting at waypoints
          if (angle != 0 && roadpoint == 0 && !in) {
            waypoint match {
              case 1 =>
                // w1 -> r1 | r9
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  roadpoint = 1
                } else {
                  roadpoint = 9
                }


              case 3 =>
                // w3 -> r6 | w4 (any angle point)
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  roadpoint = 6
                } else {
                  waypoint = 4
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                }

              case 4 =>
                // w4 -> w3 | w5
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  waypoint = 3
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                } else {
                  waypoint = 5
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                }

              case _ =>
            }
            println("DRONE2: waypoint " + waypoint, "roadpoint " + roadpoint, "angle " + angle, "in " + in)
          }

          //autonomous movement starting at roadpoints
          if (angle != 0 && roadpoint != 0 && !in) {
            roadpoint match {
              // r1 -> r2 | w1
              case 1 =>
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  roadpoint = 2
                } else {
                  waypoint = 1
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                }


              // r2 -> r1 | w6
              case 2 =>
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  roadpoint = 1
                } else {
                  waypoint = 6
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                }


              // r3 -> w5 | w6
              case 3 =>
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  waypoint = 5
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                } else {
                  waypoint = 6
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                }


              // r4 -> r5 | w5
              case 4 =>
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  roadpoint = 5
                } else {
                  waypoint = 5
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                }


              // r5 -> r4 | w2
              case 5 =>
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  roadpoint = 4
                } else {
                  waypoint = 2
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                }


              // r6 -> w2 | w3
              case 6 =>
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  waypoint = 2
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                } else {
                  waypoint = 3
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                }


              // r7 -> w2 | r8
              case 7 =>
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  waypoint = 2
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                } else {
                  roadpoint = 8
                }


              // r8 -> w6 | r7
              case 8 =>
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  waypoint = 6
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                } else {
                  roadpoint = 7
                }

              // r9 -> w1 | w2
              case 9 =>
                val randomAux = random.nextInt(100)
                val randomMax = random.nextInt(100)
                if (randomAux <= randomMax) {
                  waypoint = 1
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                } else {
                  waypoint = 2
                  angle = random.nextInt(7) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
                }
            }
            println("DRONE3: waypoint " + waypoint, "roadpoint " + roadpoint, "angle " + angle, "in " + in)
          }
        }

        //fly into waypoint and take image
        if (angle != 0 && roadpoint == 0 && in) {
          angle = 0
          takePicture()
        }
      }
  }
}
