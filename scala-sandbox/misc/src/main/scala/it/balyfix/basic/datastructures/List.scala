package it.balyfix.basic.datastructures

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {

  /**
   * Variadic function is commons idiom that can take 0 or more element arguments of type A
   * Is commons use in object apply methodto  costruct istance data type
   */

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  val x = List(1, 2, 3, 4, 5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case _ => 101
  }

  def dropWhile[A](list: List[A], n: Int): List[A] =
    if (n <= 0) list else
      list match {
        case Nil => Nil
        case Cons(_, t) => dropWhile(t, n - 1);
      }

  def tail[A](list: List[A]): List[A] = list match {
    case Nil => Nil
    case Cons(_, t) => t
  }
  
  def sum (list : List[Int]) : Int = list match  
  {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }
  
  def mul(list : List[Int]) : Int = list match 
  {
    case Nil => 1
    case Cons(x,xs) => x * mul(xs)
  }
  
  
  def myFunct[A,B](list : List[A], z : B)(f:(B,A) => B) : B = list match 
  {
    case Nil => z
    case Cons(x,xs) => myFunct(xs,f(z,x))(f)
    
    
  }
  
  
  

  def foldLeft[A, B](l: List[A], z: B)(f: (B, A) => B): B = l match {
    case Nil => z
    case Cons(h, t) => foldLeft(t, f(z, h))(f)
  }

  def init[A](l: List[A]): List[A] =
    l match {
      case Nil => sys.error("init of empty list")
      case Cons(_, Nil) => Nil
      case Cons(h, t) => Cons(h, init(t))
    }

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def lengthList(): Int =
    {
      val mylst = Cons(1, Cons(2, Cons(3, Nil)));
      val result = foldRight(mylst, 0)((x, y) => y + 1);
      result;
    }

  def testAdd1 =
    {

      val mylst = Cons(1, Cons(2, Cons(3, Nil)));

      val out = add1(mylst);

      print(out.toString());
    }

  def add1(l: List[Int]): List[Int] =
    l match {
      case Nil => Nil
      case Cons(x, t) => add1(Cons(x + 1, t))
    }

  def testfoldRigth() {

    val mylst = Cons(1, Cons(2, Cons(3, Nil)));
    val result = foldRight(mylst, 2)((x, y) => x + y);

    print(result)

  }

}