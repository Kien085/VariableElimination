package BayesNet;

import Algorithm.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public class ProperTest {

	public static void main(String[]args) {
		BayesNet net = new BayesNet();
		RandomVariable[] noParents = new RandomVariable[0];
		net.addNode("T", noParents);
		net.addNode("F", noParents);
		net.addNode("S", new RandomVariable[] {net.getNode("F")});
		net.addNode("A", new RandomVariable[] {net.getNode("T"),net.getNode("F")});
		net.addNode("L", new RandomVariable[] {net.getNode("A")});
		net.addNode("R", new RandomVariable[] {net.getNode("L")});
		
		VariableElim testing = new VariableElim(net);
		
		ArrayList<HashMap<ArrayList<String>,HashMap<Integer,Double>>> newCPTList = new ArrayList<HashMap<ArrayList<String>,HashMap<Integer,Double>>>();
		HashMap<ArrayList<String>,HashMap<Integer,Double>> newCPT1 = new HashMap<ArrayList<String>,HashMap<Integer,Double>>();
		ArrayList<String> list1 = new ArrayList<String>();
		list1.add("T");
		HashMap<Integer,Double> map1 = new HashMap<Integer,Double>();
		Integer one = new Integer(1);
		Integer two = new Integer(2);
		Integer three = new Integer(3);
		Integer four = new Integer(4);
		Integer five = new Integer(5);
		Integer six = new Integer(6);
		Integer seven = new Integer(7);
		Integer zero = new Integer(0);
		Double t0 = new Double(.98);
		Double t1 = new Double(.02);
		map1.put(zero, t0);
		map1.put(one, t1);
		newCPT1.put(list1, map1); //T finished
		newCPTList.add(newCPT1);
		
		//starting F
		HashMap<ArrayList<String>,HashMap<Integer,Double>> newCPT2 = new HashMap<ArrayList<String>,HashMap<Integer,Double>>();
		ArrayList<String> list2 = new ArrayList<String>();
		list2.add("F");
		HashMap<Integer,Double> map2 = new HashMap<Integer,Double>();
		Double f0 = new Double(.99);
		Double f1 = new Double(.01);
		map2.put(zero, f0);
		map2.put(one, f1);
		newCPT2.put(list2, map2);
		newCPTList.add(newCPT2);
		
		//starting ATF
		HashMap<ArrayList<String>,HashMap<Integer,Double>> newCPT3 = new HashMap<ArrayList<String>,HashMap<Integer,Double>>();
		ArrayList<String> list3 = new ArrayList<String>();
		HashMap<Integer,Double> map3 = new HashMap<Integer,Double>();
		list3.add("A");
		list3.add("T");
		list3.add("F");
		Double atf000 = new Double(.9999);
		Double atf100 = new Double(.0001);
		Double atf001 = new Double(.01);
		Double atf101 = new Double(.99);
		Double atf010 = new Double(.15);
		Double atf110 = new Double(.85);
		Double atf011 = new Double(.5);
		Double atf111 = new Double(.5);
		
		map3.put(zero, atf000);
		map3.put(one, atf001);
		map3.put(two, atf010);
		map3.put(three, atf011);
		map3.put(four, atf100);
		map3.put(five, atf101);
		map3.put(six, atf110);
		map3.put(seven, atf111);
		newCPT3.put(list3, map3);
		newCPTList.add(newCPT3);
		
		//starting RL
		HashMap<ArrayList<String>,HashMap<Integer,Double>> newCPT4 = new HashMap<ArrayList<String>,HashMap<Integer,Double>>();
		ArrayList<String> list4 = new ArrayList<String>();
		HashMap<Integer,Double> map4 = new HashMap<Integer,Double>();
		list4.add("R");
		list4.add("L");
		Double rl00 = new Double(.99);
		Double rl10 = new Double(.01);
		Double rl01 = new Double(.25);
		Double rl11 = new Double(.75);
		
		map4.put(zero, rl00);
		map4.put(one, rl01);
		map4.put(two, rl10);
		map4.put(three, rl11);
		newCPT4.put(list4, map4);
		newCPTList.add(newCPT4);
		
		//starting SF
		HashMap<ArrayList<String>,HashMap<Integer,Double>> newCPT5 = new HashMap<ArrayList<String>,HashMap<Integer,Double>>();
		ArrayList<String> list5 = new ArrayList<String>();
		HashMap<Integer,Double> map5 = new HashMap<Integer,Double>();
		list5.add("S");
		list5.add("F");
		Double sf00 = new Double(.99);
		Double sf10 = new Double(.01);
		Double sf01 = new Double(.1);
		Double sf11 = new Double(.9);
		
		map5.put(zero, sf00);
		map5.put(one, sf01);
		map5.put(two, sf10);
		map5.put(three, sf11);
		newCPT5.put(list5, map5);
		newCPTList.add(newCPT5);
		
		//starting LA
		HashMap<ArrayList<String>,HashMap<Integer,Double>> newCPT6 = new HashMap<ArrayList<String>,HashMap<Integer,Double>>();
		ArrayList<String> list6 = new ArrayList<String>();
		HashMap<Integer,Double> map6 = new HashMap<Integer,Double>();	
		list6.add("L");
		list6.add("A");
		
		Double la00 = new Double(.999);
		Double la10 = new Double(.001);
		Double la01 = new Double(.12);
		Double la11 = new Double(.88);
		
		map6.put(zero, la00);
		map6.put(one, la01);
		map6.put(two, la10);
		map6.put(three, la11);
		newCPT6.put(list6, map6);
		newCPTList.add(newCPT6);
		
		testing.cheatCPTs(newCPTList);
		testing.printCPTs();
		
		System.out.println("Eliminating Evidence");
		testing.doEvidence("S", 1);
		testing.doEvidence("R", 1);
		testing.printCPTs();
		
		System.out.println("L Elim");
		testing.doElim("L");
		testing.printCPTs();
		
		System.out.println("A Elim");
		testing.doElim("A");
		testing.printCPTs();
		
		System.out.println("F Elim");
		testing.doElim("F");
		testing.printCPTs();
		
		System.out.println("T Finish");
		testing.endFactor("T");
		testing.printCPTs();
		
		testing.normalize("T");
	}
	
	
}
