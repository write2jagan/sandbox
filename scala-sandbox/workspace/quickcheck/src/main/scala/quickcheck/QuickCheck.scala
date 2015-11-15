package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("list tail") =
    forAll { (x: Int, xs: List[Int]) =>
      (x :: xs).tail == xs
    }

  lazy val genHeap: Gen[H] = for {
    element <- arbitrary[A]
    heap <- frequency((1, Gen.const(empty)), (100, genHeap))
  } yield insert(element,heap)
  
  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)
  
  property("min3inline") = forAll { a: Int =>
    val h3 = insert(a, insert(a, insert(a, empty)))
    findMin(h3) == a
    findMin(deleteMin(h3)) == a
  }
  
  property("meld heap with nil") = forAll { (h : H) =>
    val m = meld(h, empty)
    val m2 = meld(empty, h)
    m == h
    m2 == h
  }
  

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }
  
 property("min3inline") = forAll { a: Int =>
    val h3 = insert(a, insert(a, insert(a, empty)))
    findMin(h3) == a
    findMin(deleteMin(h3)) == a
  }
  
  
  property("meld heap with nil") = forAll { (h : H) =>
    val m = meld(h, empty)
    val m2 = meld(empty, h)
    m == h
    m2 == h
  }
  
  property("meld 2 heaps") = forAll { (a :Int, b:Int) =>
    val vmin = Math.min(a, b)
    val vmax = Math.max(a, b)
    val h1 = insert(a, empty)
    val h2 = insert(b, empty)
   
    val melded = meld(h1, h2)
    
    findMin(melded) == vmin
    findMin(deleteMin(melded)) == vmax
  }
  
 
  property("Ins 3 heaps") = forAll { (a :Int, b:Int, c: Int) =>
    val vmax = Math.max(Math.max(a, b), c)
    val vmin = Math.min(Math.min(a, b), c)
    val mid : Int =  
      if(a == vmax) Math.max(b, c) 
      else if(b == vmax)  Math.max(a, c) 
      else  Math.max(b, a)
    
      val h1 = insert(mid, empty)
    val h2 = insert(vmin, h1)
    val h3 = insert(vmax, h2)
    deleteMin(h3) == insert(vmax, insert(mid, empty))
  }
  
  

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }
  

  property("finds min of 2 integers") = forAll { (a: Int, b: Int) =>
    val h = insert(a, empty)

    val h2 = insert(b, h)
    if (a > b)
      findMin(h2) == b
    else
      findMin(h2) == a
  }
  

}
