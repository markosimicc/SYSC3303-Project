SYSC 3303A2 - Iteration 3
Group: 3
Members: Archit Bhatia (101065674), Ross Matthew (101079586), Marko Simic (101072986), Michael Slokar (101092632), Mahmoud Al Sous (101071213)
-----------------------------------------------------------------------------------------------------------------
Files:

+ Buffer.java
	-> Stores requests from RequestInfo into a queue fro scheduling purposes

+ RequestInfo.java
	-> Used by all class to represent the input information between classes
 
+ ElevatorSubsystem.java
	->Receives input information from SchedulerElev and notifies the elevator is ready
	->Sends the information back to SchedulerElev
	->Includes a elevator state machine implementation

+ ElevatorFloors.java
	->Creates an ArrayList of floors for the ElevatorSubsystem to use

+ Scheduler.java
	->Passes information received from SchedulerFloor to SchedulerElev and vice versa
	->Uses the Buffer to send the incoming requests to the SchedulerElev
	->Includes a scheduler state machine implementation

+ SchedulerElev.java
	->Passes information received from ElevatorSubsystem to the main Scheduler

+ SchedulerFloor.java
	->Passes information received from FloorSubsystem to  the main Scheduler
	->Passes the requests into the Buffer to add to the queue

+ FloorSubsystem.java
	->Reads information from text file and puts it into an instance of RequestInfo class
	->Passes the instance to SchedulerFloor using instance of Scheduler 

+ SchTest.java
	->Creates threads for floor, elevator, and the schedulers and starts them
	->Creates instances of scheduler and 4 SchedulerElev
	
+ JUnit_Test.java
	->Tests the methods of the classes with different inputs
	->Tests the states of the elevator and the scheduler
-----------------------------------------------------------------------------------------------------------------
Instruction Set-up:

1) Run 
2) Check console for logs
3) Run JUnit.java
4) Check console for test logs
-----------------------------------------------------------------------------------------------------------------
Reflection:
For the Previous Iteration, we had a single scheduler that was sending requests between
the FloorSubsystem and the ElevatorSubsystem and vice versa. Now we have split up the
Scheduler into 3 threads, one that communicates between the elevator and the SchedulerElev,
one between the floor and the SchedulerFloor, and the main one that shcedules the elevators.
We have also included a Buffer class that is shared by the main Scheduler and the ShedulerFloor,
that puts the requests into a queue and the main sends it through the SchedulerElev and to the 
ElevatorSubsystem.
-----------------------------------------------------------------------------------------------------------------
Breakdown of Responsibilities:

UML class diagram: Ross Matthew, Marko Simic
Elevator Subsystem: Archit Bhatia, Ross Matthew
README.txt: Michael Slokar
Buffer.java: Marko Simic
RequestInfo.java: Ross Matthew
ElevatorSubsystem.java: Archit Bhatia
FloorSubsystem.java: Mahmoud Al Sous
Schedular.java: Mahmoud Al Sous, Marko Simic
SchedularElev.java: Marko Simic, Ross Matthew
SchedularFloor.java: Marko Simic
SchTest.java: Marko Simic
JUnit_Test.java: Marko Simic

