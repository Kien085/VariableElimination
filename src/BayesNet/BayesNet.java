package BayesNet;

import java.util.HashMap;

public class BayesNet{

	public HashMap<String, RandomVariable> nodes;
	
	public BayesNet()
	{
		nodes = new HashMap<String, RandomVariable>();
	}
	
	/* Add things from top to bottom, root first */
	public void addNode(String name, RandomVariable[] parents)
	{
		RandomVariable variable = new RandomVariable(name);
		//add the parents
		for(RandomVariable parent : parents)
		{
			variable.addParent(parent);
		}
		//also need to add variable as the child of the parents
		for(RandomVariable myParent : variable.parents)
		{
			myParent.addChild(variable);
		}
		nodes.put(name, variable);
	}
	
	public RandomVariable getNode(String name)
	{
		return nodes.get(name);
	}
}
