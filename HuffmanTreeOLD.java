import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.TreeMap;


public class HuffmanTreeOLD {
	
	private PriorityQueue<HuffmanNode> inputQueue = new PriorityQueue<HuffmanNode>();
	
	private HuffmanNode overallRoot;
	TreeMap<Integer, HuffmanNode> branches = new TreeMap<Integer,HuffmanNode>();
	HuffmanNode leaf;
	//private HuffmanNode[] branches;
	private int[] count;
	
	public HuffmanTreeOLD(int[] count) {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		this.count = count;
		//this.branches = new HuffmanNode[5];
		//System.out.println(count.length);
		HuffmanNode n;
		for (int i = 0; i < count.length; i++) {
			if (count[i] != 0 /* && count[i] != 10 && i != 10 && i != 13*/) {
				n = new HuffmanNode(i, count[i]);
				inputQueue.offer(n);
			}
		}
		n = new HuffmanNode(256,1);
		inputQueue.offer(n);
		buildTree(out);
//		try {
//			System.out.println();
//			overallRoot.printTree(out);
//			System.out.println(countNodes(overallRoot) + " Nodes in Tree.");
//			System.out.println();
//		} catch (IOException e) {
//			System.out.println("BufferedWrite failure during printTree() execution in HuffmanTree object constructor.");
//		}
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
		} else if (n.left == null && n.right == null){
			return result;
		}
		String left = travelTree(n.left, target);
		if (left.equals("")) {
			String right = travelTree(n.right, target);
			if (!right.equals("")) {
				result += 1;
			}
		} else {
			result += 0;
		}
		return result;
	}
	
	private void buildTree(BufferedWriter out) {
		while(!inputQueue.isEmpty()) {
			
//			if (overallRoot != null) {
				HuffmanNode n = inputQueue.poll();
				if (/*inputQueue.peek() != null && n.frequency != inputQueue.peek().frequency &&*/ leaf != null) {
					leaf.right = n;
					n = leaf;
					calcFreq(n);
					buildLevel(n);
					leaf = null;
				} else {
					buildLevel(n);
				}
//			} else {
//				overallRoot = buildNode(inputQueue.poll(), inputQueue.poll());
//				//overallRoot = n;
//			}
			// Printing out tree
//			try {
//				System.out.println();
//				overallRoot.printTree(out);
//				System.out.println(countNodes(overallRoot) + " Nodes in Tree.");
//				System.out.println("There are " + branches.size() + " branches not connected to the tree.");
//			} catch (IOException e) {
//				System.out.println("BufferedWrite failure during printTree() execution in HuffmanTree object constructor.");
//			}
		}
		// Print out unused branches
		try {
			for (HuffmanNode n : branches.values()) {
				System.out.println();
				n.printTree(out);
				System.out.println(countNodes(n) + " Nodes in Branch.");
			}
			
			//System.out.println("There are " + branches.size() + " branches not connected to the tree.");
		} catch (IOException e) {
			System.out.println("BufferedWrite failure during printTree() execution in HuffmanTree object constructor.");
		}
	}
	
	private void buildLevel(HuffmanNode n) {
		if (!n.isParent()) {
			n = buildNode(n);
		}
		if (overallRoot == null && !n.needsChild()) {
			overallRoot = n;
		} else if (overallRoot != null && n.needsChild() && n.left.frequency == overallRoot.frequency) {
			n.right = overallRoot;
			calcFreq(n);
			overallRoot = n;
			//overallRoot = buildNode(overallRoot, n);
		} else if (branches.containsKey(n.left.frequency)) {
			n.right = branches.remove(n.frequency);
			calcFreq(n);
			buildLevel(n);
		} else if (!n.needsChild()) {
				branches.put(n.frequency, n);
			} else {
				leaf = n;
			}
		}
		
		
	
//	private void buildLevel(HuffmanNode n) {
//		if (leaf != null && !n.isParent()) {
//			HuffmanNode m = buildNode(n, leaf);
//			leaf = null;
//			buildLevel(m);
//		} else {
//			if (n.frequency == overallRoot.frequency) {
//				overallRoot = buildNode(n, overallRoot);
//			}
//			if (branches.containsKey(n.frequency)) {
//				buildLevel(buildNode(n, branches.remove(n.frequency)));
//			} else {
//				if (n.isParent()) {
//					branches.put(n.frequency, n);
//				} else {
//					leaf = n;
//				}
//			}
//		}
		
		
//		for (int i = 0; i < branches.size(); i++) {
//			if (branches.get(i).frequency == n.frequency) {
//				branches.add(buildNode(n, branches.remove(i)));
//				break;
//			} else {
//				buildLevel(buildNode(n, inputQueue.remove()));
//			}
//		}
//	}
	
//	private void buildTree() {
//		while(!inputQueue.isEmpty()) {
//			HuffmanNode n = inputQueue.remove();
//			if (!inputQueue.isEmpty()) {
//				buildLevel(n, inputQueue.remove());
//			} else {
//				HuffmanNode q = buildNode(overallRoot,n);
//				/*HuffmanNode q = new HuffmanNode();
//				q.left = overallRoot;
//				q.right = n;
//				q.frequency = q.left.frequency + q.right.frequency;*/
//				overallRoot = q;
//				//buildLevel(n, overallRoot);
//			}
//		}
//	}
//	
//	private void buildLevel(HuffmanNode n, HuffmanNode m) {
//		HuffmanNode q = buildNode(n,m);
//		
//		/*HuffmanNode q = new HuffmanNode();
//		q.left = n;
//		q.right = m;
//		q.frequency = q.left.frequency + q.right.frequency;*/
//		
//		if (overallRoot == null) {
//			overallRoot = n;
//		}
//		else {
//			HuffmanNode o = new HuffmanNode();
//			if (overallRoot.frequency > q.frequency) {
//				o.left = q;
//				o.right = overallRoot;
//				o.frequency = o.left.frequency + o.right.frequency;
//				overallRoot = o;
//			} else {
//				o.left = overallRoot;
//				o.right = q;
//				o.frequency = o.left.frequency + o.right.frequency;
//				overallRoot = o;
//			}
//		}
//	}
//	
	private HuffmanNode buildNode(HuffmanNode n) {
		HuffmanNode m = new HuffmanNode();
//		if (n.frequency >= m.frequency) {
////			q.left = n;
////			q.right = m;
//			q.right = n;
//			q.left = m;
//		} else {
////			q.right = n;
////			q.left = m;
//			q.left = n;
//			q.right = m;
//		}
		m.left = n;
		return m;
	}
	
	private void calcFreq(HuffmanNode n) {
		n.frequency = n.left.frequency + n.right.frequency;
		n.ascii = n.frequency;
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
