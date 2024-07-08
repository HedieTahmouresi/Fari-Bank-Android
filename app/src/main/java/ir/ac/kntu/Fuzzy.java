package ir.ac.kntu;

public class Fuzzy {
    private double similarity;

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public Fuzzy(String first, String second) {
        if (first == null || second == null) {
            if (first == null && second == null) {
                setSimilarity(1);
            }
            setSimilarity(0);
        }
        if (first.equals(second)) {
            setSimilarity(1);
        }
        int[][] distance = new int[first.length() + 1][second.length() + 1];
        for (int i = 0; i <= first.length(); i++) {
            for (int j = 0; j <= second.length(); j++) {
                if (i == 0) {
                    distance[i][j] = j;
                } else if (j == 0) {
                    distance[i][j] = i;
                } else {
                    distance[i][j] = Math.min(
                            distance[i - 1][j - 1] + (first.charAt(i - 1) == second.charAt(j - 1) ? 0 : 1),
                            Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1)
                    );
                }
            }
        }
        int maxLength = Math.max(first.length(), second.length());
        setSimilarity(1.0 - ((double) distance[first.length()][second.length()] / maxLength));
    }
}
