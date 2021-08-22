
/* 
 I, Avishay Mamrud (315746560), assert that the work I submitted is entirely my own.
 I have not received any part from any other student in the class, nor did I give parts of it for use to others.
 I realize that if my work is found to contain code that is not originally my own, a
 formal case will be opened against me with the BGU disciplinary committee.
*/

// Last Update: dd/mm/yy

public class Assignment2 {

	
	/*--------------------------------------------------------
	   Part a: instance representation & Solution verification 
	  -------------------------------------------------------
	 */
	
	// Task 1
	public static boolean hasFlight(int[][] flights, int i, int j) { 
		// Add your code here
		boolean hasFlight = false;
		for(int x=0 ; x<flights[i].length & !hasFlight ; x=x+1)
			hasFlight = (flights[i][x] == j);
		return hasFlight; 
	}
	
	// Task 2
	public static boolean isLegalInstance(int[][] flights) {
		// Add your code here
		boolean isLegal = true;
		if(flights == null) isLegal=false;
		for(int i = 0;isLegal && i<flights.length;i=i+1){
			if(flights[i]== null) isLegal=false;
			for(int j=0;isLegal & j<flights[i].length;j=j+1){
				isLegal=(flights[i][j] != i & flights[i][j] < flights.length);
				if(isLegal) {
					isLegal=false;//negative so it can enter the loop
					for(int k=0;k<flights[flights[i][j]].length & !isLegal;k=k+1)				
						isLegal=(flights[flights[i][j]][k]==i);//TRUE force exit loop and enter again j++
				}
				
			}
		}
		return isLegal; 
	}
	
	// Task 3
	public static boolean isSolution(int[][] flights, int[] tour) {
		// Add your code here
		if(tour==null || tour.length!=flights.length) 
			throw new IllegalArgumentException();
		for (int i = 0;i<tour.length;i=i+1) 
			if(tour[i]>flights.length | tour[i]<0) 
				throw new IllegalArgumentException();
		boolean isSolution = false;
		for (int x=0;!isSolution & x<flights[tour[tour.length-1]].length;x=x+1) 
				isSolution=(flights[tour[tour.length-1]][x]==tour[0]);
		//check weather the last station comeback to the first city
		for(int k=1;k<tour.length & isSolution;k=k+1) {
			isSolution= false;
			for(int j=0;j<flights[tour[k-1]].length & !isSolution;j=j+1) {
				if(flights[tour[k-1]][j]==tour[k]) 
					isSolution=true;
			}
		}//check all others
		return isSolution;
	}
	
	/*------------------------------------------------------
	  Part b: Express the problem as a CNF formula, solve 
	  it with a SAT solver, and decode the solution
	  from the satisfying assignment
	  -----------------------------------------------------
	 */
	
	// Task 4
	public static int[][] atLeastOne(int[] vars) {
		// Add your code here
		int[][] cnf = new int[1][vars.length];
		cnf[0] = vars;
		return cnf;
	}
	
	// Task 5
	public static int[][] atMostOne(int[] vars){
		// Add your code here
		int n = vars.length;
		int cellToFill = 0;
		int[][] cnf = new int[(n*(n-1))/2][2];//num combs of pairs
		for(int i = 0;i<vars.length;i=i+1){
			for(int j = (i+1);j<vars.length;j=j+1,cellToFill=cellToFill+1){
				cnf[cellToFill][0]= -vars[i];
				cnf[cellToFill][1]= -vars[j];
			}
		}
		return cnf;
	}

	// Task 6
	public static int[][] append(int[][] arr1, int[][] arr2) {
		// Add your code here
		int[][] append;
			append = new int[arr1.length + arr2.length][];
			for(int i=0;i<arr1.length;i=i+1) {
				int[] temp = new int[arr1[i].length];
				for(int j=0;j<arr1[i].length;j=j+1) 
					temp[j]=arr1[i][j];
				append[i] = temp;
			}
			for(int x=0;x<arr2.length;x=x+1) {
				int[] temp = new int[arr2[x].length];
				for(int y=0;y<arr2[x].length;y=y+1) 
					temp[y] = arr2[x][y];
				append[x+arr1.length]=temp;
			}
		return append;
	}
	
	// Task 7
	public static int[][] exactlyOne(int[] vars){
		// Add your code here
		int n = vars.length;
		int[][] cnf = new int[1+((n*(n-1))/2)][];
		cnf = append(atLeastOne(vars),atMostOne(vars));
		return cnf;
	}
	
	// Task 8
	public static int[][] diff(int[] I1, int[] I2){
		// Add your code here
		int[][] cnf = new int[I1.length][2];
		for(int i=0;i<I1.length;i=i+1) {
			cnf[i][0]=-I1[i];
			cnf[i][1]=-I2[i];
		}//negative pairs of vars with matching index force each array to be different
		return cnf;
	}

	// Task 9
	public static int[][] createVarsMap(int n) {
		// Add your code here
		int[][] map = new int[n][n];
		int k=1;
		for(int i=0;i<n;i=i+1) {
			for(int j=0;j<n;j=j+1,k=k+1) 
				map[i][j] = k;
		}
		return map;
	}

