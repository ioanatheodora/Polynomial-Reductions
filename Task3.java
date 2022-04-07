import java.io.*;
import java.util.ArrayList;

public class Task3 extends Task {
    private int N;
    private int M;
    private int K;
    private int[][] adjMatrix;
    private int[][] variables;
    private ArrayList<Integer> result = new ArrayList<>();


    @Override
    public void solve() throws IOException, InterruptedException {
        variables = new int[N][K];

        // create the variables for the clauses
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < K; j++) {
                variables[i][j] = i * K + j + 1;
            }
        }

        formulateOracleQuestion();
    }

    @Override
    public void readProblemData() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        // on the first line there are 3 numbers that we split
        String[] inputNumbers = bufferedReader.readLine().trim().split(" ");

        // cast to int in order to use the numbers further
        N = Integer.parseInt(inputNumbers[0]);
        M = Integer.parseInt(inputNumbers[1]);
        K = Integer.parseInt(inputNumbers[2]);

        // we use an adjacency matrix to store the graph given
        // and compute the actions further
        adjMatrix = new int[N][N];

        for (int i = 0; i < M; i++) {
            int node1, node2;
            String[] edge = bufferedReader.readLine().trim().split(" ");

            // get the vertices of the edge
            node1 = Integer.parseInt(edge[0]);
            node2 = Integer.parseInt(edge[1]);

            adjMatrix[node1 - 1][node2 - 1] = 1;
            adjMatrix[node2 - 1][node1 - 1] = 1;
        }

    }

    @Override
    public void formulateOracleQuestion() throws IOException, InterruptedException {
        StringBuilder beginning = new StringBuilder();
        StringBuilder clauses = new StringBuilder();
        BufferedWriter writer = new BufferedWriter(new FileWriter("sat.cnf"));
        int number_of_variables = N * K;
        int number_of_clauses = 0;

        beginning.append("p cnf ");
        beginning.append(number_of_variables + " ");

        // the problem can reduce to the graph coloring problem
        // and we have three types of clauses

        // two adjacent nodes cannot have the same color
        for (int v = 0; v < N; v++) {
            for (int w = v + 1; w < N; w++) {
                // test if the vertices are adjacent
                if (adjMatrix[v][w] == 1) {
                    for (int k = 0; k < K; k++) {
                        clauses.append(-variables[v][k] + " " + -variables[w][k] + " 0\n");
                        number_of_clauses++;
                    }
                }
            }
        }

        // every node must have a colour assigned to it
        for (int v = 0; v < N; v++) {
            for (int k = 0; k < K; k++) {
                clauses.append(variables[v][k] + " ");
            }
            clauses.append("0\n");
            number_of_clauses++;
        }

        // every node must have assigned only one colour
        for (int v = 0; v < N; v++) {
            for (int i = 0; i < K; i++) {
                for (int j = 0; j < K; j++) {
                    if (i != j) {
                        clauses.append(-variables[v][i] + " " +
                                -variables[v][j] + " 0\n");
                        number_of_clauses++;
                    }
                }
            }
        }


        beginning.append(number_of_clauses + "\n");
        writer.write(beginning.toString());
        writer.write(clauses.toString());
        writer.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("sat.sol"));
        String resolved = reader.readLine();

        if(resolved.equals("False")) {
            result = null;
        } else {
            int n = Integer.parseInt(reader.readLine());
            String[] computeResult = reader.readLine().trim().split(" ");
            // find the right register
            for (int i = 0; i < n / K; i++) {
                for (int j = 0; j < K; j ++){
                    if(Double.parseDouble(computeResult[i * K + j]) > 0) {
                        result.add(j + 1);
                    }
                }

            }
        }

        reader.close();


    }

    @Override
    public void writeAnswer() throws IOException {
        if (result == null) {
            System.out.println("False");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            System.out.println("True");
            for (int vertex : result) {
                stringBuilder.append(vertex + " ");
            }
            System.out.println(stringBuilder);
        }
    }
}
