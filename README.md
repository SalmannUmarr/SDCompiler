**SALTIF Compiler - Lexical Analyzer**

Course: Theory of Automata / Compiler Construction

Team Members: * Salman Umar (I22-0908)

Danish Atif (I22-1095)

1. Language Overview

Language Name: SALTIF

File Extension: .saltif

SALTIF is a custom-designed, statically typed language developed for academic exploration into lexical analysis and compiler design.

2. Keyword List & Meanings

The scanner recognizes the following reserved keywords. These cannot be used as identifiers.

Keyword

Meaning

void

Specifies that a function does not return a value.

int

Declares a 32-bit integer variable.

float

Declares a floating-point variable (max 6 decimal precision).

string

Declares a sequence of characters enclosed in double quotes.

char

Declares a single character enclosed in single quotes.

boolean

Declares a logical variable (true or false).

if

Conditional branch execution.

else

Alternative branch for if conditions.

while

Loop structure that repeats while a condition is true.

return

Exits a function and optionally returns a value.

3. Identifier Rules

Identifiers are used for naming variables and functions.

Rules:

Must start with an alphabetic letter (A-Z, a-z).

Can contain letters and digits.

Case-sensitive (MyVar is different from myvar).

Maximum Length: 31 characters (Lexical error triggered if exceeded).

Examples:

✅ ValidVar, counter123, Main

❌ 123Invalid (starts with digit), too_long_identifier_exceeding_limit (exceeds 31 chars)

4. Literal Formats

Type

Format / Rule

Example

Integer

Sequence of digits, optional + or - prefix.

42, -10, +500

Float

Digits with a decimal point. Max 6 decimal places.

3.14, 0.0005, -19.95

String

Characters enclosed in double quotes. Supports escapes.

"Hello World", "C:\\Path"

Char

A single character or escape sequence in single quotes.

'A', '\n', '\t'

Boolean

Logical constants.

true, false

5. Operators & Precedence

Operators are grouped by type. The scanner identifies the following (ordered by general precedence):

Logical NOT: !

Arithmetic (Multiplicative): *, /, %

Arithmetic (Additive): +, -

Relational: <, >, <=, >=

Equality: ==, !=

Logical AND/OR: &&, ||

Assignment: =, +=, -=

6. Comment Syntax

SALTIF supports two types of comments which are stripped by the scanner:

Single-line: Uses ## to comment out the rest of the line.

int x = 10; ## This is a single line comment


Multi-line: Uses #* to start and *# to end.

#* This is a 
   multi-line comment *#


7. Sample Programs

Sample 1: Basic Declarations (test1.saltif)

void Main() {
    int Counter = +100;
    float Grade = 95.5;
    string Message = "Passed";
    char Letter = 'A';
    boolean IsActive = true;
}


Sample 2: Logic and Loops (test2.saltif)

void CheckValue() {
    if (Counter > 0 && !IsLocked) {
        Counter -= 1;
    } else {
        return false;
    }
}


Sample 3: String Escaping (test3.saltif)

void Paths() {
    string Win = "C:\\Program Files\\";
    char Tab = '\t';
    char NewLine = '\n';
}


8. Compilation & Execution

Prerequisites

Java Development Kit (JDK) 11 or higher installed.

Steps

Prepare Directory:
Ensure your source code is in src/ and test files are in test/.

Compile:

javac -d bin src/**/*.java


Run:

java -cp bin Main


Outputs:

Console: Displays real-time token scanning, statistics, and the Symbol Table.

TestResults.txt: A permanent log of the entire execution for all test files.

9. Error Handling

The scanner provides descriptive error messages for:

Float Precision: Reports if a float exceeds 6 decimal places.

Identifier Length: Reports if a name exceeds 31 characters.

Unterminated Literals: Detects strings or multi-line comments that do not close.

Invalid Characters: Reports symbols not recognized by the SALTIF alphabet.
