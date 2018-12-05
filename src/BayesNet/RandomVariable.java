package BayesNet;

import java.util.ArrayList;

public class RandomVariable {

	public String name;
	public ArrayList<RandomVariable> parents;
	public ArrayList<RandomVariable> children;
	
	public RandomVariable(String name)
	{
		this.name = name;
		parents = new ArrayList<RandomVariable>();
		children = new ArrayList<RandomVariable>();
	}
	
	public void addParent(RandomVariable parent) 
	{
		if(parent != null)
		{
			parents.add(parent);
		}
	}
	
	public void addChild(RandomVariable child)
	{
		if(child != null)
		{
			children.add(child);
		}
	}
}
