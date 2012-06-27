
package Test;

import java.util.HashMap;
import java.util.Map;

import Paxos.*;
//import Paxos.Paxos;
//import Paxos.Network;


public class Test {

	static ErrorMessages err;
	static TestNetwork n;
	static TestChannel c;
	
	public static void main(String[] inputs) {
		boolean condition = false;
		int[] ifPassed=new int[10]; //for tabulating the end results of the test cases
		do{
			
			 
			
		try{
				int [] dist = new int[100];
				int [] manip_index=new int[100];
				
				//Test case 1 - all proposers are distinguished proposers
				n=new TestNetwork(3, 3, 3);
				
				System.out.println("Test case 1 begins");
				for(int i = 0; i < 3; i++){
					dist[i] = 1;
				}
				new ParamBlock(1,5000, dist, manip_index, err.NONE );
				Paxos p=new Paxos(n);    
				p.runPaxos();				
				for(int wait = 1; wait <= 5; wait++)
					c.sleep();

				System.out.println("Test case 1 ends");
	
				
				
				
				//Test case 2- Toggling distinguished proposers
				long switchTime=10;
				n=new TestNetwork(3, 3, 3);
				System.out.println("Test case 2 begins");
				for(int i = 0; i < 3; i++){
					dist[i] = 0;
				}
				dist[1] = 1;
				int i=10;
				new ParamBlock(2,5000, dist, manip_index, err.NONE);
				p=new Paxos(n);
				long st = System.currentTimeMillis();
				p.runPaxos();
				while(i!=0){
					if((System.currentTimeMillis() - st) > switchTime){
						if(dist[0]==1){
							dist[0]=0;
							dist[1]=1;
						}
						else{
						dist[0]=1;
						dist[1]=0;
						}
						new ParamBlock(2, 1000, dist,manip_index, err.NONE);
						st+=10;
					}	
					--i;				
				}
				for(int wait = 1; wait <= 5; wait++)
					c.sleep();
				System.out.println("test case 2 ends");
				
				
				
				//Test case 3- Dropping messages of distinguished proposer
				n=new TestNetwork(3, 3, 3);
				System.out.println("Test case 3 begins");
				for(i = 0; i < 3; i++){
					dist[i] = 0;
				}
				dist[2]=1;
				manip_index[2]=1;
				new ParamBlock(3, 3000, dist,manip_index,err.DROP);
				p=new Paxos(n);
				p.runPaxos();
				for(int wait = 1; wait <= 5; wait++)
					c.sleep();

				if(n.decision!=-1)
					System.out.println("Test case 3 failed");
				System.out.println("Test case 3 ends");
				
				
				//Test case 4 - dropping message of an acceptor		
				n=new TestNetwork(3, 3, 3);
				System.out.println("Test case 4 begins");	
				for(i = 0; i < 3; i++){
					dist[i] = 0;
				}
				//for(i=0;i<n.totalProcesses;i++){
					//manip_index[i]=0;
				//}
					
					dist[2]=1;
					manip_index[2]=0;
					manip_index[5]=1;
				new ParamBlock(4, 5000, dist,manip_index,err.DROP);
				p=new Paxos(n);
				p.runPaxos();
				for(int wait = 1; wait <= 5; wait++)
					c.sleep();

				System.out.println("Test case 4 ends");
				
				
				
				//Test case 5 - Delay messages
				n=new TestNetwork(3, 3, 3);
				System.out.println("Test case 5 begins");	
				for(i = 0; i < 3; i++){
					dist[i] = 0;	
				}		
				for(i=0;i<n.totalProcesses;i++){
					manip_index[i]=0;
				}				
				dist[2]=1;
				new ParamBlock(5, 5000, dist,manip_index,err.DELAY);
				p=new Paxos(n);
				p.runPaxos();
				for(int wait = 1; wait <= 5; wait++)
					c.sleep();

				System.out.println("Test case 5 ends");
					
					
				//Test case 6 - Duplicate
		        n=new TestNetwork(3, 3, 3);
				System.out.println("Test case 6 begins");	
				for(i = 0; i < 3; i++){
					dist[i] = 0;	
				}			
				for(i = 0; i < n.totalProcesses; i++){
					manip_index[i] = 0;	
				}
				dist[1]=1;
				manip_index[5]=1;
				manip_index[4]=1;
				manip_index[1]=1;
				manip_index[8]=1;
				new ParamBlock(6, 5000, dist,manip_index,err.DUPLICATE);
				p=new Paxos(n);
				p.runPaxos();
				for(int wait = 1; wait <= 5; wait++)
					c.sleep();

				System.out.println("Test case 6 ends");
					 
		        
		        
		        //Test Case 7 - Reordering
				n=new TestNetwork(3, 3, 3);
				System.out.println("Test case 7 begins");						
				for(i = 0; i < 3; i++){
					dist[i] = 0;	
				}			
				for(i = 0; i < n.totalProcesses; i++){
					manip_index[i] = 0;	
				}	
				dist[1]=1;
				manip_index[5]=1;
				manip_index[4]=1;
				manip_index[1]=1;
				manip_index[7]=1;	
				new ParamBlock(7, 3000, dist,manip_index,err.DUPLICATE);
				p=new Paxos(n);
				p.runPaxos();
				for(int wait = 1; wait <= 5; wait++)
					c.sleep();

				System.out.println("Test case 7 ends");
					
		        
		        
		        //test case 8 :- 10 proposers,acceptors,learners [Stress Test]
				n=new TestNetwork(10, 10, 10);
				System.out.println("Test case 8 begins");
				for(i = 0; i < 3; i++){
					dist[i]=0;
				}
				for(i = 0; i < n.totalProcesses; i++){
					manip_index[i] = 0;	
				}
				dist[2] = 1;
				new ParamBlock(8,30000, dist, manip_index, err.NONE );
				p=new Paxos(n);	    
				p.runPaxos();
				for(int wait = 1; wait <= 35; wait++)
					c.sleep();

				System.out.println("Test case 8 ends");
					
				
				
				
			}catch(StopError e) {
             System.out.println(e);
			} 
		}while(condition);
			
		System.out.println("Test cases ended");	
  }
}


class ParamBlock {
	static int TestCase;
	static long TimeOut;
	static int[] dist;
	static long switchTime;
	static ErrorMessages err;
	static int[] acptrs;
	static int[] manipulating_index;

	public ParamBlock(int tc, long t, int[] d, int[] dp, ErrorMessages e)
	{
			TestCase =tc;
			TimeOut = t;
			dist = d.clone();
			manipulating_index=dp.clone();
			err = e;
	}

	
}
