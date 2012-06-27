package Paxos;

import java.io.BufferedWriter;
import java.io.FileWriter;



class Learner implements Runnable {

	Channel c;
	Network n;
	int index;
	String tokens[];
	String msg;
    public Learner(Network nTemp, int index){
    	n = nTemp;
    	c = nTemp.getChannel( index );
    	this.index = index;
    }
	
	public void run() {
		int i, value, acc_no, count;
		int from_acc[] = new int[n.numAcceptors()];
		
		 for(i = 0; i < n.numAcceptors(); i++)
				from_acc[i] = -1;
		 
	while(true)
      {
    	  while((msg = c.receiveMessage()) == null);
    	  tokens = msg.split(" ");
    	  value = Integer.parseInt(tokens[0]);
    	  acc_no = Integer.parseInt(tokens[1]);
    	  from_acc[acc_no - n.numProposers()] = value;
    	  count = 0;
    	  for(i = 0; i < n.numAcceptors(); i++)
    	  {
    		  if(value == from_acc[i])
    			  count++;
    	  }
    	 
    	  if(count >= (n.numAcceptors()/2) + 1)
    	  {
    		  c.decide(value);
    		
     	  }
     
       }
    }


}
