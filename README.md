# Don't repeat yourself - Jdry automation tests Framework
		 



### Jdry main purpose is to save time  , it run on top of TestNG and add functionality . by tagging methods on the src code , a list of steps can be define and the call information can be collect. eliminate the need to manually define . out of the box it come with jersey and rpc client that support JWT and Basic authentication . additionally plugin can be add through plugin mechanism . methods that are tags as listeners can be trigger to send information to the tester or replace arguments values 


1.	 steps 
2.	 listeners 
3.	 plugins
4.	 dynamic api 
5.	 restFul client
6.	 grpc client
7.	 auto discovery
8.	 logger

# setp
	step is method in the src code that is annotated . any type of annotation can be define or use the one
	that come with Jdry (ApiForTesting). this method will be exposed as aproxy list to the tester with all
	the relevent information needed for the call / request .
# listener
	listener is a method the tag with ListenerForTesting . it can intecept before the method call provide
	the ability to manipulate the arguments value before the method execution by setting annotation 
	property  "type = ListenerType.BEFORE" . or use as validation point by sending the method result
	to the tester using   setting annotation property  "type = ListenerType.AFTER" .
# plugin
	plugin mechanism enable adding more support protocols 
# dynamic api
	dynamic api are method annotated with ApiForTesting using grpc protocol
	this method exposed as step by the jdry agent only at the tests time . enabled testing parts that
	are not supposed to be publish in production time .  	  
# restFul
	resFul client is a jersey client supporting basic and jwt authentication
# grpc client 
	grpc client use avro grpc as the transiver mechanism
# auto discovery
	discovery tool run on the bytecode of the application and discover annotated methods according to the 
	configuration .
# logger
	Very detail trace log 

# How IT Works
Jdry works as client server architecture . it uses the SUT code impl as the building blocks for the testing . a java agent is loaded in the SUT that get the request and execute it on the SUT  .  Listener define methods are capture and send to the Listener Server running on the Tester . to be examine or update and can serve as verify point or data set .  

# Demo application and Tests
	see jdry/tests

# Getting Started

1. disovery
2. analyzer
3. compile
4. setup application for testing
5. create new test project	

for more details see (https://github.com/nimcoh0/Jdry/wiki/)


# Contact
[Project Home](https://softauto.org)
