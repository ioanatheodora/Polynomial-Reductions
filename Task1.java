import java.io.*;
import java.util.ArrayList;

public class Task1 extends Task {
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
        int number_of_variables = N*K;
        int number_of_clauses = 0;

        // write the information in a string builder in order to be
        // able to add the number of clauses after calculating it
        beginning.append("p cnf ");
        beginning.append(number_of_variables + " ");

        // there is exactly one vertex in the clique
        for (int j = 0; j < K; j++) {
            for (int i = 0; i < N; i++) {
                clauses.append(variables[i][j] + " ");
            }
            clauses.append("0\n");
            number_of_clauses++;
        }

        // for every non-edge (v, w), v and w cannot both be in the resulted clique
       for (int v = 0; v < N; v++) {
           for (int w = v + 1; w < N; w++) {
               if (adjMatrix[v][w] == 0 && v != w) {
                   for (int i = 0; i < K; i++) {
                        for (int j = 0; j < K; j++) {
                            if (i != j) {
                                clauses.append(-variables[v][i] + " "
                                        + -variables[w][j] + " 0\n");
                                number_of_clauses++;
                            }
                       }
                   }
               }
           }
       }

        // a vertex v cannot be both in the ith and jth vertex in the clique
        for (int v = 0; v < N; v++) {
            for (int i = 0; i <  K; i++) {
                for (int j = 0; j < K; j++) {
                    if (i != j) {
                        clauses.append(-variables[v][i] + " " + -variables[v][j] + " 0\n");
                        number_of_clauses++;
                    }
                }
            }
        }

        // two vertices cannot both be the ith vertex in the clique
        for (int v = 0; v < N; v++) {
            for (int w = 0; w < N; w++) {
                for (int i = 0; i < K; i++){
                    if (v != w) {
                        clauses.append(-variables[v][i] + " " + -variables[w][i] + " 0\n");
                        number_of_clauses++;
                    }

                }
            }
        }
        beginning.append(number_of_clauses + "\n");

        // write the information in the "sat.cnf" file
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

            // interpret the solution
            for (int i = 0; i < n; i++) {
                if(Double.parseDouble(computeResult[i]) > 0) {
                    int v = Integer.parseInt(computeResult[i]);

                    v = ((v - 1)/ K) + 1;

                    result.add(v);
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
