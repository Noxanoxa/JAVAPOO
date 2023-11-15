package tp1;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

public class AStar {

	public static final int DIAGONAL_COST = 1;
	public static final int V_H_COST = 1;

	private Cell[][] grid;
//	I Define a Priority Queue For Open Cells
//	Open Cells : The Set Of Nodes To Be Evaluated
//	I put Cells With Lowest Cost In First 
	private PriorityQueue<Cell> openCells;
	// Closed Cells : The Set Of Nodes Already Evaluated
	private boolean[][] closedCells;
	// Start Cell
	private int startI, startJ;
	// End Cell
	private int endI, endJ;

	public AStar(int width, int height, int si, int sj, int ei, int ej, int[][] blocks) {
		grid = new Cell[width][height];
		closedCells = new boolean[width][height];
		openCells = new PriorityQueue<Cell>((Cell c1, Cell c2) -> {
			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		});

		startCell(si, sj);
		endCell(ei, ej);

		// init heuristic and Cells
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
				grid[i][j].solution = false;
			}
		}
		grid[startI][startJ].finalCost = 0;

		// We Put Blocks On The Grid
		for (int i = 0; i < blocks.length; i++) {
			addBlockOnCell(blocks[i][0], blocks[i][1]);
		}
	}

	public void addBlockOnCell(int i, int j) {
		grid[i][j] = null;
	}

	public void startCell(int i, int j) {
		startI = i;
		startI = j;
	}

	public void endCell(int i, int j) {
		endI = i;
		endI = j;
	}

	public void updateCostIfNeeded(Cell current, Cell t, int cost) {
		if (t == null || closedCells[t.i][t.j]) {
			return;
		}
		int tFinalCost = t.heuristicCost + cost;
		boolean isOpen = openCells.contains(t);
		if (!isOpen || tFinalCost < t.finalCost) {
			t.finalCost = tFinalCost;
			t.parent = current;
			if (!isOpen) 
				openCells.add(t);
			
		}
	}

	public void process() {
		//we add the start location to open list
		openCells.add(grid[startI][startJ]);
		Cell current;
		
		
		
		
		while (true) {
			current = openCells.poll();

			if (current == null) {
				break;
			}
			// if the code doesn't work there is errors here
			closedCells[current.i][current.j] = true;
			
			if (current.equals(grid[endI][endJ])) {
				return;
			}
			Cell t;

			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);

				if (current.j - 1 >= 0) {
					t = grid[current.i - 1][current.j - 1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);

				}
				if (current.j + 1 < grid[0].length) {
					t = grid[current.i - 1][current.j + 1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);

				}
			}

			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);

			}

			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
			}

			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);

				if (current.j - 1 >= 0) {
					t = grid[current.i + 1][current.j - 1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);

				}
				if (current.j + 1 < grid[0].length) {
					t = grid[current.i + 1][current.j + 1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);

				}

			}

		}
	}

	public void display() {
		System.out.println("Grid :");
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (i == startI && j == startJ) 
					System.out.print("SO  ");// Source Cell
				else if (i == endI && j == endJ) 
					System.out.print("DE  "); // Destination Cell
				 else if (grid[i][j] != null) 
					System.out.printf("%-3d", 0);
				 else
					System.out.print("BL  ");// Block Cell
			}
			System.out.println();
		}
		System.out.println();
	}

	public void displayScores() {
		System.out.println("\nScores For Cells : ");

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {

				if (grid[i][j] != null)
					System.out.printf("%-3d", grid[i][j].finalCost);
				else
					System.out.print("BL  ");
			}
			
			System.out.println();
		}
		
		System.out.println();
	}

	public void displaySolution() {
		if (closedCells[endI][endJ]) {
			System.out.println("Path : ");
			Cell current = grid[endI][endJ];
			System.out.println(current);
			grid[current.i][current.j].solution = true;

			while (current.parent != null) {
				System.out.print(" -> " + current.parent);
				grid[current.parent.i][current.parent.j].solution = true;
				current = current.parent;
			}
			
			System.out.println("\n");

			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					if (i == startI && j == startJ) {
						System.out.print("SO  ");// Source Cell
					} else if (i == endI && j == endJ) {
						System.out.print("DE  "); // Destination Cell
					} else if (grid[i][j] != null) {
						System.out.printf("%-3s", grid[i][j].solution ? "X" : "0");
					} else
						System.out.print("BL  ");// Block Cell
				}
				
				System.out.println();
				
			}
			System.out.println();
		} else
			System.out.println("No Possible Path");
	}

	public static void main(String[] args) {
		int x,y,z;
		Random r = new Random();
		x = r.nextInt(4);
		System.out.println(x);
		y = r.nextInt(10);
		System.out.println(y);
		z = r.nextInt(3);
		System.out.println(z);
		
		AStar astar = new AStar(y, y, z, z, x, x,
				new int[][] { { 0, 4 }, { 2, 2 }, { 3, 1 }, { 3, 3 }, { 2, 1 }, { 2, 3 } });
		
		
		AStar astar2 = new AStar(5, 5, 0, 0, 3, 3,
				new int[][] { { 0, 4 }, { 2, 2 }, { 3, 1 }, { 3, 3 }, { 2, 1 }, { 2, 3 } });
		astar.display();
		astar.process();// Apply A* Algorithm
		astar.displayScores();// Display Scores On Grid
		astar.displaySolution();// Display Solution Path
//		astar2.display();
//		astar2.process();// Apply A* Algorithm
//		astar2.displayScores();// Display Scores On Grid
//		astar2.displaySolution();// Display Solution Path
	}

}
