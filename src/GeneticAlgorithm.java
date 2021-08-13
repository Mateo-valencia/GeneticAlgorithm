import java.util.*;

public class GeneticAlgorithm {

    private final String TEAM_MATE = "Mateo Valencia Muriel";
    private final int MAX_POBLATION = 20;
    private final int MUTATION_PORCENT_PROHABILITY = 70;
    private String firstString = null;
    private String secondString = null;
    private ArrayList<String> poblation = new ArrayList<>();
    private boolean wordFounded = false;
    private int totalIterations = 0;
    private final Random random = new Random();

    public GeneticAlgorithm() {
        this.createInitialPoblation();
        this.fitness();
        this.runGenetic();
    }

    private String randomLetter() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghjiklmnopqrstuvwxyz,! ";
        int positionLetters = random.nextInt(letters.length());
        return String.valueOf(letters.charAt(positionLetters));
    }

    private String randomString() {
        StringBuilder stringOutput = null;
        for (int i = 0; i < TEAM_MATE.length(); i++){
            if (i == 0){
                stringOutput = new StringBuilder(randomLetter());
            }else{
                stringOutput.append(randomLetter());
            }
        }
        return stringOutput.toString();
    }

    private void fitness() {
        HashMap<String,Integer> hashMapPoints = new HashMap<>();

        scoreByRandomString(hashMapPoints);

        Map.Entry<String, Integer> maxEntry = getTheBest(hashMapPoints);

        firstString = maxEntry.getKey();
        String firstScore = maxEntry.getValue().toString();
        hashMapPoints.remove(maxEntry.getKey());

        Map.Entry<String, Integer> maxEntrySecond = getTheBest(hashMapPoints);

        secondString = maxEntrySecond.getKey();
        String secondScore = maxEntrySecond.getValue().toString();
        hashMapPoints.remove(maxEntrySecond.getKey());

        if (TEAM_MATE.equals(firstString)){
            this.wordFounded = true;
        }
    }

    private void scoreByRandomString(HashMap<String, Integer> hashMapPoints) {
        poblation.forEach( randomString -> {
            char[] letters = randomString.toCharArray();
            int point = 0;

            for (int i = 0; i < letters.length; i++) {
                if (letters[i] == TEAM_MATE.charAt(i)){
                    point += 1;
                }
            }

            hashMapPoints.put(randomString,point);
        });
    }

    private Map.Entry<String, Integer> getTheBest(HashMap<String, Integer> hashMapPoints) {
        return Collections.max(
                hashMapPoints.entrySet(), Map.Entry.comparingByValue()
        );
    }

    private void createInitialPoblation() {
        for (int i = 0; i < MAX_POBLATION; i ++) {
            poblation.add(randomString());
        }
    }

    private void runGenetic() {
        do {
            crossAndMutate();
            fitness();
            System.out.println();
            System.out.println("Iteration " + totalIterations);
            System.out.println(this.TEAM_MATE + " == " + this.firstString);
            System.out.println("----------------------------------------------------");
            poblation.forEach(System.out::println);
            System.out.println("----------------------------------------------------");
            totalIterations += 1;
        } while (!wordFounded);
    }

    private void crossAndMutate() {
        for (int i = 0; i < MAX_POBLATION; i++) {
            String nextString = crossStrings(i);
            nextString = mutateString(nextString);
            this.poblation.set(i, nextString);
        }
    }

    private String crossStrings(int i) {
        int splitAtIndex = random.nextInt(TEAM_MATE.length());
        if (i <= MAX_POBLATION / 2) {
            return firstString.substring(0, splitAtIndex) + secondString.substring(splitAtIndex, TEAM_MATE.length());
        }
        return secondString.substring(0, splitAtIndex) + firstString.substring(splitAtIndex, TEAM_MATE.length());
    }

    private String mutateString(String string) {
        if (random.nextInt(101) <= MUTATION_PORCENT_PROHABILITY) {
            int mutateAtIndex = random.nextInt(TEAM_MATE.length());
            StringBuilder stringBuilder = new StringBuilder(string);
            stringBuilder.setCharAt(mutateAtIndex, randomLetter().charAt(0));
            return stringBuilder.toString();
        }
        return string;
    }
}
