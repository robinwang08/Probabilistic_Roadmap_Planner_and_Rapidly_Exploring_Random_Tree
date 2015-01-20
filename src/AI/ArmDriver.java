package AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class ArmDriver extends Application {
	// default window size
	protected int window_width = 600;
	protected int window_height = 400;
	
	public void addPolygon(Group g, Double[] points) {
		Polygon p = new Polygon();
	    p.getPoints().addAll(points);
	    
	    g.getChildren().add(p);
	}
	
	// plot a ArmRobot;
	public void plotArmRobot(Group g, ArmRobot arm, double[] config) {
		arm.set(config);
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 1; i <= arm.getLinks(); i++) {
			current = arm.getLinkBox(i);
			
			
			to_add = new Double[2*current.length];
			for (int j = 0; j < current.length; j++) {
				System.out.println(current[j][0] + ", " + current[j][1]);
				to_add[2*j] = current[j][0];
				//to_add[2*j+1] = current[j][1];
				to_add[2*j+1] = window_height - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			p.setStroke(Color.BLUE);
			p.setFill(Color.LIGHTBLUE);
			g.getChildren().add(p);
		}
		
	}
	
	public void plotArmRobot2(Group g, ArmRobot arm, double[] config) {
		arm.set(config);
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 1; i <= arm.getLinks(); i++) {
			current = arm.getLinkBox(i);
			
			
			to_add = new Double[2*current.length];
			for (int j = 0; j < current.length; j++) {
				System.out.println(current[j][0] + ", " + current[j][1]);
				to_add[2*j] = current[j][0];
				//to_add[2*j+1] = current[j][1];
				to_add[2*j+1] = window_height - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			p.setStroke(Color.RED);
			p.setFill(Color.PINK);
			g.getChildren().add(p);
		}
		
	}
	
	public void plotArmRobot3(Group g, ArmRobot arm, double[] config) {
		arm.set(config);
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 1; i <= arm.getLinks(); i++) {
			current = arm.getLinkBox(i);
			
			
			to_add = new Double[2*current.length];
			for (int j = 0; j < current.length; j++) {
				System.out.println(current[j][0] + ", " + current[j][1]);
				to_add[2*j] = current[j][0];
				//to_add[2*j+1] = current[j][1];
				to_add[2*j+1] = window_height - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			p.setStroke(Color.PURPLE);
			p.setFill(Color.LAVENDER);
			g.getChildren().add(p);
		}
		
	}
	
	
	public void plotWorld(Group g, World w) {
		int len = w.getNumOfObstacles();
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 0; i < len; i++) {
			current = w.getObstacle(i);
			to_add = new Double[2*current.length];
			for (int j = 0; j < current.length; j++) {
				to_add[2*j] = current[j][0];
				//to_add[2*j+1] = current[j][1];
				to_add[2*j+1] = window_height - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			g.getChildren().add(p);
		}
	}
	
	// The start function; will call the drawing;
	// You can run your PRM or RRT to find the path; 
	// call them in start; then plot the entire path using
	// interfaces provided;
	@Override
	public void start(Stage primaryStage) {
		
		
		// setting up javafx graphics environments;
		primaryStage.setTitle("CS 76 2D world");

		Group root = new Group();
		Scene scene = new Scene(root, window_width, window_height);

		primaryStage.setScene(scene);
		
		Group g = new Group();

		// setting up the world;
		
		// creating polygon as obstacles;
/*		

		double a[][] = {{200, 200}, {200, 250}, {300, 250}, {300, 200}};
		Poly obstacle1 = new Poly(a);
		
		double b[][] = {{200, 300}, {200, 400}, {300, 400}, {300, 300}};
		Poly obstacle2 = new Poly(b);
		
		double c[][] = {{200, 1}, {200, 70}, {300, 70}, {300, 1}};
		Poly obstacle3 = new Poly(c);
		
		
		double d[][] = {{0,0},{0, 400},{1,400},{1,0}};
		Poly obstacle4 = new Poly(d);
		
		double e[][] = {{0,0},{0,1},{600,1}, {600,0}};
		Poly obstacle5 = new Poly(e);
		
		double f[][] = {{0,400},{600,400}, {600,399},{0,399}};
		Poly obstacle6 = new Poly(f);
		
		double h[][] = {{600,0},{599,0},{599,400}, {600,400}};
		Poly obstacle7 = new Poly(h);
		
		*/
		
		double a[][] = { { 0, 250 }, { 0, 300 }, { 400, 300 }, {400, 250}};
		Poly obstacle1 = new Poly(a);


		double b[][] = { { 600, 100 }, { 600, 150 }, { 250, 150 }, {250,100} };



		Poly obstacle2 = new Poly(b);



		double c[][] = { { 250, 0 }, { 250, 400 }, { 350, 400 }, {350,0}};

		Poly obstacle3 = new Poly(c);



		//double d[][] = { { 0, 50 }, { 250, 50 }, { 250, 0 }, { 0, 0 } };

		//Poly obstacle4 = new Poly(d);



		//double e[][] = { { 300, 30 }, { 500, 30 }, { 500, 0 }, { 300, 0 } };

		//Poly obstacle5 = new Poly(e);


		
		// Declaring a world; 
		World w = new World();
		// Add obstacles to the world;
		w.addObstacle(obstacle1);
		w.addObstacle(obstacle2);
	//w.addObstacle(obstacle3);
	//w.addObstacle(obstacle4);
	//w.addObstacle(obstacle5);
	//w.addObstacle(obstacle6);
	//w.addObstacle(obstacle7);
		
		plotWorld(g, w);
	
		double[] config1 = {50, 350, 80, 0, 80, 0};
		double[] config2 = {420, 20, 80, 0, 80, 0};
		
		ArmRobot arm1 = new ArmRobot(2);
		plotArmRobot3(g, arm1, config1);
		plotArmRobot3(g, arm1, config2);
	
		
		PRM prm = new PRM(500,15,2,config1,config2,w,window_width,window_height);
		
		LinkedList<ArmRobot> list = new LinkedList<ArmRobot>();
		
		HashMap<ArmRobot, Set<ArmRobot>> check = prm.generateMap();
		
		
		
		for(ArmRobot node1:check.keySet() ){
			ArmRobot arm = new ArmRobot(2);
			plotArmRobot(g, arm, node1.get());
		}
	
		
		list = prm.astar(check);
		
		for(ArmRobot node2:list ){
			ArmRobot arm2 = new ArmRobot(2);
			plotArmRobot2(g, arm2, node2.get());
		}
		
		
		
		
	/*
		ArmRobot arm = new ArmRobot(2);
			
		arm.set(config2);
		
		// Plan path between two configurations;
		ArmLocalPlanner ap = new ArmLocalPlanner();
		// get the time to move from config1 to config2;
		double time = ap.moveInParallel(config1, config2);
		System.out.println(time);
		
		arm.set(config2);
		
		boolean result;
		result = w.armCollisionPath(arm, config1, config2);
		System.out.println(result);
		// plot robot arm
		
		
		
		//Change this part here
		
		plotArmRobot(g, arm, config2);
		plotArmRobot(g, arm, config1);    
		
		*/
		
		
	    scene.setRoot(g);
	    primaryStage.show();
		

	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
