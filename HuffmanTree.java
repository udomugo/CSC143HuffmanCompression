import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Class will create a binary tree using the Huffman Algorithm
 * 
 * @author Programmer James
 *
 */
public class HuffmanTree {
	
	// Declaring class variables
	private PriorityQueue<HuffmanNode> inputQueue = new PriorityQueue<HuffmanNode>();
	private HuffmanNode overallRoot;
	
	// Used for printing out tree with the displayTree() method
	private BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
	
	/**
	 * Constructor creates HuffmanTree from an integer array containing a file's ASCII character
	 * content frequencies. Using the Huffman Algorithm a binary tree is created using the ASCII
	 * character frequency data.
	 * @param count
	 */
	public HuffmanTree(int[] count) {
		
		// Declaring Method Variables
		HuffmanNode node;
		
		for (int i = 0; i < count.length; i++) {
			if (count[i] != 0) {
				
				// Adding ASCII character and frequency values to new Nodes
				node = new HuffmanNode(i, count[i]);
				
				// Adding new Nodes to a Priority Queue 
				inputQueue.offer(node);
			}
		}
		
		// Adding defined end of file character Node to Priority Queue
		node = new HuffmanNode(256,1);
		inputQueue.offer(node);
		
		// Constructing tree structure
		buildTree();
		
		// Setting the Overall Root of the HuffmanTree
		overallRoot = inputQueue.poll();
		
		// Prints out a visual representation of the tree into console
		//displayTree();
	}
	
	/**
	 * Constructor creates HuffmanTree from a Scanner containing a document code file that contains a 
	 * list of ASCII characters and their binary tree paths previously generated based on the frequency of 
	 * the ASCII character within the original document. Relying that the document code file was
	 * generated using the same Huffman Algorithm the tree will be recreated using the binary tree path
	 * to grow Node out from the root and place the ASCII character values in the appropriate leaves.
	 *  
	 * @param input
	 */
	public HuffmanTree(Scanner input) {
		
		// Declaring Method Variables
		int ascii;
		String treePath;
		
		while(input.hasNextLine()) {
			
			// Getting the ASCII character value
			ascii = Integer.parseInt(input.nextLine());
			
			// Getting the binary tree path code
			treePath = input.nextLine();
			
			// Creating new leaf and adding values
			HuffmanNode node = new HuffmanNode();
			node.ascii = ascii;
			node.frequency = node.ascii;
			
			if (overallRoot == null) {
				
				// Plants the root of the binary tree
				plantRoot(node, treePath);
			} else {
				
				// Finds the correct place for the next leaf
				growTree(node, treePath);
			}
		}
		
		// Prints out a visual representation of the tree into console
		//displayTree();
		
	}
	
	/**
	 * Method takes a BitInputStream containing an encoded file, a PrintStream to write the decoded
	 * data to, and an End of File character. The encoded file is read one bit at a time. A pointer
	 * is defined and points to the overallRoot of the tree. Each bit represents a direction in the 
	 * binary tree to take, and the pointer is updated to the new position in the tree. Pointer is 
	 * checked for being a leaf. Returns the ASCII value if pointer is a leaf otherwise returns -1.
	 * 
	 * @param input
	 * @param output
	 * @param eof
	 */
	public void decode(BitInputStream input, PrintStream output, int eof) {
		
		// Declaring method variables
		HuffmanNode pointer = overallRoot;
		int direction;
		int result;
		
		while (true) {
			
			// Getting new direction bit
			direction = input.readBit();
			
			// Moving pointer to new position
			if (direction == 0) {
				pointer = pointer.left;
			}
			if (direction == 1) {
				pointer = pointer.right;
			}
			
			// Checking if new position is a leaf
			if (!pointer.isBranch()) {
				// Getting ASCII character value
				result = pointer.ascii;
			} else {
				result = -1;
			}
			
			// Terminates decoding when End of File character is reached
			if ( result == eof) {
				break;
			}
			
			// Writes the ASCII character when found and resets pointer to tree root
			if (result != -1) {
				output.write(result);;
				pointer = overallRoot;
			}
		}
		System.out.println("File decoding completed.");
	}
	
	/**
	 * Method writes code file of current HuffmanTree structure.
	 * @param output
	 */
	public void write(PrintStream output) {
		
		// Gathering tree data
		String result = recordTree();
		
		// Cleaning data and writing to output
		output.println(result.trim());
		
		// Message to console to notify file write completed
		System.out.println("Code file generated.");
	}
	
	/**
	 * Method takes a HuffmanNode and a String containing a binary tree path code from 
	 * a code file. The Node is the first leaf containing an ASCIi value. The binary 
	 * tree path is traversed backwards each direction creates a new node up the path 
	 * until the root node is created and attached with the last direction. The root 
	 * node is assigned to the overallRoot variable and is used to build the rest of the 
	 * Huffman tree from the code file.
	 * 
	 * @param node
	 * @param path
	 */
	private void plantRoot(HuffmanNode node, String path) {
		
		// Declaring method variables
		Character current = path.charAt(path.length()-1);
		HuffmanNode m = new HuffmanNode();
		
		// Traversing binary tree path in reverse order
		if (path.length() != 1) {
			if (current == '0') {
				m.left = node;
				plantRoot(m, path.substring(0, path.length()-1));
			}
			if (current == '1') {
				m.right = node;
				plantRoot(m, path.substring(0, path.length()-1));
			}
		} else {
			if (current == '0') {
				m.left = node;
			}
			if (current == '1') {
				m.right = node;
			}
			overallRoot = m;
		}
	}
	
