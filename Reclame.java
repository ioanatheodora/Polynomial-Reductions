import java.io.IOException;

class Reclame {
    public static void main(String[] args) throws IOException, InterruptedException {
        Task2 task2 = new Task2();

        task2.readProblemData();

        task2.solve();

//        task2.decipherOracleAnswer();

        task2.writeAnswer();
    }
}