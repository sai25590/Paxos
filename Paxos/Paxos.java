package Paxos;

public class Paxos {
  Network network;
  public Paxos(Network network) {
    this.network=network;
  }

  /** This should start your Paxos implementation and return immediately. */
  public void runPaxos() {

  int i;
  
  for(i = 0; i < network.numProposers(); i++)
  {
	  (new Thread(new Proposer(network, i))).start();
  }
  for(i = network.numProposers; i < network.numProposers + network.numAcceptors; i++)
  {
	  (new Thread(new Acceptor(network, i))).start();
  }
  for(i = network.numProposers + network.numAcceptors; i < network.numProposers + network.numAcceptors + network.numLearners; i++)
  {
	  (new Thread(new Learner(network, i))).start();
  } 
  
  
  }
}