	/**
	 * Method takes a HuffmanNode containing an ASCII character value and a String object containing a
	 * the ASCII character's binary tree path code. The method recursively follows the binary tree path
	 * and plants the node in the corresponding place in the binary tree.
	 * 
	 * @param node
	 * @param path
	 */
	private void growTree(HuffmanNode node, String path) {
		growTree(overallRoot, node, path);
	}
	
	private void growTree(HuffmanNode parent, HuffmanNode node, String path) {
		Character current = path.charAt(0);
		if (path.length() == 1) {
			if (current == '0') {
				parent.left = node;
			}
			if (current == '1') {
				parent.right = node;
			}
		} else {
			if (current == '0') {
				if (parent.left == null) {
					parent.left = new HuffmanNode();
				}
				growTree(parent.left, node, path.substring(1));
			}
			if (current == '1') {
				if (parent.right == null) {
					parent.right = new HuffmanNode();
				}
				growTree(parent.right, node, path.substring(1));
			}
		}
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
	
	/**
	 * Method prints out the current HuffmanTree object. Used for internal testing
	 * purposes, is not a public method.
	 */
	private void displayTree() {
		try {
			System.out.println();
			overallRoot.printTree(out);
			System.out.println(countNodes(overallRoot) + " Nodes in Tree.");
			System.out.println();
		} catch (IOException e) {
			System.out.println("BufferedWrite failure during printTree() execution in HuffmanTree object constructor.");
		}
	}
	
	/**
	 * Method will return a String object representing the current HuffmanTree structure.
	 * The string is formated to display the ASCII character value on the first line
	 * and the binary tree path code on the following line, making a list of every ASCII
	 * character contained in the tree in this fashion.
	 * 
	 * @return
	 */
	private String recordTree() {
		String record = "";
		return recordTree(overallRoot, record);
	}
	
	private String recordTree(HuffmanNode root, String record) {
		String result = "";
		
		if (!root.isBranch()) {
			return root.ascii + "\n" + record + "\n";
		}
		if (root.isBranch()) {
			result += recordTree(root.left, record + 0);
			result += recordTree(root.right, record + 1);
		}
		return result;
	}
	
	/**
	 * Method utilizes a PriorityQueue of HuffmanNodes to create a binary
	 * tree representing the frequency of ASCII characters' use within a 
	 * given document.
	 * 
	 */
	private void buildTree() {
		while(inputQueue.size() != 1) {
			
			HuffmanNode left = inputQueue.poll();
			HuffmanNode right = inputQueue.poll();
			inputQueue.offer(buildNode(left, right));
		}
	}
	
	/**
	 * Method creates new HuffmanNodes when building the initial 
	 * binary tree of a document file's frequency of ASCII characters.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	private HuffmanNode buildNode(HuffmanNode left, HuffmanNode right) {
		
		HuffmanNode node = new HuffmanNode();
		node.left = left;
		node.right = right;
		node.frequency = node.left.frequency + node.right.frequency;
		node.ascii = node.frequency;
		return node;
	}
	
	/**
	 * Private internal class to construct new HuffmanNodes for use in a HuffmanTree object.
	 * @author Programmer James
	 *
	 */
	private class HuffmanNode implements Comparable<HuffmanNode>{
		
		// Declaring class variables
		public int frequency;
		public int ascii;
		public HuffmanNode left;
		public HuffmanNode right;
		
		/**
		 * Default constructor requiring no parameters
		 */
		public HuffmanNode() {
			this.ascii = 0;
			this.frequency = 0;
		}
		
		/**
		 * Constructor takes an integer containing an ASCII character value and an integer representing
		 * the ASCII character's frequency of occurrence.
		 * 
		 * @param ascii
		 * @param frequency
		 */
		public HuffmanNode(int ascii, int frequency) {
			this.ascii = ascii;
			this.frequency = frequency;
		}
		
		/**
		 * Method returns a String representation of the the HuffmanNode object.
		 */
		public String toString() {
			return "" + ascii + " : " + frequency;
		}
		
		/**
		 * Method returns true if the HuffmanNode has a left and a right child HuffmanNode.
		 * Otherwise returns false that the HuffmanNode is a branch of the binary tree.
		 * @return
		 */
		public boolean isBranch() {
			return (left != null && right != null);
		}
		
		/**
		 * Method compares a HuffmanNode object with another HuffmanNode object.
		 */
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
		
		// The last three methods are code borrowed from StackOverflow
		// They allow the binary tree to be displayed visually for teting
		//purposes
		public void printTree(BufferedWriter out) throws IOException {
	        if (right != null) {
	            right.printTree(out, true, "");
	        }
	        printNodeValue(out);
	        if (left != null) {
	            left.printTree(out, false, "");
	        }
	    }
		
		// The last three methods are code borrowed from StackOverflow
		// They allow the binary tree to be displayed visually for teting
		//purposes
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

	    // The last three methods are code borrowed from StackOverflow
 		// They allow the binary tree to be displayed visually for teting
 		//purposes
	    
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
