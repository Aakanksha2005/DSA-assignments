package com.thealgorithms.datastructures.trees;

import java.util.LinkedList;
import java.util.List;

public class SplayTree {
    public static final TreeTraversal PRE_ORDER = new PreOrderTraversal();
    public static final TreeTraversal IN_ORDER = new InOrderTraversal();
    public static final TreeTraversal POST_ORDER = new PostOrderTraversal();

    private Node root;
    private static int operationCount = 0;  // Track the number of operations

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(final int key) {
        root = insertRec(root, key);
        root = splay(root, key);
    }

    public boolean search(int key) {
        root = splay(root, key);
        return root != null && root.key == key;
    }

    public void delete(final int key) {
        if (isEmpty()) {
            throw new EmptyTreeException("Cannot delete from an empty tree");
        }

        root = splay(root, key);

        if (root.key != key) {
            return;
        }

        if (root.left == null) {
            root = root.right;
        } else {
            Node temp = root;
            root = splay(root.left, findMax(root.left).key);
            root.right = temp.right;
        }
    }

    public List<Integer> traverse(TreeTraversal traversal) {
        List<Integer> result = new LinkedList<>();
        traversal.traverse(root, result);
        return result;
    }

    private Node findMax(Node root) {
        while (root.right != null) {
            root = root.right;
        }
        return root;
    }

    private Node rotateRight(Node x) {
        operationCount++; // Count each rotation
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node rotateLeft(Node x) {
        operationCount++; // Count each rotation
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private Node splay(Node root, final int key) {
        operationCount++; // Count each splay operation
        if (root == null || root.key == key) {
            return root;
        }

        if (root.key > key) {
            if (root.left == null) {
                return root;
            }
            if (root.left.key > key) {
                root.left.left = splay(root.left.left, key);
                root = rotateRight(root);
            } else if (root.left.key < key) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null) {
                    root.left = rotateLeft(root.left);
                }
            }
            return (root.left == null) ? root : rotateRight(root);
        } else {
            if (root.right == null) {
                return root;
            }
            if (root.right.key > key) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null) {
                    root.right = rotateRight(root.right);
                }
            } else if (root.right.key < key) {
                root.right.right = splay(root.right.right, key);
                root = rotateLeft(root);
            }
            return (root.right == null) ? root : rotateLeft(root);
        }
    }

    private Node insertRec(Node root, final int key) {
        if (root == null) {
            return new Node(key);
        }

        if (key < root.key) {
            root.left = insertRec(root.left, key);
        } else if (key > root.key) {
            root.right = insertRec(root.right, key);
        } else {
            throw new DuplicateKeyException("Duplicate key: " + key);
        }

        return root;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public void resetOperationCount() {
        operationCount = 0;
    }

    public static class EmptyTreeException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public EmptyTreeException(String message) {
            super(message);
        }
    }

    public static class DuplicateKeyException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public DuplicateKeyException(String message) {
            super(message);
        }
    }

    private static class Node {
        final int key;
        Node left;
        Node right;

        Node(int key) {
            this.key = key;
            left = null;
            right = null;
        }
    }

    public interface TreeTraversal {
        void traverse(Node root, List<Integer> result);
    }

    private static final class InOrderTraversal implements TreeTraversal {
        private InOrderTraversal() {
        }

        public void traverse(Node root, List<Integer> result) {
            if (root != null) {
                traverse(root.left, result);
                result.add(root.key);
                traverse(root.right, result);
            }
        }
    }

    private static final class PreOrderTraversal implements TreeTraversal {
        private PreOrderTraversal() {
        }

        public void traverse(Node root, List<Integer> result) {
            if (root != null) {
                result.add(root.key);
                traverse(root.left, result);
                traverse(root.right, result);
            }
        }
    }

    private static final class PostOrderTraversal implements TreeTraversal {
        private PostOrderTraversal() {
        }

        public void traverse(Node root, List<Integer> result) {
            if (root != null) {
                traverse(root.left, result);
                traverse(root.right, result);
                result.add(root.key);
            }
        }
    }
}
