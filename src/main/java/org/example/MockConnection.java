package org.example;

public class MockConnection {

    public void connect() {
        //Mimic connection establishment
        System.out.println("Mock connection established");
    }

    public void executeQuery(String query) {
        //Mimic query execution
        System.out.println("Executing query: " + query);

        //Simulate result
        System.out.println("Query executed successfully");
    }

    public void close() {
        //Mimic connection closing
        System.out.println("Mock connection closed");
    }
}
