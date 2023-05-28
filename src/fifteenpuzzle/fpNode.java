package fifteenpuzzle;

import java.util.ArrayList;
import java.util.List;

public class fpNode<T> {
	private List<List<Integer>> data;
	private int[][] data2;
	//private ArrayList<fpNode<T>> children;
	private fpNode<T> parent;
	private int g; // f(n) = g(n) + h(n)
	private int score;


	public List<List<Integer>> getData() {
		return data;
	}

	public int[][] getData2(){
		return this.data2;
	}

	public fpNode<T> getParent() {
		return parent;
	}

	public void setData(int[][] data) {
		List<List<Integer>> b = new ArrayList<>();
		for (int i = 0; i < data.length; i++) {
			List<Integer> c = new ArrayList<>();
			b.add(c);
			for (int j = 0; j < data.length; j++) {
				c.add(data[i][j]);
			}
		}
		this.data = b;
		this.data2 = data;
	}

	public void setParent(fpNode<T> parent) {
		this.parent = parent;
		//setChild(this);
	}

	//public void setChild(fpNode<T> child){
	//	this.children.add(child);
	//}



	public void setG(int g){
		this.g = g;
	}
	public void setScore(int score){
		this.score = score;
	}

	public int getG(){
		return g;
	}
	public int getScore(){
		return score;
	}

	//public boolean isRoot() {
	//	return (getParent() == null);
	//}

}