	// Task 10
	public static int[][] declareInts(int[][] map) {
		// Add your code here
		int[][] cnf = new int[0][];
		for(int i=0;i<map.length;i=i+1) 
			cnf = append(cnf,exactlyOne(map[i]));
		return cnf;
	}
	
	// Task 11
	public static int[][] allDiff(int[][] map) {
		// Add your code here
		int[][] cnf = new int[0][];
		for(int i = 0;i<map.length;i=i+1){
			for(int j=(i+1);j<map.length;j=j+1)
				cnf = append(cnf,diff(map[i],map[j]));//force every line in map to be differant
		}
		return cnf;
	}

	// Task 12
	public static int[][] allStepsAreLegal(int[][] flights, int[][] map) {
		// Add your code here
		int n= flights.length;
		int[][] cnf = new int[0][];
		for(int j=0;j<n;j=j+1) {
			for(int k=0;k<n;k=k+1) {
				if(j!=k && !hasFlight(flights, j, k)) {//no need to take care of j==k because of task 11
					int[][] temp = new int[n][2];//array of every flight from j to k (negative)
					for(int x=0;x<(n-1);x=x+1) {
						temp[x][0] = -map[x][j];
						temp[x][1] = -map[x+1][k];
					}
					temp[n-1][0] = -map[n-1][j];
					temp[n-1][1] = -map[0][k];
					cnf=append(cnf,temp);//add clause that prevents SAT from suggesting this flights
				}
			}
		}
		return cnf;
	}
	
	// Task 13
	public static void encode(int[][] flights, int[][] map) {
		// Add your code here
		int x=1;
		if(map.length!=flights.length)  throw new IllegalArgumentException();
		for(int i=0;i<map.length;i=i+1) {
			if(map[i].length!=flights.length) throw new IllegalArgumentException();
			for(int j=0;j<map[i].length;j=j+1,x=x+1) {
				if(map[i][j] != x) throw new IllegalArgumentException();
			}
		}
		int[][] cnf = null;
		cnf = allDiff(map);//no similar lines
		cnf = append(cnf,declareInts(map));//exactly one true in each line in map
		cnf = append(cnf,allStepsAreLegal(flights, map));//prevent illegal solutions
		SATSolver.addClauses(cnf);//add cnf to SAT
	}
	 
	// Task 14
	public static int[] decode(boolean[] assignment, int[][] map) {
		// Add your code here
		int[] solution = new int[map.length];
		int location = 1;//the first cell in assignment is meaningless
		int nextCity = 0;
		for(int i=0;i<map.length;i=i+1) {
			for(int j=0;j<map[i].length;j=j+1,location=location+1) {
				if(assignment[location]==true) {
					solution[nextCity] = j;//the location of TRUE in map[i] is the next City
					nextCity = nextCity+1;
				}
			}
		}
		return solution;
	}
	
	// Task 15
	public static int[] solve(int[][] flights) {
		// Add your code here
		if(!isLegalInstance(flights))
			throw new IllegalArgumentException();
		int nVars = flights.length * flights.length;
		SATSolver.init(nVars);
		int[] solve = assist(flights,nVars);
		return solve;
	}
	
	//assistance function
	public static int[] assist(int[][] flights, int nVars) {
		int[] solve=null;
		int[][] map = createVarsMap(flights.length);
		encode(flights,map);
		boolean[] assignment = SATSolver.getSolution();
		if(assignment==null) {
			throw new RuntimeException();
		}else if(assignment.length==(nVars+1)) {
			int[] tour = decode(assignment,map);
			if(isSolution(flights,tour)){
				solve = tour;
			}else{
				throw new IllegalArgumentException("the solution isn't legal");
			}
		}
		return solve;
	}
	
	//task16
	public static boolean solve2(int[][] flights, int s, int t) {
		// Add your code here
		boolean twoAns = false;
		if(!isLegalInstance(flights)) {
			throw new IllegalArgumentException();
		}else if(s<flights.length & s>=0 & t<flights.length & t>=0){//if s,t not legal the ans stays false
			int[][] map = createVarsMap(flights.length);
			int nVars = flights.length * flights.length;
			SATSolver.init(nVars);
			int[][] moreClauses = {{s+1},{map[flights.length-1][t]}};//forces s = first city and t = last city
			SATSolver.addClauses(moreClauses);
			int[] solve1 = assist(flights,nVars);
			if(solve1 != null && solve1.length==flights.length) {
				int[] anotherAdd = new int[flights.length-2];
				for(int j=1;j<(flights.length-1);j=j+1) 
					anotherAdd[j-1] = -map[j][solve1[j]];//forces to give different trail
				SATSolver.init(nVars);
				SATSolver.addClauses(moreClauses);
				SATSolver.addClause(anotherAdd);
				int[] solve2 = assist(flights,nVars);
				if(solve2 != null && solve2.length==flights.length) 
					twoAns = true;
			}
		}
		return twoAns;
	}
}