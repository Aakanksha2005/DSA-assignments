package com.thealgorithms.datastructures.trees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SplayTreeTest {
    public static void main(String[] args) {
        SplayTree sTree = new SplayTree();
        int n = 15500;
        long m = 10000000L;

        // Insert values from 1 to n
        for (int i =1; i <= n; i++) {
            sTree.insert(i);
        }

        // Test for different values of c (frequency of accessing the hot element)
        for (double c = 0.1; c <= 0.9; c += 0.1) {
            sTree.resetOperationCount();
            int hotElement = 100; // Frequently accessed element
            
            // Step 1: Create a list of queries
            ArrayList<Integer> queries = new ArrayList<>((int) m);
            int hotElementCount = (int) (c * m);
            
            // Fill c * m slots with hotElement
            for (int i = 0; i < hotElementCount; i++) {
                queries.add(hotElement);
            }
            
            // Fill the remaining slots with random elements
            Random rand = new Random();
            for (int i = hotElementCount; i < m; i++) {
                int randomElement = 1 + rand.nextInt(n); // Random element between 1 and n
                queries.add(randomElement);
            }
            
            // Shuffle the list to randomize the distribution of hotElement
            Collections.shuffle(queries);

            // Step 2: Execute the queries
            for (int query : queries) {
                sTree.search(query);
            }

            // Calculate the amortized cost
            int opCount = sTree.getOperationCount();
            System.out.println("Amortized cost for c=" + c + ": O(" + (double) opCount / m + ")");
        }
    }
}