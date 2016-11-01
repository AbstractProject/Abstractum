/**
  * Created by Gellért on 2016. 11. 01..
  */
class Demo {

  def factorial(n: Int): Int = {
    var i: Int = 1;
    var s: Int = 1;
    while (i <= n) {
      s*=i;
      i+=1;
    }
    return s;
  }

  def isPrime (n: Int): Boolean = {
    for (i <- 2 to Math.sqrt(n).asInstanceOf[Int]) {
      if (n % i == 0) return false;
    }
    return true;
  }

  def printAllPrimes(n: Int): Unit = {
    println("All prime numbers smaller than" + n);
    for( a <- 1 to n){
      if (isPrime(a)) {
        print(a + ", ");
      }
    }
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val funcs = new Demo();
    println("factorial(5) = " + funcs.factorial(5));
    funcs.printAllPrimes(50000000);
  }
}