SYSC 3303A2 - Iteration 1
Group: 3
Members: Archit Bhatia (101065674), Ross Matthew (), Marko Simic (101072986), Michael Slokar (), Mahmoud Al Sous (101071213)
-----------------------------------------------------------------------------------------------------------------
Files:

+ RequestInfo.java
	-> Used by all class to represent the input information between classes
 
+ ElevatorSubsystem.java
	->Receives input information from scheduler and notifies the elevator is ready
	->Sends the information back to scheduler 

+ Scheduler.java
	->Passes information received from FloorSubsystem to ElevatorSubsystem and vice versa

+ FloorSubsystem.java
	->Reads information from text file and puts it into an instance of RequestInfo class
	->Passes the instance to scheduler using instance of scheduler 

+ Test.java
	->Creates threads for floor and elevator and starts them
	->Creates instance of scheduler
	
+ JUnit_Test.java
	->Tests the methods of the classes with different inputs
-----------------------------------------------------------------------------------------------------------------
Instruction Set-up:

1) Run Test.java
2) Check console for logs
-----------------------------------------------------------------------------------------------------------------
Breakdown of Responsibilities:

UML class diagram: Archit Bhatia
README.txt: Archit Bhatia
UML sequence diagram: Michael Slokar
RequestInfo.java: Ross Matthew
ElevatorSubsystem.java: Ross Matthew
Schedular.java: Marko Simic
Test.java: Marko Simic
FloorSubsystem.java: Mahmoud Al Sous
JUnit_Test.java: Ross Matthew, Mahmoud Al Sous
