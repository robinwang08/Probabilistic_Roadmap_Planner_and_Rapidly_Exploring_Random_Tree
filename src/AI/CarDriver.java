package AI;

import java.util.HashMap;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class CarDriver extends Application {
	// default window size
	protected int window_width = 600;
	protected int window_height = 400;

	// Draw a polygon;
	public void addPolygon(Group g, Double[] points) {
		Polygon p = new Polygon();
		p.getPoints().addAll(points);

		g.getChildren().add(p);
	}

	public void plotCarRobot3(Group g, CarRobot car, CarState s) {
		car.set(s);
		double[][] current = car.get();
		Double[] to_add = new Double[2 * current.length];
		for (int j = 0; j < current.length; j++) {
			to_add[2 * j] = current[j][0];
			to_add[2 * j + 1] = window_height - current[j][1];
		}
		Polygon p = new Polygon();
		p.getPoints().addAll(to_add);

		p.setStroke(Color.PURPLE);
		p.setFill(Color.DARKVIOLET);
		g.getChildren().add(p);
	}

	// plot a car robot
	public void plotCarRobot2(Group g, CarRobot car, CarState s) {
		car.set(s);
		double[][] current = car.get();
		Double[] to_add = new Double[2 * current.length];
		for (int j = 0; j < current.length; j++) {
			to_add[2 * j] = current[j][0];
			to_add[2 * j + 1] = window_height - current[j][1];
		}
		Polygon p = new Polygon();
		p.getPoints().addAll(to_add);

		p.setStroke(Color.RED);
		p.setFill(Color.PINK);
		g.getChildren().add(p);
	}

	public void plotCarRobot(Group g, CarRobot car, CarState s) {
		car.set(s);
		double[][] current = car.get();
		Double[] to_add = new Double[2 * current.length];
		for (int j = 0; j < current.length; j++) {
			to_add[2 * j] = current[j][0];
			to_add[2 * j + 1] = window_height - current[j][1];
		}
		Polygon p = new Polygon();
		p.getPoints().addAll(to_add);

		p.setStroke(Color.BLUE);
		p.setFill(Color.LIGHTBLUE);
		g.getChildren().add(p);
	}

	// plot the World with all the obstacles;
	public void plotWorld(Group g, World w) {
		int len = w.getNumOfObstacles();
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 0; i < len; i++) {
			current = w.getObstacle(i);
			to_add = new Double[2 * current.length];
			for (int j = 0; j < current.length; j++) {
				to_add[2 * j] = current[j][0];
				to_add[2 * j + 1] = window_height - current[j][1];
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

		primaryStage.setTitle("2D world");
		Group root = new Group();
		Scene scene = new Scene(root, window_width, window_height);

		primaryStage.setScene(scene);

		Group g = new Group();

		double a[][] = { { 10, 400 }, { 150, 300 }, { 100, 210 } };

		Poly obstacle1 = new Poly(a);



		double b[][] = { { 350, 30 }, { 300, 200 }, { 430, 125 } };



		Poly obstacle2 = new Poly(b);



		double c[][] = { { 110, 220 }, { 250, 380 }, { 320, 220 } };

		Poly obstacle3 = new Poly(c);



		double d[][] = { { 0, 50 }, { 250, 50 }, { 250, 0 }, { 0, 0 } };

		Poly obstacle4 = new Poly(d);



		double e[][] = { { 300, 30 }, { 500, 30 }, { 500, 0 }, { 300, 0 } };

		Poly obstacle5 = new Poly(e);


		

		// Declaring a world;
		World w = new World();
		// Add obstacles to the world;
		w.addObstacle(obstacle1);
		w.addObstacle(obstacle2);
		w.addObstacle(obstacle3);
		w.addObstacle(obstacle4);
		w.addObstacle(obstacle5);

		plotWorld(g, w);

		
		
		CarRobot carz = new CarRobot();
		CarState state1 = new CarState(35, 360, 0);
		CarState state2 = new CarState(550, 90, 65);
		plotCarRobot3(g, carz, state1);
		
		plotCarRobot3(g, carz, state2);
		
		

		RRT rt = new RRT(1000, state1, state2, w, window_width, window_height);

		LinkedList<CarState> path = new LinkedList<CarState>();
		path = rt.generateTree();

		for (CarState check : rt.tree.keySet()) {
			if(check.equals(state1) || check.equals(state2) )
				continue;
			CarRobot car = new CarRobot();
			car.set(check);
			plotCarRobot(g, car, check);
		}
		for (CarState node : path) {

			if(node.equals(state1) || node.equals(state2) )
				continue;
			CarRobot car = new CarRobot();
			car.set(node);
			plotCarRobot2(g, car, node);
		}

		plotCarRobot3(g, carz, state2);

		scene.setRoot(g);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
