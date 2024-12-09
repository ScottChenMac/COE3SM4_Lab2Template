import java.util.Vector;

// To be modified for Integration

public class HandsRBT {
    
    private HandsRBTNode root;
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    // This inline class is typically required in certain post-deletion correction cases
    private static class NilNode extends HandsRBTNode
    {
        private NilNode()
        {
            super(null);
            this.colour = BLACK;
        }
    }

    // Deletion Method 1 - promote the smallest of the right tree
    private HandsRBTNode findMin(HandsRBTNode thisNode)
    {
        while(thisNode.left != null)    
            thisNode = thisNode.left;

        return thisNode;
    }

    // Deletion Method 2 - promote the largest of the left tree
    private HandsRBTNode findMax(HandsRBTNode thisNode)
    {
        while(thisNode.right != null)    
            thisNode = thisNode.right;

        return thisNode;
    }
    
    private boolean isBlack(HandsRBTNode thisNode)
    {
        return (thisNode == null || thisNode.colour == BLACK);
    }

    
    // [Problem 1] - Helper Functions to support all the RBT colour correction operations

    // [Problem 1-1] Get Sibling
    private HandsRBTNode getSibling(HandsRBTNode thisNode)
    {
        // Use the parent member to access the sibling
        
        // If thisNode has no sibling, you can then
        // throw new IllegalStateException("Parent and Children are Unrelated.");            
    }    

    // [Problem 1-2] Get Uncle
    private HandsRBTNode getUncle(HandsRBTNode parent)
    {
        // Use the parent member to access the grandparent
        // Then find the uncle of the parent from the grandparent
        
        // If parent does not have an uncle, you can then
        // throw new IllegalStateException("Provided Parent is NOT a child of the Grandparent");
    }


    
    
    // [Problem 2] Design Rotation Algorithms

    // [Problem 2-1] Complete the Parent-Child replacement algorithm (required for many RBT operations)
    private void replaceParentsChild(HandsRBTNode parent, HandsRBTNode oldChild, HandsRBTNode newChild)
    {
        if(parent == null)  root = newChild;
        else if(parent.left == oldChild) parent.left = newChild;
        else if(parent.right == oldChild) parent.right = newChild;
        else throw new IllegalStateException("Parents and Children are unrelated...");

        if(newChild != null) newChild.parent = parent;
    }

    // [Problem 2-2] Complete the Classic child-parent rotation LEFTWARD
    private void rotateLeft(HandsRBTNode thisNode)
    {
        HandsRBTNode parent = thisNode.parent;
        HandsRBTNode rightChild = thisNode.right;

        thisNode.right = rightChild.left;
        if(rightChild.left != null) rightChild.left.parent = thisNode;

        rightChild.left = thisNode;
        thisNode.parent = rightChild;

        replaceParentsChild(parent, thisNode, rightChild);
    }

    // [Problem 2-3] Complete the Classic child-parent rotation RIGHTWARD
    private void rotateRight(HandsRBTNode thisNode)
    {
        HandsRBTNode parent = thisNode.parent;
        HandsRBTNode leftChild = thisNode.left;

        thisNode.left = leftChild.right;
        if(leftChild.right != null) leftChild.right.parent = thisNode;

        leftChild.right = thisNode;
        thisNode.parent = leftChild;

        replaceParentsChild(parent, thisNode, leftChild);
    }

    




    public HandsRBTNode getRoot() { return root; }

    public HandsRBTNode findNode(Hands thisHand)
    {
        HandsRBTNode node = root;

        while(node != null)
        {           

            if(node.myHand.isMyHandLarger(thisHand)) 
            {
                node = node.left;
            }
            else if(node.myHand.isMyHandSmaller(thisHand)) 
            {
                node = node.right;
            }
            else // Matched
            {
                return node;
            }
        }

        return null;
        // can also use recursive method
    }



    // [Problem 3] RBT Node Insertion and Colour Correction (Hard!!)

