# My notes
TA help queue: help.cs240.click

Autograder: cs240.click

Use Javadoc. for documentation (accessible on programming test)

### Github:  
- Verify you have the latest code (git pull) 
- Refactor, test, and/or implement a small portion of cohesive code (test code test) 
- Commit and push (git commit git push) 
- Repeat 

### Chess Game Notes
#### Phase 0: Chess Moves
- King (1): 1 square any direction (including diagonal)
  - not allowed to make any move that would allow opponent to capture
- Pawn (8): move forward 1 square if unoccupied
  - 1st turn: can be moved 2 squares
  - capture forward diagonally (1 forward 1 sideways)
  - moves to end of board: promoted to Rook, Knight, Bishop, or Queen
- Rook (2): move in straight line as far as open space
  - if enemy piece in line: move to position & capture
- Knight (2): Move in L shape (2 square one direction, 1 square in other)
  - jump over pieces in-between
  - move to occupied enemy square: capture enemy piece
- Bishop (2): move in diagonal lines as far as open space
  - if enemy at end of diagonal: move to position & capture
- Queen (1): most powerful piece!
  - move in straight line & diagonal as far as open
  - if enemy piece at end: move to position & capture
  - *all moves Rook/Bishop take from Queen position

Board Setup: 
- each player: 8 pawns, 1 King, 1 Queen, 2 Bishop, 2 Knight, 2 Rooks
- White pawns = row 2; white rest = row 1
- Black pawns = row 7; black rest = row 8
- 1: Rook, 2: Knight, 3: Bishop, 4: Queen, 5: King, 6: Bishop, 7: Knight, 8: Rook
     
black
|R|N|B|K|Q|B|N|R| 
|P|P|P|P|P|P|P|P|
| | | | | | | | |
| | | | | | | | |
| | | | | | | | |
| | | | | | | | |
|P|P|P|P|P|P|P|P|
|R|N|B|K|Q|B|N|R| 
white 

### Java Notes
Primitive Data Types:
- byte = 8 bit
- short
- int = 32 bit
- long - 10L (assigns 32 remaining 0s to front of int 10)
- float = 32 bit floating number
- double - same as python & c++
- char = 16 bit, single character = 'a' or '\unicode'
- boolean = true or false (all lowercase, no strings)

Print:
- println(..+..) -> prints line
- printf("%d, %d/n", myInt, myBool) -> format print

Convert String to Int:
- Wrapper Class: convert string to corresponding class
  - same as data type, just first letter cap
    - Byte, Short, Double, Long, Float, Double, Boolean
    - int Integer.parseInt(String value)

Strings: immutable, shares strings
- Declaration: 
  - String s = "Hello"; --> use this method
  - String s = new String ("Hello");
- Concatenation:
  String s1 = "Hello";
  String s2 = "BYU";
  String s3 = s1 + "" + s2;
- String formatting:
  - String s3 = String.format("%s %%s", s1, s2);
- String Methods:
  - int length()
  - char charAt
  - String trim
  - boolean startsWith(String)
  - int indexOf(int)
  - int indexOf(String)
  - String substring(int)
  - String substring(int, int)
- Special characters:

Java Files
- MyCLass.java = source file (one Java class per .java file)
  - file name = class name
- MyClass.class = executable file (made from compiling .java, executed by JVM, portable code)
- main method: used when you want program to run but not compiled as class (invoke in command line)
  - public static voic main(string [] args)
  - public static void main(String...args)

Javadoc
- documentation for Java class library
  - generated from code and Javadoc comments in the code
  /** Javadoc comment
  */
  public class MyClass {
    /** summary; 
      @ param myParam
      @ return
      */
      public void myMethod (myParam){}
    }