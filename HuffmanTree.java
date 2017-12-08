import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.TreeMap;


public class HuffmanTree {
	
	private PriorityQueue<HuffmanNode> inputQueue = new PriorityQueue<HuffmanNode>();
	private HuffmanNode overallRoot;
	
	//TreeMap<Integer, HuffmanNode> branches = new TreeMap<Integer,HuffmanNode>();
	//HuffmanNode leaf;
	//private int[] count;
	
	public HuffmanTree(int[] count) {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		//this.count = count;
		HuffmanNode node;
		for (int i = 0; i < count.length; i++) {
			if (count[i] != 0) {
				node = new HuffmanNode(i, count[i]);
				inputQueue.offer(node);
			}
		}
		node = new HuffmanNode(256,1);
		inputQueue.offer(node);
		buildTree();
		overallRoot = inputQueue.poll();
		try {
			System.out.println();
			overallRoot.printTree(out);
			System.out.println(countNodes(overallRoot) + " Nodes in Tree.");
			System.out.println();
		} catch (IOException e) {
			System.out.println("BufferedWrite failure during printTree() execution in HuffmanTree object constructor.");
		}
	}
	
	public void write(PrintStream output) {
		String result = recordTree();
		output.println(result);
		System.out.println("File Write Method Executed.");
	}
	
	/**
	 * Gets the number of nodes of a subtree
	 *
	 * @param root
	 *            the root of the subtree
	 * @return the number of nodes in the subtree rooted at root
	 */
	private int countNodes(HuffmanNode root) {
		if (root == null) {
			return 0;
		} else {
			return 1 + countNodes(root.left) + countNodes(root.right);
		}
	}
	
	private String recordTree() {
		String record = "";
		return recordTree(overallRoot, record);
	}
	
	private String recordTree(HuffmanNode root, String record) {
		String result = "";
		
		if (root.needsChild()) {
			return root.ascii + "\n" + record + "\n";
		}
		if (root.isParent()) {
			result += recordTree(root.left, record + 0);
			result += recordTree(root.right, record + 1);
		}
		return result;
	}
	
	private void buildTree() {
		while(inputQueue.size() != 1) {
			
			HuffmanNode left = inputQueue.poll();
			HuffmanNode right = inputQueue.poll();
			inputQueue.offer(buildNode(left, right));
		}
	}
	
	private HuffmanNode buildNode(HuffmanNode left, HuffmanNode right) {
		
		HuffmanNode node = new HuffmanNode();
		node.left = left;
		node.right = right;
		node.frequency = node.left.frequency + node.right.frequency;
		node.ascii = node.frequency;
		return node;
	}
	
	private class HuffmanNode implements Comparable<HuffmanNode>{
		
		public int frequency;
		public int ascii;
		public HuffmanNode left;
		public HuffmanNode right;
		
		public HuffmanNode() {
		}
		
		public HuffmanNode(int ascii, int frequency) {
			this.ascii = ascii;
			this.frequency = frequency;
		}
		
		public String toString() {
			return "" + ascii + " : " + frequency;
		}
		
		public boolean isParent() {
			return (left != null || right != null);
		}
		
		public boolean needsChild() {
			return (left == null || right == null);
		}

		@Override
		public int compareTo(HuffmanNode o) {
			// TODO Auto-generated method stub
			if (this.frequency > o.frequency) {
				return 1;
			} else if (this.frequency < o.frequency) {
				return -1;
			} else {
				return 0;
			}
		}
		
		public void printTree(BufferedWriter out) throws IOException {
	        if (right != null) {
	            right.printTree(out, true, "");
	        }
	        printNodeValue(out);
	        if (left != null) {
	            left.printTree(out, false, "");
	        }
	    }

	    private void printNodeValue(BufferedWriter out) throws IOException {
	        if (ascii == 256) {
	            out.write("<End Of File>");
	            out.flush();
	        } else {
	            //out.write(frequency + " : " + ascii + " :: " +  ((char) ascii));
	        	out.write(ascii + " : " + frequency);
	            out.flush();
	        }

	        out.write('\n');
	        out.flush();
	    }

	    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
	    private void printTree(BufferedWriter out, boolean isRight, String indent) throws IOException {
	        if (right != null) {
	            right.printTree(out, true, indent + (isRight ? "        " : " |      "));
	        }

	        out.write(indent);
	        out.flush();

	        if (isRight) {
	            out.write(" /");
	            out.flush();
	        } else {
	            out.write(" \\");
	            out.flush();
	        }

	        out.write("----- ");
	        out.flush();
	        printNodeValue(out);

	        if (left != null) {
	            left.printTree(out, false, indent + (isRight ? " |      " : "        "));
	        }
	    }
	}
}
