package AI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class PRM {
	private int N;
	private int k;
	private int links;

	private double width;
	private double height;

	private double[] start;
	private double[] goal;

	private World theWorld;

	private ArmLocalPlanner planner;

	public ArmRobot sRobot, gRobot;

	// Constructor
	public PRM(int num1, int num2, int num3, double[] sConfig,
			double[] gConfig, World world, double wid, double hei) {
		// ArmPlanner
		planner = new ArmLocalPlanner();
		// The World objected created in the Driver
		theWorld = world;
		// Number of random configurations
		N = num1;
		// Number of neighbors
		k = num2;
		// Number of links for each ArmRobot
		links = num3;
		// Start ArmRobot
		start = sConfig;
		// Goal ArmRobot
		goal = gConfig;
		// Width and Height of the window
		width = wid;
		height = hei;
		
		
		sRobot = new ArmRobot(links);
		gRobot = new ArmRobot(links);
		sRobot.set(start);
		gRobot.set(goal);
		gRobot.setGoal(gRobot);
		sRobot.setGoal(gRobot);
	}

	public HashMap<ArmRobot, Set<ArmRobot>> generateMap() {
		// Hashmap to hold the vertices and their k neighbors
		HashMap<ArmRobot, Set<ArmRobot>> roadmap = new HashMap<ArmRobot, Set<ArmRobot>>();
		// Initialize start and end ArmRobots
		
		// Add in start and end ArmRobots to the roadmap
		roadmap.put(sRobot, null);
		roadmap.put(gRobot, null);
		// Generate N random configurations
		while (roadmap.size() < N + 2) {
			double[] configs = { Math.random() * width, Math.random() * height,
					80, Math.random() * 2 * Math.PI, 80,
					Math.random() * 2 * Math.PI };
			ArmRobot robot1 = new ArmRobot(links);
			robot1.set(configs);
			// Set the goal state for each generated ArmRobots
			robot1.setGoal(gRobot);
			// Check to see if the generated ArmRobot collides with any obstacle
			if (!theWorld.armCollision(robot1)) {
				// Add the configuration to the roadmap
				roadmap.put(robot1, null);
			}
		}

		// Getting the k nearest neighbors
		for (ArmRobot key1 : roadmap.keySet()) {
			// Used my own comparator for the priority queue
			RobotComparator comparator = new RobotComparator();
			comparator.setRobot(key1);
			// Priority queue to hold the closest neighbors
			PriorityQueue<ArmRobot> queue = new PriorityQueue<ArmRobot>(k,
					comparator);
			// Set of k neighbors
			Set<ArmRobot> neighbors = new HashSet<ArmRobot>();
			for (ArmRobot key2 : roadmap.keySet()) {
				// Don't want to have a node be its own neighbor
				if (key2.equals(key1))
					continue;
				// Check if the neighbor is reachable
				ArmRobot roboTest = new ArmRobot(links);
				if (!theWorld
						.armCollisionPath(roboTest, key1.get(), key2.get()))
					queue.add(key2);
			}
			// Add all into queue; no for loop
			for (int i = 0; i < k; i++) {
				// Add the k neighbors
				if(queue.peek() != null){
				neighbors.add(queue.poll());
				}
			}
			roadmap.put(key1, neighbors);
		}
		return roadmap;
	}


	public LinkedList<ArmRobot> astar(HashMap<ArmRobot, Set<ArmRobot>> map) {
		// Priority queue and hash maps to help with our search
		PriorityQueue<ArmRobot> frontier = new PriorityQueue<ArmRobot>();
		HashMap<ArmRobot, Double> explored = new HashMap<ArmRobot, Double>();
		HashMap<ArmRobot, ArmRobot> parents = new HashMap<ArmRobot, ArmRobot>();
		// Add the start robot to the frontier
		frontier.add(sRobot);
		sRobot.setCost(0);
		explored.put(sRobot, sRobot.priority());
		parents.put(sRobot, null);
		while (!frontier.isEmpty()) {
			// Get the front of the priority queue
			ArmRobot blah = frontier.poll();
			// Get rid of duplicates
			if (explored.containsKey(blah)) {
				if (explored.get(blah) < blah.priority())
					continue;
			}
			// Goal test
			if (blah.equals(gRobot)) {
				return backchainz(blah, parents);
			}
			//Get neighboring configurations
			Set<ArmRobot> successors = map.get(blah);			
			for (ArmRobot node : successors) {
				// Set goal to be our goal
				node.setGoal(gRobot);
				// Set the cost of the configuration
				node.setCost(blah.getCost() + planner.moveInParallel(blah.get(), node.get()));
				// Check and mark duplicate
				if (!explored.containsKey(node)) {
					frontier.add(node);
					explored.put(node, node.priority());
					parents.put(node, blah);
				} else if (explored.containsKey(node)) {
					if (explored.get(node) > node.priority()) {
						explored.put(node, node.priority());
						parents.put(node, blah);
						frontier.add(node);
					}
				}

			}

		}
		return null;
	}
	
	
	
	
	
	public LinkedList<ArmRobot> BFS(HashMap<ArmRobot, Set<ArmRobot>> map) {
		// Fringe
		Queue<ArmRobot> fringe = new LinkedList<ArmRobot>();
		// Map to store backchain information
		HashMap<ArmRobot, ArmRobot> reachedFrom = new HashMap<ArmRobot, ArmRobot>();
		// Start node has no parent, add to fringe
		reachedFrom.put(sRobot, null);
		fringe.add(sRobot);
		while (!fringe.isEmpty()) {
			// Get node from fringe
			ArmRobot currentNode = fringe.remove();
			// Goal test
			if (currentNode.equals(gRobot)) {
				return backchainz(currentNode, reachedFrom);
			}
			// Get the set of neighbors of the current node
			Set<ArmRobot> successors = map.get(currentNode);
			//Check each neighbor
			for (ArmRobot node : successors) {
				// If not visited
				if (!reachedFrom.containsKey(node)) {
					reachedFrom.put(node, currentNode);
					fringe.add(node);
				}
			}
		}
		//No solution
		return null;
	}

	protected LinkedList<ArmRobot> backchainz(ArmRobot node,
			HashMap<ArmRobot, ArmRobot> parent) {

		LinkedList<ArmRobot> solution = new LinkedList<ArmRobot>();
		solution.addFirst(node);

		while (parent.get(node) != null) {
			solution.addFirst(parent.get(node));
			node = parent.get(node);
		}
		return solution;
	}

	public class RobotComparator implements Comparator<ArmRobot> {
		private ArmRobot mainRobot;
		public void setRobot(ArmRobot a) {
			mainRobot = a;
		}
		@Override
		public int compare(ArmRobot x, ArmRobot y) {
			ArmLocalPlanner ap = new ArmLocalPlanner();
			double xx, yy;
			// Time as a measure of closeness
			xx = ap.moveInParallel(mainRobot.get(), x.get());
			yy = ap.moveInParallel(mainRobot.get(), y.get());
			if (xx > yy)
				return 1;
			if (xx < yy)
				return -1;
			return 0;
		}
	}

}