package com.valiant;

import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	private static int inf = Integer.MAX_VALUE;

	private int n;
	
	private int[][] V = new int[][]{
			{1, 1, 1, 1, 1, 1},
			{-1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1},
	};
	private int[] v = new int[]{-1, -1, -1, -1};
	private int[][] A = new int[][]{
			{-1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1},
	};
	private int[][] u;

	private int[][] M = new int[this.n][this.n];
	private int[][] P = new int[this.n][this.n];

	private PrintWriter printWriter;

	public static void main(String[] args) {
		new Main();
	}
	
	private Main() {
		super();
		
		printWriter = new PrintWriter(System.out);

		this.fillU();

		this.printWriter.println("=== Step 0 ===");
		this.fillM().fillP().printP().printM();

		this.printWriter.println("=== Step 1 ===");
		this.runStep1().printP().printM();

		this.printWriter.println("=== Step 2 ===");

		this.runStep2();
	}


	private Main runStep1() {
		for (int q = 0; q < this.n - 2; q++) {
			this.printWriter.println("Iteration " + String.valueOf(q + 1) + ":");

			int i = this.getMinStV(q);
			if (i < 0) {
				continue;
			}

			this.v[q] = i;

			this.printWriter.println("v^" + String.valueOf(q + 1) + " = " + "v" + String.valueOf(this.v[q] + 1));

			this.printWriter.print("V" + String.valueOf(q + 1) + " = {");
			for (int vi = 0; vi < this.n; vi++) {
				this.V[q + 1][vi] = this.V[q][vi] < 0 || vi == i ? -1 : 1;
				if (this.V[q + 1][vi] == 1) {
					this.printWriter.print(" v" + String.valueOf(vi + 1));
				}
			}
			this.printWriter.println(" };");

			this.printWriter.print("A" + String.valueOf(q + 1) + " = {");
			for (int ai = 0; ai < this.n; ai++) {
				if (this.V[q][ai] == 1 && (this.M[i][ai] != Main.inf || this.M[ai][i] != Main.inf)) {
					this.A[q + 1][ai] = 1;
					this.printWriter.print(" v" + String.valueOf(ai + 1));
				}
			}
			this.printWriter.println(" };");

			for (int j = 0; j < n; j++) {
				if (this.A[q + 1][j] < 1) {
					continue;
				}

				for (int k = 0; k < n; k++) {
					if (this.A[q + 1][k] < 1 || j == k) {
						continue;
					}

					int W = this.M[j][i] + this.M[i][k];
					if (this.M[j][k] == Main.inf || this.M[j][k] > W) {
						this.M[j][k] = W > Main.inf ? Main.inf : W;
						if (W < Main.inf) {
							this.P[j][k] = i;
						}
					}

					W = this.M[i][j] + this.M[k][i];
					if (this.M[k][j] == Main.inf || this.M[j][k] > W) {
						this.M[k][j] = W > Main.inf ? Main.inf : W;
						if (W < Main.inf) {
							this.P[k][j] = i;
						}
					}
				}
			}

			this.printWriter.println("--------------");
			this.printP().printM();
			this.printWriter.println("--------------");
		}

		return this;
	}
	
	private Main runStep2() {
		for (int q = this.n - 3; q >= 0; q--) {
			int i = this.v[q];
			this.printWriter.println("Iteration " + String.valueOf(q + 1) + ":");

			this.printWriter.println("v^" + String.valueOf(q + 1) + " = v" + String.valueOf(i + 1));
			this.printWriter.print("A" + String.valueOf(q + 1) + " = {");
			for (int ai = 0; ai < this.n; ai++) {
				if (this.A[q + 1][ai] == 1) {
					this.printWriter.print(" v" + String.valueOf(ai + 1));
				}
			}
			this.printWriter.println(" };");

			// Текущие вершины в графе
			int[] vertexes = this.V[q + 1];
			for (int fVertex = 0; fVertex < vertexes.length; fVertex++) {
				if (vertexes[fVertex] < 0) {
					continue;
				}
				for (int nVertex = 0; nVertex < vertexes.length; nVertex++) {
					if (nVertex == fVertex || vertexes[nVertex] < 0) {
						continue;
					}

					int newM;

					// Ищем мин.путь от i до fVertex
					if (this.M[i][nVertex] != Main.inf && this.M[nVertex][fVertex] != Main.inf) {
						newM = Math.min(this.M[i][fVertex], this.M[i][nVertex] + this.M[nVertex][fVertex]);
						if (this.M[i][fVertex] > newM) {
							this.M[i][fVertex] = newM;
							this.P[i][fVertex] = nVertex;
						}
					}

					// Ищем мин.путь от fVertex до i
					if (this.M[nVertex][i] != Main.inf && this.M[fVertex][nVertex] != Main.inf) {
						newM = Math.min(this.M[fVertex][i], this.M[nVertex][i] + this.M[fVertex][nVertex]);
						if (this.M[fVertex][i] > newM) {
							this.M[fVertex][i] = newM;
							this.P[fVertex][i] = nVertex;
						}
					}
				}
			}
			this.printP().printM();
		}
		
		return this;
	}

	private void fillU() {
		Scanner scanner = new Scanner(System.in);
		int scenario;
		do {
			this.printWriter.println("Enter scenario (1-3)");
			scenario = scanner.nextInt();
		} while (scenario >= 1 && scenario <= 3);

		switch (scenario) {
			case 1:
				this.n = 6;
				this.u = new int[][]{
						{Main.inf, 7, 9, 21, Main.inf, 2},
						{7, Main.inf, 10, 15, Main.inf, Main.inf},
						{9, 10, Main.inf, 11, Main.inf, 14},
						{21, 15, 11, Main.inf, 6, Main.inf},
						{Main.inf, Main.inf, Main.inf, 6, Main.inf, 9},
						{2, Main.inf, 14, Main.inf, 9, Main.inf}
				};
				break;
			case 2:
				this.n = 6;
				this.u = new int[][]{
						{Main.inf, Main.inf, 5, 5, 2, 12},
						{Main.inf, Main.inf, Main.inf, Main.inf, Main.inf, 2},
						{Main.inf, 2, Main.inf, Main.inf, Main.inf, Main.inf},
						{Main.inf, 2, Main.inf, Main.inf, Main.inf, Main.inf},
						{Main.inf, Main.inf, 1, 2, Main.inf, Main.inf},
						{Main.inf, Main.inf, Main.inf, Main.inf, Main.inf, Main.inf}
				};
				break;
			case 3:
				this.n = 6;
				this.u = new int[][]{
						{Main.inf, Main.inf, 5, 5, 2, 12},
						{Main.inf, Main.inf, 2, 2, Main.inf, 2},
						{5, 2, Main.inf, Main.inf, 1, Main.inf},
						{5, 2, Main.inf, Main.inf, 2, Main.inf},
						{2, Main.inf, 1, 2, Main.inf, Main.inf},
						{12, 2, Main.inf, Main.inf, Main.inf, Main.inf}
				};

				break;
		}
	}
	

	private int getMinStV(int q) {
		int minSt = Main.inf;
		int v = -1;
		for (int i = 0; i < this.n; i++) {
			if (this.V[q][i] < 1) {
				continue;
			}

			int st = 0;

			for (int k = 0; k < this.n; k++) {
				if (this.V[q][k] < 1) {
					continue;
				}
				if (this.M[i][k] != Main.inf) {
					st++;
				}
				if (this.M[k][i] != Main.inf) {
					st++;
				}
			}

			if (st > 0 && st < minSt) {
				minSt = st;
				v = i;
			}
		}

		return v;
	}

	private Main printP() {
		this.printWriter.println("Matrix P:");
		this.printMatrix(this.P, true);
		return this;
	}

	private Main printM() {
		this.printWriter.println("Matrix M:");
		this.printMatrix(this.M, false);

		return this;
	}

	private void printMatrix(int[][] matrix, boolean isV) {
		for (int i = 0; i < matrix.length; i++) {
			this.printWriter.print(i + 1);
			this.printWriter.print(":\t");
			for (int j = 0; j < matrix[i].length; j++) {
				if (i == j) {
					this.printWriter.print("0");
				} else if (matrix[i][j] >= Main.inf) {
					this.printWriter.print("∞");
				} else if (matrix[i][j] < 0) {
					this.printWriter.print("Ø");
				} else if (isV) {
					this.printWriter.print("v" + String.valueOf(matrix[i][j] + 1));
				} else {
					this.printWriter.print(matrix[i][j]);
				}
				this.printWriter.print("\t");
			}
			this.printWriter.println();
		}
		this.printWriter.println();
	}

	private Main fillM() {
		for (int l = 0; l < this.n; l++) {
			for (int j = 0; j < this.n; j++) {
				M[j][l] = this.u[j][l];
			}
		}
		return this;
	}

	private Main fillP() {
		for (int l = 0; l < this.n; l++) {
			for (int j = 0; j < this.n; j++) {
				if (this.u[j][l] < Main.inf) {
					this.P[j][l] = l;
				} else {
					this.P[j][l] = -1;
				}
			}
		}
		return this;
	}
}
