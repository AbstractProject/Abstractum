/**
  * Created by Gellért on 2016. 12. 06..
  */
package systemEngineeringProject.local

import akka.actor._

class Operator(drone: ActorRef, client: ActorRef) extends Actor {

	var k: Int = 0
	// fatigue level measured by completed tasks
	var t: Int = 0
	// workload level
	var s: Int = 0
	// status of image processing, 0: init, 1: good, 2: bad
	var c: Int = 0
	// choices at the check point

	var w: Int = 1

	var stop: Boolean = false
	val rand = scala.util.Random


	def resetInitialState() = {
		k = 0
		t = 0
		s = 0
		c = 0
		w = 1
		for( a <- 0 to 5) {
			Globals.visited(a) = false
		}
	}

	override def receive = {
		case input =>
			val decomposition: Array[String] = input.toString.split(" ")

			if (decomposition(0) == "start") {
				resetInitialState()
				println("OPERATOR: start")
				drone ! "restart"
				drone ! "fly 0"
			}
			else {

				if (decomposition(0) == "picture") {
					println("OPERATOR: got picture from drone at " + decomposition(1))
					t = 0
					s = 0
					w = decomposition(1).toInt
					Globals.totalMissionTime += decomposition(2).toInt
				}

				takeOneStep()

				if (s == 1) {
					Globals.visited(w) = true
					println("OPERATOR: picture OK")
					if (!terminateOnSucccess()) {
						drone ! "fly " + c
					}
					else {
						//System.exit(0)
					}

				}
				else if (s == 2) {
					println("OPERATOR: picture BAD")
					drone ! "picture"
				}
			}

	}

	def terminateOnSucccess(): Boolean = {
		if (Globals.visited(1) && Globals.visited(2) && Globals.visited(6)) {
			println("OPERATOR: Mission success! Total time: " + Globals.totalMissionTime)
			client ! "success " + Globals.totalMissionTime
			return true
		}
		false
	}

	def takeOneStep() = {

		if (!stop) {
			while (s == 0) {
				s match {
					case 0 =>
						t match {
							case 0 =>
								val p: Float = rand.nextFloat()
								if (p < Globals.p) {
									t = 2
									s = 0
								}
								else {
									t = 1
									s = 0
								}
							case 1 =>
								val p: Float = rand.nextFloat()
								if (k <= Globals.COUNTER) {
									if (p < Globals.accu_load1) {
										s = 1
										k = k + 1
									}
									else {
										s = 2
										k = k + 1
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
										s = 1
										k = k + 1
									}
									else {
										s = 2
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
									c = 2
									t = 0
									s = 0
								}
								else if (p < (Globals.risky2 + (1 - Globals.risky2) / 3.0)) {
									c = 3
									t = 0
									s = 0
								}
								else if (p < (Globals.risky2 + 2 * (1 - Globals.risky2) / 3.0)) {
									c = 1
									t = 0
									s = 0
								}
								else {
									c = 0
									t = 0
									s = 0
								}
							case 5 =>
								val p = rand.nextFloat()
								if (p < 1.0 / 3) {
									c = 2
									t = 0
									s = 0
								}
								else if (p < 2.0 / 3) {
									c = 1
									t = 0
									s = 0
								}
								else {
									c = 0
									t = 0
									s = 0
								}
							case 6 =>
								val p = rand.nextFloat()
								if (p < Globals.risky6) {
									c = 2
									t = 0
									s = 0
								}
								else if (p < (Globals.risky2 + (1 - Globals.risky2) / 2)) {
									c = 1
									t = 0
									s = 0
								}
								else {
									c = 0
									t = 0
									s = 0
								}
							case _ =>
								t = 0
								s = 0;
						}
					case 2 =>
						t = 0
						s = 0
				}
			}
		}
	}
}
