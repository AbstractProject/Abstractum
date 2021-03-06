import akka.actor.Actor

/**
  * Created by gohonel on 08.11.2016.
  */

class Drone extends Actor {

  //drone state variable
  var waypoint = 1
  // marks the Goals of the drone

  var angle = 1
  // marks the direction the drone is looking

  var roadpoint = 0
  // marks diffrent points on the paths of the drone

  var in = false

  //to be used when taking random decisions
  val random = scala.util.Random

  def takePicture() = {
    println("       DRONE:    taking picture")
    Thread.sleep(500)
    sender ! "#picture"
  }

  def autoFlying() = {
    while (waypoint != 2 && waypoint != 5 && waypoint != 6) {
      println(angle + " " + roadpoint + " " + in)
      //autonomous movement starting at waypoints
      if (angle != 0 && roadpoint == 0 && !in) {
        waypoint match {
          case 1 =>
            // w1 -> r1 | r9
            val randomAux = random.nextInt(2)
            println(randomAux)
            if (randomAux == 0) {
              roadpoint = 1
              waypoint = 0
            } else {
              roadpoint = 9
              waypoint = 0
            }


          case 3 =>
            // w3 -> r6 | w4 (any angle point)
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              roadpoint = 6
              waypoint = 0
            } else {
              waypoint = 4
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            }

          case 4 =>
            // w4 -> w3 | w5
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              waypoint = 3
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            } else {
              waypoint = 5
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            }
        }
      }

      //autonomous movement starting at roadpoints
      else if (angle != 0 && roadpoint != 0 && !in) {
        roadpoint match {
          // r1 -> r2 | w1
          case 1 =>
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              roadpoint = 2
              waypoint = 0
            } else {
              waypoint = 1
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            }


          // r2 -> r1 | w6
          case 2 =>
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              roadpoint = 1
              waypoint = 0
            } else {
              waypoint = 6
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            }


          // r3 -> w5 | w6
          case 3 =>
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              waypoint = 5
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            } else {
              waypoint = 6
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            }


          // r4 -> r5 | w5
          case 4 =>
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              roadpoint = 5
              waypoint = 0
            } else {
              waypoint = 5
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            }


          // r5 -> r4 | w2
          case 5 =>
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              roadpoint = 4
              waypoint = 0
            } else {
              waypoint = 2
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            }


          // r6 -> w2 | w3
          case 6 =>
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              waypoint = 2
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            } else {
              waypoint = 3
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            }


          // r7 -> w2 | r8
          case 7 =>
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              waypoint = 2
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            } else {
              roadpoint = 8
              waypoint = 0
            }


          // r8 -> w6 | r7
          case 8 =>
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              waypoint = 6
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            } else {
              roadpoint = 7
              waypoint = 0
            }

          // r9 -> w1 | w2
          case 9 =>
            val randomAux = random.nextInt(2)
            if (randomAux == 0) {
              waypoint = 1
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            } else {
              waypoint = 2
              angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
              roadpoint = 0
              in = true
            }
        }
      }

      Thread.sleep(500)

      if (waypoint != 0 && !in) {
        println("       DRONE:    at waypoint " + waypoint)
        takePicture()
      } else if(!in){
        println("       DRONE:    at roadpoint " + roadpoint)
      }


    }

    sendForInstructions()

  }

  def sendForInstructions() = {
    Thread.sleep(500)
    println("       DRONE:     send for instructions")
    sender ! "#sendNext" + waypoint
  }

  def receive = {
    case input =>
      val decomposition: Array[String] = input.toString.split(" ")

      if (decomposition(0) == "#start") {
        println("       DRONE:    starting")
        autoFlying()
      }

      else if (decomposition(0) == "#takePicture") {
        takePicture()
      }

      else if(decomposition(0) == "#goodPicture"){
        in = false
      }

      else if (decomposition(0) == "#fly") {
        // receive a fly command
        val newDestination = decomposition(1).toInt

        // movement by orders
        if (angle != 0 && roadpoint == 0 && !in) {


          waypoint match {
            case 2 =>
              // w2 -> r5 |r6 |r7 |r9
              newDestination match {
                case 0 =>
                  roadpoint = 5
                  waypoint = 0
                case 1 =>
                  roadpoint = 6
                  waypoint = 0
                case 2 =>
                  roadpoint = 7
                  waypoint = 0
                case 3 =>
                  roadpoint = 9
                  waypoint = 0
              }


            case 5 =>
              // w5 -> r3 | r4 | w4 (at any angle point)
              newDestination match {
                case 0 =>
                  roadpoint = 3
                  waypoint = 0
                case 1 =>
                  roadpoint = 4
                  waypoint = 0
                case 2 =>
                  waypoint = 4
                  angle = random.nextInt(8) + 1 // angle is set to a value of 1...8 randomly
                  roadpoint = 0
                  in = true
              }


            case 6 =>
              // w6 -> r2 | r3 |r8
              newDestination match {
                case 0 =>
                  roadpoint = 2
                  waypoint = 0
                case 1 =>
                  roadpoint = 3
                  waypoint = 0
                case 2 =>
                  roadpoint = 8
                  waypoint = 0
              }
          }
        }

      }
  }


}
