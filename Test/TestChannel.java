package Test;

import Paxos.Network;
import Paxos.Channel;
import java.util.*;

public class TestChannel extends Channel {
  TestNetwork network;
  int index;
  
  
  
  /** Send the message message to process destination. */

  public void sendMessage(int destination, String message) {
    synchronized(network.queues[destination]) {
    	
    	network.shouldKill();
    	
    	//Duplicate messages added to queue
    	if(ParamBlock.err == ErrorMessages.DUPLICATE){
    		for(int i = 0; i < 10; i++)
    		network.queues[destination].add(message);	  	
        }
    	
    	//Delay messages 
    	else if(ParamBlock.err == ErrorMessages.DELAY){
    			
				for(int wait = 1; wait <= 5; wait++)
					sleep();

     					network.queues[destination].add(message);
    	}
    	else network.queues[destination].add(message);
    }
  }

  /** Receive a message. */

  public String receiveMessage() {
    synchronized(network.queues[index]) {
    	network.shouldKill();
    	
    	
    	if(ParamBlock.err == ErrorMessages.DROP){
    		if(ParamBlock.manipulating_index[index] == 1){
    			if (!network.queues[index].isEmpty())
    				network.queues[index].remove();
    			return null;
    		}
    	}
    			
    	
    	else if(ParamBlock.err == ErrorMessages.DELAY){
    		   		
    			if(ParamBlock.manipulating_index[index] == 1){
    				for(int wait = 1; wait <= 5; wait++)
    					sleep();

    					synchronized(network.log){
    						if (!network.queues[index].isEmpty()){
    							network.log.add(network.queues[index].toString());
    							return network.queues[index].remove();
    						}
    						
    						else 
    							return null;
    					}	
   				}
    	}
    

    	else if(ParamBlock.err == ErrorMessages.REORDER){
    		int[] isFirst=new int[(network.numProposers()+network.numAcceptors()+network.numLearners())];
    		//Stack<String> stack=new Stack<String>();
    		Stack<String>[] stack;
    		stack=new Stack[network.totalProcesses];
    		for(int i=0;i<network.totalProcesses;i++) {
  		      stack[i]=new Stack<String>();
  		    }
    		 synchronized(stack[index]) {
    			 if(ParamBlock.manipulating_index[index] == 1){
    				 	
    					 while(!network.queues[index].isEmpty()){
    						 stack[index].push(network.queues[index].remove());
    					
    					 }
    					 
    					 synchronized(network.log){
    						 while (!stack[index].empty()) 
    						 {
    							 network.log.add(network.queues[index].toString());
    							 return stack[index].pop();
    						 }
    					 } 
    					return null;
    				 }
    			 
    		 }	
    	} 	
    	
    	
         	
    	    if (!network.queues[index].isEmpty()){
         			synchronized(network.log) {
         			if(network.queues[index].peek()!=null)
         				network.log.add(network.queues[index].toString());
         			}
         			return network.queues[index].remove();
         	}
         	else 
         		return null;
     	
    }
 } 

  /** Call this function to determine whether a proposer is distinguished. */

  public boolean isDistinguished() {
    if (ParamBlock.dist[index] == 1)
      return true;
    if (index>=network.numProposers)
      throw new Error("Non-proposers should not be asking whether they are distinguished");
    return false;
  }

  /** Call this function to register a decision by a learner. */

  public void decide(int decision) {
    if (index<(network.numProposers+network.numAcceptors))
      throw new Error("Non-learner should not be deciding a value");

    if (decision>=network.numProposers)
      throw new Error("The decided value was not an initial value...");

    synchronized(network) {
      if (network.decision==-1)
      {
	network.decision=decision;
      }
      else {
	if (network.decision!=decision)
	{
		System.out.println(network.log);
		System.out.println("Disagreement between Learners");
	}
      }
    }
  }

  /** Call this function to get the initial value for a proposer. */

  public int getInitialValue() {
    if (index>=network.numProposers)
      throw new Error("Non-proposers should not be asking for initial value");
    return index;
  }

  
  //Sleep call
  public void sleep() {
		try {
			Thread.sleep(1000);
	       } catch (InterruptedException e) {
	               //ignore exception
	              System.out.println("Interrupted");
	      }
	}

  
  
  
}