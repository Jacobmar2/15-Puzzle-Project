package fifteenpuzzle;

import java.io.*;
import java.util.*;

public class BlockPuzzle {
    public final static int UP = 0;
    public final static int DOWN = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;

    int SIZE;
    public int[][] board; //holds current board order
    public int[][] solved; //holds solved version of board
    private HashSet<List<List<Integer>>> visited;

    //borrows methods from assignment 1 solution with few modifications
    //and additions (ex. custom board parameter)
    public void BlockPuzzle(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        SIZE = br.read() - '0'; //gets size of board
        br.read(); //next line
        //System.out.println(SIZE); //prints size

        board = new int[SIZE][SIZE];
        int c1, c2, s;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                c1 = br.read();
                c2 = br.read();
                s = br.read(); // skip the space
                if (c1 == ' ')
                    c1 = '0';
                if (c2 == ' ')
                    c2 = '0';
                board[i][j] = 10 * (c1 - '0') + (c2 - '0');
            }
        }

        br.close();
    }

    private class Pair {
        int i, j;

        Pair(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private Pair findCoord(int tile) {
        int i = 0, j = 0;
        for (i = 0; i < SIZE; i++)
            for (j = 0; j < SIZE; j++)
                if (board[i][j] == tile)
                    return new Pair(i, j);
        return null;
    }

    private Pair findCoordCustom(int tile, int[][] b) {
        int i = 0, j = 0;
        for (i = 0; i < SIZE; i++)
            for (j = 0; j < SIZE; j++)
                if (b[i][j] == tile)
                    return new Pair(i, j);
        return null;
    }

    private Pair findCoordSolved(int tile) {
        int i = 0, j = 0;
        for (i = 0; i < SIZE; i++)
            for (j = 0; j < SIZE; j++)
                if (solved[i][j] == tile)
                    return new Pair(i, j);
        return null;
    }

    public void makeMove(int tile, int direction) {
        Pair p = findCoord(tile);
        int i = p.i;
        int j = p.j;

        // the tile is in position [i][j]
        switch (direction) {
            case UP: {
                if (i > 0 && board[i - 1][j] == 0) {
                    board[i - 1][j] = tile;
                    board[i][j] = 0;
                    break;
                }
            }
            case DOWN: {
                if (i < SIZE - 1 && board[i + 1][j] == 0) {
                    board[i + 1][j] = tile;
                    board[i][j] = 0;
                    break;
                }
            }
            case RIGHT: {
                if (j < SIZE - 1 && board[i][j + 1] == 0) {
                    board[i][j + 1] = tile;
                    board[i][j] = 0;
                    break;
                }
            }
            case LEFT: {
                if (j > 0 && board[i][j - 1] == 0) {
                    board[i][j - 1] = tile;
                    board[i][j] = 0;
                    break;
                }
            }
        }

    }

    public void makeMoveCustom(int tile, int direction, int[][] b) {
        Pair p = findCoordCustom(tile, b);
        int i = p.i;
        int j = p.j;

        // the tile is in position [i][j]
        switch (direction) {
            case UP: {
                if (i > 0 && b[i - 1][j] == 0) {
                    b[i - 1][j] = tile;
                    b[i][j] = 0;
                    break;
                }
            }
            case DOWN: {
                if (i < SIZE - 1 && b[i + 1][j] == 0) {
                    b[i + 1][j] = tile;
                    b[i][j] = 0;
                    break;
                }
            }
            case RIGHT: {
                if (j < SIZE - 1 && b[i][j + 1] == 0) {
                    b[i][j + 1] = tile;
                    b[i][j] = 0;
                    break;
                }
            }
            case LEFT: {
                if (j > 0 && b[i][j - 1] == 0) {
                    b[i][j - 1] = tile;
                    b[i][j] = 0;
                    break;
                }
            }
        }
    }

    public ArrayList<Integer> availableMoves() {
        Pair p = findCoord(0);
        int i = p.i;
        int j = p.j;
        ArrayList<Integer> moves = new ArrayList<Integer>();

        // empty tile is in position [i][j]
        if (i + 1 <= SIZE - 1)
            moves.add(UP);
        if (i - 1 >= 0)
            moves.add(DOWN);
        if (j + 1 <= SIZE - 1)
            moves.add(LEFT);
        if (j - 1 >= 0)
            moves.add(RIGHT);

        return moves;
    }

    public ArrayList<Integer> availableTiles() { //corresponding tiles for available moves
        Pair p = findCoord(0);
        int i = p.i;
        int j = p.j;
        ArrayList<Integer> tiles = new ArrayList<Integer>();

        // empty tile is in position [i][j]
        if (i + 1 <= SIZE - 1)
            tiles.add(board[i + 1][j]);
        if (i - 1 >= 0)
            tiles.add(board[i - 1][j]);
        if (j + 1 <= SIZE - 1)
            tiles.add(board[i][j + 1]);
        if (j - 1 >= 0)
            tiles.add(board[i][j - 1]);

        return tiles;
    }

    public int oneManhattanDist(int tile, int[][] board) { //finds Manhattan distance of a single tile
        Pair p = findCoordCustom(tile, board);              //aka walk distance
        int i = p.i;
        int j = p.j;
        Pair q = findCoordSolved(tile);
        int ii = q.i;
        int jj = q.j;

        int dist = 0;
        dist += Math.abs(ii - i);
        dist += Math.abs(jj - j);
        return dist;
    }

    public int[] optimalMove(ArrayList<Integer> moves, ArrayList<Integer> tiles, int pMove) {
        int[] opMove = new int[2];
        int[][] curBoard;
        int bestMove = 0;
        int bestTile = 0;
        int lowestDist = 20; //holds the lowest walk distance
        for (int i = 0; i < moves.size(); i++) {
            curBoard = cloneBoard(board);
            makeMoveCustom(tiles.get(i), moves.get(i), curBoard);

            if (oneManhattanDist(tiles.get(i), curBoard) < lowestDist
                    && !this.isVisited(curBoard)
                    && moves.get(i) != oppositeMove(pMove)) {
                bestMove = moves.get(i);
                bestTile = tiles.get(i);
                lowestDist = oneManhattanDist(tiles.get(i), curBoard);
            }
        }
        opMove[0] = bestMove;
        opMove[1] = bestTile;
        return opMove;
    }

    public void moveBack(int pMove) {
        int tile = findTile(pMove);
        makeMove(tile, oppositeMove(pMove));
    }

    public int oppositeMove(int move) {
        if (move == UP)
            return DOWN;
        else if (move == DOWN)
            return UP;
        else if (move == LEFT)
            return RIGHT;
        else if (move == RIGHT)
            return LEFT;
        else
            return -1;
    }

    public int findTile(int move) {  //finds tile one move from 0, for moving back
        Pair p = findCoord(0);
        int i = p.i;
        int j = p.j;
        if (move == UP)
            return board[i - 1][j];
        else if (move == DOWN)
            return board[i + 1][j];
        else if (move == LEFT)
            return board[i][j - 1];
        else if (move == RIGHT)
            return board[i][j + 1];
        else
            return -1;
    }

    public int[] goodMove(ArrayList<Integer> moves, ArrayList<Integer> tiles, int pMove) {
        int[] opMove = new int[2];      //gets move based on if tile move gets closer to solution or not
        int[][] curBoard;
        int bestMove = 0;
        int bestTile = 0;
        int walkDist;
        int walkDist2;
        for (int i = 0; i < moves.size(); i++) {
            curBoard = cloneBoard(board);
            walkDist = oneManhattanDist(tiles.get(i), curBoard);
            makeMoveCustom(tiles.get(i), moves.get(i), curBoard);
            walkDist2 = oneManhattanDist(tiles.get(i), curBoard);

            if (walkDist2 < walkDist && moves.get(i) != oppositeMove(pMove)
                    && !this.isVisited(curBoard)) {
                bestMove = moves.get(i);
                bestTile = tiles.get(i);
            }
        }
        opMove[0] = bestMove;
        opMove[1] = bestTile;
        return opMove;
    }


    public void solvedBoard() {
        int[][] solved = new int[SIZE][SIZE];
        int curTile = 1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                solved[i][j] = curTile;
                curTile++;
            }
        }
        solved[SIZE - 1][SIZE - 1] = 0;
        this.solved = solved;
    }

    public void printSolved() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(solved[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean isSolved() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] != solved[i][j])
                    return false;
        return true;
    }

    public boolean isSolved(int[][] board) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] != solved[i][j])
                    return false;
        return true;
    }

    //displays board to show processes and for debugging
    public void printBoard(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int[][] cloneBoard(int[][] board) {
        int[][] b = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                b[i][j] = board[i][j];
            }
        }
        return b;
    }

    public int[][] tempBoard(int[][] board, int tile, int move){
        int[][] b = cloneBoard(board);
        makeMoveCustom(tile,move,b);
        return b;
    }

    public List<List<Integer>> toArrayList(int[][] board) {
        List<List<Integer>> b = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            List<Integer> c = new ArrayList<>();
            b.add(c);
            for (int j = 0; j < SIZE; j++) {
                c.add(board[i][j]);
            }
        }
        return b;
    }

    public void setVisited() {
        visited = new HashSet<>();
    }

    public void addToVisited() {
        int[][] board = cloneBoard(this.board);
        visited.add(toArrayList(board));
        //System.out.print(visited.size() + "   ");
    }

    public boolean isVisited(int[][] board) {
        return visited.contains(toArrayList(board));
    }


    //All functions for solving bigger boards
    public int ManhattanDist(int[][] board) {
        int sum = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sum += oneManhattanDist(board[i][j], board);
            }
        }
        return sum;
    }


}