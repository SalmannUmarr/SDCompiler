**SALTIF Compiler - Lexical Analyzer**

Compiler Construction

Salman Umar (I22-0908)

Danish Atif (I22-1095)

   📑 Table of Contents

      Language Overview

      Keyword List

      Identifier Rules

      Literal Formats

      Operators & Precedence

      Comment Syntax

      Sample Programs

      Compilation & Execution

      Error Handling

**1. Language Overview**

            Language Name: SALTIF

            File Extension: .saltif

SALTIF is a custom-designed, statically typed language developed for academic exploration into lexical analysis and compiler design.

**2. Keyword List & Meanings**

The scanner recognizes the following reserved keywords. These cannot be used as identifiers.

      void Specifies that a function does not return a value.

      int Declares a 32-bit integer variable.

      float Declares a floating-point variable (max 6 decimal precision).
      
      string Declares a sequence of characters enclosed in double quotes.

      char Declares a single character enclosed in single quotes.

      boolean Declares a logical variable (true or false).
      
      if Conditional branch execution.
      
      else Alternative branch for if conditions.
      
      while Loop structure that repeats while a condition is true.

      return Exits a function and optionally returns a value.

**3. Identifier Rules**

Identifiers are used for naming variables and functions.

      [!IMPORTANT]
      Strict Rules:

      Must start with an alphabetic letter (A-Z, a-z).

      Can contain letters and digits.

      Case-sensitive (MyVar is different from myvar).

      Maximum Length: 31 characters.

            Examples:

            ✅ ValidVar, counter123, Main

            ❌ 123Invalid (starts with digit)

            ❌ too_long_identifier_exceeding_limit (exceeds 31 chars)

** 4. Literal Formats**

         Integer       Sequence of digits, optional + or - prefix.      42, -10, +500

         Float         Digits with a decimal point. Max 6 decimal places.      3.14, 0.0005

         String        Characters in double quotes. Supports escapes.      "Hello World", "C:\\Path"

         Char          Single character or escape in single quotes.      'A', '\n', '\t'
         
         Boolean       Logical constants.     true, false

**5. Operators & Precedence**

Operators are grouped by type (ordered from highest to lowest precedence):

      Logical NOT: !

      Arithmetic (Multiplicative): *, /, %

      Arithmetic (Additive): +, -

      Relational: <, >, <=, >=

      Equality: ==, !=

      Logical AND/OR: &&, ||

      Assignment: =, +=, -=

**6. Comment Syntax**

   SALTIF supports two types of comments:

   Single-line: Uses ##

   int x = 10; ## This is a single line comment


   Multi-line: Uses #* and *#

   #* This is a 
      multi-line comment *#


**7. Sample Programs**

   Basic Declarations (test1.saltif)

   void Main() {
       int Counter = +100;
       float Grade = 95.5;
       string Message = "Passed";
       char Letter = 'A';
       boolean IsActive = true;
   }


   Logic and Loops (test2.saltif)

   void CheckValue() {
    if (Counter > 0 && !IsLocked) {
           Counter -= 1;
       } else {
           return false;
       }
   }


**8. Compilation & Execution**

**Prerequisites**

   JDK 11+ installed.

   Steps

      Prepare Directory: Ensure source is in src/ and test files are in test/.

      Compile:

      javac -d bin src/**/*.java


      Run:

      java -cp bin Main


**9. Error Handling**

   The scanner detects and reports the following lexical errors:

      Float Precision: Reports if a float exceeds 6 decimal places.

      Identifier Length: Reports if a name exceeds 31 characters.

      Unterminated Literals: Detects unclosed strings or comments.

      Invalid Characters: Reports symbols not in the SALTIF alphabet.