    // [Problem 3-1] Implement the basic BST traversal and insertion algorithm
    //               Then insert the node RED, and carry out recursive colour correction
    public void insert(Hands thisHand)
    {
        HandsRBTNode node = root;
        HandsRBTNode parent = null;

        // Traverse
        while(node != null)
        {
            parent = node;
            if(node.myHand.isMyHandLarger(thisHand)) 
            {
                node = node.left;
            }
            else if(node.myHand.isMyHandSmaller(thisHand)) 
            {
                node = node.right;
            }
            else // matched
            {
                System.out.println("This Hand already in the RBT: ");
                thisHand.printMyHand();
                return;

                // Alternatively, throw exception:
                    // throw new IllegalArgumentException("This hand is already in BST");
            }
        }  
        
        

        // Insert
        HandsRBTNode newNode = new HandsRBTNode(thisHand);
        newNode.colour = RED;  // always default RED, then fix

        if(parent == null)
        {
            root = newNode;
        }
        else if(parent.myHand.isMyHandLarger(thisHand)) 
        {
            parent.left = newNode;
        }
        else
        {
            parent.right = newNode;
        }

        newNode.parent = parent;

        // Fix RBT Properties
        PostInsertRBTCorrection(newNode);
    }

    
    // [Problem 3-2] Design the post-insertion RBT colour correction algorithm
    //               Heed the FIVE CASES to handle!!! (Test cases are provided for each case)
    private void PostInsertRBTCorrection(HandsRBTNode thisNode)
    {
        HandsRBTNode parent = thisNode.parent;

        // Five Cases to Handle
        
        // Case 1 - parent is null (root) or black
        if(parent == null) return; // can optionally force the root node to be Black

        if(parent.colour == BLACK) return; // nothing to do, because red after black



        // The remaining cases all have RED parents
        HandsRBTNode grandparent = parent.parent;
        
        // Case 2: No grandparent (i.e. parent is root, must be black)
        if(grandparent == null)
        {
            parent.colour = BLACK;
            return;
        }

        HandsRBTNode uncle = getUncle(parent);

        // Case 3: Uncle is RED
        //   Must recolour parent and uncle to BLACK
        //   and grandparent to BLACK
        //   and recursively up to the root
        if(uncle != null && uncle.colour == RED)
        {
            parent.colour = BLACK;
            uncle.colour = BLACK;
            grandparent.colour = RED;

            PostInsertRBTCorrection(grandparent);
        }

        // Case 4 and 5 can take place either on the LEFT or RIGHT of grandparent
        else if(parent == grandparent.left)  // LEFT
        {
            // Case 4-L: Uncle is black, and thisNode is inner child of grandparent
            if(thisNode == parent.right)
            {
                rotateLeft(parent);
                parent = thisNode;
            }
            
            // Case 5-L: Uncle is black, and thisNode is outer child of grandparent
            rotateRight(grandparent);
            parent.colour = BLACK;
            grandparent.colour = RED;
        }
        else  // RIGHT
        {
            // Case 4-R: Uncle is black, and thisNode is inner child of grandparent
            if(thisNode == parent.left)
            {
                rotateRight(parent);
                parent = thisNode;
            }
            
            // Case 5-R: Uncle is black, and thisNode is outer child of grandparent
            rotateLeft(grandparent);
            parent.colour = BLACK;
            grandparent.colour = RED;
        } 
    }


    // The rest of the code are provided as existing features
    // The rest of the code are provided as existing features
    // The rest of the code are provided as existing features

    // No need to modify the remaining contents
    // No need to modify the remaining contents
    // No need to modify the remaining contents
    
    public void deleteInvalidHands(Hands consumedHand)
    {
        for(int i = 0; i < 5; i++)
        {
            deleteHandsWithCard(consumedHand.getCard(i));
        }
    }

    private void deleteHandsWithCard(Card thisCard)
    {
        // traverse the tree, keep every Node reference that has thisCard using a Vector
        Vector<Hands> targetHands = new Vector<Hands>();
        registerHandsWithCard(targetHands, thisCard, root);

        // then, invoke delete for every identified node.
        for(int i = 0; i < targetHands.size(); i++)
        {            
            delete(targetHands.get(i));
        }
    }

    // Pre-order traversal with card-matching scanning step
    private void registerHandsWithCard(Vector<Hands> targets, Card thisCard, HandsRBTNode thisNode)
    {
        if(thisNode == null) return;

        if(thisNode.myHand.hasCard(thisCard))
        {
            targets.addElement(thisNode.myHand);
        }

        registerHandsWithCard(targets, thisCard, thisNode.left);
        registerHandsWithCard(targets, thisCard, thisNode.right);
    }

    
    
    

    public void delete(Hands thisHand)
    {
        HandsRBTNode node = root;

        // First, search for node
        while( node != null )
        {
            if(node.myHand.isMyHandLarger(thisHand))
            {
                node = node.left;
            }
            else if(node.myHand.isMyHandSmaller(thisHand))
            {
                node = node.right;
            }
            else 
            {
                break;  // equal
            }
        }
        // If not found, don't do anything
        if(node == null) return;

        // If node found, take the respective action
        HandsRBTNode promotedNode;
        boolean deletedNodeColour;

        //  Either the node has zero or one child
        if(node.left == null || node.right == null)
        {
            promotedNode = deleteNodeWithMaxOneChild(node);
            deletedNodeColour = node.colour;
        }
        //  Or the node has two children
        else
        {
            HandsRBTNode successorNode = findMin(node.right);   // OR, = findMax(node.left);            
            node.myHand = successorNode.myHand;

            promotedNode = deleteNodeWithMaxOneChild(successorNode);
            deletedNodeColour = successorNode.colour;
        }

        // Finally, fix the RBT
        if(deletedNodeColour == BLACK)
        {
            PostDeleteRBTCorrection(promotedNode);

            if(promotedNode.getClass() == NilNode.class)
            {
                replaceParentsChild(promotedNode.parent, promotedNode, null);
            }
        }
    }

