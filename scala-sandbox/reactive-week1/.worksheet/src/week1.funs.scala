package week1

object funs {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(72); 
  println("Welcome to the Scala worksheet");$skip(48); 

  val f: (Int => String) = List("a", "b", "c");System.out.println("""f  : Int => String = """ + $show(f ));$skip(7); val res$0 = 
  f(2);System.out.println("""res0: String = """ + $show(res$0));$skip(72); 

  val fun: PartialFunction[String, String] = { case "ping" => "pong" };System.out.println("""fun  : PartialFunction[String,String] = """ + $show(fun ));$skip(14); val res$1 = 
  fun("ping");System.out.println("""res1: String = """ + $show(res$1));$skip(26); val res$2 = 
  fun.isDefinedAt("ping");System.out.println("""res2: Boolean = """ + $show(res$2));$skip(24); 

  val sample = 1 to 10;System.out.println("""sample  : scala.collection.immutable.Range.Inclusive = """ + $show(sample ));$skip(94); 
  val isEven: PartialFunction[Int, String] = {
    case x if x % 2 == 0 => x + " is even"
  };System.out.println("""isEven  : PartialFunction[Int,String] = """ + $show(isEven ));$skip(122); 

  // the method collect can use isDefinedAt to select which members to collect
  val evenNumbers = sample collect isEven;System.out.println("""evenNumbers  : scala.collection.immutable.IndexedSeq[String] = """ + $show(evenNumbers ));$skip(93); 

  val isOdd: PartialFunction[Int, String] = {
    case x if x % 2 == 1 => x + " is odd"
  };System.out.println("""isOdd  : PartialFunction[Int,String] = """ + $show(isOdd ));$skip(163); 

  // the method orElse allows chaining another partial function to handle
  // input outside the declared domain
  val numbers = sample map (isEven orElse isOdd);System.out.println("""numbers  : scala.collection.immutable.IndexedSeq[String] = """ + $show(numbers ))}
  
 
}
