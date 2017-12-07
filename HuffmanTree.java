import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.PriorityQueue;


public class HuffmanTree {
	
	private PriorityQueue<HuffmanNode> inputQueue = new PriorityQueue<HuffmanNode>();
	
	private HuffmanNode overallRoot;
	private HuffmanNode branch;
	private int[] count;
	
	public HuffmanTree(int[] count) {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		this.count = count;
		System.out.println(count.length);
		for (int i = 0; i < count.length; i++) {
			if (count[i] != 0) {
				HuffmanNode n = new HuffmanNode();
				n.ascii = i;
				n.frequency = count[i];
				inputQueue.add(n);
			}
		}
		buildTree();
		try {
			overallRoot.printTree(out);
			System.out.println(countNodes(overallRoot) + " Node in Tree.");
		} catch (IOException e) {
			System.out.println("BufferedWrite failure during printTree() execution in HuffmanTree object constructor.");
		}
	}
	
	public void write(PrintStream output) {
		for ( int i = 0; i < count.length; i ++) {
			if (count[i] != 0) {
				output.println(travelTree(i));
			}
		}
		System.out.println("File Write Completed.");
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
	
	private String travelTree(int target) {
		return travelTree(overallRoot, target);
	}
	
	private String travelTree(HuffmanNode n, int target) {
		String result = "";
		
		if (n == null) {
			return result;
		}
		if (n.left == null && n.right == null && n.ascii == target) {
			return ":" + n.ascii;
		} /*else if (n.left == null && n.right == null){
			return result;
		}*/
		String left = travelTree(n.left, target);
		if (left.equals("")) {
			String right = travelTree(n.right, target);
			if (!right.equals("")) {
				result += 1;
			}
		} else {
			result += 0;
		}
		
		
		
//		if(n.left != null && n.right == null) {
//			result = 1 + travelTree(n.right, target);
//		} else {
//			result = 0 + travelTree(n.left, target);
//		}
		return result;
	}
	
//	public void write(PrintStream output) {
//		while(!inputQueue.isEmpty()) {
//			HuffmanNode n = inputQueue.remove();
//			if (n.frequency !=0) {
//				output.println(n);
//			}
//		}
//		System.out.println("File Write Completed.");
//	}
	
	private void buildTree() {
		while(!inputQueue.isEmpty()) {
			//HuffmanNode n = inputQueue.peek();
			HuffmanNode n = inputQueue.remove();
			if (!inputQueue.isEmpty()) {
				buildLevel(n, inputQueue.remove());
			} else {
				buildLevel(n, overallRoot);
			}
			
		}
	}
	
	private void buildLevel(HuffmanNode n, HuffmanNode m) {
		HuffmanNode q = new HuffmanNode();
		q.left = n;
		q.right = m;
		q.frequency = q.left.frequency + q.right.frequency;
		if (overallRoot == null) {
			overallRoot = n;
		}
		else {
			HuffmanNode o = new HuffmanNode();
			if (overallRoot.frequency > q.frequency) {
				o.left = q;
				o.right = overallRoot;
				overallRoot = o;
			} else {
				o.left = overallRoot;
				o.right = q;
				overallRoot = o;
			}
		}
	}
	
//	private void buildTree() {
//		while(!inputQueue.isEmpty()) {
//			HuffmanNode n = inputQueue.peek();
//			buildLevel(n.frequency);
//		}
//	}
//	
//	private void buildLevel(int frequencyValue) {
//		int count = 0;
//		while(inputQueue.peek().frequency == frequencyValue) {
//			HuffmanNode n = new HuffmanNode();
//			n.left = inputQueue.remove();
//			n.right = inputQueue.remove();
//			n.frequency = n.left.frequency + n.right.frequency;
//			if (overAllRoot == null) {
//				overAllRoot = n;
//			} else if (overAllRoot.frequency == n.frequency) {
//				HuffmanNode m = new HuffmanNode();
//				m.left = n;
//				m.right = overAllRoot;
//				overAllRoot = m;
//			} else if (overAllRoot.frequency >)
//		}
//	}
	
	
	
	private class HuffmanNode implements Comparable<HuffmanNode>{
		
		public int frequency;
		public int ascii;
		public HuffmanNode left;
		public HuffmanNode right;
		
		
		public HuffmanNode() {
		}
		
		public String toString() {
			return "" + ascii + "\n" + frequency;
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
	            out.write(frequency + " : " + ascii + " :: " +  ((char) ascii));
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
