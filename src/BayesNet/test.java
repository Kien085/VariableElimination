package BayesNet;


import Algorithm.*;
public class test {
	
	public static void main(String[]args) {
		BayesNet net = new BayesNet();
		RandomVariable[] noParents = new RandomVariable[0];
		net.addNode("root",noParents);
		net.addNode("root2", noParents);
		net.addNode("child", new RandomVariable[] {net.getNode("root"),net.getNode("root2")});
		net.addNode("GrandChild", new RandomVariable[] {net.getNode("child")});
		/* Testing out the Bayes Net
		System.out.println(net.getNode("root").name);
		System.out.println(net.getNode("root").parents);
		System.out.println(net.getNode("root").children.get(0).name);
		System.out.println(net.getNode("root").children.get(0).parents.get(0).name);
		System.out.println(net.getNode("root2").children.get(0).parents.get(1).name);
		*/
		
		VariableElim testing = new VariableElim(net);
		testing.printCPTs();
		System.out.println("Root=0 evidence");
		testing.doEvidence("root", 0);
		testing.printCPTs();
		System.out.println("Root2=1 evidence");
		testing.doEvidence("root2", 1);
		testing.printCPTs();
		System.out.println("child elim");
		testing.doElim("child");
		testing.printCPTs();
	}
}
