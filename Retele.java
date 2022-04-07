import java.io.IOException;

class Retele {
    public static void main(String[] args) throws IOException, InterruptedException {
        Task1 task1 = new Task1();
        task1.readProblemData();

        task1.solve();

        task1.askOracle();

        task1.decipherOracleAnswer();

        task1.writeAnswer();
    }
}