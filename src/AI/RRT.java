package AI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class RRT {
	private int k;
	public double width;
	public double height;
	private CarState start;
	private CarState goal;
	private World theWorld;
	public SteeredCar sc;
	private double time;
	HashMap<CarState, CarState> tree;

	// Constructor
	public RRT(int num2, CarState sConfig, CarState gConfig, World world,
			double wid, double hei) {
		theWorld = world;
		k = num2;
		width = wid;
		height = hei;
		start = sConfig;
		goal = gConfig;
		sc = new SteeredCar();
		time = 1.0;

	}

	public LinkedList<CarState> generateTree() {
		// Hash map to hold the generated tree
		tree = new HashMap<CarState, CarState>();
		// Counter
		int kk = 0;
		// Put in the start node
		tree.put(start, null);
		// Before the counter hits k
		while (kk < k) {
			// Generate random CarState
			// A Probability of choosing the goal state
			CarState rando = new CarState();
			if (Math.random() > 0.75) {
				rando = goal;
			} else {
				rando = new CarState((Math.random() * width),
						(Math.random() * height), (Math.random() * Math.PI));
			}

			// Find closest vertex in tree to the generated state
			CarState current = new CarState();
			double closestN = Double.MAX_VALUE;
			for (CarState node : tree.keySet()) {
				if (euclid(node, rando) < closestN) {
					current = node;
					closestN = euclid(node, rando);
				}
			}

			// Out of the 6 ctrls, find one that is closest to the generated
			// state
			CarState closeCS = new CarState();
			double closest = Double.MAX_VALUE;
			CarRobot p = new CarRobot();

			// Generate the 6 ctrls
			for (int i = 0; i < 6; i++) {
				CarState next = sc.move(current, i, time);
				p.set(next);
				// Make sure there is no collision
				if (!theWorld.carCollision(p)) {
					if (!theWorld.carCollisionPath(current, i, time)) {
						if (euclid(next, rando) < closest) {
							closest = euclid(next, rando);
							closeCS = next;
						}
					}
				}
			}
			// Add in the new state to tree
			tree.put(closeCS, current);
			// Increment the counter
			kk++;
		}

		// Backchaining Portion
		LinkedList<CarState> solution = new LinkedList<CarState>();

		// Find node closest to goal
		CarState Cgoal = null;
		double closestG = Double.MAX_VALUE;
		for (CarState node : tree.keySet()) {
			if (euclid(node, goal) < closestG) {
				Cgoal = node;
				closestG = euclid(node, goal);
			}
		}
		// Node closest to goal
		CarState node = Cgoal;

		// Back chain from this node
		while (node != null) {
			solution.addFirst(node);
			node = tree.get(node);
		}
		// Solution path
		return solution;
	}

	public double euclid(CarState a, CarState b) {
		double dist = 0.0;
		// Constant multipler for the delta theta value
		double constant = 3.0;
		double diffX = a.getX() - b.getX();
		double diffY = a.getY() - b.getY();
		double diffT = a.getTheta() - b.getTheta();
		// Distance in x and y
		dist = Math.sqrt(diffX * diffX + diffY * diffY);
		// Add in the angular difference
		dist = dist + Math.abs(diffT) * constant;
		return dist;
	}

}