package it.balyfix.basic.state

class CheckState {

}

trait RNG {
  def nextInt: (Int, RNG) // Should generate a random `Int`. We'll later define other functions in terms of `nextInt`.
}

object RNG {

  // this dummy class is design to be a pure functional style cause is separete 
  //the concern of computing from the concern of comunication 
  // Every next int we return the value and the Obj to continue the sequence   

  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = Simple(newSeed) // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
    }
  }

  //example of use in REPL
  // val rng = Simple(42)
  // val (n, RNG) = rng.nextInt 

  def nonNegativeInt(rng: RNG): (Int, RNG) =
  {
      val (n, r) = rng.nextInt
      n +1;
      if(n < 0) (n * -1,r) else (n,r)
  }
 

}

