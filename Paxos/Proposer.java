package Paxos;

class Proposer implements Runnable {

	Channel c;
	Network n;
	int pnum, index;
	String msg;
    public Proposer(Network nTemp, int index ){
    	c = nTemp.getChannel( index );
    	n = nTemp;
    	pnum = index;
    	this.index = index;
    }
	
	public void run() {
		
		int i;
        String tokens[];
        int highNa = -1, valA, nA, value = -1;
        int [] dest = new int[ n.numAcceptors() ];
        int timeout;
             		
 		while(true)
 		{
    
 		while(c.isDistinguished() == false);
 		
        for(i = n.numProposers(); i < n.numProposers() + n.numAcceptors() ; i++)
        {
        	msg = Integer.toString(pnum); 
        	c.sendMessage(i, msg);
        }
        
        i = 0;
        timeout = 80;
        do
        {
        	while(timeout != 0)
        	{
        	if((msg = c.receiveMessage()) == null)
        		{
        			timeout--;
					c.sleep();
        			continue;
        		}
        	break;
        	}
        	    	
        	if(timeout == 0)
                		break;
        	        	
        	
        	tokens = msg.split(" ");
        	
        	nA = Integer.parseInt(tokens[0]);
        	valA = Integer.parseInt(tokens[1]);        	
        	
        	
        	if(dest[ (Integer.parseInt(tokens[2]) - n.numProposers())] == 0 && Integer.parseInt(tokens[3]) == pnum  )
        	{        
        		
        	dest[ Integer.parseInt(tokens[2]) - n.numProposers()] =   Integer.parseInt(tokens[2]);
        	i++;
        	
        	
        		if(nA > highNa)
        		{
        			value = valA;
        			highNa = nA;
        		}
        	}
        	
        } while(i < (n.numAcceptors()/2) + 1); 
        
        if(timeout == 0)
        {
        	pnum += n.numProposers();
        	continue;
        }
        
        if(value == -1)
        	value = c.getInitialValue();
        
        msg = pnum + " " + value;
        
        for(i = 0; i < n.numAcceptors(); i++)
        {
        	if(dest[i] > 0)
        	     c.sendMessage(dest[i], msg);
        
        }
             
        pnum += n.numProposers(); 
 		
 		}    //while(true) loop -end 		
 } // run - end
	
} // class - end