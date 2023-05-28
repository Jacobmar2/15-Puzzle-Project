package fifteenpuzzle;

import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class Solver {
	//to compile and run in terminal, use:
	//javac fifteenpuzzle/Solver.java
	//java fifteenpuzzle.Solver input_board.txt sol.txt
	//java fifteenpuzzle.Solver board1.txt sol1.txt


	public static <T extends Comparable<T>> void main(String[] args) throws IOException {
//		System.out.println("number of arguments: " + args.length);
//		for (int i = 0; i < args.length; i++) {
//			System.out.println(args[i]);
//		}

		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}

		BlockPuzzle p = new BlockPuzzle();
		//Heap opQ = new Heap();

		//TODO file stuff
		//File input = new File(args[0]);
		// solve...
		//File output = new File(args[1]);

		p.BlockPuzzle(args[0]);
		p.solvedBoard();

		//p.printBoard(p.board);
		//System.out.println(p.availableMoves());
		//System.out.println(p.availableTiles());
		//System.out.println(Arrays.toString(p.optimalMove(p.availableMoves(), p.availableTiles())));

		//beginning of search
		int[] opSol;
		int tile;
		int move;
		ArrayList<Integer> moves = new ArrayList<>();
		ArrayList<Integer> tiles;
		int movesMade = 0;	//limits moves
		p.setVisited();
		fpNode<T> v; //board for A* search

		File f = new File(args[1]);
		FileWriter fw = new FileWriter(f);

		fpNode<T> s = new fpNode<>();
		s.setData(p.board);
		s.setScore(p.ManhattanDist(p.board));
		PriorityQueue<fpNode<T>> opQueue = new PriorityQueue<>();
		HashSet<fpNode<T>> set = new HashSet<>();

		opQueue.add(s);
		while (!opQueue.isEmpty()){
			v = opQueue.remove();
			moves = p.availableMoves();
			tiles = p.availableTiles();
			for (int i = 0; i < moves.size(); i++){
				fpNode<T> u = new fpNode<>();
				u.setData(p.tempBoard(v.getData2(), tiles.get(i), moves.get(i)));
				if (p.isSolved(u.getData2())){
					u.setG(v.getG()+1);
					System.out.println("Done!");
				}
				else{
					if (opQueue.contains(u) && u.getScore() > v.getScore()){
						u.setScore(v.getG() + 1 + p.ManhattanDist(u.getData2()));
					}
					else if (set.contains(u.getData()) && u.getScore() > v.getScore()){
						u.setScore(v.getG() + 1 + p.ManhattanDist(u.getData2()));
						opQueue.add(u);
					}
					else{
						opQueue.add(u);
					}
				}
				u.setParent(v);
			}
			set.add(v);
		}



		//p.printBoard(p.board);
		//System.out.println("next step " + movesMade);

/*
		while(!p.isSolved()){// && movesMade < 1000000
			p.addToVisited();
			boolean moved = false;
			moves = p.availableMoves();
			tiles = p.availableTiles();

			opSol = p.goodMove(moves, tiles, pMoves.peek());
			tile = opSol[1];
			move = opSol[0];
			if (tile > 0) {
					p.makeMove(tile,move);
					//System.out.println(tile + " gM " + move); //shows tile moved, and move made
					fw.write(tile + " " + writeMove(move) + "\n");
					moved = true;
					pMoves.push(move);
				}

			if (!moved){	//if it can't find a good move, it makes next best move instead
				opSol = p.optimalMove(moves, tiles, pMoves.peek());
				tile = opSol[1];
				move = opSol[0];
				if (tile == 0){
					move = pMoves.pop();
					p.moveBack(move);
					move = p.oppositeMove(move);
					tile = p.findTile(move);
					//System.out.println(tile + " backMove " + move);
					fw.write(tile + " " + writeMove(move) + "\n");
				}
				else {
					p.makeMove(tile, move);
					pMoves.push(move);
					//System.out.println(tile + " oM " + move); //shows tile moved, and move made
					fw.write(tile + " " + writeMove(move) + "\n");
				}
			}
			movesMade++;
		}
		fw.close();
		//p.printBoard(p.board);

		//if (p.isSolved())
		//	System.out.println("Done! " + movesMade);

 */

	}

	public static char writeMove(int move){
		if (move == 0)
			return 'U';
		else if (move == 1)
			return 'D';
		else if (move == 2)
			return 'L';
		else if (move == 3)
			return 'R';
		else
			return ' ';
	}

}
