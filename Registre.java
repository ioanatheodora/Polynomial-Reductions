import java.io.IOException;

class Registre {
    public static void main(String[] args) throws IOException, InterruptedException {
        Task3 task3 = new Task3();

        task3.readProblemData();
        task3.solve();
        task3.askOracle();
        task3.decipherOracleAnswer();
        task3.writeAnswer();
    }
}