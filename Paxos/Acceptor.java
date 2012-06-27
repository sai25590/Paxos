package Paxos;

class Acceptor implements Runnable {

	Channel c;
	Network network;
	String msg;
	int index;
	
	/* hPrep - highest prepare request to which it is already responded
	   highAcc - highest proposal it has accepted
	 */
	int hPrep, highAcc;
	// highAcc's corresponding value!!
	int val;
	
    public Acceptor(Network nTemp, int index){
    	network = nTemp;
    	c = nTemp.getChannel( index );
    	this.index = index;
    	hPrep = -1;
    	val = -1;
    	highAcc = -1;
    }
	
	public void run() {
		String tokens[];
		int i, n;
		while(true)
        {
			while((msg = c.receiveMessage()) == null);
			
			tokens = msg.split(" ");
		
			if(tokens.length == 1)
			{       	
					n = Integer.parseInt(msg);
					
					if(n > hPrep)  // promise the proposer! 
					{
							msg = highAcc + " " + val + " " + index + " " + n;
							c.sendMessage( (n % network.numProposers()) , msg); 
							hPrep = n;
					}
		    }
		 	else
		 	{
		 		
		 		n = Integer.parseInt(tokens[0]);
		 		
		 			if(n >= hPrep)
		 			{
		 					highAcc = n;
		 					val = Integer.parseInt(tokens[1]);
		 					
		 				    msg = tokens[1] + " " + index;
		 					for(i = network.numProposers() + network.numAcceptors() ; i < network.numProposers() + network.numLearners() + network.numAcceptors(); i++)
		 					{
		 							c.sendMessage(i, msg);
		 					}
		 			}
     
		 	}
			
        
        }
		
    }
}