    private HandsRBTNode deleteNodeWithMaxOneChild(HandsRBTNode thisNode)
    {
        // Only left child
        if(thisNode.left != null)
        {
            replaceParentsChild(thisNode.parent, thisNode, thisNode.left);
            return thisNode.left;
        }
        // Only right child
        else if(thisNode.right != null)
        {
            replaceParentsChild(thisNode.parent, thisNode, thisNode.right);
            return thisNode.right;
        }
        // No child
        else
        {
            HandsRBTNode newChild = null;
            if(thisNode.colour == BLACK)
                newChild = new NilNode();
            
            replaceParentsChild(thisNode.parent, thisNode, newChild);
            return newChild;
        }


    }

    // 6 Cases to handle.  4 in this method, and 2 in its helper function
    private void PostDeleteRBTCorrection(HandsRBTNode thisNode)
    {
        // Case 1 - thisNode is root.
        if(thisNode == root) return;  
        // may want to optionally colour the root node BLACK

        HandsRBTNode sibling = getSibling(thisNode);
           
        // Case 2 - Red Sibling
        if(sibling.colour == RED)
        {
            handleRedSibling(thisNode, sibling);
            sibling = getSibling(thisNode); // keep track of the new sibling for subsequent cases
        }


        // Case 3 + 4 - Black sibling with two black children
        if(isBlack(sibling.left) && isBlack(sibling.right))
        {
            sibling.colour = RED;

            // Case 3 - Black sibling with two black children + red parent
            if(thisNode.parent.colour == RED)
            {
                thisNode.parent.colour = BLACK;
            }
            // Case 4 - Black sibling with two black children + black parent
            else
            {
                PostDeleteRBTCorrection(thisNode.parent);
                // recursively correct the colour upwards
            }
        }

        // Case 5 + 6 - Black Sibling with at least One Child
        else
        {
            handleBlackSiblingWithMinOneRedChild(thisNode, sibling);
        }
    }

    private void handleRedSibling(HandsRBTNode thisNode, HandsRBTNode sibling)
    {
        // First, recolour
        sibling.colour = BLACK;
        thisNode.parent.colour = RED;

        // Then, rotate
        if(thisNode == thisNode.parent.left)
        {
            rotateLeft(thisNode.parent);
        }
        else
        {
            rotateRight(thisNode.parent);
        }
    }

    private void handleBlackSiblingWithMinOneRedChild(HandsRBTNode thisNode, HandsRBTNode sibling)
    {
        boolean isLeftChild = (thisNode == thisNode.parent.left);

        // Case 5: Black Sibling with min one red child + outer nephew is black
        if(isLeftChild && isBlack(sibling.right))
        {
            sibling.left.colour = BLACK;
            sibling.colour = RED;
            rotateRight(sibling);
            sibling = thisNode.parent.right;
        }
        else if(!isLeftChild && isBlack(sibling.left))
        {
            sibling.right.colour = BLACK;
            sibling.colour = RED;
            rotateLeft(sibling);
            sibling = thisNode.parent.left;
        }

        // Case 6: Black Sibling with min one red child + outer nephew is red
        sibling.colour = thisNode.parent.colour;
        thisNode.parent.colour = BLACK;

        if(isLeftChild)
        {
            sibling.right.colour = BLACK;
            rotateLeft(thisNode.parent);
        }
        else
        {
            sibling.left.colour = BLACK;
            rotateRight(thisNode.parent);
        }
    }
    
    public void printRBT()
    {        
        // First, get the height of the tree
        int height = getHeight(root);        
        

        // Then, print using leveled order 
        for(int i = 1; i <= height; i++)
        {
            for(int j = 0; j < (height - i); j++)
            {
                System.out.print("  ");
            }
            printCurrentLevel(root, i);
            System.out.println(">>");
        }
            
    }

    private int getHeight(HandsRBTNode thisNode)
    {
        if(thisNode == null) return 0;
        
        int leftHeight = getHeight( thisNode.left );
        int rightHeight = getHeight( thisNode.right );

        if(leftHeight > rightHeight) 
            return (leftHeight + 1);
        else 
            return (rightHeight + 1);
    }

    private void printCurrentLevel(HandsRBTNode thisNode, int level)
    {
        if(thisNode == null) 
        {
            System.out.print("==NIL==");
            return;
        }

        if(level == 1) 
        {
            printNode(thisNode);
        }
        else if(level > 1)
        {
            printCurrentLevel(thisNode.left, level - 1);
            System.out.print("\t");
            printCurrentLevel(thisNode.right, level - 1);
            System.out.print("\t");
        }
    }

    private void printNode(HandsRBTNode thisNode)
    {
        if(thisNode.colour == BLACK)
            System.out.printf("(B)");
        else
            System.out.printf("(R)");
        thisNode.myHand.printMyHand();
        System.out.printf(" ");
    }

}
