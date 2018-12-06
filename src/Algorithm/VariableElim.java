package Algorithm;
import BayesNet.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public class VariableElim {
	
	public BayesNet net;
	public ArrayList<HashMap<ArrayList<String>,HashMap<Integer,Double>>> CPTs = new ArrayList<HashMap<ArrayList<String>,HashMap<Integer,Double>>>();
	public double value = 1;
	
	public VariableElim(BayesNet argNet)
	{
		net = argNet;
		createCPTs();
	}
	
	public void createCPTs()
	{
		for(RandomVariable var : net.nodes.values())
		{
			HashMap<Integer,Double> newCPT = new HashMap<Integer,Double>();
			int thing = 1 + var.parents.size(); // how many rows will be in the CPT
			thing = (int)Math.pow(2, thing);
			for(int i = 0; i < thing; i++)
			{
				Integer newI = new Integer(i);
				Double newDub = new Double(value);
				newCPT.put(newI, newDub);
				value += 1;
			}
			HashMap<ArrayList<String>,HashMap<Integer,Double>> addName = new HashMap<ArrayList<String>,HashMap<Integer,Double>>();
			ArrayList<String> names = new ArrayList<String>();
			names.add(var.name); //THINGS WILL ALWAYS BE [CHILD,PARENTS] (until the order gets messed up by variable elimination)
			for(RandomVariable parent : var.parents) 
			{
				names.add(parent.name);
			}
			addName.put(names, newCPT);
			CPTs.add(addName);
		}
	}
	
	public void doEvidence(String name, int evidence)
	{
		for(HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt: CPTs) //search through all CPTs
		{
			for(ArrayList<String> vars: cpt.keySet()) //remember that vars has all of the variables that are in this CPT
			{
				ArrayList<String> copyVars = (ArrayList<String>) vars.clone();
				if(vars.contains(name))
				{
					int index = vars.indexOf(name);
					//depending on Index, we want to get rid of different things
					//Delete with steps
					//step = how many to delete
					int size = 0;
					for(HashMap<Integer,Double> realHash : cpt.values())
					{
						size = realHash.size();
					}
					int step = (vars.size() - index) - 1;
					for(int y = 0; y < vars.size(); y++)
					{
						if(vars.get(y).contains("="))
						{
							step--;
						}
					}
					step = (int)Math.pow(2, step);
					int stepCount = 0;
					boolean inStep = false;
					if(evidence == 0)
					{
						for(int i = 0; i < size; i++)
						{
							Integer newI = new Integer(i);
							if(inStep)
							{
								for(HashMap<Integer,Double> realHash : cpt.values())
								{
									realHash.remove(newI);
								}
							}
							stepCount++;
							if(stepCount == step)
							{
								stepCount = 0;
								inStep = !inStep;
							}
						}
					}
					else
					{
						inStep = true;
						for(int i = 0; i < size; i++)
						{
							Integer newI = new Integer(i);
							if(inStep)
							{
								for(HashMap<Integer,Double> realHash : cpt.values())
								{
									realHash.remove(newI);
								}
							}
							stepCount++;
							if(stepCount == step)
							{
								stepCount = 0;
								inStep = !inStep;
							}
						}
					}
					
					Integer e = new Integer(evidence);
					String stringE = e.toString();
					String newName = name + "=" + stringE;
					copyVars.remove(index);
					copyVars.add(newName); //change name of the CPT
					
					for(HashMap<Integer,Double> realHash: cpt.values()) //need to rename everything again
					{
						HashMap<Integer,Double> tempHash = new HashMap<Integer,Double>();
						int x = 0;
						for(Integer i: realHash.keySet())
						{
							
							Integer key = new Integer(x);
							Double value = realHash.get(i);
							x++;
							tempHash.put(key, value);
						}
						cpt.remove(vars);
						cpt.put(copyVars, tempHash);
						
						
					}
				}
			}
		}
	}
	
	public void doElim(String name)
	{
		int rows = 0;
		ArrayList<String> newVars = new ArrayList<String>(); //name of new CPT
		for(HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt: CPTs) //for each of the CPTs
		{
			for(ArrayList<String> vars: cpt.keySet()) //search through all of the names of the CPT
			{
				if(vars.contains(name))
				{
					//need to combine it
					//how can I do it
					//Need to find where the variable = 0 and = 1
					//How do I know how many rows there are?
					//I guess it's only the amount of people in other names of cpts that have not been evidenced yet
					rows += (vars.size() - 1); //-1 to take out the variable that we want gone
					for(int y = 0; y < vars.size(); y++) //if there are still evidence variables in the titles of the CPTs
					{
						if(vars.get(y).contains("="))
						{
							rows--;
						}
						else
						{
							if(!vars.get(y).equals(name))
							{
								newVars.add(vars.get(y));
							}
						}
					}
				}
			}
		}
		if(rows == 0 )
		{
			rows = 1;
		}
		rows = (int)Math.pow(2, rows);
//		System.out.println(rows);
//		System.out.println(newVars);
		
		//ok now what
		//should I make 2 different doubles for each side of the addition per row
		//
		ArrayList<Double> addThing = new ArrayList<Double>();
		int thingRows = 2 * rows;
		Double one = new Double(1); 
		for(int i = 0; i < thingRows; i++) //filling out the doubles with starting 1
		{
			addThing.add(one);
		}
		//also create another ArrayList<ArrayList<String>> in order to regurgitate what created what
		ArrayList<ArrayList<String>> learning = new ArrayList<ArrayList<String>>();
		ArrayList<String> empty = new ArrayList<String>();
		
		//another arraylist in case people want the table entries not numbered
		ArrayList<ArrayList<String>> tables = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < thingRows; i++) //filling out the doubles with starting 1
		{
			learning.add(empty);
		}
		for(int i = 0; i < thingRows; i++) //filling out the doubles with starting 1
		{
			tables.add(empty);
		}
		
		for(HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt: CPTs) //for each of the CPTs
		{
			for(ArrayList<String> vars: cpt.keySet()) //search through all of the names of the CPT
			{
				int thingie = 0;
				if(vars.contains(name))
				{
					if((vars.contains(newVars.get(0)))) //needs to both have the variable and something else
					{
						//then we must multiply something onto the numbers
						//but how do we know which row to multiply
						//actually, we're going to have to multiply every row, since at this point this CPT is the only one that has things relying on the variable
						for(int i = 0; i < thingRows; i++) //going through each row
						{
							for(HashMap<Integer,Double> realValues : cpt.values()) //actually being able to access the CPTs
							{
								double tempDubs = addThing.get(i);
								//System.out.println(tempDubs);
								if(anotherBugFix(vars.get(0),vars.get(1)))
								{
									tempDubs = tempDubs * realValues.get(this.bugFix(vars.size(),i));
									//System.out.println(realValues.get(this.bugFix(vars.size(),i)));
									Double stringInt = new Double(realValues.get(this.bugFix(vars.size(),i)));
									ArrayList<String> copyLearning = (ArrayList<String>) learning.get(i).clone();
									copyLearning.add(stringInt.toString());
									learning.set(i, copyLearning);
									
									Integer stringDouble = new Integer(CPTs.indexOf(cpt));	//table we get from
									ArrayList<String> copyTables = (ArrayList<String>) tables.get(i).clone();
									StringBuilder newString = new StringBuilder();
									newString.append("f" + stringDouble +"(");
									for(int y = 0; y < vars.size(); y++)
									{
										int thing = (int)Math.pow(2, vars.size()-1-y);
										thing = this.bugFix(vars.size(),i) & thing;
										thing = thing >> vars.size()-1-y;
									    newString.append(vars.get(y) + " = " + thing);
									    if(y != vars.size()-1)
									    {
										  newString.append(",");
									    }
									}
									newString.append(")");
									copyTables.add(newString.toString());
									tables.set(i, copyTables);
								}
								else
								{
									tempDubs = tempDubs * realValues.get(i);
									//System.out.println(realValues.get(i));
									Double stringInt = new Double(realValues.get(i));
									ArrayList<String> copyLearning = (ArrayList<String>) learning.get(i).clone();
									copyLearning.add(stringInt.toString());
									learning.set(i, copyLearning);
									
									Integer stringDouble = new Integer(CPTs.indexOf(cpt));	//table we get from
									ArrayList<String> copyTables = (ArrayList<String>) tables.get(i).clone();
									StringBuilder newString = new StringBuilder();
									newString.append("f" + stringDouble +"(");
									for(int y = 0; y < vars.size(); y++)
									{
										int thing = (int)Math.pow(2, vars.size()-1-y);
										thing = i & thing;
										thing = thing >> vars.size()-1-y;
										newString.append(vars.get(y) + " = " + thing);
										if(y != vars.size()-1)
									    {
										  newString.append(",");
									    }
									}
									newString.append(")");
									copyTables.add(newString.toString());
									tables.set(i, copyTables);
								}
								
								Double tempDouble = new Double(tempDubs);
								addThing.set(i, tempDouble);
							}
						}
						thingie++;
					}
					// need to also multiply the variable = 0 and = 1 into the two numbers
					// the only remaining cpts with the variable should only be two rows long
					else
					{
						for(int i = 0; i < thingRows; i++) 
						{
							if(i%2 == 0) //i is even, meaning we want variable = 0
							{
								for(HashMap<Integer,Double> realValues : cpt.values()) //actually being able to access the CPTs
								{
									double tempDubs = addThing.get(i);
									tempDubs = tempDubs * realValues.get(0); //multiply the 0th row to all these rows
									//System.out.println(tempDubs);
									Double tempDouble = new Double(tempDubs);
									addThing.set(i, tempDouble);
									
									Double stringInt = new Double(realValues.get(0));									
									ArrayList<String> copyLearning = (ArrayList<String>) learning.get(i).clone();
									copyLearning.add(stringInt.toString());
									learning.set(i, copyLearning);
									
									Integer stringDouble = new Integer(CPTs.indexOf(cpt));									
									ArrayList<String> copyTables = (ArrayList<String>) tables.get(i).clone();
									copyTables.add("f" + stringDouble +"(" + name + " = 0)");
									tables.set(i, copyTables);
									
								}
							}
							else //i is odd, meaning that we want variable = 1
							{
								for(HashMap<Integer,Double> realValues : cpt.values()) //actually being able to access the CPTs
								{
									double tempDubs = addThing.get(i);
									tempDubs = tempDubs * realValues.get(1); //multiply the 1st row to all these numbers
									//System.out.println(tempDubs);
									Double tempDouble = new Double(tempDubs);
									addThing.set(i, tempDouble);
									
									Double stringInt = new Double(realValues.get(1));
									ArrayList<String> copyLearning = (ArrayList<String>) learning.get(i).clone();
									copyLearning.add(stringInt.toString());
									learning.set(i, copyLearning);
									
									Integer stringDouble = new Integer(CPTs.indexOf(cpt));									
									ArrayList<String> copyTables = (ArrayList<String>) tables.get(i).clone();
									copyTables.add("f" + stringDouble +"(" + name + " = 1)");
									tables.set(i, copyTables);
								}
							}
						}
					}
				}
			}
		}
		/*for(int i = 0; i < thingRows; i++) //just printing out the thing for testing
		{
			System.out.println(addThing.get(i));
		}*/
		

		//do I want/need to show the steps for the user, or does it just need to solve?
		
		//now add the two digits together, create the CPT, and delete the old ones
		
		HashMap<Integer,Double> newHash = new HashMap<Integer,Double>();
		int counter = 0; //maybe would like to know which row we are on
		for(int i = 0; i < thingRows; i += 2) //want to create a new CPT
		{
			double tempDubs = 0;
			//System.out.println(addThing.get(i));
			//System.out.println(addThing.get(i+1));
			tempDubs = addThing.get(i) + addThing.get(i+1);
			Double tempDouble = new Double(tempDubs);
			Integer tempInt = new Integer(counter);
			newHash.put(tempInt, tempDouble);
			counter++;
		}
		//System.out.println(thingRows);
		HashMap<ArrayList<String>,HashMap<Integer,Double>> cptName = new HashMap<ArrayList<String>,HashMap<Integer,Double>>();
		cptName.put(newVars, newHash);
		//for(HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt: CPTs) //want to access the CPTs
		//{
		for(int i = 0; i < CPTs.size(); i++)
		{
			HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt = CPTs.get(i);
			for(ArrayList<String> vars: cpt.keySet()) //search through all of the names of the CPT
			{
				if(vars.contains(name))
				{
					int index = CPTs.indexOf(cpt); //delete all the cpts with the variable name
					CPTs.remove(index); // i guess i could have just deleted with object and not index
					i--;
				}
			}
		}
		CPTs.add(cptName); //add new CPT
		
		//print out the values that made up the CPT
		System.out.println("-----------------------------------------");
		System.out.println("This is how the new CPT is formed");
		System.out.println(cptName.keySet());
		for(int i = 0; i < learning.size(); i++)
		{
			for(int j = 0; j < learning.get(i).size(); j++)
			{
				if(j == 0 && i % 2 == 0)
				{
					System.out.print(i/2);
					System.out.print(" ");
				}
				System.out.print(learning.get(i).get(j));
				if(j != learning.get(i).size()-1)
				{
					System.out.print(" * ");
				}
			}
			if(i % 2 == 0) //add +
			{
				System.out.print(" + ");
			}
			else
			{
				System.out.println("");
			}
		}
		System.out.println("-----------------------------------------");
		
		System.out.println("This is how the new CPT is formed from the previous tables");
		System.out.println(cptName.keySet());
		for(int i = 0; i < tables.size(); i++)
		{
			for(int j = 0; j < tables.get(i).size(); j++)
			{
				if(j == 0 && i % 2 == 0)
				{
					System.out.print(i/2);
					System.out.print(" ");
				}
				System.out.print(tables.get(i).get(j));
				if(j != tables.get(i).size()-1)
				{
					System.out.print(" * ");
				}
			}
			if(i % 2 == 0) //add +
			{
				System.out.print(" + ");
			}
			else
			{
				System.out.println("");
			}
		}
		System.out.println("-----------------------------------------");
		
		//next, do I try and recover the actual rows/columns from the CPTs
		//might be a little hard since I don't number the CPTs
		
	} //end of function
	
	public void printCPTs()
	{
		for(int j = 0; j < this.CPTs.size(); j++)
		{
			System.out.print("f" + j + " ");
			System.out.println(this.CPTs.get(j).keySet());
			for(HashMap<Integer, Double> i: this.CPTs.get(j).values())
			{
				for(Integer m : i.keySet())
				{
					String key = m.toString();
					String value = i.get(m).toString();
					System.out.println(key + " " + value);
				}
			}
			System.out.println("");
		}
	}
	
	public void cheatCPTs(ArrayList<HashMap<ArrayList<String>,HashMap<Integer,Double>>> input)
	{
		CPTs = input;
	}
	
	public static int bugFix(int size, int num)
	{
		int x = num & 1; //get last bit of num
		num = num >> 1; //shift all down
		x = x << size-1; //size is number of variables
		num = num | x;
		return num;
	}
	
	public boolean anotherBugFix(String child,String parent)
	{
		if(net.getNode(child).parents.contains(net.getNode(parent)))
		{
			return true;
		}
		return false;
	}
	
	
	public void endFactor(String name) //a lot of this is copy pasted from the early code
	{
		int rows = 0;
		ArrayList<String> newVars = new ArrayList<String>(); //name of new CPT
		for(HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt: CPTs) //for each of the CPTs
		{
			for(ArrayList<String> vars: cpt.keySet()) //search through all of the names of the CPT
			{
				if(vars.contains(name))
				{
					//need to combine it
					//how can I do it
					//Need to find where the variable = 0 and = 1
					//How do I know how many rows there are?
					//I guess it's only the amount of people in other names of cpts that have not been evidenced yet
					rows += (vars.size() - 1); //-1 to take out the variable that we want gone
					for(int y = 0; y < vars.size(); y++) //if there are still evidence variables in the titles of the CPTs
					{
						if(vars.get(y).contains("="))
						{
							rows--;
						}
						else
						{
							if(!vars.get(y).equals(name))
							{
								newVars.add(vars.get(y));
							}
						}
					}
				}
			}
		}
		if(rows == 0 )
		{
			rows = 1;
		}
		newVars.add(name);
		rows = (int)Math.pow(2, rows);
	    //System.out.println(rows);
		//System.out.println(newVars);
		
		//ok now what
		//should I make 2 different doubles for each side of the addition per row
		ArrayList<Double> addThing = new ArrayList<Double>();
		int thingRows = rows;
		Double one = new Double(1); //don't mind that zero is actually 1
		for(int i = 0; i < thingRows; i++) //filling out the doubles with starting 1
		{
			addThing.add(one);
		}
		
		
		
		for(HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt: CPTs) //for each of the CPTs
		{
			for(ArrayList<String> vars: cpt.keySet()) //search through all of the names of the CPT
			{
				if(vars.contains(name))
				{
					// need to also multiply the variable = 0 and = 1 into the two numbers
					// the only remaining cpts with the variable should only be two rows long

						for(int i = 0; i < thingRows; i++) 
						{
							if(i%2 == 0) //i is even, meaning we want variable = 0
							{
								for(HashMap<Integer,Double> realValues : cpt.values()) //actually being able to access the CPTs
								{
									double tempDubs = addThing.get(i);
									tempDubs = tempDubs * realValues.get(0); //multiply the 0th row to all these rows
									//System.out.println(tempDubs);
									Double tempDouble = new Double(tempDubs);
									addThing.set(i, tempDouble);
								}
							}
							else //i is odd, meaning that we want variable = 1
							{
								for(HashMap<Integer,Double> realValues : cpt.values()) //actually being able to access the CPTs
								{
									double tempDubs = addThing.get(i);
									tempDubs = tempDubs * realValues.get(1); //multiply the 1st row to all these numbers
									//System.out.println(tempDubs);
									Double tempDouble = new Double(tempDubs);
									addThing.set(i, tempDouble);
								}
							}
						}
					
				}
			}
		}
		/*for(int i = 0; i < thingRows; i++) //just printing out the thing for testing
		{
			System.out.println(addThing.get(i));
		}*/
		//do I want/need to show the steps for the user, or does it just need to solve?
		
		//now add the two digits together, create the CPT, and delete the old ones
		
		HashMap<Integer,Double> newHash = new HashMap<Integer,Double>();
		int counter = 0; //maybe would like to know which row we are on
		for(int i = 0; i < thingRows; i++) //want to create a new CPT
		{
			double tempDubs = 0;
			tempDubs = addThing.get(i);
			Double tempDouble = new Double(tempDubs);
			Integer tempInt = new Integer(counter);
			newHash.put(tempInt, tempDouble);
			counter++;
		}
		//System.out.println(thingRows);
		HashMap<ArrayList<String>,HashMap<Integer,Double>> cptName = new HashMap<ArrayList<String>,HashMap<Integer,Double>>();
		cptName.put(newVars, newHash);
		//for(HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt: CPTs) //want to access the CPTs
		//{
		for(int i = 0; i < CPTs.size(); i++)
		{
			HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt = CPTs.get(i);
			for(ArrayList<String> vars: cpt.keySet()) //search through all of the names of the CPT
			{
				if(vars.contains(name))
				{
					int index = CPTs.indexOf(cpt); //delete all the cpts with the variable name
					CPTs.remove(index); // i guess i could have just deleted with object and not index
					i--;
				}
			}
		}
		CPTs.add(cptName); //add new CPT
	}//end of function
	
	public void normalize(String name)
	{
		
		//I only want to see this called with one one by two CPT left, ok?????????
		Integer zero = new Integer(0);
		Integer one = new Integer(1);
		HashMap<ArrayList<String>,HashMap<Integer,Double>> cpt = CPTs.get(0);
		ArrayList<String> whatever = new ArrayList<String>();
		whatever.add(name);
		double total = cpt.get(whatever).get(zero) + cpt.get(whatever).get(one);
		double t0 = cpt.get(whatever).get(zero)/total;
		double t1 = cpt.get(whatever).get(one)/total;
		System.out.println(name + "=0: " + t0);
		System.out.println(name + "=1: " + t1);
	}
	//TODO: Update normalize and endFactor so it isn't hardcoded for just wanting one factor
	// A little difficult to do, since there doesn't seem to be any sources online with more than one factor on variable elimination
	// Not many sources online with any cpts, really
	
}
