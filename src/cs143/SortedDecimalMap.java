package cs143;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SortedDecimalMap<E extends DecimalSortable>
        implements DecimalSearchTree<E> {

    //private fields
    private DecimalNode root;
    private int digitCount;

    /**
     * Constructor.
     *
     * @param digitCount the largest number of digits a sorting key will contain
     * in this sorted decimal map
     */
    public SortedDecimalMap(int digitCount) {
        root = new DecimalNode();
        this.digitCount = digitCount;
    }

    @Override
    public boolean contains(int key) {
        //Integers required to break up the key in digits
        int index;
        int remainder = key;
        DecimalNode node = root;
        for (int i = 0; i < digitCount; i++) {
            //get the first digit in the key
            index = remainder / (int) Math.pow(10, digitCount - i - 1);
            //get the last digits in the key
            remainder -= (index * Math.pow(10, digitCount - i - 1));
            //Move to the next child if it exists
            if (node.children[index] != null) {
                node = node.children[index];
            } else {
                break;
            }
        }
        //return true if the leaf has a value
        return node.value != null;
    }

    @Override
    public E get(int key) {
        //Integers required to break up the key in digits
        int index;
        int remainder = key;
        DecimalNode node = root;
        for (int i = 0; i < digitCount; i++) {
            //get the first digit in the key
            index = remainder / (int) Math.pow(10, digitCount - i - 1);
            //get the last digits in the key
            remainder -= (index * Math.pow(10, digitCount - i - 1));
            //Move to the next child if it exists
            if (node.children[index] != null) {
                node = node.children[index];
            } else {
                break;
            }
        }
        //Return the value inside the leaf node
        return (E) node.value;
    }

    @Override
    public boolean insert(E e) {
        //Integers required to break up the key in digits
        int index;
        int remainder = e.getKey();
        DecimalNode node = root;
        for (int i = 0; i < digitCount; i++) {
            //get the first digit in the key
            index = remainder / (int) Math.pow(10, digitCount - i - 1);
            //get the last digits in the key
            remainder -= (index * Math.pow(10, digitCount - i - 1));
            //Create the child if it doesn't exist
            if (node.children[index] == null) {
                node.makeChild(index);
            }
            //Move to the next child
            node = node.children[index];
        }
        //Return false if there is already a value in the node
        if (node.value != null) {
            return false;
        }
        //Place the value in the node
        node.value = e;
        return true;
    }

    @Override
    public boolean remove(int key) {
        //Integers required to break up the key in digits
        int index;
        int remainder = key;
        DecimalNode node = root;
        for (int i = 0; i < digitCount; i++) {
            //get the first digit in the key
            index = remainder / (int) Math.pow(10, digitCount - i - 1);
            //get the last digits in the key
            remainder -= (index * Math.pow(10, digitCount - i - 1));
            //No need to go further if the child doesn't exist - return false
            if (node.children[index] == null) {
                return false;
            }
            //Move to the next child
            node = node.children[index];
        }
        //Remove the value at the leaf if it exists
        if (node.value != null) {
            node.value = null;
            return true;
        }
        return false;
    }

    /**
     * Reports if the tree is empty or not.
     *
     * @return true if the tree is empty, false if not
     */
    @Override
    public boolean isEmpty() {
        return isEmpty(root, true);
    }

    /**
     * Private recursive method to determine if the tree is empty.
     *
     * @param node the current node the recursion is on
     * @return false if a value is found in the tree, true if recursion is
     * complete and no values were found
     */
    private boolean isEmpty(DecimalNode node, boolean empty) {
        if (node.value != null) {
            return false;
        } else {
            for (int i = 0; i < 10; i++) {
                if (node.children[i] != null) {
                    empty = empty && isEmpty(node.children[i], empty);
                }
            }
        }
        return empty;
    }

    /**
     * Provides access to a type-specific iterator.
     *
     * @return a new iterator object
     */
    @Override
    public Iterator<E> iterator() {
        return new DecimalIterator();
    }

    /**
     * An inner class that defines a type-specific iterator. Uses a queue
     * internally to manage iterating through the tree.
     */
    private class DecimalIterator implements Iterator<E> {

        //private fields
        private Queue<E> queue;
        private E lastRemoved;

        /**
         * Default constructor.
         */
        public DecimalIterator() {
            fillQueue();
        }

        /**
         * A private method used to fill the queue.
         */
        private void fillQueue() {
            queue = new LinkedList<>();
            fillQueue(root);
        }

        /**
         * A private recursive method to fill the queue with the value of each
         * node in sorted order.
         *
         * @param node the current node in the recursive process
         */
        private void fillQueue(DecimalNode node) {
            for (int i = 0; i < 10; i++) {
                if (node.children[i] != null) {
                    if (node.children[i].value != null) {
                        //Base case: the child is a leaf - just add it to the queue
                        queue.add((E) node.children[i].value);
                    } else {
                        //When the node is a parent, recurse
                        fillQueue(node.children[i]);
                    }
                }
            }
        }

        /**
         * Determines if there is a next value in the map.
         *
         * @return true if there is a next value, false if not
         */
        @Override
        public boolean hasNext() {
            if (queue.isEmpty()) {
                return false;
            }
            return true;
        }

        /**
         * Provides access to the next value in the map, in sorted order.
         *
         * @return the next value
         */
        @Override
        public E next() {
            lastRemoved = queue.poll();
            return lastRemoved;
        }

        /**
         * Removes the last reported value from the map.
         */
        @Override
        public void remove() {
            SortedDecimalMap.this.remove(lastRemoved.getKey());
        }
    }

    /**
     * An inner class that defines the decimal tree node.
     */
    private class DecimalNode<E> {

        /**
         * the value stored in this node - will be null for all nodes except the
         * lowest-level leaf nodes
         */
        public E value;

        /**
         * the array used to store the children of this node
         */
        public DecimalNode[] children;

        /**
         * Default constructor.
         */
        public DecimalNode() {
            children = new DecimalNode[10];
        }

        /**
         * A convenience method to create a new node at the given index of the
         * current node.
         *
         * @param index the index of the children array where the new node
         * should be stored
         */
        public void makeChild(int index) {
            children[index] = new DecimalNode();
        }
    }

}
